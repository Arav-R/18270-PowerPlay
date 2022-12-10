package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Blinker;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gyroscope;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp

public class AndroidCopy extends LinearOpMode {

    private Blinker control_Hub;
    private Blinker expansion_Hub_2;
    private DcMotor arm;
    private DcMotor backleft;
    private DcMotor backright;
    private DcMotor frontleft;
    private DcMotor frontright;
    private Gyroscope imu;
    private CRServo intake1;
    private CRServo intake2;
    private DcMotor slide1;
    private DcMotor slide2;
    double LFPower, RFPower, LBPower, RBPower;

    public static final int intakePosition = 1450;
    public static final int hoverPosition = 1150;
    public static final int vertPosition = 700;
    public static final int outakePosition = 80;

    public static final int retractPosition = 0;
    public static final int lowPosition = 285;
    public static final int midPosition = 600;
    public static final int highPosition = 900;

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        frontleft = hardwareMap.dcMotor.get("frontleft");
        frontright = hardwareMap.dcMotor.get("frontright");
        backleft = hardwareMap.dcMotor.get("backleft");
        backright = hardwareMap.dcMotor.get("backright");

        slide1 = hardwareMap.dcMotor.get("slide1");
        slide2 = hardwareMap.dcMotor.get("slide2");
        arm = hardwareMap.dcMotor.get("arm");
        intake1 = hardwareMap.crservo.get("intake1");
        intake2 = hardwareMap.crservo.get("intake2");

        frontleft.setDirection(DcMotor.Direction.REVERSE);
        backleft.setDirection(DcMotor.Direction.REVERSE);

        slide1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slide2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        slide1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        slide2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        slide1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        slide2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontleft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontright.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backleft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backright.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        //arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        slide2.setDirection(DcMotor.Direction.REVERSE);


        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        int slidePosition = 0;
        int resetPosition = 0;
        ElapsedTime runtime = new ElapsedTime();
        int on_or_not = 0;
        int ground = 0;
        boolean prevY = false;
        double intakePower = 0;
        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            //CR servo control====================================================
            if (gamepad1.right_bumper) {
                intakePower = 1;
            }
            if (gamepad1.right_trigger > 0.5) {
                intakePower = 0;
            }
            //end CR servo control================================================


            //begin arm control===================================================
            if (gamepad1.x) {
                //hover
                movetopos(arm, hoverPosition, 1);
                //intakePower=1;
                /*slide1.setPower(-0.5);
                slide2.setPower(-0.5);*/
            } else if (gamepad1.dpad_left) {
                movetopos(arm, retractPosition, 1);
            }



            if (gamepad1.b) {
                runtime.reset();
                on_or_not = 1;

            }
            //end arm control=====================================================
            //begin lift control==================================================
            if (gamepad1.y) {
                slidePosition = 3;
            }
            if (gamepad1.dpad_up) {
                slidePosition = 2;
            }
            if (gamepad1.dpad_right) {
                slidePosition = 1;
            }
            if (gamepad1.a) {
                slidePosition = 0;
            }


            if (slidePosition == 3) {
                movetopos(slide1, highPosition, 0.7);
                movetopos(slide2, highPosition, 0.7);
            } else if (slidePosition == 2) {
                movetopos(slide1, midPosition, .7);
                movetopos(slide2, midPosition, .7);
            } else if (slidePosition == 1) {
                movetopos(slide1, lowPosition, .7);
                movetopos(slide2, lowPosition, .7);
            } else {
                movetopos(slide1, retractPosition, 0.7);
                movetopos(slide2, retractPosition, 0.7);
            }


            if (runtime.milliseconds() < 750 && on_or_not == 1) { //less than 700ms
                movetopos(arm, intakePosition, 1);
                intakePower = -1; //intake
            } else if (runtime.milliseconds() < 1500 && on_or_not == 1) { //
                movetopos(arm, outakePosition, 1);
            } else if (runtime.milliseconds() < 2000 && on_or_not == 1) {
                intakePower = 1;
            } else if (runtime.milliseconds() < 3000 && on_or_not == 1) {
                movetopos(arm, hoverPosition, 1);
                intakePower = 0;
            } else {
                on_or_not = 0;
            }

            if(gamepad1.dpad_down) {
                runtime.reset();
                ground = 1;
            }

            if (runtime.milliseconds() < 1000 && ground == 1) {
                movetopos(arm, -50, 1);
                intakePower = -1; //intake
            } else if (runtime.milliseconds() < 2000 && ground == 1) {
                movetopos(arm, intakePosition - 100, 1);
            }  else if (runtime.milliseconds() < 2500 && ground == 1) {
                intakePower = 1; //outtake
            } else if (runtime.milliseconds() < 2600 && ground == 1) {
                intakePower = 0; //outtake
                movetopos(arm, hoverPosition - 100, 1);
            }else {
                ground = 0;
            }


            intake1.setPower(-intakePower);
            intake2.setPower(intakePower);


            //arm.setPower(gamepad1.right_trigger-gamepad1.left_trigger);

            //moveMec(-gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x/2);


            double y = gamepad1.left_stick_y; // Remember, this is reversed!
            double x = -gamepad1.left_stick_x * 1.1; // Counteract imperfect strafing
            double rx = gamepad1.right_stick_x;

            // Denominator is the largest motor power (absolute value) or 1
            // This ensures all the powers maintain the same ratio, but only when
            // at least one is out of the range [-1, 1]
            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
            double frontLeftPower = (y + x + rx) / denominator;
            double backLeftPower = (y - x + rx) / denominator;
            double frontRightPower = (y - x - rx) / denominator;
            double backRightPower = (y + x - rx) / denominator;


            if (gamepad1.left_bumper) {

                frontleft.setPower(frontLeftPower * 0.3);
                backleft.setPower(backLeftPower * 0.3);
                frontright.setPower(frontRightPower * 0.3);
                backright.setPower(backRightPower * 0.3);

            } else {
                frontleft.setPower(frontLeftPower);
                backleft.setPower(backLeftPower);
                frontright.setPower(frontRightPower);
                backright.setPower(backRightPower);
            }


            telemetry.addData("slide1 Pos:", slide1.getCurrentPosition());
            telemetry.addData("slide2 Pos:", arm.getCurrentPosition());
            telemetry.addData("arm Pos:", arm.getCurrentPosition());
            telemetry.addData("runtime:", runtime.milliseconds());


            telemetry.update();

        }
    }
    //helper functions===================================================
    public void moveMec ( double forward, double strafe, double turn){
        LFPower = forward + turn + strafe;
        RFPower = forward - turn - strafe;
        LBPower = forward + turn - strafe;
        RBPower = forward - turn + strafe;
        frontleft.setPower(LFPower);
        frontright.setPower(RFPower);
        backleft.setPower(LBPower);
        backright.setPower(RBPower);
    }
    public void movetopos (DcMotor motortomove,int pos, double speed){
        motortomove.setTargetPosition(pos);
        motortomove.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motortomove.setPower(speed);
    }

}

