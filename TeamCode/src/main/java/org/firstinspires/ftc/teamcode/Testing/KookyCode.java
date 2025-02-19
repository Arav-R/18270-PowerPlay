package org.firstinspires.ftc.teamcode.Testing;


import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

//@Disabled
@Config
@TeleOp
public class KookyCode extends OpMode{
    private PIDController controller;

    public static double p = 0, i = 0, d = 0;
    public static double f = 0;

    public static int target = 0;


    private DcMotorEx motor;


    @Override
    public void init(){
        controller = new PIDController(p, i , d);
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        motor = hardwareMap.get(DcMotorEx.class, "intakeslide");
        motor.setDirection(DcMotorEx.Direction.REVERSE);
        motor.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);

        motor.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);


    }

    @Override
    public void loop(){
        controller.setPID(p, i, d);
        int motorPos = motor.getCurrentPosition();
        double pid = controller.calculate(motorPos, target);

        double power = pid + f;

        motor.setPower(power);


        telemetry.addData("pos: ", motorPos);
        telemetry.addData("target: ", target);

        telemetry.addData("p: ", p);
        telemetry.addData("i: ", i);
        telemetry.addData("d: ", d);
        telemetry.addData("power from motor: ", motor.getPower());
        telemetry.addData("power to motor: ", power);
        telemetry.update();


    }
}
