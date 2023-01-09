package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.outoftheboxrobotics.photoncore.PhotonCore;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.RoadRunner.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.RoadRunner.trajectorysequence.TrajectorySequence;
import org.firstinspires.ftc.teamcode.opmodes.subsystems.Intake;
import org.firstinspires.ftc.teamcode.opmodes.subsystems.Outtake;
import org.firstinspires.ftc.teamcode.opmodes.teleop.AutomatedTransfer;

/*
 * This is an example of a more complex path to really test the tuning.
 */

@Config
@Autonomous(group = "drive")
public class CombinedAuto extends LinearOpMode {




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

        // In init
        intake.armStartingPosition();


        telemetry.addLine("Ready to start");

        waitForStart();

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
                        intake.autoPosition();


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
                    if (scoreTimer.seconds() >= flipTime) {
                        intake.openClaw();

                        scoreTimer.reset();
                        scoreState = ScoreState.FLIP;
                    }
                    break;
                case FLIP:
                    if (scoreTimer.seconds() >= transferTime) {
                        intake.readyPosition();
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










    }
}
