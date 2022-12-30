package org.firstinspires.ftc.teamcode.opmodes.teleop.Testing;


import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;


import org.firstinspires.ftc.teamcode.opmodes.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.opmodes.subsystems.Intake;
import org.firstinspires.ftc.teamcode.opmodes.subsystems.Outtake;

@Config
@TeleOp
public class ServoPos extends LinearOpMode {

    public static double servoPos = 0;


    @Override
    public void runOpMode() throws InterruptedException {

        Servo depositServo = hardwareMap.servo.get("deposit");



        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {








            telemetry.addData("Servo Position: ", servoPos);

            telemetry.update();
        }
    }
}
