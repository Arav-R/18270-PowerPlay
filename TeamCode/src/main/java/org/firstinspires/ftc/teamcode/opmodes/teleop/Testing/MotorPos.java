package org.firstinspires.ftc.teamcode.opmodes.teleop.Testing;


import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;


import org.firstinspires.ftc.teamcode.opmodes.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.opmodes.subsystems.Intake;
import org.firstinspires.ftc.teamcode.opmodes.subsystems.Outtake;

@Config
@TeleOp
public class MotorPos extends LinearOpMode {

    public static int motorPos = 0;


    @Override
    public void runOpMode() throws InterruptedException {

        DcMotor motor = hardwareMap.dcMotor.get("turret");

        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);



        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {



            motor.setTargetPosition(motorPos);
            motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            motor.setPower(0.7);





            telemetry.addData("Motor Position: ", motorPos);

            telemetry.update();
        }
    }
}
