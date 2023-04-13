package org.firstinspires.ftc.teamcode.Testing;


import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.profile.MotionProfile;
import com.acmerobotics.roadrunner.profile.MotionProfileGenerator;
import com.acmerobotics.roadrunner.profile.MotionState;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

//@Disabled
@Config
@TeleOp
public class MotionProfileTest extends LinearOpMode {


    // get battery voltage
    private VoltageSensor batteryVoltageSensor;
    private double batteryVoltage;

    private Servo flip1Servo, flip2Servo;



    public static double servoPos = 0;
    public static double testValue = 100;

    public static String servoName = "deposit";




    @Override
    public void runOpMode() throws InterruptedException {

        batteryVoltageSensor = hardwareMap.voltageSensor.iterator().next();

        batteryVoltage = batteryVoltageSensor.getVoltage();
        // voltage comp
        testValue = testValue * (12 / batteryVoltage);




        flip1Servo = hardwareMap.servo.get("flip1");
        flip2Servo = hardwareMap.servo.get("flip2");

        flip1Servo.setDirection(Servo.Direction.REVERSE);



        MotionProfile profile = MotionProfileGenerator.generateSimpleMotionProfile(
                new MotionState(0, 0, 0),
                new MotionState(.555, 0, 0),
                .1,
                .1,
                1
        );

        ElapsedTime elapsedTime = new ElapsedTime();



        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {

            batteryVoltage = batteryVoltageSensor.getVoltage();

            MotionState state = profile.get(elapsedTime.seconds());




            flip1Servo.setPosition(state.getX());
            flip2Servo.setPosition(state.getX());











            telemetry.addData("Voltage: ", batteryVoltage);
            telemetry.addData("Test Value: ", testValue);

            telemetry.addData("profile X: ", state.getX());
            telemetry.addData("profile V: ", state.getV());
            telemetry.addData("profile A: ", state.getA());
            telemetry.addData("profile J: ", state.getJ());

            telemetry.addData("Flip1 Pos: ", flip1Servo.getPosition());
            telemetry.addData("Flip2 Pos: ", flip2Servo.getPosition());





            telemetry.update();
        }
    }
}
