package org.firstinspires.ftc.teamcode.Testing;


import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;

//@Disabled
@Config
@TeleOp
public class KookyCodeOuttake extends OpMode{
    private PIDController controller;

    public static double p = 0, i = 0, d = 0;
    public static double f = 0;

    public static int target = 0;

    private DcMotorEx outtakeSlide1;
    private DcMotorEx outtakeSlide2;


    @Override
    public void init(){
        controller = new PIDController(p, i , d);
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        outtakeSlide1 = hardwareMap.get(DcMotorEx.class,"outtake1");
        outtakeSlide2 = hardwareMap.get(DcMotorEx.class,"outtake2");
        outtakeSlide2.setDirection(DcMotorEx.Direction.REVERSE);
        //motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

    }

    @Override
    public void loop(){
        controller.setPID(p, i, d);
        int motorPos = outtakeSlide1.getCurrentPosition();
        double pid = controller.calculate(motorPos, target);

        double power = pid + f;

        outtakeSlide1.setPower(power);
        outtakeSlide2.setPower(power);



        telemetry.addData("pos1: ", outtakeSlide1.getCurrentPosition());
        telemetry.addData("pos2: ", outtakeSlide2.getCurrentPosition());
        telemetry.addData("target: ", target);

        telemetry.addData("p: ", p);
        telemetry.addData("i: ", i);
        telemetry.addData("d: ", d);
        telemetry.addData("power: ", power);
        telemetry.update();


    }
}
