package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.outoftheboxrobotics.photoncore.PhotonCore;
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
public class CombinedAuto extends LinearOpMode {


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

    public static double expansionDelay = 1.5;

    public static int cycles = 3;
    public static double cycleDelay = 0.25;


    int currentCycle = 0;

    boolean retract = false;



    public static double depositTime = .7;
    public static double grabTime = .5;
    public static double flipTime = .95;
    public static double transferTime = .6;
    public static double intakeTime = .25;


    // States

    public enum ScoreState {
        READY,
        DEPOSIT,
        PREPARE,
        GRAB,
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
    //ElapsedTime readyTimer = new ElapsedTime();

    //Drivetrain drive = new Drivetrain();
    Intake intake = new Intake();
    Outtake outtake = new Outtake();






    @Override
    public void runOpMode() throws InterruptedException {

        PhotonCore.enable(); // Enable PhotonCore


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


                .UNSTABLE_addTemporalMarkerOffset(0, () -> {
                    intake.zeroPosition();
                    intake.dropArm();
                    intake.openClaw();
                })

                .waitSeconds(0.25)
                .forward(48)
                .UNSTABLE_addTemporalMarkerOffset(-0.5, () -> {
                    intake.flipArm();
                })
                .turn(Math.toRadians(-95))
                .UNSTABLE_addTemporalMarkerOffset(-0.5, () -> {
                    outtake.transferDeposit();
                })


                .build();



        Trajectory leftApril = drive.trajectoryBuilder(trajSeq.end())
                .lineToLinearHeading(new Pose2d(7, -11, Math.toRadians(0)))
                .build();
        Trajectory midApril = drive.trajectoryBuilder(trajSeq.end())
                .lineToLinearHeading(new Pose2d(35.4, -12.5, Math.toRadians(0)))
                .build();
        Trajectory rightApril = drive.trajectoryBuilder(trajSeq.end())
                .lineToLinearHeading(new Pose2d(58, -12.5, Math.toRadians(0)))
                .build();





        // In init
        intake.armStartingPosition();


        telemetry.addLine("Ready to start");

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
            intake.dropArmAuto(2); //5 cone

            outtake.extendSlideLeft();
            outtake.setTurretAutoLeft();
            outtake.midDeposit();
        }



        scoreTimer.reset();
        while (currentCycle < cycles && opModeIsActive()) {
            switch (scoreState) {
                case READY:
                    if (scoreTimer.seconds() >= cycleDelay) {

                        outtake.scoreDeposit();

                        scoreTimer.reset();
                        scoreState = ScoreState.DEPOSIT;

                    }
                    break;
                case DEPOSIT:
                    if (scoreTimer.seconds() >= depositTime) {
                        currentCycle++;

                        outtake.transferDeposit();
                        outtake.retractSlide();
                        outtake.setTurretMiddle();

                        intake.openClaw();
                        intake.autoStackPosition();


                        scoreState = ScoreState.PREPARE;
                    }
                    break;
                case PREPARE:
                    if (intake.intakeOutDiff() < 20) {
                        intake.closeClaw();


                        scoreTimer.reset();
                        scoreState = ScoreState.GRAB;
                    }
                    break;
                case GRAB:
                    if (scoreTimer.seconds() >= grabTime) {
                        intake.transferPositionAuto();
                        intake.flipArm();

                        scoreTimer.reset();
                        scoreState = ScoreState.RETRACT_INTAKE;
                    }
                    break;
                case RETRACT_INTAKE:
                    if (scoreTimer.seconds() >= flipTime && intake.intakeInDiff() < 10) {
                        intake.openClaw();

                        scoreTimer.reset();
                        scoreState = ScoreState.FLIP;
                    }
                    break;
                case FLIP:
                    if (scoreTimer.seconds() >= transferTime) {
                        //intake.readyPosition();
                        intake.autoStackPosition();
                        intake.dropArmAuto(currentCycle + 2); // Starts at 2

                        scoreTimer.reset();
                        scoreState = ScoreState.EXTEND_INTAKE;
                    }
                    break;
                case EXTEND_INTAKE:
                    if (scoreTimer.seconds() >= intakeTime) {
                        outtake.midDeposit();
                        outtake.setTurretAutoLeft();
                        outtake.extendSlideLeft();

                        scoreState = ScoreState.EXTEND_OUTTAKE;
                    }
                    break;
                case EXTEND_OUTTAKE:
                    if (outtake.slideOutDiffLeft() < 10) {

                        scoreTimer.reset();
                        scoreState = ScoreState.READY;
                    }
                    break;
                default:
                    // should never be reached, as liftState should never be null
                    scoreState = ScoreState.READY;
                    break;

            }
            telemetry.addData("Current Cycle", currentCycle);

        }


        scoreTimer.reset();
        while (!retract && opModeIsActive()) {
            switch (retractState) {
                case OUTTAKE:
                    outtake.setTurretMiddle();
                    outtake.transferDeposit();
                    outtake.moveSlide(0, 0.5);

                    scoreTimer.reset();
                    retractState = RetractState.INTAKE;
                    break;
                case INTAKE:

                    if (scoreTimer.seconds() >= .6) {
                        intake.moveToPos(0, 0.5);
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
