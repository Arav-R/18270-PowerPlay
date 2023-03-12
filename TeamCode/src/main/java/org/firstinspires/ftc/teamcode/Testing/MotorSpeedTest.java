package org.firstinspires.ftc.teamcode.Testing;


import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;


@Config
@TeleOp
public class MotorSpeedTest extends LinearOpMode {

//    public static int motorPos = 0;
//    public static double motorSpeed = 0.7;

    public static String motorName = "turret";


    @Override
    public void runOpMode() throws InterruptedException {

        DcMotorEx motor = hardwareMap.get(DcMotorEx.class, motorName);

        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);



        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {


            if (gamepad1.left_trigger > 0) {
                motor.setPower(-gamepad1.left_trigger);
            } else if (gamepad1.right_trigger > 0) {
                motor.setPower(gamepad1.right_trigger);
            } else {
                motor.setPower(0);
            }





            telemetry.addData("Real Motor Position: ", motor.getCurrentPosition());
            telemetry.addData("Real Motor Position: ", motor.getVelocity());

            telemetry.update();
        }
    }
}
