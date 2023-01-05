package org.firstinspires.ftc.teamcode.Testing;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;


import org.firstinspires.ftc.teamcode.opmodes.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.opmodes.subsystems.Intake;
import org.firstinspires.ftc.teamcode.opmodes.subsystems.Outtake;

@TeleOp
public class ZeroTurret extends LinearOpMode {

    private DcMotor turret;

    @Override
    public void runOpMode() throws InterruptedException {
        turret = hardwareMap.dcMotor.get("turret");


        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {

            if(gamepad1.left_bumper) {
                turret.setPower(.5);
            } else if (gamepad1.right_bumper) {
                turret.setPower(-.5);
            } else {
                turret.setPower(0);
            }
        }
    }
}
