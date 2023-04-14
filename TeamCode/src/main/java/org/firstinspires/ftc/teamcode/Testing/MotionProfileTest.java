package org.firstinspires.ftc.teamcode.Testing;


import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.roadrunner.profile.MotionProfile;
import com.acmerobotics.roadrunner.profile.MotionProfileGenerator;
import com.acmerobotics.roadrunner.profile.MotionState;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;
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

    public boolean motion = false;




    @Override
    public void runOpMode() throws InterruptedException {
        Gamepad currentGamepad1 = new Gamepad();
        Gamepad previousGamepad1 = new Gamepad();

        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());



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
            currentGamepad1.copy(gamepad1);

            batteryVoltage = batteryVoltageSensor.getVoltage();


            MotionState state = profile.get(elapsedTime.seconds());




            // Rising edge detector
            if (currentGamepad1.a && !previousGamepad1.a) {
                // This will set intakeToggle to true if it was previously false
                // and intakeToggle to false if it was previously true,
                // providing a toggling behavior.
                motion = !motion;
            }

            // Using the toggle variable to control the robot.
            if (motion) {

                flip1Servo.setPosition(state.getX());
                flip2Servo.setPosition(state.getX());
            }
            else {
                flip1Servo.setPosition(0);
                flip2Servo.setPosition(0);

                elapsedTime.reset();
            }



















            previousGamepad1.copy(currentGamepad1);

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
