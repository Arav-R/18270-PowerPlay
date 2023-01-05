package org.firstinspires.ftc.teamcode.Testing;


import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;


import org.firstinspires.ftc.teamcode.opmodes.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.opmodes.subsystems.Intake;
import org.firstinspires.ftc.teamcode.opmodes.subsystems.Outtake;

@Disabled
@TeleOp()
public class Testing extends LinearOpMode {
    Drivetrain drive = new Drivetrain();
    Intake intake = new Intake();
    Outtake outtake = new Outtake();


    ElapsedTime scoreTimer = new ElapsedTime();

    double clawPos = 0;
    int slidePos = 0;



    @Override
    public void runOpMode() throws InterruptedException {
        Gamepad currentGamepad1 = new Gamepad();
        Gamepad previousGamepad1 = new Gamepad();


        drive.init(hardwareMap);
        intake.init(hardwareMap);
        outtake.init(hardwareMap);


        scoreTimer.reset();

        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {
            currentGamepad1.copy(gamepad1);


            double forward = -gamepad1.left_stick_y;
            double strafe = gamepad1.left_stick_x;
            double turn = gamepad1.right_stick_x;
/*
            if(gamepad1.left_bumper) {
                drive.drive(forward * .3, strafe * .3, turn * .3);
            } else {
                drive.drive(forward, strafe, turn);
            }
*/
/*

            if (currentGamepad1.dpad_up && !previousGamepad1.dpad_up) {
                if (clawPos <= 1) {
                    clawPos += .05;

                }


            } else if (currentGamepad1.dpad_down && !previousGamepad1.dpad_down) {
                if (clawPos >= 0) {
                    clawPos -= .05;

                }

            }

            intake.moveJoint2(clawPos);
*/



            if (currentGamepad1.dpad_right && !previousGamepad1.dpad_right) {
                intake.closeClaw();
            } else if (currentGamepad1.dpad_left && !previousGamepad1.dpad_left) {
                intake.openClaw();
            }

            if (currentGamepad1.dpad_up && !previousGamepad1.dpad_up) {
                intake.flipArm();
            } else if (currentGamepad1.dpad_down && !previousGamepad1.dpad_down) {
                intake.dropArm();
            }


            if (currentGamepad1.b && !previousGamepad1.b) {
                intake.intakePosition();


            } else if (currentGamepad1.a && !previousGamepad1.a) {
                intake.transferPosition();

            }






            if (currentGamepad1.y && !previousGamepad1.y) {
                outtake.scoreDeposit();

            } else if (currentGamepad1.x && !previousGamepad1.x) {
                outtake.transferDeposit();

            }


            if (currentGamepad1.left_bumper && !previousGamepad1.left_bumper) {
                slidePos -= 10;

            } else if (currentGamepad1.right_bumper && !previousGamepad1.right_bumper) {
                slidePos += 10;

            }

            outtake.moveTurret(slidePos, 0.7);



            if (currentGamepad1.left_stick_button && !previousGamepad1.left_stick_button) {
                outtake.retractSlide();

            } else if (currentGamepad1.right_stick_button && !previousGamepad1.right_stick_button) {

                outtake.extendSlideLeft();
            }











            previousGamepad1.copy(currentGamepad1);



            telemetry.addData("Intake Slide Position: ", slidePos);

            telemetry.update();
        }
    }
}
