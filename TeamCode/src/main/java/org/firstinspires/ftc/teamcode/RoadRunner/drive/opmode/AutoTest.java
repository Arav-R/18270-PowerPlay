package org.firstinspires.ftc.teamcode.RoadRunner.drive.opmode;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.outoftheboxrobotics.photoncore.PhotonCore;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.RoadRunner.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.RoadRunner.trajectorysequence.TrajectorySequence;

/*
 * This is an example of a more complex path to really test the tuning.
 */
@Autonomous(group = "drive")
public class AutoTest extends LinearOpMode {

    private DcMotor slide1;
    private DcMotor slide2;


    @Override
    public void runOpMode() throws InterruptedException {

        PhotonCore.enable(); // Enable PhotonCore

        // Initialize the hardware variables.
        slide1 = hardwareMap.dcMotor.get("slide1");
        slide2 = hardwareMap.dcMotor.get("slide2");

        // Reset Encoders
        slide1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slide2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        // Set to RUN_USING_ENCODER
        slide1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        slide2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Set to BRAKE
        slide1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        slide2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);





        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

        Pose2d startPose = new Pose2d(35, -63, Math.toRadians(90));

        drive.setPoseEstimate(startPose);

        TrajectorySequence trajSeq = drive.trajectorySequenceBuilder(startPose)

                .forward(53)
                .turn(Math.toRadians(-135))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> {
                    slide1.setTargetPosition(900); //intake pos
                    slide2.setTargetPosition(-900); //intake pos

                    slide1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    slide2.setMode(DcMotor.RunMode.RUN_TO_POSITION);

                    slide1.setPower(0.5);
                    slide2.setPower(-0.5);
                })
                .back(10)
                //score cone
                .waitSeconds(1)
                .UNSTABLE_addTemporalMarkerOffset(0, () -> {
                    slide1.setTargetPosition(0); //intake pos
                    slide2.setTargetPosition(0); //intake pos

                    slide1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    slide2.setMode(DcMotor.RunMode.RUN_TO_POSITION);

                    slide1.setPower(0.5);
                    slide2.setPower(-0.5);
                })
                .forward(15)

                .turn(Math.toRadians(45))
                //1 cycle
                .forward(25)

                .build();




        waitForStart();

        if (!isStopRequested())
            drive.followTrajectorySequence(trajSeq);



    }
}
