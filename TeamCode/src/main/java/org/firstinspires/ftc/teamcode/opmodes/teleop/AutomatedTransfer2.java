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
public class AutomatedTransfer2 extends LinearOpMode {


    boolean reverseToggle = true;

    boolean clawToggle = false;

    boolean armToggle = false;

    int coneHeight = 6;



    public static double guideOffset = -0.4; //.9
    public static double depositTime = 0.6; //.9
    public static double grabTime = .3;
    public static double flipTime = .8; //.95
    public static double transferTime = .2; //.5
    public static double intakeTime = .25;
    public static int depBuffer = 350;


    // States

    public enum RobotState {
        CONTRACT,
        LEFT,
        RIGHT,
        GROUND,
        SEPARATE

    }

    public enum RetractState {
        OUTTAKE,
        INTAKE,
        DONE
    }



    public enum ScoreState {
        READY,
        UNGUIDE,
        DEPOSIT,
        PREPARE,
        GRAB,
        RETRACT_INTAKE,
        FLIP,
        EXTEND_INTAKE,
        EXTEND_OUTTAKE
    }

    public enum GrabState {

        READY,
        EXTEND_INTAKE,
        GRAB,
        RETRACT_INTAKE,
        TRANSFER,
        WAIT,
        EXTEND_OUTTAKE,
        READY2,
        RETRACT_OUTTAKE

    }

    RobotState robotState = RobotState.CONTRACT;

    RetractState retractState = RetractState.DONE;

    ScoreState scoreState = ScoreState.READY;

    GrabState grabState = GrabState.READY;






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


        scoreTimer.reset();

        
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





            // Rising edge detector
            if (currentGamepad1.dpad_left && !previousGamepad1.dpad_left && robotState == RobotState.CONTRACT) { // dpad_left and contract
                // This will set intakeToggle to true if it was previously false
                // and intakeToggle to false if it was previously true,
                // providing a toggling behavior.

                reverseToggle = !reverseToggle;


            }

            // Using the toggle variable to control the robot.
            if (reverseToggle) {
                if(gamepad1.left_bumper) {
                    drive.driveReverse(forward * .3, strafe * .3, turn * .3);
                } else {
                    drive.driveReverse(forward, strafe, turn);
                }
            }
            else {
                if(gamepad1.left_bumper) {
                    drive.drive(forward * .3, strafe * .3, turn * .3);
                } else {
                    drive.drive(forward, strafe, turn);
                }
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
                            outtake.guideUpLeft();

                            scoreState = ScoreState.READY;
                            robotState = RobotState.LEFT;
                        }

                        // Rising edge detector
                        if (currentGamepad1.b && !previousGamepad1.b) {

                            intake.readyPosition();
                            intake.openClaw();
                            intake.dropArm();

                            outtake.extendSlideRight();
                            outtake.setTurretRight();
                            outtake.midDeposit();
                            outtake.guideUpLeft();

                            scoreState = ScoreState.READY;
                            robotState = RobotState.RIGHT;
                        }

                        // Rising edge detector
                        if (currentGamepad1.dpad_down && !previousGamepad1.dpad_down) {

                            robotState = RobotState.GROUND;
                        }

                        // Rising edge detector
                        if (currentGamepad2.dpad_down && !previousGamepad2.dpad_down) {

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
                        outtake.scoreDepositLeft(); // Score
                    } else if (!currentGamepad1.right_bumper && previousGamepad1.right_bumper) { // Falling edge detector
                        outtake.midDeposit(); // Go Back
                    }


                    // Adjust Outtake Extension
                    if (currentGamepad1.dpad_down && !previousGamepad1.dpad_down) {
                        outtake.lessExtendLeft();
                    } else if (currentGamepad1.dpad_up && !previousGamepad1.dpad_up) {
                        outtake.moreExtendLeft();
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
                        outtake.scoreDepositRight(); // Score
                    } else if (!currentGamepad1.right_bumper && previousGamepad1.right_bumper) { // Falling edge detector
                        outtake.midDeposit(); // Go Back
                    }


                    // Adjust Outtake Extension
                    if (currentGamepad1.dpad_down && !previousGamepad1.dpad_down) {
                        outtake.lessExtendRight();
                    } else if (currentGamepad1.dpad_up && !previousGamepad1.dpad_up) {
                        outtake.moreExtendRight();
                    }



                    break;
                case SEPARATE:

                    grabCone();

                    if (currentGamepad1.dpad_up && !currentGamepad1.dpad_up &&    coneHeight < 6){
                        coneHeight++;
                    } else if (currentGamepad1.dpad_down && !currentGamepad1.dpad_down &&    coneHeight > 2){
                        coneHeight--;
                    }


                    break;
                case GROUND:





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
                        intake.dropArmGround();
                    }



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




                    break;

            }

            if (gamepad1.a) { // Retract
                retractState = RetractState.OUTTAKE;
                robotState = RobotState.CONTRACT;
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

    public void contract() {
        switch (retractState) {
            case OUTTAKE:
                outtake.setTurretMiddle();
                outtake.transferDeposit();
                outtake.moveSlide(5, 0.5);
                outtake.guideDown();

                scoreTimer.reset();
                retractState = RetractState.INTAKE;
                break;
            case INTAKE:

                if (scoreTimer.seconds() >= .7) {
                    intake.moveToPos(5, 0.5);
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

                    intake.openClaw();
                    intake.intakePosition();


                    scoreState = ScoreState.PREPARE;
                }
                break;
            case PREPARE:
                if (intake.intakeOutDiff() < 20 || intake.getDistanceCM() < 1) {
                    intake.closeClaw();


                    scoreTimer.reset();
                    scoreState = ScoreState.GRAB;
                }
                break;
            case GRAB:
                if (scoreTimer.seconds() >= grabTime) {
                    intake.transferPosition();
                    intake.flipArm();

                    scoreTimer.reset();
                    scoreState = ScoreState.RETRACT_INTAKE;
                }
                break;
            case RETRACT_INTAKE:
                if (scoreTimer.seconds() >= flipTime) {
                    intake.openClaw();

                    outtake.zeroOuttake();
                    scoreTimer.reset();
                    scoreState = ScoreState.FLIP;
                }
                break;
            case FLIP:
                if (scoreTimer.seconds() >= transferTime) {
                    intake.readyPosition();
                    intake.dropArm();

                    scoreTimer.reset();
                    scoreState = ScoreState.EXTEND_INTAKE;
                }
                break;
            case EXTEND_INTAKE:
                if (scoreTimer.seconds() >= intakeTime) {
                    outtake.midDeposit();
                    outtake.setTurretLeft();
                    outtake.extendSlideLeft();
                    outtake.guideUpLeft();

                    scoreState = ScoreState.EXTEND_OUTTAKE;
                }
                break;
            case EXTEND_OUTTAKE:
                if (outtake.slideOutDiffLeft() < depBuffer) {

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

                    outtake.scoreDepositRight();

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
                if (scoreTimer.seconds() >= grabTime) {
                    intake.transferPosition();
                    intake.flipArm();

                    scoreTimer.reset();
                    scoreState = ScoreState.RETRACT_INTAKE;
                }
                break;
            case RETRACT_INTAKE:
                if (scoreTimer.seconds() >= flipTime) {
                    intake.openClaw();
                    outtake.zeroOuttake();
                    scoreTimer.reset();
                    scoreState = ScoreState.FLIP;
                }
                break;
            case FLIP:
                if (scoreTimer.seconds() >= transferTime) {
                    intake.readyPosition();
                    intake.dropArm();

                    scoreTimer.reset();
                    scoreState = ScoreState.EXTEND_INTAKE;
                }
                break;
            case EXTEND_INTAKE:
                if (scoreTimer.seconds() >= intakeTime) {
                    outtake.midDeposit();
                    outtake.setTurretRight();
                    outtake.extendSlideRight();
                    outtake.guideUpLeft();

                    scoreState = ScoreState.EXTEND_OUTTAKE;
                }
                break;
            case EXTEND_OUTTAKE:
                if (outtake.slideOutDiffRight() < 40) {

                    scoreState = ScoreState.READY;
                }
                break;
            default:
                // should never be reached, as liftState should never be null
                scoreState = ScoreState.READY;
                break;

        }
    }




    public void grabCone(){
        switch (grabState){
            case READY:
                if (gamepad2.a) {
                    intake.openClaw();
                    intake.dropArmAuto(coneHeight);
                    intake.intakePosition();

                    grabState = GrabState.EXTEND_INTAKE;
                }
                break;
            case EXTEND_INTAKE:
                if (gamepad1.y) {
                    intake.closeClaw();

                    scoreTimer.reset();
                    grabState = GrabState.GRAB;
                }
                break;
            case GRAB:
                if (scoreTimer.seconds() >= grabTime) {
                    intake.transferPosition();
                    intake.flipArm();

                    scoreTimer.reset();
                    grabState = GrabState.RETRACT_INTAKE;
                }
                break;
            case RETRACT_INTAKE:
                if (scoreTimer.seconds() >= flipTime && intake.intakeInDiff() < 10 && outtake.retractDiff() < 10) {
                    intake.openClaw();

                    scoreTimer.reset();
                    grabState = GrabState.TRANSFER;
                }
                break;
            case TRANSFER:
                if (scoreTimer.seconds() >= transferTime) {
                    intake.contractArm();
                    intake.openClaw();

                    scoreTimer.reset();
                    grabState = GrabState.WAIT;
                }
                break;
            case WAIT:
                if (gamepad2.x){
                    outtake.setTurretLeft();
                    outtake.extendSlideLeft();

                    grabState = GrabState.EXTEND_OUTTAKE;
                }

                if (gamepad2.b){
                    outtake.setTurretRight();
                    outtake.extendSlideRight();

                    grabState = GrabState.EXTEND_OUTTAKE;
                }

                break;
            case EXTEND_OUTTAKE:
                if (outtake.slideOutDiffLeft() < 40 || outtake.slideOutDiffRight() < 40) {

                    grabState = GrabState.READY2;
                }
                break;
            case READY2:
                if (gamepad1.right_bumper){
                    outtake.scoreDepositRight();
                }
                if (gamepad2.a) {

                    outtake.transferDeposit();
                    outtake.retractSlide();
                    outtake.setTurretMiddle();

                    grabState = GrabState.RETRACT_OUTTAKE;
                }
                break;
            case RETRACT_OUTTAKE:

                if (outtake.getExtend() < 50) {
                    grabState = GrabState.READY;
                }

                break;
        }
    }






}
