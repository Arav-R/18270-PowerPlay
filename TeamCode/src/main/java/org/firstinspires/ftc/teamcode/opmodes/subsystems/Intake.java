package org.firstinspires.ftc.teamcode.opmodes.subsystems;


import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.controller.PIDController;
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

    // slide PIDF
    private PIDController controller;

    public static double p = 0, i = 0, d = 0;
    public static double f = 0;
    public static int target = 0;



    double clawClose = .22; //.2
    double clawOpen = 0.0; //.75

    public static double flipDown = 0; //0.015
    public static double flipUp = 0.555; //.57
    public static double flipContract = 0.4;
    public static double flipStartingPosition = 0.51;
    public static double flipLowPole = .23;

    public static double flip5 = 0.077; //.095
    public static double flip4 = 0.061; //.075
    public static double flip3 = 0.037; //.06
    public static double flip2 = .015; //0.03
    public static double flip1 = 0; //0.015

    public static double flip5L = 0.080; //.0.077
    public static double flip4L = 0.067; //.075
    public static double flip3L = 0.042; //0.037
    public static double flip2L = .02; //0.017
    public static double flip1L = 0; //0.015


    public static double multiplier = 103.8 / 145.1;

    public static int slideOut = 300 ; // 420
    public static int slideOutExtend = 286; // 400


    public static int slideIn = 0; //65

    public static int slideInAuto = 0; //0
    public static int slideOutAuto = 298; //417

    public static int slideOutAuto5R = 303; //423
    public static int slideOutAuto4R = 304; //425
    public static int slideOutAuto3R = 304; //425
    public static int slideOutAuto2R = 309; //432
    public static int slideOutAuto1R = 309; //432


    public static int slideOutAuto5L = 348; //457 330
    public static int slideOutAuto4L = 351; //462
    public static int slideOutAuto3L = 354; //470
    public static int slideOutAuto2L = 375; //470
    public static int slideOutAuto1L = 400; //484


    public void init(HardwareMap hardwareMap){
        intakeSlide = hardwareMap.get(DcMotorEx.class, "intakeslide");
        clawServo = hardwareMap.servo.get("claw");

        flip1Servo = hardwareMap.servo.get("flip1");
        flip2Servo = hardwareMap.servo.get("flip2");


        // Distance Sensor
        //sensorRange = hardwareMap.get(DistanceSensor.class, "sensor_range");


        // intake brake
        intakeSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        intakeSlide.setDirection(DcMotor.Direction.REVERSE);

        flip1Servo.setDirection(Servo.Direction.REVERSE);

        intakeSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        intakeSlide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // intake slide pid
        //intakeSlide.setPositionPIDFCoefficients(3.5);


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
        intakeSlide.setPositionPIDFCoefficients(3.5);

        intakeSlide.setTargetPosition(slideOut);
        intakeSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        intakeSlide.setPower(1);
        
    }

    public void intakePositionCircuit (){
        intakeSlide.setPositionPIDFCoefficients(3.5);

        intakeSlide.setTargetPosition(slideOutExtend);
        intakeSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        intakeSlide.setPower(1);


    }

    public void intakeLessP (){
        intakeSlide.setPositionPIDFCoefficients(3.5);
    }
    public void intakeMoreP (){
        intakeSlide.setPositionPIDFCoefficients(10);
    }
/*
    public void autoStackPosition(){
        intakeSlide.setTargetPosition(slideOutAuto);
        intakeSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        intakeSlide.setPower(0.7);
    }
*/
    public void autoStackPositionRight(int cone){
        intakeSlide.setPositionPIDFCoefficients(5);

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
        intakeSlide.setPositionPIDFCoefficients(5);

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
            intakeSlide.setPositionPIDFCoefficients(7.5);

            intakeSlide.setTargetPosition(slideOutAuto2L);
            intakeSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            intakeSlide.setPower(0.85);
        } else if (cone == 6) {
            intakeSlide.setPositionPIDFCoefficients(7.5);

            intakeSlide.setTargetPosition(slideOutAuto1L);
            intakeSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            intakeSlide.setPower(0.95);
        }



    }


    public void readyPosition (){
        intakeSlide.setPositionPIDFCoefficients(3.5);

        intakeSlide.setTargetPosition(slideOut - 250);
        intakeSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        intakeSlide.setPower(0.7);

    }

    public void transferPosition (){
        intakeSlide.setPositionPIDFCoefficients(25);

        intakeSlide.setTargetPosition(-10);
        intakeSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        intakeSlide.setPower(1);
    }

    public void holdIntakeSlide (){
        intakeSlide.setPositionPIDFCoefficients(10);

        intakeSlide.setTargetPosition(3);
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
        intakeSlide.setPositionPIDFCoefficients(10);

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

    public void intakeForward(){
        intakeSlide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        intakeSlide.setPower(0.6);
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
            clawServo.setPosition(clawClose); //0.02
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

    // Outtake Velocity

    public double getIntakeSlideVelocity1(){
        return intakeSlide.getVelocity();
    }



//    public double getDistanceCM(){
//        return sensorRange.getDistance(DistanceUnit.CM);
//    }



    // PID
    public void initPID(){
        controller = new PIDController(p, i , d);
    }

    public void setPID(double p, double i, double d){
        controller.setPID(p, i, d);
    }

    public void powerPID(){
        // motors run without encoders
        intakeSlide.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        int motorPos = intakeSlide.getCurrentPosition();
        double pid = controller.calculate(motorPos, target);

        double power = pid + f;

        intakeSlide.setPower(power);

    }




}
