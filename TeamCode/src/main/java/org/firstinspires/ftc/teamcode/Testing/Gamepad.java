package org.firstinspires.ftc.teamcode.Testing;


import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.opmodes.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.opmodes.subsystems.Intake;
import org.firstinspires.ftc.teamcode.opmodes.subsystems.Outtake;


@TeleOp()
public class Gamepad extends LinearOpMode {
    Drivetrain drive = new Drivetrain();
    Intake intake = new Intake();
    Outtake outtake = new Outtake();


    ElapsedTime loopTimer = new ElapsedTime();

    double clawPos = 0;
    int slidePos = 0;



    @Override
    public void runOpMode() throws InterruptedException {
        Gamepad currentGamepad1 = new Gamepad();
        Gamepad previousGamepad1 = new Gamepad();

        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());


        drive.init(hardwareMap);
        intake.init(hardwareMap);
        outtake.init(hardwareMap);


        loopTimer.reset();

        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {
            //currentGamepad1.copy(gamepad1);

            gamepad1.rumble(1000);

            gamepad1.setLedColor(1.0, 0, 0, 1000);




            //previousGamepad1.copy(currentGamepad1);



            telemetry.addData("Distance: ", intake.getDistanceCM());

            telemetry.update();
        }
    }
}
