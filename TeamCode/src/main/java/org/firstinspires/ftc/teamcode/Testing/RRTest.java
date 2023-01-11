package org.firstinspires.ftc.teamcode.Testing;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.RoadRunner.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.RoadRunner.trajectorysequence.TrajectorySequence;
import org.firstinspires.ftc.teamcode.opmodes.subsystems.Intake;

/*
 * This is an example of a more complex path to really test the tuning.
 */
@Disabled
@Autonomous(group = "drive")
public class RRTest extends LinearOpMode {



    public static double MAX_VEL = 52.48180821614297;
    public static double MAX_ACCEL = 52.48180821614297;


    Intake intake = new Intake();




    @Override
    public void runOpMode() throws InterruptedException {

        //PhotonCore.enable(); // Enable PhotonCore

        intake.init(hardwareMap);





        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

        Pose2d startPose = new Pose2d(35, -63, Math.toRadians(90));


        drive.setPoseEstimate(startPose);

        TrajectorySequence trajSeq = drive.trajectorySequenceBuilder(startPose)


                .UNSTABLE_addTemporalMarkerOffset(0, () -> {
                    intake.zeroPosition();
                    intake.dropArm();
                    intake.openClaw();
                })

                .waitSeconds(0.25)
                .forward(48)
                .UNSTABLE_addTemporalMarkerOffset(-0.5, () -> {
                    intake.flipArm();
                })
                .turn(Math.toRadians(-95))


                .build();




        waitForStart();

        if (!isStopRequested())
            drive.followTrajectorySequence(trajSeq);



    }
}
