package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.RoadRunner.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.RoadRunner.trajectorysequence.TrajectorySequence;
import org.firstinspires.ftc.teamcode.opmodes.auto.vision.AprilTagDetectionPipeline;
import org.firstinspires.ftc.teamcode.opmodes.subsystems.Intake;
import org.firstinspires.ftc.teamcode.opmodes.subsystems.Outtake;
import org.openftc.apriltag.AprilTagDetection;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

import java.util.ArrayList;

/*
 * This is an example of a more complex path to really test the tuning.
 */

@Config
@Autonomous(group = "drive")
public class LeftAuto4 extends LinearOpMode {


    // APRILTAG STUFF

    OpenCvCamera camera;
    AprilTagDetectionPipeline aprilTagDetectionPipeline;

    static final double FEET_PER_METER = 3.28084;

    // Lens intrinsics
    // UNITS ARE PIXELS
    // NOTE: this calibration is for the C920 webcam at 800x448.
    // You will need to do your own calibration for other configurations!
    double fx = 578.272;
    double fy = 578.272;
    double cx = 402.145;
    double cy = 221.506;

    // UNITS ARE METERS
    double tagsize = 0.166;

    // Tag ID 1,2,3 from the 36h11 family
    int LEFT = 1;
    int MIDDLE = 2;
    int RIGHT = 3;

    AprilTagDetection tagOfInterest = null;




    // ROBOT STUFF

    public static double expansionDelay = 0.8;

    public static int cones = 5;
    public static double cycleDelay = 0;

    public static double forwardDistance  = 51.6;

    public static double armFlipTime  = -0.8;



    int currentCycle = 0;

    boolean retract = false;



    public static double depositTime = 0.6;
    public static double grabTime = .8;
    public static double flipTime = .8;
    public static double transferTime = .3;
    public static double intakeTime = .25;
    public static int depBuffer = 300;


    // States

    public enum ScoreState {
        READY,
        DEPOSIT,
        PREPARE,
        GRAB,
        UNSTACK,
        RETRACT_INTAKE,
        FLIP,
        EXTEND_INTAKE,
        EXTEND_OUTTAKE

    }

    public enum RetractState {
        OUTTAKE,
        INTAKE,
        DONE
    }

    ScoreState scoreState = ScoreState.READY;
    RetractState retractState = RetractState.DONE;





    ElapsedTime scoreTimer = new ElapsedTime();

    ElapsedTime cycleTimer = new ElapsedTime();

    double cycleTime = 0;
    //ElapsedTime readyTimer = new ElapsedTime();

    //Drivetrain drive = new Drivetrain();
    Intake intake = new Intake();
    Outtake outtake = new Outtake();






    @Override
    public void runOpMode() throws InterruptedException {

        //PhotonCore.enable(); // Enable PhotonCore
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());


        // APRILTAG

        // with live view port
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        camera = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);

        // no live viewport
        //camera = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"));

        aprilTagDetectionPipeline = new AprilTagDetectionPipeline(tagsize, fx, fy, cx, cy);

        camera.setPipeline(aprilTagDetectionPipeline);
        camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
        {
            @Override
            public void onOpened()
            {
                camera.startStreaming(1280,720, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode)
            {

            }
        });

        telemetry.setMsTransmissionInterval(50);




        // Telemetry
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());


        // Declare our motors
        // Make sure your ID's match your configuration
        intake.init(hardwareMap);
        outtake.init(hardwareMap);





        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

        Pose2d startPose = new Pose2d(35, -63, Math.toRadians(90));


        drive.setPoseEstimate(startPose);

        TrajectorySequence trajSeq = drive.trajectorySequenceBuilder(startPose)


                .waitSeconds(4.2)

                .UNSTABLE_addTemporalMarkerOffset(0, () -> {
                    intake.zeroPosition();
                    intake.dropArm();
                    intake.openClaw();
                })

                .waitSeconds(0.45)
                .forward(forwardDistance)
                .UNSTABLE_addTemporalMarkerOffset(armFlipTime, () -> {
                    intake.flipArm();
                })
                .turn(Math.toRadians(92))
                .UNSTABLE_addTemporalMarkerOffset(-0.5, () -> {
                    outtake.transferDeposit();
                    outtake.setTurretAutoRightPreload();
                })


                .build();



        Trajectory leftApril = drive.trajectoryBuilder(trajSeq.end())
                .lineToLinearHeading(new Pose2d(7, -12, Math.toRadians(180)))
                .build();
        Trajectory midApril = drive.trajectoryBuilder(trajSeq.end())
                .lineToLinearHeading(new Pose2d(34, -12.5, Math.toRadians(180)))
                .build();
        Trajectory rightApril = drive.trajectoryBuilder(trajSeq.end())
                .lineToLinearHeading(new Pose2d(59, -14, Math.toRadians(180)))
                .build();





        // In init
        intake.armStartingPosition();


        telemetry.addLine("Ready to start");

        telemetry.update();

        /*
         * The INIT-loop:
         * This REPLACES waitForStart!
         */
        while (!isStarted() && !isStopRequested())
        {
            ArrayList<AprilTagDetection> currentDetections = aprilTagDetectionPipeline.getLatestDetections();

            if(currentDetections.size() != 0)
            {
                boolean tagFound = false;

                for(AprilTagDetection tag : currentDetections)
                {
                    if(tag.id == LEFT || tag.id == MIDDLE || tag.id == RIGHT)
                    {
                        tagOfInterest = tag;
                        tagFound = true;
                        break;
                    }
                }

                if(tagFound)
                {
                    telemetry.addLine("Tag of interest is in sight!\n\nLocation data:");
                    tagToTelemetry(tagOfInterest);
                }
                else
                {
                    telemetry.addLine("Don't see tag of interest :(");

                    if(tagOfInterest == null)
                    {
                        telemetry.addLine("(The tag has never been seen)");
                    }
                    else
                    {
                        telemetry.addLine("\nBut we HAVE seen the tag before; last seen at:");
                        tagToTelemetry(tagOfInterest);
                    }
                }

            }
            else
            {
                telemetry.addLine("Don't see tag of interest :(");

                if(tagOfInterest == null)
                {
                    telemetry.addLine("(The tag has never been seen)");
                }
                else
                {
                    telemetry.addLine("\nBut we HAVE seen the tag before; last seen at:");
                    tagToTelemetry(tagOfInterest);
                }

            }

            telemetry.update();
            sleep(20);
        }

        /*
         * The START command just came in: now work off the latest snapshot acquired
         * during the init loop.
         */

        /* Update the telemetry */
        if(tagOfInterest != null)
        {
            telemetry.addLine("Tag snapshot:\n");
            tagToTelemetry(tagOfInterest);
            telemetry.update();
        }
        else
        {
            telemetry.addLine("No tag snapshot available, it was never sighted during the init loop :(");
            telemetry.update();
        }

        /* Actually do something useful */






        if (!isStopRequested())
            drive.followTrajectorySequence(trajSeq);


        scoreTimer.reset();
        while (scoreTimer.seconds() <= expansionDelay && opModeIsActive()) {
            // Expand
            intake.readyPosition();
            intake.openClaw();
            intake.dropArmAutoL(2); //5 cone

            outtake.extendSlidePreloadRight();
            outtake.setTurretAutoRightPreload();
            outtake.midDeposit();
            outtake.guideUpLeft();
        }



        cycleTimer.reset();
        scoreTimer.reset();
        while (currentCycle < cones && opModeIsActive()) {
            switch (scoreState) {
                case READY:
                    if (scoreTimer.seconds() >= cycleDelay) {

                        outtake.scoreDepositRight();

                        scoreTimer.reset();
                        scoreState = ScoreState.DEPOSIT;

                    }
                    break;
                case DEPOSIT:
                    if (scoreTimer.seconds() >= depositTime - .4) {
                        outtake.guideScore();
                    }

                    if (scoreTimer.seconds() >= depositTime) {
                        currentCycle++;

                        outtake.transferDeposit();
                        outtake.retractSlide();
                        outtake.setTurretMiddle();
                        outtake.guideDown();

                        intake.openClaw();
                        intake.autoStackPositionLeft(currentCycle + 1);


                        scoreState = ScoreState.PREPARE;
                    }
                    break;
                case PREPARE:
                    if (intake.intakeOutAutoDiffL(currentCycle + 1) < 20 || intake.getDistanceCM() < 2) {
                        intake.closeClawAuto(currentCycle + 1);


                        if (currentCycle == 2) {
                            grabTime = 1;
                        } else {
                            grabTime = .8;
                        }

                        scoreTimer.reset();
                        scoreState = ScoreState.GRAB;
                    }
                    break;
                case GRAB:
                    if (scoreTimer.seconds() >= grabTime) {


                        //intake.transferPosition();
                        intake.flipArm();

                        scoreTimer.reset();
                        scoreState = ScoreState.UNSTACK;
                    }
                    break;
                case UNSTACK:
                    if (scoreTimer.seconds() >= .1) {


                        intake.transferPosition();

                        scoreTimer.reset();
                        scoreState = ScoreState.RETRACT_INTAKE;
                    }
                    break;
                case RETRACT_INTAKE:
                    if (scoreTimer.seconds() >= flipTime && intake.intakeInDiff() < 10) {
                        intake.openClaw();

                        outtake.zeroOuttake();
                        scoreTimer.reset();
                        scoreState = ScoreState.FLIP;
                    }
                    break;
                case FLIP:
                    if (scoreTimer.seconds() >= transferTime) {
                        //intake.readyPosition();
                        intake.autoStackPositionLeft(currentCycle + 2);
                        intake.dropArmAutoL(currentCycle + 2); // Starts at 2

                        scoreTimer.reset();
                        scoreState = ScoreState.EXTEND_INTAKE;
                    }
                    break;
                case EXTEND_INTAKE:
                    if (scoreTimer.seconds() >= intakeTime) {
                        outtake.midDeposit();
                        outtake.setTurretAutoRight();
                        outtake.extendSlideAutoRight();
                        outtake.guideUpLeft();

                        scoreState = ScoreState.EXTEND_OUTTAKE;
                    }
                    break;
                case EXTEND_OUTTAKE:
                    if (outtake.slideOutDiffAutoRight() < depBuffer) {

                        cycleTime = cycleTimer.seconds();

                        cycleTimer.reset();
                        scoreTimer.reset();
                        scoreState = ScoreState.READY;
                    }
                    break;
                default:
                    // should never be reached, as liftState should never be null
                    scoreState = ScoreState.READY;
                    break;

            }
            telemetry.addData("Current Cycle: ", currentCycle);
            telemetry.addData("Cone: ", cones);
            telemetry.addData("Previous Cycle time: ", cycleTime);

            telemetry.addData("Outtake Slide: ", outtake.getExtend());
            telemetry.addData("Turret Pos: ", outtake.getTurret());

            telemetry.addData("Intake Pos: ", intake.getSlide());
            telemetry.addData("Distance: ", intake.getDistanceCM());


            telemetry.update();

        }


        RetractState retractState = RetractState.OUTTAKE;
        scoreTimer.reset();
        while (!retract && opModeIsActive()) {
            switch (retractState) {
                case OUTTAKE:
                    outtake.transferDeposit();
                    outtake.moveSlide(-25, 0.5); // zero outtake slide

                    scoreTimer.reset();
                    retractState = RetractState.INTAKE;
                    break;
                case INTAKE:

                    if (scoreTimer.seconds() >= .6) {
                        outtake.moveTurret(-25, 0.2); // zero turret

                        intake.transferPosition(); // zero intake
                        intake.openClaw();
                        intake.contractArm();

                        retractState = RetractState.DONE;
                    }
                    break;
                case DONE:
                    retract = true;

                    break;
            }
        }



        // PARK

        if(tagOfInterest == null || tagOfInterest.id == LEFT){
            //trajectory

            drive.followTrajectory(leftApril);

        }else if(tagOfInterest.id == MIDDLE){
            //trajectory

            drive.followTrajectory(midApril);

        }else{
            //trajectory

            drive.followTrajectory(rightApril);

        }

        while (intake.intakeInDiff() > 10 && outtake.getTurret() > 10 && opModeIsActive()){

        }

        telemetry.addLine("Auto Done");
        telemetry.update();










    }

    void tagToTelemetry(AprilTagDetection detection)
    {
        telemetry.addLine(String.format("\nDetected tag ID=%d", detection.id));
        telemetry.addLine(String.format("Translation X: %.2f feet", detection.pose.x*FEET_PER_METER));
        telemetry.addLine(String.format("Translation Y: %.2f feet", detection.pose.y*FEET_PER_METER));
        telemetry.addLine(String.format("Translation Z: %.2f feet", detection.pose.z*FEET_PER_METER));
        telemetry.addLine(String.format("Rotation Yaw: %.2f degrees", Math.toDegrees(detection.pose.yaw)));
        telemetry.addLine(String.format("Rotation Pitch: %.2f degrees", Math.toDegrees(detection.pose.pitch)));
        telemetry.addLine(String.format("Rotation Roll: %.2f degrees", Math.toDegrees(detection.pose.roll)));
    }
}
