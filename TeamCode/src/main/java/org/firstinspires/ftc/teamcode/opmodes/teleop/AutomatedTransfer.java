package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;


import org.firstinspires.ftc.teamcode.opmodes.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.opmodes.subsystems.Intake;
import org.firstinspires.ftc.teamcode.opmodes.subsystems.Outtake;

@TeleOp
public class AutomatedTransfer extends LinearOpMode {


    boolean clawToggle = false;

    boolean armToggle = false;

    // States

    public enum RobotState {
        CONTRACT,
        LEFT,
        RIGHT,
        GROUND

    }

    public enum RetractState {
        OUTTAKE,
        INTAKE,
        DONE
    }



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

    RobotState robotState = RobotState.CONTRACT;

    RetractState retractState = RetractState.DONE;

    ScoreState scoreState = ScoreState.READY;





    ElapsedTime scoreTimer = new ElapsedTime();
    //ElapsedTime readyTimer = new ElapsedTime();

    Drivetrain drive = new Drivetrain();
    Intake intake = new Intake();
    Outtake outtake = new Outtake();







    @Override
    public void runOpMode() throws InterruptedException {
        Gamepad currentGamepad1 = new Gamepad();
        Gamepad previousGamepad1 = new Gamepad();

        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());



        drive.init(hardwareMap);
        intake.init(hardwareMap);
        outtake.init(hardwareMap);


        scoreTimer.reset();

        waitForStart();

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

            // State machine

            switch (robotState) {
                case CONTRACT:

                    contract();

                    if (retractState == RetractState.DONE) {
                        // Rising edge detector
                        if (currentGamepad1.x && !previousGamepad1.x) {

                            intake.readyPosition();
                            intake.openClaw();
                            intake.dropArm();

                            outtake.extendSlideLeft();
                            outtake.setTurretLeft();
                            outtake.midDeposit();

                            robotState = RobotState.LEFT;
                        }

                        // Rising edge detector
                        if (currentGamepad1.b && !previousGamepad1.b) {

                            intake.readyPosition();
                            intake.openClaw();
                            intake.dropArm();

                            outtake.extendSlideLeft();
                            outtake.setTurretRight();
                            outtake.midDeposit();

                            robotState = RobotState.RIGHT;
                        }

                        // Rising edge detector
                        if (currentGamepad1.dpad_down && !previousGamepad1.dpad_down) {

                            robotState = RobotState.GROUND;
                        }
                    }

                    break;
                case LEFT:

                    cycleLeft();

                    // Adjust Side to Side
                    if (currentGamepad1.dpad_left && !previousGamepad1.dpad_left) {
                        outtake.nudgeLeftLeft();
                    } else if (currentGamepad1.dpad_right && !previousGamepad1.dpad_right) {
                        outtake.nudgeLeftRight();
                    }

                    // Test Placement
                    if (currentGamepad1.right_bumper && !previousGamepad1.right_bumper) {
                        outtake.scoreDeposit(); // Score
                    } else if (!currentGamepad1.right_bumper && previousGamepad1.right_bumper) { // Falling edge detector
                        outtake.midDeposit(); // Go Back
                    }


                    // Adjust Outtake Extension
                    if (currentGamepad1.dpad_down && !previousGamepad1.dpad_down) {
                        outtake.lessExtend();
                    } else if (currentGamepad1.dpad_up && !previousGamepad1.dpad_up) {
                        outtake.moreExtend();
                    }





                    break;
                case RIGHT:

                    cycleRight();

                    // Adjust Side to Side
                    if (currentGamepad1.dpad_left && !previousGamepad1.dpad_left) {
                        outtake.nudgeRightLeft();
                    } else if (currentGamepad1.dpad_right && !previousGamepad1.dpad_right) {
                        outtake.nudgeRightRight();
                    }

                    // Test Placement
                    if (currentGamepad1.right_bumper && !previousGamepad1.right_bumper) {
                        outtake.scoreDeposit(); // Score
                    } else if (!currentGamepad1.right_bumper && previousGamepad1.right_bumper) { // Falling edge detector
                        outtake.midDeposit(); // Go Back
                    }


                    // Adjust Outtake Extension
                    if (currentGamepad1.dpad_down && !previousGamepad1.dpad_down) {
                        outtake.lessExtend();
                    } else if (currentGamepad1.dpad_up && !previousGamepad1.dpad_up) {
                        outtake.moreExtend();
                    }



                    break;
                case GROUND:


                    // Rising edge detector
                    if (currentGamepad1.b && !previousGamepad1.b) {
                        // This will set intakeToggle to true if it was previously false
                        // and intakeToggle to false if it was previously true,
                        // providing a toggling behavior.
                        clawToggle = !clawToggle;
                    }

                    // Using the toggle variable to control the robot.
                    if (clawToggle) {
                        intake.closeClaw();
                    }
                    else {
                        if (!armToggle) {  // Arm down
                            intake.openClaw();
                        }

                    }


                    // Rising edge detector
                    if (currentGamepad1.y && !previousGamepad1.y) {
                        // This will set intakeToggle to true if it was previously false
                        // and intakeToggle to false if it was previously true,
                        // providing a toggling behavior.
                        armToggle = !armToggle;
                    }

                    // Using the toggle variable to control the robot.
                    if (armToggle) {
                        intake.flipArm();
                    }
                    else {
                        intake.dropArm();
                    }

                    break;
            }

            if (gamepad1.a) { // Retract
                retractState = RetractState.OUTTAKE;
                robotState = RobotState.CONTRACT;
            }















            previousGamepad1.copy(currentGamepad1);

            telemetry.addData("Extend Position: ", outtake.getExtend());
            telemetry.addData("Turret Position: ", outtake.getTurret());

            telemetry.update();
        }
    }

    public void contract() {
        switch (retractState) {
            case OUTTAKE:
                outtake.setTurretMiddle();
                outtake.transferDeposit();
                outtake.moveSlide(0, 0.5);

                scoreTimer.reset();
                retractState = RetractState.INTAKE;
                break;
            case INTAKE:

                if (scoreTimer.seconds() >= .7) {
                    intake.moveToPos(0, 0.5);
                    intake.openClaw();
                    intake.contractArm();

                    retractState = RetractState.DONE;
                }
                break;
            case DONE:


                break;
        }
    }

    public void cycleLeft() {
        switch (scoreState) {
            case READY:
                if (gamepad1.y) {

                    outtake.scoreDeposit();

                    scoreTimer.reset();
                    scoreState = ScoreState.DEPOSIT;
                }
                break;
            case DEPOSIT:
                if (scoreTimer.seconds() >= .7) {
                    outtake.transferDeposit();
                    outtake.retractSlide();
                    outtake.setTurretMiddle();

                    intake.openClaw();
                    intake.intakePosition();


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
                if (scoreTimer.seconds() >= .5) {
                    intake.transferPosition();
                    intake.flipArm();

                    scoreTimer.reset();
                    scoreState = ScoreState.RETRACT_INTAKE;
                }
                break;
            case RETRACT_INTAKE:
                if (scoreTimer.seconds() >= .75) {
                    intake.openClaw();

                    scoreTimer.reset();
                    scoreState = ScoreState.FLIP;
                }
                break;
            case FLIP:
                if (scoreTimer.seconds() >= .5) {
                    intake.readyPosition();
                    intake.dropArm();

                    scoreTimer.reset();
                    scoreState = ScoreState.EXTEND_INTAKE;
                }
                break;
            case EXTEND_INTAKE:
                if (scoreTimer.seconds() >= .25) {
                    outtake.midDeposit();
                    outtake.setTurretLeft();
                    outtake.extendSlideLeft();

                    scoreState = ScoreState.EXTEND_OUTTAKE;
                }
                break;
            case EXTEND_OUTTAKE:
                if (outtake.slideOutDiff() < 10) {

                    scoreState = ScoreState.READY;
                }
                break;
            default:
                // should never be reached, as liftState should never be null
                scoreState = ScoreState.READY;
                break;

        }
    }

    public void cycleRight() {
        switch (scoreState) {
            case READY:
                if (gamepad1.y) {

                    outtake.scoreDeposit();
                    scoreTimer.reset();

                    scoreState = ScoreState.DEPOSIT;
                }
                break;
            case DEPOSIT:
                if (scoreTimer.seconds() >= .7) {
                    outtake.transferDeposit();
                    outtake.retractSlide();
                    outtake.setTurretMiddle();

                    intake.openClaw();
                    intake.intakePosition();


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
                if (scoreTimer.seconds() >= .5) {
                    intake.transferPosition();
                    intake.flipArm();

                    scoreTimer.reset();
                    scoreState = ScoreState.RETRACT_INTAKE;
                }
                break;
            case RETRACT_INTAKE:
                if (scoreTimer.seconds() >= .6) {
                    intake.openClaw();

                    scoreTimer.reset();
                    scoreState = ScoreState.FLIP;
                }
                break;
            case FLIP:
                if (scoreTimer.seconds() >= .75) {
                    intake.readyPosition();
                    intake.dropArm();

                    scoreTimer.reset();
                    scoreState = ScoreState.EXTEND_INTAKE;
                }
                break;
            case EXTEND_INTAKE:
                if (scoreTimer.seconds() >= .25) {
                    outtake.midDeposit();
                    outtake.setTurretRight();
                    outtake.extendSlideRight();



                    scoreState = ScoreState.EXTEND_OUTTAKE;
                }
                break;
            case EXTEND_OUTTAKE:
                if (outtake.slideOutDiff() < 10) {

                    scoreState = ScoreState.READY;
                }
                break;
            default:
                // should never be reached, as liftState should never be null
                scoreState = ScoreState.READY;
                break;

        }
    }




}
