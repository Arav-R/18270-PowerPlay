package org.firstinspires.ftc.teamcode.opmodes.subsystems;


import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.ColorRangeSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@Config
public class Intake {

    public DcMotorEx intakeSlide;
    private Servo clawServo, flip1Servo, flip2Servo;

    private ColorRangeSensor colorSensor;

    // Distance Sensor
    private DistanceSensor sensorRange;



    double clawClose = .2; //.27
    double clawOpen = 0.0; //.75

    public static double flipDown = 0; //0.015
    public static double flipUp = 0.57; //.64
    public static double flipContract = 0.4;
    public static double flipStartingPosition = 0.51;
    public static double flipLowPole = .3;

    public static double flip5 = 0.073; //.095
    public static double flip4 = 0.065; //.075
    public static double flip3 = 0.037; //.06
    public static double flip2 = .02; //0.03
    public static double flip1 = 0; //0.015

    public static double flip5L = 0.0745; //.095
    public static double flip4L = 0.065; //.075
    public static double flip3L = 0.037; //.06
    public static double flip2L = .02; //0.03
    public static double flip1L = 0; //0.015



    public static int slideOut = 420;

    public static int slideIn = 0; //65

    public static int slideInAuto = 0; //235
    public static int slideOutAuto = 417; //65

    public static int slideOutAuto5R = 398; //65
    public static int slideOutAuto4R = 400; //65
    public static int slideOutAuto3R = 400; //65
    public static int slideOutAuto2R = 402; //65
    public static int slideOutAuto1R = 402; //65


    public static int slideOutAuto5L = 462; //65
    public static int slideOutAuto4L = 467; //65
    public static int slideOutAuto3L = 471; //65
    public static int slideOutAuto2L = 471; //65
    public static int slideOutAuto1L = 485; //65


    public void init(HardwareMap hardwareMap){
        intakeSlide = hardwareMap.get(DcMotorEx.class, "intakeslide");
        clawServo = hardwareMap.servo.get("claw");

        flip1Servo = hardwareMap.servo.get("flip1");
        flip2Servo = hardwareMap.servo.get("flip2");


        // Distance Sensor
        //sensorRange = hardwareMap.get(DistanceSensor.class, "sensor_range");



        intakeSlide.setDirection(DcMotor.Direction.REVERSE);

        flip2Servo.setDirection(Servo.Direction.REVERSE);

        intakeSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        intakeSlide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


        // V3 color
        colorSensor = hardwareMap.get(ColorRangeSensor.class, "sensor_color");

    }

    public void moveIntakeZero(){
        intakeSlide.setPower(-.3);
    }

    public void zeroIntake(){

        intakeSlide.setPower(0);

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
/*
    public void autoStackPosition(){
        intakeSlide.setTargetPosition(slideOutAuto);
        intakeSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        intakeSlide.setPower(0.7);
    }
*/
    public void autoStackPositionRight(int cone){

        if (cone == 2) { // Top cone starting stack
            intakeSlide.setTargetPosition(slideOutAuto5R);
            intakeSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            intakeSlide.setPower(0.7);
        } else if (cone == 3) {
            intakeSlide.setTargetPosition(slideOutAuto4R);
            intakeSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            intakeSlide.setPower(0.7);
        } else if (cone == 4) {
            intakeSlide.setTargetPosition(slideOutAuto3R);
            intakeSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            intakeSlide.setPower(0.7);
        } else if (cone == 5) {
            intakeSlide.setTargetPosition(slideOutAuto2R);
            intakeSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            intakeSlide.setPower(0.7);
        } else if (cone == 6) {
            intakeSlide.setTargetPosition(slideOutAuto1R);
            intakeSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            intakeSlide.setPower(0.7);
        }


    }

    public void autoStackPositionLeft(int cone){

        if (cone == 2) { // Top cone starting stack
            intakeSlide.setTargetPosition(slideOutAuto5L);
            intakeSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            intakeSlide.setPower(0.7);
        } else if (cone == 3) {
            intakeSlide.setTargetPosition(slideOutAuto4L);
            intakeSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            intakeSlide.setPower(0.7);
        } else if (cone == 4) {
            intakeSlide.setTargetPosition(slideOutAuto3L);
            intakeSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            intakeSlide.setPower(0.7);
        } else if (cone == 5) {
            intakeSlide.setTargetPosition(slideOutAuto2L);
            intakeSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            intakeSlide.setPower(0.7);
        } else if (cone == 6) {
            intakeSlide.setTargetPosition(slideOutAuto1L);
            intakeSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            intakeSlide.setPower(0.7);
        }


    }


    public void readyPosition (){
        intakeSlide.setTargetPosition(slideOut - 250);
        intakeSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        intakeSlide.setPower(0.7);
    }

    public void transferPosition (){
        intakeSlide.setTargetPosition(0);
        intakeSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        intakeSlide.setPower(1);
    }

    public void holdIntakeSlide (){
        intakeSlide.setTargetPosition(5);
        intakeSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        intakeSlide.setPower(.01);
    }

    public double getIntakePower (){
        return intakeSlide.getPower();
    }

    public double getIntakeTarget (){
        return intakeSlide.getTargetPosition();
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

        return intakeSlide.getCurrentPosition();

    }

    public int intakeOutAutoDiffR(int cone){

        if (cone == 2) { // Top cone starting stack
            return Math.abs(intakeSlide.getCurrentPosition() - slideOutAuto5R);
        } else if (cone == 3) {
            return Math.abs(intakeSlide.getCurrentPosition() - slideOutAuto4R);
        } else if (cone == 4) {
            return Math.abs(intakeSlide.getCurrentPosition() - slideOutAuto3R);
        } else if (cone == 5) {
            return Math.abs(intakeSlide.getCurrentPosition() - slideOutAuto2R);
        } else if (cone == 6) {
            return Math.abs(intakeSlide.getCurrentPosition() - slideOutAuto1R);
        }
        return Math.abs(intakeSlide.getCurrentPosition() - slideInAuto);

    }

    public int intakeOutAutoDiffL(int cone){

        if (cone == 2) { // Top cone starting stack
            return Math.abs(intakeSlide.getCurrentPosition() - slideOutAuto5L);
        } else if (cone == 3) {
            return Math.abs(intakeSlide.getCurrentPosition() - slideOutAuto4L);
        } else if (cone == 4) {
            return Math.abs(intakeSlide.getCurrentPosition() - slideOutAuto3L);
        } else if (cone == 5) {
            return Math.abs(intakeSlide.getCurrentPosition() - slideOutAuto2L);
        } else if (cone == 6) {
            return Math.abs(intakeSlide.getCurrentPosition() - slideOutAuto1L);
        }
        return Math.abs(intakeSlide.getCurrentPosition() - slideInAuto);

    }

    public int getSlide(){

        return intakeSlide.getCurrentPosition();

    }

/*
    public void initSlide(){

        intakeSlide.setPower(0.2);

    }
*/






    public void openClaw (){
        clawServo.setPosition(clawOpen);
    }

    public void closeClaw (){
        clawServo.setPosition(clawClose);
    }

    public void closeClawAuto (int cone){
        if (cone == 2) { // Top cone starting stack
            clawServo.setPosition(clawClose - 0.02);
        } else if (cone == 3) {
            clawServo.setPosition(clawClose);
        } else if (cone == 4) {
            clawServo.setPosition(clawClose);
        } else if (cone == 5) {
            clawServo.setPosition(clawClose);
        } else if (cone == 6) {
            clawServo.setPosition(clawClose);
        }
    }

    public void moveClaw (double pos){
        clawServo.setPosition(pos);
    }



    public void dropArm (){
        flip1Servo.setPosition(flipDown);
        flip2Servo.setPosition(flipDown);

    }

    public void dropArmGround (){
        flip1Servo.setPosition(0.02);
        flip2Servo.setPosition(0.02);

    }

    // For Auto
    public void dropArmAutoR(int cone){

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

    // For Auto
    public void dropArmAutoL(int cone){

        if (cone == 2) { // Top cone starting stack
            flip1Servo.setPosition(flip5L);
            flip2Servo.setPosition(flip5L);
        } else if (cone == 3) {
            flip1Servo.setPosition(flip4L);
            flip2Servo.setPosition(flip4L);
        } else if (cone == 4) {
            flip1Servo.setPosition(flip3L);
            flip2Servo.setPosition(flip3L);
        } else if (cone == 5) {
            flip1Servo.setPosition(flip2L);
            flip2Servo.setPosition(flip2L);
        } else if (cone == 6) {
            flip1Servo.setPosition(flip1L);
            flip2Servo.setPosition(flip1L);
        }


    }

    public void flipArm (){
        flip1Servo.setPosition(flipUp);
        flip2Servo.setPosition(flipUp);

    }

    public void armStartingPosition (){
        flip1Servo.setPosition(flipStartingPosition);
        flip2Servo.setPosition(flipStartingPosition);
    }

    public void contractArm (){
        flip1Servo.setPosition(flipContract);
        flip2Servo.setPosition(flipContract);

    }

    // get arm position
    public boolean isArmDown(){
        return flip1Servo.getPosition() < .1;
    }

    public void moveJoint1 (double pos){
        flip1Servo.setPosition(pos);

    }

    public void moveJoint2 (double pos){
        flip2Servo.setPosition(pos);

    }

    public void armLowPole (){
        flip1Servo.setPosition(flipLowPole);
        flip2Servo.setPosition(flipLowPole);

    }


    // V3 color

    public double getDistanceCM (){
        return colorSensor.getDistance(DistanceUnit.CM);
    }

    public int getRed(){
        return colorSensor.red();
    }

    public int getBlue(){
        return colorSensor.red();
    }

    // Intake slide current

    public double getIntakeSlideCurrent(){
        return intakeSlide.getCurrent(CurrentUnit.AMPS);
    }



//    public double getDistanceCM(){
//        return sensorRange.getDistance(DistanceUnit.CM);
//    }


}
