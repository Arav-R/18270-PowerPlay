package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Blinker;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gyroscope;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp

public class FSMtele extends LinearOpMode {

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
    public static final int outakePosition = 0;

    public static final int retractPosition = 0;
    public static final int lowPosition = 300;
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

            // Drive Code

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


            // FSM Code


        }
    }
    //helper functions===================================================

    public void movetopos (DcMotor motortomove,int pos, double speed){
        motortomove.setTargetPosition(pos);
        motortomove.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motortomove.setPower(speed);
    }

}

