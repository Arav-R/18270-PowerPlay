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


    boolean intakeToggle = false;

    boolean haveCone = false;

    // Variables
    public static double guideOffset = -0.4; //.9
    public static double depositTime = 0.6; //.9


    int coneHeight = 6;




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

    ElapsedTime endgameTimer = new ElapsedTime();


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

        Gamepad.RumbleEffect countdown = new Gamepad.RumbleEffect.Builder()
                .addStep(1, 0, 500)  //  Rumble right motor 100% for 500 mSec
                .addStep(0.0, 0.0, 500)  //  Pause for 300 mSec

                .addStep(0, 1, 500)  //  Rumble left motor 100% for 250 mSec
                .addStep(0.0, 0.0, 500)  //  Pause for 300 mSec

                .addStep(1, 0, 500)  //  Rumble right motor 100% for 500 mSec
                .addStep(0.0, 0.0, 500)  //  Pause for 300 mSec

                .addStep(0, 1, 500)  //  Rumble left motor 100% for 250 mSec
                .addStep(0.0, 0.0, 500)  //  Pause for 250 mSec

                .addStep(1.0, 1, 500)  //  Rumble left motor 100% for 250 mSec
                .build();



        drive.init(hardwareMap);
        intake.init(hardwareMap);
        outtake.init(hardwareMap);


        clawTimer.reset();


        // Zero mechanisms
        outtake.transferDeposit();
        intake.contractArm();


        intake.moveIntakeZero();
        outtake.moveTurretZero();
        outtake.moveOuttakeZero();

        waitForStart();

        endgameTimer.reset();

        intake.zeroIntake();
        outtake.zeroTurret();
        outtake.zeroOuttake();

        // runon init

        outtake.transferDeposit();
        outtake.retractSlide();
        outtake.setTurretMiddle();
        outtake.guideDown();

        intake.contractArm();
        intake.openClaw();
        intake.holdIntakeSlide();

        if (isStopRequested()) return;

        while (opModeIsActive()) {
            currentGamepad1.copy(gamepad1);


            // Drive
            double forward = -gamepad1.left_stick_y;
            double strafe = gamepad1.left_stick_x;
            double turn = gamepad1.right_stick_x;

            if(gamepad1.left_bumper) {
                drive.driveReverse(forward * .3, strafe * .3, turn * .3); // outtake forward
            } else {
                drive.driveReverse(forward, strafe, turn);
            }



            // toggle
            // Rising edge detector
            if (currentGamepad1.x && !previousGamepad1.x) {
                // This will set intakeToggle to true if it was previously false
                // and intakeToggle to false if it was previously true,
                // providing a toggling behavior.
                intakeToggle = !intakeToggle;
            }

            // Using the toggle variable to control the robot.
            if (intakeToggle) {
                // grab, flip, ungrab, retract FSM

                grabCone();

            }
            else { // arm down
                intake.dropArmAutoR(coneHeight);
                intake.openClaw();

                clawState = ClawState.READY;

                if (intake.getDistanceCM() < 1) {
                    intakeToggle = true;
                }

                // Velocity auto zero outtake
                if (outtake.getExtend() < 100 && Math.abs(outtake.getOuttakeSlideVelocity1()) < 0.1 && outtake.getExtendTarget() == 0) {
                    outtake.zeroOuttake();
                }

            }


            // arm height

            if (currentGamepad2.dpad_up && !currentGamepad2.dpad_up &&    coneHeight < 6){
                coneHeight++;
            } else if (currentGamepad2.dpad_down && !currentGamepad2.dpad_down &&    coneHeight > 2){
                coneHeight--;
            }


            // retract emergency

            if (gamepad1.left_trigger > .15) { // left High
                outtake.transferDeposit();
                outtake.retractSlide();
                outtake.setTurretMiddle();
                outtake.guideDown();
            }

            // left outtake positions

            if (gamepad1.dpad_up) { // left High
                outtake.setTurretLeft();
                outtake.extendSlideLeft();
                outtake.midDeposit();

                scoreState = ScoreState.READY;
            } else if (gamepad1.dpad_left) { // left Medium
                outtake.setTurretLeft();
                outtake.slideLeftMid();
                outtake.midDeposit();

                scoreState = ScoreState.READY;
            } else if (gamepad1.dpad_down) { // left Low
                //outtake.setTurretLeft();
                //outtake.slideLeftLow();
                outtake.midDeposit();

                scoreState = ScoreState.READY;
            }

            // right outtake positions

            if (gamepad1.y) { // right High
                outtake.setTurretRight();
                outtake.extendSlideRight();
                outtake.midDeposit();

                scoreState = ScoreState.READY;
            } else if (gamepad1.b) { // right Medium
                outtake.setTurretRight();
                outtake.slideRightMid();
                outtake.midDeposit();

                scoreState = ScoreState.READY;
            } else if (gamepad1.a) { // right Low
                //outtake.setTurretRightLow();
                // outtake.extendLeftMedium
                outtake.midDeposit();

                scoreState = ScoreState.READY;
            }


            //guide and score
            if (gamepad1.right_trigger > .15) {
                if (outtake.getExtend() < 100) {
                    outtake.guideUpLow();
                } else {
                    outtake.guideUpLeft();
                }
            }else {
                outtake.guideDown();
            }

            // Score FSM (Flip, auto retract)

            scoreCone();


            // Endgame timer
            if (endgameTimer.seconds() == 90) {
                gamepad1.rumble(1000); // rumble for 1 second

                gamepad1.setLedColor(0, 1, 0, Gamepad.LED_DURATION_CONTINUOUS);
            }

            if (endgameTimer.seconds() == 115) { // 5 seconds left
                gamepad1.runRumbleEffect(countdown); // rumble for 1 second

                gamepad1.setLedColor(1.0, 0, 0, Gamepad.LED_DURATION_CONTINUOUS);
            }





            previousGamepad1.copy(currentGamepad1);
            previousGamepad2.copy(currentGamepad2);

            telemetry.addData("Extend Position: ", outtake.getExtend());
            telemetry.addData("Turret Position: ", outtake.getTurret());
            telemetry.addData("Intake Slide Position: ", intake.getSlide());
            telemetry.addData("Distance CM: ", intake.getDistanceCM());

            // motor currents
            telemetry.addData("Intake Slide Current: ", intake.getIntakeSlideCurrent());
            telemetry.addData("Outtake Slide 1 Current: ", outtake.getOuttakeSlideCurrent1());
            telemetry.addData("Outtake Slide 2 Current: ", outtake.getOuttakeSlideCurrent2());
            telemetry.addData("Turret Current: ", outtake.getTurretCurrent());

            telemetry.addData("Outtake Velocity: ", outtake.getOuttakeSlideVelocity1());



            telemetry.update();
        }
    }

    public void grabCone(){
        switch (clawState) {
            case READY:

                if (intake.isArmDown()) {
                    intake.closeClaw();

                    clawTimer.reset();
                    clawState = ClawState.GRAB;
                }

                break;
            case GRAB:

                if (clawTimer.seconds() > .3) {
                    intake.flipArm();
                    intake.holdIntakeSlide();

                    outtake.transferDeposit();
                    outtake.retractSlide();
                    outtake.setTurretMiddle();
                    outtake.guideDown();

                    clawTimer.reset();
                    clawState = ClawState.FLIP;
                }

                break;
            case FLIP:

                if (clawTimer.seconds() > .9) {
                    intake.openClaw();

                    outtake.zeroOuttake();

                    clawTimer.reset();
                    clawState = ClawState.RELEASE;
                }

                // bypass if no cone
                if (intake.getDistanceCM() > 3) {
                    intake.contractArm();

                    clawTimer.reset();
                    clawState = ClawState.RETRACT;
                }

                break;
            case RELEASE:

                if (clawTimer.seconds() > .35) {
                    haveCone = true;

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

    }

    public void scoreCone(){
        switch (scoreState) {
            case READY:
                if (gamepad1.right_bumper && haveCone) { // right bumper button and have a cone

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

    }







}
