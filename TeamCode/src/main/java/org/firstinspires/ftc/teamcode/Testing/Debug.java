package org.firstinspires.ftc.teamcode.Testing;


import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;


import org.firstinspires.ftc.teamcode.opmodes.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.opmodes.subsystems.Intake;
import org.firstinspires.ftc.teamcode.opmodes.subsystems.Outtake;

//@Disabled
@Config
@TeleOp
public class Debug extends LinearOpMode {

    private DcMotor backleft;
    private DcMotor backright;
    private DcMotor frontleft;
    private DcMotor frontright;

    public DcMotorEx intakeSlide;
    public DcMotorEx outtakeSlide1, outtakeSlide2, turret;


    public static double servoPos = 0;

    public static String servoName = "deposit";


    @Override
    public void runOpMode() throws InterruptedException {


        frontleft = hardwareMap.dcMotor.get("frontleft");
        frontright = hardwareMap.dcMotor.get("frontright");
        backleft = hardwareMap.dcMotor.get("backleft");
        backright = hardwareMap.dcMotor.get("backright");

        frontleft.setDirection(DcMotor.Direction.REVERSE);
        backleft.setDirection(DcMotor.Direction.REVERSE);


        intakeSlide = hardwareMap.get(DcMotorEx.class, "intakeslide");

        outtakeSlide1 = hardwareMap.get(DcMotorEx.class,"outtake1");
        outtakeSlide2 = hardwareMap.get(DcMotorEx.class,"outtake2");
        outtakeSlide2.setDirection(DcMotor.Direction.REVERSE);

        turret = hardwareMap.get(DcMotorEx.class,"turret");


        Servo servo = hardwareMap.servo.get(servoName);



        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {

            servo.setPosition(servoPos);




            telemetry.addLine("//Drivetrain encoders: ");
            telemetry.addData("frontleft: ", frontleft.getCurrentPosition());
            telemetry.addData("frontright: ", frontright.getCurrentPosition());
            telemetry.addData("backleft: ", backleft.getCurrentPosition());
            telemetry.addData("backright: ", backright.getCurrentPosition());

            telemetry.addLine("/////////////////////////////////////////////////");
            telemetry.addData("Intake encoders: ", intakeSlide.getCurrentPosition());

            telemetry.addLine("/////////////////////////////////////////////////");
            telemetry.addData("Turret Encoders: ", turret.getCurrentPosition());
            telemetry.addData("Outtake Slide1: ", outtakeSlide1.getCurrentPosition());
            telemetry.addData("Outtake Slide2: ", outtakeSlide2.getCurrentPosition());

            telemetry.addLine("/////////////////////////////////////////////////");
            telemetry.addData("Servo Set Position: ", servoPos);

            telemetry.update();
        }
    }
}
