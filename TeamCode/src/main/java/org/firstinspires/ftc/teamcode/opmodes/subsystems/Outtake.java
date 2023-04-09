package org.firstinspires.ftc.teamcode.opmodes.subsystems;


import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;

@Config
public class Outtake {

    public DcMotorEx outtakeSlide1, outtakeSlide2, turret;
    private Servo depositServo, guideServo, lockServo;

    // outtake slide PIDF
    private PIDController controller;

    public static double p = 0, i = 0, d = 0;
    public static double f = 0;
    public static int target = 0;




    // deposit
    public static  double transferPos = 0.19; //0.18
    public static double midPos = 0.38; //0.37
    public static double scorePosLeft = 0.81; //.82
    public static double scorePosRight = .85; //.82
    public static double scorePosLow = 1; //.82


    // turret
    public static int turretTransfer = 312;

    public static int leftHighTurret = turretTransfer + 182; // 488
    public static int leftMidTurret = turretTransfer + 287; // 250

    public static int rightHighTurret = turretTransfer - 160; // 160
    public static int rightMidTurret = turretTransfer - 260; // 210


    public static int turretAutoLeft = turretTransfer + 175; //483
    public static int turretAutoRight = turretTransfer - 163; //149


    public static int rightHighLow = 120;

    public static int leftHighFar = 550; // ground to high
    public static int rightHighFar = 130; // ground to high

    // outtake slides
    public static int fullExtendLeft = 1015; // 935
    public static int fullExtendRight = 1040; // 1030

    public static int fullExtendAutoLeft = 1050; // 934
    public static int fullExtendAutoRight = 1050; // 990



    public static double guideUpPosLeft = .48;
    public static double guideUpPosRight = .5;
    public static double guideScorePos = .52;
    public static double guideDownPos = .75;

    public static double guideUpLow = .45;

    public static double lockClosePos = .75;
    public static double lockOpenPos = 0;


    public static int preloadTurretOffsetL = 0; //510
    public static int preloadLeftExtendOffset = 0; //510

    public static int preloadTurretOffsetR = 0; //510
    public static int preloadRightExtendOffset = 0; //510




    // different junctions left and right positions Outtake
    public static int leftMid = 430; // 390
    public static int rightMid = 410; // 410

    public static int leftlow = 0;
    public static int rightlow = 0;


    public void init(HardwareMap hardwareMap){
        outtakeSlide1 = hardwareMap.get(DcMotorEx.class,"outtake1");
        outtakeSlide2 = hardwareMap.get(DcMotorEx.class,"outtake2");
        turret = hardwareMap.get(DcMotorEx.class,"turret");
        depositServo = hardwareMap.servo.get("deposit");

        guideServo = hardwareMap.servo.get("guide");
        //lockServo = hardwareMap.servo.get("lock");


        outtakeSlide1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        outtakeSlide2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        //turret.setDirection(DcMotor.Direction.REVERSE);

        outtakeSlide2.setDirection(DcMotor.Direction.REVERSE);

        outtakeSlide1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        outtakeSlide2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        outtakeSlide1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        outtakeSlide2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        turret.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        turret.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

    }


    // Turret Code
    public void moveTurretZero(){
        turret.setPower(-.35);
    }

    public void zeroTurret(){
        turret.setPower(0);

        turret.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        turret.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void moveOuttakeZero(){
        outtakeSlide1.setPower(-.2);
        outtakeSlide2.setPower(-.2);
    }


    public void moveTurret (int pos, double speed){
        turret.setTargetPosition(pos);
        turret.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        turret.setPower(speed);
    }


    public void nudgeLeftLeft(){
        leftHighTurret += 2;
    }
    public void nudgeLeftRight(){
        leftHighTurret -= 2;
    }

    public void nudgeRightLeft(){
        rightHighTurret += 2;
    }
    public void nudgeRightRight(){
        rightHighTurret -= 2;
    }

    public int getTurret (){
        return turret.getCurrentPosition();
    }

    public int turretFarLeftDiff (){
        return Math.abs(turret.getCurrentPosition() - leftHighFar);
    }

    public int turretFarRightDiff (){
        return Math.abs(turret.getCurrentPosition() - rightHighFar);
    }




    public void setTurretLeftHigh(){
        turret.setTargetPosition(leftHighTurret);
        turret.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        turret.setPower(0.7);
    }

    public void setTurretLeftMid(){
        turret.setTargetPosition(leftMidTurret);
        turret.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        turret.setPower(0.7);
    }

    public void setTurretAutoLeft (){
        turret.setTargetPosition(turretAutoLeft);
        turret.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        turret.setPower(0.7);
    }

    public void setTurretAutoLeftPreload (){
        turret.setTargetPosition(turretAutoLeft + preloadTurretOffsetL);
        turret.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        turret.setPower(0.7);
    }



    public void setTurretMiddle (){
        turret.setTargetPosition(turretTransfer);
        turret.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        turret.setPower(.4);
    }

    public void setTurretRightHigh(){
        turret.setTargetPosition(rightHighTurret);
        turret.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        turret.setPower(0.7);
    }

    public void setTurretRightMid(){
        turret.setTargetPosition(rightMidTurret);
        turret.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        turret.setPower(0.7);
    }

    public void setTurretRightLow (){
        turret.setTargetPosition(rightlow);
        turret.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        turret.setPower(0.7);
    }

    public void setTurretAutoRight (){
        turret.setTargetPosition(turretAutoRight);
        turret.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        turret.setPower(0.7);
    }

    public void setTurretAutoRightPreload (){
        turret.setTargetPosition(turretAutoRight + preloadTurretOffsetR);
        turret.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        turret.setPower(0.7);
    }




    // Outtake
    public void zeroOuttake(){
        outtakeSlide1.setPower(0);
        outtakeSlide2.setPower(0);

        outtakeSlide1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        outtakeSlide2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        outtakeSlide1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        outtakeSlide2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void moveSlide (int pos, double speed){
        outtakeSlide1.setTargetPosition(pos);
        outtakeSlide1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        outtakeSlide1.setPower(speed);

        outtakeSlide2.setTargetPosition(pos);
        outtakeSlide2.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        outtakeSlide2.setPower(speed);
    }

    public void extendSlideLeft(){
        outtakeSlide1.setTargetPosition(fullExtendLeft);
        outtakeSlide1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        outtakeSlide1.setPower(1);

        outtakeSlide2.setTargetPosition(fullExtendLeft);
        outtakeSlide2.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        outtakeSlide2.setPower(1);
    }

    public void extendSlideLeftCycle(){
        outtakeSlide1.setTargetPosition(fullExtendLeft + 15);
        outtakeSlide1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        outtakeSlide1.setPower(1);

        outtakeSlide2.setTargetPosition(fullExtendLeft + 15);
        outtakeSlide2.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        outtakeSlide2.setPower(1);
    }

    public void extendSlideAutoLeft(){
        outtakeSlide1.setTargetPosition(fullExtendAutoLeft);
        outtakeSlide1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        outtakeSlide1.setPower(0.8);

        outtakeSlide2.setTargetPosition(fullExtendAutoLeft);
        outtakeSlide2.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        outtakeSlide2.setPower(0.8);
    }

    public void extendSlidePreloadLeft(){
        outtakeSlide1.setTargetPosition(fullExtendAutoLeft + preloadLeftExtendOffset);
        outtakeSlide1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        outtakeSlide1.setPower(0.8);

        outtakeSlide2.setTargetPosition(fullExtendAutoLeft + preloadLeftExtendOffset);
        outtakeSlide2.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        outtakeSlide2.setPower(0.8);
    }

    public void extendSlideRight(){
        outtakeSlide1.setTargetPosition(fullExtendRight);
        outtakeSlide1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        outtakeSlide1.setPower(1);

        outtakeSlide2.setTargetPosition(fullExtendRight);
        outtakeSlide2.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        outtakeSlide2.setPower(1);
    }

    public void extendSlideAutoRight(){
        outtakeSlide1.setTargetPosition(fullExtendAutoRight);
        outtakeSlide1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        outtakeSlide1.setPower(0.8);

        outtakeSlide2.setTargetPosition(fullExtendAutoRight);
        outtakeSlide2.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        outtakeSlide2.setPower(0.8);
    }

    public void extendSlidePreloadRight(){
        outtakeSlide1.setTargetPosition(fullExtendAutoRight + preloadRightExtendOffset);
        outtakeSlide1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        outtakeSlide1.setPower(0.8);

        outtakeSlide2.setTargetPosition(fullExtendAutoRight + preloadRightExtendOffset);
        outtakeSlide2.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        outtakeSlide2.setPower(0.8);
    }

    public void moreExtendLeft(){
        fullExtendLeft += 5;
    }

    public void lessExtendLeft(){
        fullExtendLeft -= 5;
    }

    public void moreExtendRight(){
        fullExtendRight += 5;
    }

    public void lessExtendRight(){
        fullExtendRight -= 5;
    }

    public int getExtend (){
        return outtakeSlide1.getCurrentPosition();
    }

    public int getExtendTarget (){
        return outtakeSlide1.getTargetPosition();
    }

    public int slideOutDiffLeft(){

        return Math.abs(outtakeSlide1.getCurrentPosition() - fullExtendLeft);

    }

    public int slideOutDiffAutoLeft(){

        return Math.abs(outtakeSlide1.getCurrentPosition() - fullExtendAutoLeft);

    }

    public int slideOutDiffRight(){

        return Math.abs(outtakeSlide1.getCurrentPosition() - fullExtendRight);

    }

    public int slideOutDiffAutoRight(){

        return Math.abs(outtakeSlide1.getCurrentPosition() - fullExtendAutoRight);

    }

    public void retractSlide (){
        outtakeSlide1.setTargetPosition(0);
        outtakeSlide1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        outtakeSlide1.setPower(0.5);

        outtakeSlide2.setTargetPosition(0);
        outtakeSlide2.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        outtakeSlide2.setPower(0.5);
    }

    public void retractSlideSlow (){
        outtakeSlide1.setTargetPosition(0);
        outtakeSlide1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        outtakeSlide1.setPower(0.25);

        outtakeSlide2.setTargetPosition(0);
        outtakeSlide2.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        outtakeSlide2.setPower(0.25);
    }

    public int retractDiff (){

        return outtakeSlide1.getCurrentPosition();
        //return Math.abs(outtakeSlide1.getCurrentPosition());

    }



    // Deposit

    public void transferDeposit(){
        depositServo.setPosition(transferPos);
    }

    public void midDeposit(){
        depositServo.setPosition(midPos);
    }

    public void scoreDepositLeft(){
        depositServo.setPosition(scorePosLeft);
    }

    public void scoreDepositRight(){
        depositServo.setPosition(scorePosRight);
    }

    public void scoreDepositLow(){
        depositServo.setPosition(scorePosLow);
    }


    // Guide

    public void guideUpLeft(){
        guideServo.setPosition(guideUpPosLeft);
    }
    public void guideUpRight(){
        guideServo.setPosition(guideUpPosRight);
    }

    public void guideUpLow(){
        guideServo.setPosition(guideUpLow);
    }

    public void guideScore(){
        guideServo.setPosition(guideScorePos);
    }

    public void guideDown(){
        guideServo.setPosition(guideDownPos);
    }


/*

    public void initOutSlide(){

        outtakeSlide1.setPower(-0.2);
        outtakeSlide2.setPower(-0.2);

    }

    public void initTurret(){

        outtakeSlide1.setPower(-0.2);
        outtakeSlide2.setPower(-0.2);

    }
*/




//    public void moveToPos (int pos, double speed){
//        outtakeSlide1.setTargetPosition(pos);
//        outtakeSlide1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//        outtakeSlide1.setPower(speed);
//    }


    // Different junction positions

    public void slideLeftMid(){
        outtakeSlide1.setTargetPosition(leftMid);
        outtakeSlide1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        outtakeSlide1.setPower(1);

        outtakeSlide2.setTargetPosition(leftMid);
        outtakeSlide2.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        outtakeSlide2.setPower(1);
    }

    public void slideRightMid(){
        outtakeSlide1.setTargetPosition(rightMid);
        outtakeSlide1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        outtakeSlide1.setPower(1);

        outtakeSlide2.setTargetPosition(rightMid);
        outtakeSlide2.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        outtakeSlide2.setPower(1);
    }

    public void setLockOpen(){
        lockServo.setPosition(lockOpenPos);
    }

    public void setLockClosed(){
        lockServo.setPosition(lockClosePos);
    }




    // Outtake slide current

    public double getOuttakeSlideCurrent1(){
        return outtakeSlide1.getCurrent(CurrentUnit.AMPS);
    }

    public double getOuttakeSlideCurrent2(){
        return outtakeSlide2.getCurrent(CurrentUnit.AMPS);
    }

    public double getTurretCurrent(){
        return turret.getCurrent(CurrentUnit.AMPS);
    }


    // Outtake Velocity

    public double getOuttakeSlideVelocity1(){
        return outtakeSlide1.getVelocity();
    }




    // Slide PID
    public void initSlidePID(){
        controller = new PIDController(p, i , d);
    }

    public void setSlidePID(double p, double i, double d){
        controller.setPID(p, i, d);
    }

    public void powerSlidePID(){
        // motors run without encoders
        outtakeSlide1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        outtakeSlide2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        int motorPos = outtakeSlide1.getCurrentPosition();
        double pid = controller.calculate(motorPos, target);

        double power = pid + f;

        outtakeSlide1.setPower(power);
        outtakeSlide2.setPower(power);

    }





}
