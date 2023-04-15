package org.firstinspires.ftc.teamcode.Testing;


import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;


import org.firstinspires.ftc.teamcode.opmodes.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.opmodes.subsystems.Intake;
import org.firstinspires.ftc.teamcode.opmodes.subsystems.Outtake;

//@Disabled
@Config
@TeleOp
public class MotorPos extends LinearOpMode {

    public static int motorPos = 0;
    public static double motorSpeed = 0.7;

    public static String motorName = "intakeslide";


    @Override
    public void runOpMode() throws InterruptedException {

        DcMotor motor = hardwareMap.dcMotor.get(motorName);

        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);



        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {



            motor.setTargetPosition(motorPos);
            motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            motor.setPower(motorSpeed);





            telemetry.addData("Real Motor Position: ", motor.getCurrentPosition());

            telemetry.update();
        }
    }
}
