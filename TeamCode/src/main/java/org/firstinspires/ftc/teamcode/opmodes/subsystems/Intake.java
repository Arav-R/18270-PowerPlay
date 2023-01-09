package org.firstinspires.ftc.teamcode.opmodes.subsystems;


import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

@Config
public class Intake {

    private DcMotor intakeSlide;
    private Servo clawServo, flip1Servo, flip2Servo;

    double frontLeftPower, frontRightPower, backLeftPower, backRightPower;

    double clawClose = 1;
    double clawOpen = .75;

    double flipDown = 0.015;
    double flipUp = 0.55; //.64
    double flipContract = 0.4;

    public static double flip5 = 0.1;
    public static double flip4 = 0.08;
    public static double flip3 = 0.06;
    public static double flip2 = 0.03;
    public static double flip1 = 0.015;



    int slideOut = 430;

    public static int slideIn = 210; //65

    public static int slideInAuto = -5; //235
    public static int slideOutAuto = 430; //65

    public void init(HardwareMap hardwareMap){
        intakeSlide = hardwareMap.dcMotor.get("intakeslide");
        clawServo = hardwareMap.servo.get("claw");

        flip1Servo = hardwareMap.servo.get("flip1");
        flip2Servo = hardwareMap.servo.get("flip2");



        intakeSlide.setDirection(DcMotor.Direction.REVERSE);

        flip2Servo.setDirection(Servo.Direction.REVERSE);

        intakeSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        intakeSlide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

    }




    public void moveToPos (int pos, double speed){
        intakeSlide.setTargetPosition(pos);
        intakeSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        intakeSlide.setPower(speed);
    }

    public void intakePosition (){
        intakeSlide.setTargetPosition(slideOut);
        intakeSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        intakeSlide.setPower(1);
    }

    public void autoPosition (){
        intakeSlide.setTargetPosition(slideOutAuto);
        intakeSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        intakeSlide.setPower(0.7);
    }


    public void readyPosition (){
        intakeSlide.setTargetPosition(slideOut - 150);
        intakeSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        intakeSlide.setPower(0.7);
    }

    public void transferPosition (){
        intakeSlide.setTargetPosition(-5);
        intakeSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        intakeSlide.setPower(.8);
    }

    public void transferPositionAuto (){
        intakeSlide.setTargetPosition(slideInAuto);
        intakeSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        intakeSlide.setPower(0.6);
    }

    public void zeroPosition(){
        intakeSlide.setTargetPosition(-15);
        intakeSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        intakeSlide.setPower(0.7);
    }

    public int intakeOutDiff(){

        return Math.abs(intakeSlide.getCurrentPosition() - slideOut);

    }

    public int intakeInDiff(){

        return Math.abs(intakeSlide.getCurrentPosition() - slideIn);

    }

    public int intakeInAutoDiff(){

        return Math.abs(intakeSlide.getCurrentPosition() - slideInAuto);

    }







    public void openClaw (){
        clawServo.setPosition(clawOpen);
    }

    public void closeClaw (){
        clawServo.setPosition(clawClose);
    }

    public void moveClaw (double pos){
        clawServo.setPosition(pos);
    }



    public void dropArm (){
        flip1Servo.setPosition(flipDown);
        flip2Servo.setPosition(flipDown);

    }

    // For Auto
    public void dropArmAuto (int cone){

        if (cone == 2) { // Top cone starting stack
            flip1Servo.setPosition(flip5);
            flip2Servo.setPosition(flip5);
        } else if (cone == 3) {
            flip1Servo.setPosition(flip4);
            flip2Servo.setPosition(flip4);
        } else if (cone == 4) {
            flip1Servo.setPosition(flip3);
            flip2Servo.setPosition(flip3);
        } else if (cone == 5) {
            flip1Servo.setPosition(flip2);
            flip2Servo.setPosition(flip2);
        } else if (cone == 6) {
            flip1Servo.setPosition(flip1);
            flip2Servo.setPosition(flip1);
        }


    }

    public void flipArm (){
        flip1Servo.setPosition(flipUp);
        flip2Servo.setPosition(flipUp);

    }

    public void armStartingPosition (){
        flip1Servo.setPosition(flipUp);
        flip2Servo.setPosition(flipUp);
    }

    public void contractArm (){
        flip1Servo.setPosition(flipContract);
        flip2Servo.setPosition(flipContract);

    }

    public void moveJoint1 (double pos){
        flip1Servo.setPosition(pos);

    }

    public void moveJoint2 (double pos){
        flip2Servo.setPosition(pos);

    }


}
