package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.opmodes.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.opmodes.subsystems.Intake;
import org.firstinspires.ftc.teamcode.opmodes.subsystems.Outtake;

@Config
@TeleOp
public class AllPoles extends LinearOpMode {


    // Variables
    public static double guideOffset = -0.4; //.9
    public static double depositTime = 0.6; //.9




    // Grab FSM
    public enum ClawState {
        READY,
        GRAB,
        FLIP,
        RELEASE,
        RETRACT
    }

    ClawState clawState = ClawState.READY;


    // Score FSM
    public enum ScoreState {
        READY,
        UNGUIDE,
        DEPOSIT,
        PREPARE
    }

    ScoreState scoreState = ScoreState.READY;



    // Instantiate Classes

    ElapsedTime clawTimer = new ElapsedTime();
    ElapsedTime scoreTimer = new ElapsedTime();
    //ElapsedTime readyTimer = new ElapsedTime();

    Drivetrain drive = new Drivetrain();
    Intake intake = new Intake();
    Outtake outtake = new Outtake();

    @Override
    public void runOpMode() throws InterruptedException {

        Gamepad currentGamepad1 = new Gamepad();
        Gamepad previousGamepad1 = new Gamepad();

        Gamepad currentGamepad2 = new Gamepad();
        Gamepad previousGamepad2 = new Gamepad();

        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());



        drive.init(hardwareMap);
        intake.init(hardwareMap);
        outtake.init(hardwareMap);


        clawTimer.reset();


        // Zero mechanisms
        outtake.transferDeposit();

        intake.moveIntakeZero();
        outtake.moveTurretZero();
        outtake.moveOuttakeZero();

        waitForStart();

        intake.zeroIntake();
        outtake.zeroTurret();
        outtake.zeroOuttake();

        if (isStopRequested()) return;

        while (opModeIsActive()) {
            currentGamepad1.copy(gamepad1);


            // Drive
            double forward = -gamepad1.left_stick_y;
            double strafe = gamepad1.left_stick_x;
            double turn = gamepad1.right_stick_x;

            if(gamepad1.left_bumper) {
                drive.drive(forward * .3, strafe * .3, turn * .3);
            } else {
                drive.drive(forward, strafe, turn);
            }




            // arm and grab

            if (gamepad1.dpad_right) {
                intake.dropArm();
                intake.openClaw();
            }
            // grab, flip, ungrab, retract FSM

            switch (clawState) {
                case READY:

                    if (gamepad1.x && intake.isArmDown()) {
                        intake.closeClaw();

                        clawTimer.reset();
                        clawState = ClawState.GRAB;
                    }

                    break;
                case GRAB:

                    if (clawTimer.seconds() > .3) {
                        intake.flipArm();
                        intake.transferPosition();

                        outtake.transferDeposit();
                        outtake.retractSlide();
                        outtake.setTurretMiddle();
                        outtake.guideDown();

                        clawTimer.reset();
                        clawState = ClawState.FLIP;
                    }

                    break;
                case FLIP:

                    if (clawTimer.seconds() > .8) {
                        intake.openClaw();

                        outtake.zeroOuttake();

                        clawTimer.reset();
                        clawState = ClawState.RELEASE;
                    }

                    break;
                case RELEASE:

                    if (clawTimer.seconds() > .2) {
                        intake.contractArm();

                        clawTimer.reset();
                        clawState = ClawState.RETRACT;
                    }

                    break;
                case RETRACT:

                    if (clawTimer.seconds() > .1) {
                        clawState = ClawState.READY;
                    }

                    break;
            }




            // left outtake positions

            if (gamepad1.dpad_up) { // left High
                outtake.setTurretLeft();
                outtake.extendSlideLeft();
                outtake.midDeposit();
            } else if (gamepad1.dpad_left) { // left Medium
                outtake.setTurretLeft();
                outtake.slideLeftMid();
                outtake.midDeposit();
            } else if (gamepad1.dpad_down) { // left Low
                outtake.setTurretLeft();
                //outtake.slideLeftLow();
                outtake.midDeposit();
            }

            // right outtake positions

            if (gamepad1.y) { // right High
                outtake.setTurretRight();
                outtake.extendSlideRight();
                outtake.midDeposit();
            } else if (gamepad1.b) { // right Medium
                outtake.setTurretRight();
                outtake.slideRightMid();
                outtake.midDeposit();
            } else if (gamepad1.a) { // right Low
                outtake.setTurretRight();
                // outtake.extendLeftMedium
                outtake.midDeposit();
            }


            //guide and score
            if (gamepad1.right_trigger > .15) {
                outtake.guideUpLeft();
            }else {
                outtake.guideDown();
            }

            // Score FSM (Flip, auto retract)

            switch (scoreState) {
                case READY:
                    if (gamepad1.y && ((outtake.getTurret() < 300) || (outtake.getTurret() > 330))) { // y button and turret not middle

                        outtake.scoreDepositLeft();

                        scoreTimer.reset();
                        scoreState = ScoreState.UNGUIDE;
                    }
                    break;
                case UNGUIDE:
                    if (scoreTimer.seconds() >= depositTime + guideOffset) {

                        outtake.guideScore();

                        scoreState = ScoreState.DEPOSIT;
                    }

                    break;
                case DEPOSIT:
                    if (scoreTimer.seconds() >= depositTime) {
                        outtake.transferDeposit();
                        outtake.retractSlide();
                        outtake.setTurretMiddle();
                        outtake.guideDown();


                        scoreState = ScoreState.PREPARE;
                    }
                    break;
                case PREPARE:
                    if (outtake.getExtend() < 50) {

                        scoreTimer.reset();
                        scoreState = ScoreState.READY;
                    }
                    break;
            }












            previousGamepad1.copy(currentGamepad1);
            previousGamepad2.copy(currentGamepad2);

            telemetry.addData("Extend Position: ", outtake.getExtend());
            telemetry.addData("Turret Position: ", outtake.getTurret());
            telemetry.addData("Intake Slide Position: ", intake.getSlide());
            //telemetry.addData("Distance CM: ", intake.getDistanceCM());

            telemetry.update();
        }
    }








}
