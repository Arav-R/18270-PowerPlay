package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.opmodes.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.opmodes.subsystems.Intake;
import org.firstinspires.ftc.teamcode.opmodes.subsystems.Outtake;

@Config
@TeleOp
public class AutomatedStack extends LinearOpMode {


    // circuit variables
    
    boolean circuitToggle = true;


    boolean intakeToggle = true;

    boolean haveCone = false;

    boolean beacon = false;

    // Variables
    public static double guideOffset = -0.4; //.9
    public static double depositTime = 0.6; //.9

    public static double grabTime = .5; //.35
    public static double flipTime = .9; //.7
    public static double transferTime = .9; //.35
    public static double intakeTime = .1; //
    public static double beaconTime = .3; //






    int coneHeight = 6;




    // Grab FSM
    public enum ClawState {
        READY,
        GRAB,
        FLIP,
        RELEASE,
        RETRACT,
        BEACON
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

    // Low Pole Arm FSM
    public enum LowState {
        READY,
        GRAB,
        LIFT,
        READY2,
        RELEASE
    }

    LowState lowState = LowState.READY;



    // Instantiate Classes

    ElapsedTime clawTimer = new ElapsedTime();
    ElapsedTime scoreTimer = new ElapsedTime();
    ElapsedTime lowTimer = new ElapsedTime();


    ElapsedTime endgameTimer = new ElapsedTime();

    // get battery voltage
    private VoltageSensor batteryVoltageSensor;
    private double batteryVoltage;


    // cycle variables


    //public static double guideOffset = -0.4; //.9
    //public static double depositTime = 0.6; //.9
    public static double grabTime2 = .25; //
    public static double flipTime2 = .72; //.7
    public static double transferTime2 = .15; //.1
    public static double intakeTime2 = .25; //
    public static int depBuffer = 650; // 650
    public static double depositTime2 = 0.55; //.9




    public enum RobotState {
        CONTRACT,
        LEFT,
        RIGHT

    }

    public enum RetractState {
        OUTTAKE,
        INTAKE,
        DONE
    }

    
    public enum CycleState {
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
    
    RobotState robotState = RobotState.CONTRACT;

    RetractState retractState = RetractState.OUTTAKE;

    CycleState cycleState = CycleState.READY;


    //ElapsedTime readyTimer = new ElapsedTime();

    Drivetrain drive = new Drivetrain();
    Intake intake = new Intake();
    Outtake outtake = new Outtake();

    @Override
    public void runOpMode() throws InterruptedException {

        batteryVoltageSensor = hardwareMap.voltageSensor.iterator().next();

        batteryVoltage = batteryVoltageSensor.getVoltage();
        // voltage comp
        guideOffset = guideOffset * (12 / batteryVoltage);
        depositTime = depositTime * (12 / batteryVoltage);

        grabTime = grabTime * (12 / batteryVoltage);
        flipTime = flipTime * (12 / batteryVoltage);
        transferTime = transferTime * (12 / batteryVoltage);
        intakeTime = intakeTime * (12 / batteryVoltage);


        grabTime2 = grabTime2 * (12 / batteryVoltage);
        flipTime2 = flipTime2 * (12 / batteryVoltage);
        transferTime2 = transferTime2 * (12 / batteryVoltage);
        intakeTime2 = intakeTime2 * (12 / batteryVoltage);
        depositTime2 = depositTime2 * (12 / batteryVoltage);



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


            // Voltage
            batteryVoltage = batteryVoltageSensor.getVoltage();


            // Drive
            double forward = -gamepad1.left_stick_y;
            double strafe = gamepad1.left_stick_x;
            double turn = gamepad1.right_stick_x;

            if(gamepad1.left_bumper) {
                drive.driveReverse(forward * .3, strafe * .3, turn * .3); // outtake forward
            } else {
                drive.driveReverse(forward, strafe, turn);
            }


            // BIG ROBOT TOGGLE

            // Rising edge detector
            if (currentGamepad1.ps && !previousGamepad1.ps) { // home button
                // This will set intakeToggle to true if it was previously false
                // and intakeToggle to false if it was previously true,
                // providing a toggling behavior.
                outtake.transferDeposit();
                outtake.retractSlide();
                outtake.setTurretMiddle();
                outtake.guideDown();

                intake.contractArm();
                intake.openClaw();

                if (intake.getSlide() < 10) {
                    intake.holdIntakeSlide();
                } else {
                    intake.transferPosition(); // fast retraction
                }

                // Circuit
                ClawState clawState = ClawState.READY;
                ScoreState scoreState = ScoreState.READY;


                // Cycle
                RobotState robotState = RobotState.CONTRACT;
                RetractState retractState = RetractState.OUTTAKE;
                CycleState cycleState = CycleState.READY;




                circuitToggle = !circuitToggle;
            }







            // Using the toggle variable to control the robot.
            if (circuitToggle) { // default


                // toggle
                // Rising edge detector
                if ((currentGamepad1.x && !previousGamepad1.x) || (currentGamepad1.a && !previousGamepad1.a) || (currentGamepad1.share && !previousGamepad1.share)){

                    if (currentGamepad1.share && !previousGamepad1.share) {
                        beacon = true;
                    }

                    if (intakeToggle && intake.getDistanceCM() < 3) { // fsm and have cone on press
                        intake.flipArm();
                        if (intake.getSlide() < 10) {
                            intake.holdIntakeSlide();
                        } else {
                            intake.transferPosition(); // fast retraction
                        }

                        clawTimer.reset();
                        clawState = ClawState.BEACON;
                    } else {
                        // This will set intakeToggle to true if it was previously false
                        // and intakeToggle to false if it was previously true,
                        // providing a toggling behavior.
                        intakeToggle = !intakeToggle;
                    }

                }

                // Using the toggle variable to control the robot.
                if (intakeToggle) {
                    // grab, flip, ungrab, retract FSM

                    grabCone();

                }
                else { // arm down
                    if (gamepad1.x) {
                        intake.dropArmAutoR(coneHeight);
                        intake.openClaw();

                        if (intake.getSlide() < 10) {
                            intake.holdIntakeSlide();
                        } else {
                            intake.transferPosition(); // fast retraction
                        }
                    } else if (gamepad1.a) {
                        intake.dropArmAutoR(coneHeight);
                        intake.openClaw();

                        intake.intakePositionCircuit();
                    }

                    clawState = ClawState.READY;

                    if (intake.getDistanceCM() < 2) {
                        //intakeToggle = true;
                    }


                }

                // low pole with arm FSM
                //lowPoleArm();


                // arm height

                if (currentGamepad1.dpad_down && !previousGamepad1.dpad_down &&    coneHeight < 6){
                    coneHeight++;
                } else if (currentGamepad1.dpad_right && !previousGamepad1.dpad_right &&    coneHeight > 2){
                    coneHeight--;
                }

                // Velocity auto zero outtake
                if (outtake.getExtend() < 100 && Math.abs(outtake.getOuttakeSlideVelocity1()) < 0.1 && outtake.getExtendTarget() == 0) {
                    outtake.zeroOuttake();
                }

                // Velocity auto zero intake
                if (intake.getSlide() < 10 && Math.abs(intake.getIntakeSlideVelocity1()) < 0.1 && intake.getIntakeTarget() <= 0) {
                    intake.zeroIntake();

                    intake.holdIntakeSlide();
                }


                // retract emergency

                if (gamepad1.left_trigger > .15) {
                    //outtake.transferDeposit();
                    outtake.retractSlideSlow();
                    outtake.setTurretMiddle();
                    //outtake.guideDown();
                }


                if (clawState == ClawState.READY) {

                    // left outtake positions

                    if (gamepad1.dpad_up) { // left High
                        outtake.setTurretLeftHigh();
                        outtake.extendSlideLeft();
                        outtake.midDeposit();

                        scoreState = ScoreState.READY;
                    } else if (gamepad1.dpad_left) { // left Medium
                        outtake.setTurretLeftMid();
                        outtake.slideLeftMid();
                        outtake.midDeposit();

                        scoreState = ScoreState.READY;
                    }
//            else if (gamepad1.dpad_down) { // left Low
//                //outtake.setTurretLeft();
//                //outtake.slideLeftLow();
//                outtake.midDeposit();
//
//                scoreState = ScoreState.READY;
//            }

                    // right outtake positions


                    if (gamepad1.y) { // right High
                        outtake.setTurretRightHigh();
                        outtake.extendSlideRight();
                        outtake.midDeposit();

                        scoreState = ScoreState.READY;
                    } else if (gamepad1.b) { // right Medium
                        outtake.setTurretRightMid();
                        outtake.slideRightMid();
                        outtake.midDeposit();

                        scoreState = ScoreState.READY;
                    }
//                    else if (gamepad1.a) { // right Low
//                        //outtake.setTurretRightLow();
//                        // outtake.extendLeftMedium
//                        outtake.midDeposit();
//
//                        scoreState = ScoreState.READY;
//                    }

                }



                //guide and score
                if (gamepad1.right_trigger > .15) {
                    if (scoreState != ScoreState.UNGUIDE && scoreState != ScoreState.DEPOSIT) {
                        if (outtake.getExtend() < 100) {
                            outtake.guideUpLow();
                            outtake.midDeposit();
                        } else {
                            outtake.guideUpLeft();
                        }
                    }
                }else {
                    outtake.guideDown();
                }

                // Score FSM (Flip, auto retract)

                scoreCone();

            }
            else { // cycle code



                // State machine

                switch (robotState) {
                    case CONTRACT:

                        contract();

                        if (retractState == RetractState.DONE) {
                            // Rising edge detector
                            if (currentGamepad1.x && !previousGamepad1.x) {

                                intake.readyPosition();
                                intake.openClaw();
                                intake.dropArmAutoR(coneHeight);

                                outtake.extendSlideLeftCycle();
                                outtake.setTurretLeftHigh();
                                outtake.midDeposit();
                                outtake.guideUpLeft();

                                cycleState = CycleState.READY;
                                robotState = RobotState.LEFT;
                            }

                            // Rising edge detector
                            if (currentGamepad1.b && !previousGamepad1.b) {

                                intake.readyPosition();
                                intake.openClaw();
                                intake.dropArmAutoR(coneHeight);

                                outtake.extendSlideRight();
                                outtake.setTurretRightHigh();
                                outtake.midDeposit();
                                outtake.guideUpLeft();

                                cycleState = CycleState.READY;
                                robotState = RobotState.RIGHT;
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

                }

                if (gamepad1.a) { // Retract
                    retractState = RetractState.OUTTAKE;
                    robotState = RobotState.CONTRACT;
                }

                if (gamepad1.share) {
                    coneHeight = 2;
                } else if ((currentGamepad1.left_trigger > .5) && !(previousGamepad1.left_trigger > .5) &&    coneHeight < 6){ // down
                    coneHeight++;
                } else if ((currentGamepad1.right_trigger > .5) && !(previousGamepad1.right_trigger > .5) &&    coneHeight > 2){ // up
                    coneHeight--;
                }


            }




            // Endgame timer
            if (endgameTimer.seconds() >= 90 && endgameTimer.seconds() < 92) { // rumble for 2 seconds at endgame start
                gamepad1.rumble(2000); // rumble for 1 second

                //gamepad1.setLedColor(0, 1, 0, Gamepad.LED_DURATION_CONTINUOUS);
            }

            if (endgameTimer.seconds() >= 110 && endgameTimer.seconds() < 110.5) { // 5 seconds left
                gamepad1.runRumbleEffect(countdown); // rumble for 1 second

                //gamepad1.setLedColor(1.0, 0, 0, Gamepad.LED_DURATION_CONTINUOUS);
            }




            if (circuitToggle) {
                gamepad1.setLedColor(1.0, 0, 0, Gamepad.LED_DURATION_CONTINUOUS); // red for circuit
            } else {
                gamepad1.setLedColor(0, 1.0, 0, Gamepad.LED_DURATION_CONTINUOUS); // green for cycle
            }







            previousGamepad1.copy(currentGamepad1);
            previousGamepad2.copy(currentGamepad2);

            telemetry.addData("Voltage: ", batteryVoltage);

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

            telemetry.addData("haveCone: ", haveCone);

            telemetry.addData("Intake Slide Power: ", intake.getIntakePower());
            telemetry.addData("Intake Slide Target: ", intake.getIntakeTarget());

            telemetry.addData("coneHeight: ", coneHeight);






            telemetry.update();
        }
    }








    // Circuit Functions

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

                if (clawTimer.seconds() > grabTime) {

                    // beacon
                    if (haveCone) {
                        intake.contractArm();

                        if (intake.getSlide() < 10) {
                            intake.holdIntakeSlide();
                        } else {
                            intake.transferPosition(); // fast retraction
                        }

                        telemetry.addLine("BEACON");

                        clawTimer.reset();
                        clawState = ClawState.RETRACT;

                        break;
                    }


                    intake.flipArm();

                    if (intake.getSlide() < 10) {
                        intake.holdIntakeSlide();
                    } else {
                        intake.transferPosition(); // fast retraction
                    }
                    outtake.transferDeposit();
                    outtake.retractSlide();
                    outtake.setTurretMiddle();
                    outtake.guideDown();

                    clawTimer.reset();
                    clawState = ClawState.FLIP;
                }

                break;
            case FLIP:

                if (clawTimer.seconds() > flipTime && intake.getSlide() < 10) {

                    if (beacon) {
                        intake.closeClaw();
                    } else {
                        intake.openClaw();
                    }
                    outtake.zeroOuttake();

                    clawTimer.reset();
                    clawState = ClawState.RELEASE;
                }

                // bypass if no cone
                if (intake.getDistanceCM() > 5) {
                    intake.contractArm();
                    intake.closeClaw();

                    clawTimer.reset();
                    clawState = ClawState.RETRACT;
                }

                break;
            case RELEASE:

                if (clawTimer.seconds() > transferTime) {
                    haveCone = true;

                    intake.contractArm();
                    intake.closeClaw();

                    clawTimer.reset();
                    clawState = ClawState.RETRACT;
                }

                break;
            case RETRACT:

                if (clawTimer.seconds() > intakeTime) {
                    clawState = ClawState.READY;
                }

                break;
            case BEACON:
                if (clawTimer.seconds() > beaconTime) { // arm from retract to flip
                    intake.openClaw();
                    outtake.zeroOuttake();

                    beacon = false;

                    clawTimer.reset();
                    clawState = ClawState.RELEASE;
                }

                break;
        }

    }

    public void scoreCone(){
        switch (scoreState) {
            case READY:
                if (gamepad1.right_bumper && haveCone) { // right bumper button and have a cone

                    if (outtake.getExtend() < 100) {
                        outtake.scoreDepositLow();

                        depositTime = 1.4;
                        guideOffset = -1;
                    } else {
                        outtake.scoreDepositLeft();

                        depositTime = .6;
                        guideOffset = -.4;
                    }


                    scoreTimer.reset();
                    scoreState = ScoreState.UNGUIDE;
                }
                break;
            case UNGUIDE:
                if (scoreTimer.seconds() >= depositTime + guideOffset) {

                    if (outtake.getExtend() < 100) {
                        outtake.guideDown();
                    } else {
                        outtake.guideScore();
                    }



                    scoreState = ScoreState.DEPOSIT;
                }

                break;
            case DEPOSIT:
                if (scoreTimer.seconds() >= depositTime) {
                    haveCone = false;

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



    public void lowPoleArm(){
        switch (lowState) {
            case READY:
                if (gamepad1.touchpad && intake.isArmDown()) { // right bumper button and have a cone

                    intake.closeClaw();

                    lowTimer.reset();
                    lowState = LowState.GRAB;
                }
                break;
            case GRAB:
                if (lowTimer.seconds() >= grabTime2) {

                    intake.armLowPole();
                    if (intake.getSlide() < 10) {
                        intake.holdIntakeSlide();
                    } else {
                        intake.transferPosition(); // fast retraction
                    }

                    lowTimer.reset();
                    lowState = LowState.LIFT;
                }

                break;
            case LIFT:
                if (lowTimer.seconds() >= .5) {

                    lowState = LowState.READY2;
                }
                break;
            case READY2:
                if (gamepad1.touchpad) { // right bumper button and have a cone

                    intake.openClaw();

                    lowState = LowState.RELEASE;
                }
                break;
            case RELEASE:
                if (lowTimer.seconds() >= .3) {

                    intake.contractArm();

                    intakeToggle = true;
                    clawState = ClawState.READY;

                    lowState = LowState.READY;
                }
                break;
        }

    }






    
    // Cycle Functions

    public void contract() {
        switch (retractState) {
            case OUTTAKE:
                outtake.setTurretMiddle();
                outtake.transferDeposit();
                outtake.moveSlide(35, 0.5);
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

                // Test Placement
                if (gamepad1.right_bumper) {
                    outtake.scoreDepositLeft(); // Score
                } else { // Falling edge detector
                    outtake.transferDeposit(); // Go Back
                }

                // Test Placement
                if (gamepad1.right_trigger > .15) {
                    outtake.guideUpLeft(); // Score
                } else { // Falling edge detector
                    outtake.guideDown(); // Go Back
                }


                break;
        }
    }

    public void cycleLeft() {
        switch (cycleState) {
            case READY:
                if (gamepad1.y) {

                    outtake.scoreDepositLeft();

                    intake.dropArmAutoR(coneHeight);

                    scoreTimer.reset();
                    cycleState = CycleState.UNGUIDE;
                }
                break;
            case UNGUIDE:
                if (scoreTimer.seconds() >= depositTime2 + guideOffset) {

                    outtake.guideScore();

                    cycleState = CycleState.DEPOSIT;
                }

                break;
            case DEPOSIT:
                if (scoreTimer.seconds() >= depositTime2) {

                    outtake.transferDeposit();
                    outtake.retractSlide();
                    outtake.setTurretMiddle();
                    outtake.guideDown();

                    intake.openClaw();
                    intake.intakePosition();


                    cycleState = CycleState.PREPARE;
                }
                break;
            case PREPARE:
                if (intake.intakeOutDiff() < 20 || intake.getDistanceCM() < 1.75) {
                    intake.closeClaw();


                    scoreTimer.reset();
                    cycleState = CycleState.GRAB;
                }
                break;
            case GRAB:
                if (scoreTimer.seconds() >= grabTime2) {

                    if (intake.getDistanceCM() < 4) { // have cone
                        if (coneHeight < 6) {
                            coneHeight++; // arm lower next cycle
                        }
                    }

                    intake.transferPosition();
                    intake.flipArm();

                    scoreTimer.reset();
                    cycleState = CycleState.RETRACT_INTAKE;
                }
                break;
            case RETRACT_INTAKE:
                if (scoreTimer.seconds() >= flipTime2) {
                    intake.openClaw();

                    outtake.zeroOuttake();
                    scoreTimer.reset();
                    cycleState = CycleState.FLIP;
                }
                break;
            case FLIP:
                if (scoreTimer.seconds() >= transferTime2) {
                    intake.readyPosition();
                    intake.dropArmAutoR(coneHeight);

                    scoreTimer.reset();
                    cycleState = CycleState.EXTEND_INTAKE;
                }
                break;
            case EXTEND_INTAKE:
                if (scoreTimer.seconds() >= intakeTime2) {
                    outtake.midDeposit();
                    outtake.setTurretLeftHigh();
                    outtake.extendSlideLeftCycle();
                    outtake.guideUpLeft();

                    cycleState = CycleState.EXTEND_OUTTAKE;
                }
                break;
            case EXTEND_OUTTAKE:
                if (outtake.slideOutDiffLeft() < depBuffer) {

                    cycleState = CycleState.READY;
                }
                break;
            default:
                // should never be reached, as liftState should never be null
                cycleState = CycleState.READY;
                break;

        }
    }

    public void cycleRight() {
        switch (cycleState) {
            case READY:
                if (gamepad1.y) {

                    outtake.scoreDepositRight();

                    intake.dropArmAutoR(coneHeight);

                    scoreTimer.reset();
                    cycleState = CycleState.UNGUIDE;
                }
                break;
            case UNGUIDE:
                if (scoreTimer.seconds() >= depositTime2 + guideOffset) {

                    outtake.guideScore();

                    cycleState = CycleState.DEPOSIT;
                }
                break;
            case DEPOSIT:
                if (scoreTimer.seconds() >= depositTime2) {
                    outtake.transferDeposit();
                    outtake.retractSlide();
                    outtake.setTurretMiddle();
                    outtake.guideDown();

                    intake.openClaw();
                    intake.intakePosition();


                    cycleState = CycleState.PREPARE;
                }
                break;
            case PREPARE:
                if (intake.intakeOutDiff() < 20 || intake.getDistanceCM() < 1.75) {
                    intake.closeClaw();


                    scoreTimer.reset();
                    cycleState = CycleState.GRAB;
                }
                break;
            case GRAB:
                if (scoreTimer.seconds() >= grabTime2) {


                    if (intake.getDistanceCM() < 4) { // have cone
                        if (coneHeight < 6) {
                            coneHeight++; // arm lower next cycle
                        }
                    }

                    intake.transferPosition();
                    intake.flipArm();

                    scoreTimer.reset();
                    cycleState = CycleState.RETRACT_INTAKE;
                }
                break;
            case RETRACT_INTAKE:
                if (scoreTimer.seconds() >= flipTime2) {
                    intake.openClaw();
                    outtake.zeroOuttake();
                    scoreTimer.reset();
                    cycleState = CycleState.FLIP;
                }
                break;
            case FLIP:
                if (scoreTimer.seconds() >= transferTime2) {
                    intake.readyPosition();
                    intake.dropArmAutoR(coneHeight);

                    scoreTimer.reset();
                    cycleState = CycleState.EXTEND_INTAKE;
                }
                break;
            case EXTEND_INTAKE:
                if (scoreTimer.seconds() >= intakeTime2) {
                    outtake.midDeposit();
                    outtake.setTurretRightHigh();
                    outtake.extendSlideRight();
                    outtake.guideUpLeft();

                    cycleState = CycleState.EXTEND_OUTTAKE;
                }
                break;
            case EXTEND_OUTTAKE:
                if (outtake.slideOutDiffRight() < 40) {

                    cycleState = CycleState.READY;
                }
                break;
            default:
                // should never be reached, as liftState should never be null
                cycleState = CycleState.READY;
                break;

        }
    }







}
