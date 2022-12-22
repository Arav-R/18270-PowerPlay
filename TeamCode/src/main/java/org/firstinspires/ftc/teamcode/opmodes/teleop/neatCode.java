package org.firstinspires.ftc.teamcode.opmodes.teleop;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;


import org.firstinspires.ftc.teamcode.opmodes.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.opmodes.subsystems.Intake;

@TeleOp()
public class neatCode extends LinearOpMode {
    Drivetrain drive = new Drivetrain();
    Intake intake = new Intake();

    public enum ScoreState {
        SCORE_GRAB,
        SCORE_RETRACT,
        SCORE_TRANSFER,
        SCORE_EXTEND,
        SCORE_DEPOSIT,
        SCORE_RESET
    }

    ElapsedTime scoreTimer = new ElapsedTime();



    @Override
    public void runOpMode() throws InterruptedException {
        drive.init(hardwareMap);

        scoreTimer.reset();

        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {
            double forward = -gamepad1.left_stick_y;
            double strafe = gamepad1.left_stick_x;
            double turn = gamepad1.right_stick_x;

            if(gamepad1.left_bumper) {
                drive.drive(forward * .3, strafe * .3, turn * .3);
            } else {
                drive.drive(forward, strafe, turn);
            }

            if (gamepad1.dpad_right) {
                intake.moveToPos(500, 0.7);
            }

            if (gamepad1.a) {
                intake.closeClaw();
                intake.moveToPos(0, 0.7);
            }




        }
    }
}
