package org.firstinspires.ftc.teamcode.Testing;


import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.VoltageSensor;

//@Disabled
@Config
@TeleOp
public class VoltageTest extends LinearOpMode {

    private DcMotor backleft;
    private DcMotor backright;
    private DcMotor frontleft;
    private DcMotor frontright;

    public DcMotorEx intakeSlide;
    public DcMotorEx outtakeSlide1, outtakeSlide2, turret;

    // get battery voltage
    private VoltageSensor batteryVoltageSensor;
    private double batteryVoltage;


    public static double servoPos = 0;
    public static double testValue = 100;

    public static String servoName = "deposit";


    @Override
    public void runOpMode() throws InterruptedException {

        batteryVoltageSensor = hardwareMap.voltageSensor.iterator().next();

        batteryVoltage = batteryVoltageSensor.getVoltage();
        // voltage comp
        testValue = testValue * (12 / batteryVoltage);


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

            batteryVoltage = batteryVoltageSensor.getVoltage();





            telemetry.addData("Voltage: ", batteryVoltage);
            telemetry.addData("Test Value: ", testValue);




            telemetry.update();
        }
    }
}
