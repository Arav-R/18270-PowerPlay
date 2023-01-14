package org.firstinspires.ftc.teamcode.opmodes.subsystems;


import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

@Config
public class Outtake {

    private DcMotor outtakeSlide1, outtakeSlide2, turret;
    private Servo depositServo;

    public static  double transferPos = 0.19; //0.18
    public static double midPos = 0.38; //0.37
    public static double scorePosLeft = .83; //.82
    public static double scorePosRight = .85; //.82

    public static int leftHigh = 458;
    public static int rightHigh = 160;
    public static int turretTransfer = 315;

    public static int fullExtendLeft = 915;
    public static int fullExtendRight = 1030;

    public static int fullExtendAutoLeft = 933;

    public static int turretAutoLeft = 510;

    public void init(HardwareMap hardwareMap){
        outtakeSlide1 = hardwareMap.dcMotor.get("outtake1");
        outtakeSlide2 = hardwareMap.dcMotor.get("outtake2");
        turret = hardwareMap.dcMotor.get("turret");
        depositServo = hardwareMap.servo.get("deposit");


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




//    public void moveToPos (int pos, double speed){
//        outtakeSlide1.setTargetPosition(pos);
//        outtakeSlide1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//        outtakeSlide1.setPower(speed);
//    }



    public void moveTurret (int pos, double speed){
        turret.setTargetPosition(pos);
        turret.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        turret.setPower(speed);
    }



    public void nudgeLeftLeft(){
        leftHigh += 2;
    }
    public void nudgeLeftRight(){
        leftHigh -= 2;
    }

    public void nudgeRightLeft(){
        rightHigh += 2;
    }
    public void nudgeRightRight(){
        rightHigh -= 2;
    }

    public int getTurret (){
        return turret.getCurrentPosition();
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
        outtakeSlide1.setPower(0.8);

        outtakeSlide2.setTargetPosition(fullExtendLeft);
        outtakeSlide2.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        outtakeSlide2.setPower(0.8);
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
        outtakeSlide1.setTargetPosition(fullExtendAutoLeft + 12);
        outtakeSlide1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        outtakeSlide1.setPower(0.8);

        outtakeSlide2.setTargetPosition(fullExtendAutoLeft + 12);
        outtakeSlide2.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        outtakeSlide2.setPower(0.8);
    }

    public void extendSlideRight(){
        outtakeSlide1.setTargetPosition(fullExtendRight);
        outtakeSlide1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        outtakeSlide1.setPower(0.8);

        outtakeSlide2.setTargetPosition(fullExtendRight);
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

    public int slideOutDiffLeft(){

        return Math.abs(outtakeSlide1.getCurrentPosition() - fullExtendLeft);

    }

    public int slideOutDiffAutoLeft(){

        return Math.abs(outtakeSlide1.getCurrentPosition() - fullExtendLeft);

    }

    public int slideOutDiffRight(){

        return Math.abs(outtakeSlide1.getCurrentPosition() - fullExtendRight);

    }

    public void retractSlide (){
        outtakeSlide1.setTargetPosition(-50);
        outtakeSlide1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        outtakeSlide1.setPower(0.5);

        outtakeSlide2.setTargetPosition(-50);
        outtakeSlide2.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        outtakeSlide2.setPower(0.5);
    }

    public void setTurretLeft (){
        turret.setTargetPosition(leftHigh);
        turret.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        turret.setPower(0.7);
    }

    public void setTurretAutoLeft (){
        turret.setTargetPosition(turretAutoLeft);
        turret.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        turret.setPower(0.7);
    }

    public void setTurretMiddle (){
        turret.setTargetPosition(turretTransfer);
        turret.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        turret.setPower(0.7);
    }

    public void setTurretRight (){
        turret.setTargetPosition(rightHigh);
        turret.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        turret.setPower(0.7);
    }


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



}
