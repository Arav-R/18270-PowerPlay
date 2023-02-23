package org.firstinspires.ftc.teamcode.Testing;


import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
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

@Disabled
@Config
@TeleOp
public class FlipPos extends LinearOpMode {

    public static double servoPos = 0;


    @Override
    public void runOpMode() throws InterruptedException {

        Servo flip1 = hardwareMap.servo.get("flip1");
        Servo flip2 = hardwareMap.servo.get("flip2");

        flip2.setDirection(Servo.Direction.REVERSE);

        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {

            flip1.setPosition(servoPos);
            flip2.setPosition(servoPos);
/*
            if(gamepad1.a){
                intake.closeClaw();
            } else if (gamepad1.b){
                intake.openClaw();
            }
*/



            telemetry.addData("Servo Position: ", servoPos);

            telemetry.update();
        }
    }
}
