package org.firstinspires.ftc.teamcode.opmodes.subsystems;


import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Intake {

    private DcMotor intakeSlide;
    private Servo clawServo, flip1, flip2;

    double frontLeftPower, frontRightPower, backLeftPower, backRightPower;

    double clawClose = 1;
    double clawOpen = .75;

    double flip1Down = .08;
    double flip1Flip = .7;

    double flip2Down = .97;
    double flip2Flip = .4;


    int slideOut = 360;
    int slideIn = 95;

    public void init(HardwareMap hardwareMap){
        intakeSlide = hardwareMap.dcMotor.get("intakeslide");
        clawServo = hardwareMap.servo.get("claw");

        flip1 = hardwareMap.servo.get("flip1");
        flip2 = hardwareMap.servo.get("flip2");



        intakeSlide.setDirection(DcMotor.Direction.REVERSE);

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
        intakeSlide.setPower(0.7);
    }

    public void readyPosition (){
        intakeSlide.setTargetPosition(slideOut - 200);
        intakeSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        intakeSlide.setPower(0.7);
    }

    public void transferPosition (){
        intakeSlide.setTargetPosition(slideIn);
        intakeSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        intakeSlide.setPower(0.4);
    }

    public int intakeOutDiff(){

        return Math.abs(intakeSlide.getCurrentPosition() - slideOut);

    }

    public int intakeInDiff(){

        return Math.abs(intakeSlide.getCurrentPosition() - slideIn);

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
        flip1.setPosition(flip1Down);
        flip2.setPosition(flip2Down);

    }

    public void flipArm (){
        flip1.setPosition(flip1Flip);
        flip2.setPosition(flip2Flip);

    }

    public void moveJoint1 (double pos){
        flip1.setPosition(pos);

    }

    public void moveJoint2 (double pos){
        flip2.setPosition(pos);

    }


}
