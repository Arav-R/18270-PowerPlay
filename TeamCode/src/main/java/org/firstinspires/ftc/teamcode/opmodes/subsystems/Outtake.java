package org.firstinspires.ftc.teamcode.opmodes.subsystems;


import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Outtake {

    private DcMotor outtakeSlide1, outtakeSlide2, turret;
    private Servo depositServo;

    double transferPos = 0;
    double scorePos = 0.8;

    int turretPos = 140;

    int fullExtend = 980;

    public void init(HardwareMap hardwareMap){
        outtakeSlide1 = hardwareMap.dcMotor.get("outtake1");
        outtakeSlide2 = hardwareMap.dcMotor.get("outtake2");
        turret = hardwareMap.dcMotor.get("turret");
        depositServo = hardwareMap.servo.get("deposit");


        turret.setDirection(DcMotor.Direction.REVERSE);

        outtakeSlide2.setDirection(DcMotor.Direction.REVERSE);

        outtakeSlide1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        outtakeSlide2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        outtakeSlide1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        outtakeSlide2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        turret.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        turret.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

    }




    public void moveToPos (int pos, double speed){
        outtakeSlide1.setTargetPosition(pos);
        outtakeSlide1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        outtakeSlide1.setPower(speed);
    }



    public void moveTurret (int pos, double speed){
        turret.setTargetPosition(pos);
        turret.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        turret.setPower(speed);
    }

    public void moveSlide (int pos, double speed){
        outtakeSlide1.setTargetPosition(pos);
        outtakeSlide1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        outtakeSlide1.setPower(speed);

        outtakeSlide2.setTargetPosition(pos);
        outtakeSlide2.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        outtakeSlide2.setPower(speed);
    }

    public void extendSlide (){
        outtakeSlide1.setTargetPosition(fullExtend);
        outtakeSlide1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        outtakeSlide1.setPower(0.5);

        outtakeSlide2.setTargetPosition(fullExtend);
        outtakeSlide2.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        outtakeSlide2.setPower(0.5);
    }

    public void retractSlide (){
        outtakeSlide1.setTargetPosition(0);
        outtakeSlide1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        outtakeSlide1.setPower(0.5);

        outtakeSlide2.setTargetPosition(0);
        outtakeSlide2.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        outtakeSlide2.setPower(0.5);
    }

    public void setTurretLeft (){
        turret.setTargetPosition(-turretPos);
        turret.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        turret.setPower(0.4);
    }

    public void setTurretRight (){
        turret.setTargetPosition(turretPos);
        turret.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        turret.setPower(0.4);
    }


    public void transferDeposit(){
        depositServo.setPosition(transferPos);
    }

    public void scoreDeposit(){
        depositServo.setPosition(scorePos);
    }











}
