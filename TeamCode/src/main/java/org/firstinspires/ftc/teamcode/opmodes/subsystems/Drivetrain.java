package org.firstinspires.ftc.teamcode.opmodes.subsystems;


import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Drivetrain {

    private DcMotor backleft;
    private DcMotor backright;
    private DcMotor frontleft;
    private DcMotor frontright;

    double frontLeftPower, frontRightPower, backLeftPower, backRightPower;

    public void init(HardwareMap hardwareMap){
        frontleft = hardwareMap.dcMotor.get("frontleft");
        frontright = hardwareMap.dcMotor.get("frontright");
        backleft = hardwareMap.dcMotor.get("backleft");
        backright = hardwareMap.dcMotor.get("backright");

        frontleft.setDirection(DcMotor.Direction.REVERSE);
        backleft.setDirection(DcMotor.Direction.REVERSE);


        frontleft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontright.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backleft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backright.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }




    private void setPowers(double frontLeftPower, double frontRightPower, double backLeftPower, double backRightPower) {
         double maxSpeed = 1.0;
         maxSpeed = Math.max(maxSpeed, Math.abs(frontLeftPower));
         maxSpeed = Math.max(maxSpeed, Math.abs(frontRightPower));
         maxSpeed = Math.max(maxSpeed, Math.abs(backLeftPower));
         maxSpeed = Math.max(maxSpeed, Math.abs(backRightPower));

         frontLeftPower /= maxSpeed;
         frontRightPower /= maxSpeed;
         backLeftPower /= maxSpeed;
         backRightPower /= maxSpeed;

         frontleft.setPower(frontLeftPower);
         frontright.setPower(frontRightPower);
         backleft.setPower(backLeftPower);
         backright.setPower(backRightPower);
    }

    public void drive ( double forward, double strafe, double turn){
        frontLeftPower = forward + turn + strafe;
        frontRightPower = forward - turn - strafe;
        backLeftPower = forward + turn - strafe;
        backRightPower = forward - turn + strafe;


        setPowers(frontLeftPower, frontRightPower, backLeftPower, backRightPower);


    }


}
