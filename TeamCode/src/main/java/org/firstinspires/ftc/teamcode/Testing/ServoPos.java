package org.firstinspires.ftc.teamcode.Testing;


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

    Intake intake = new Intake();


    @Override
    public void runOpMode() throws InterruptedException {

        Servo servo = hardwareMap.servo.get("flip1");
        intake.init(hardwareMap);




        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {

            servo.setPosition(servoPos);

            if(gamepad1.a){
                intake.closeClaw();
            } else if (gamepad1.b){
                intake.openClaw();
            }




            telemetry.addData("Servo Position: ", servoPos);

            telemetry.update();
        }
    }
}
