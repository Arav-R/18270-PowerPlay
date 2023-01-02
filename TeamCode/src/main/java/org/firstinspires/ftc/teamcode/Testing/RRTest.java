package org.firstinspires.ftc.teamcode.Testing;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.RoadRunner.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.RoadRunner.trajectorysequence.TrajectorySequence;

/*
 * This is an example of a more complex path to really test the tuning.
 */
@Autonomous(group = "drive")
public class RRTest extends LinearOpMode {



    public static double MAX_VEL = 52.48180821614297;
    public static double MAX_ACCEL = 52.48180821614297;


    @Override
    public void runOpMode() throws InterruptedException {

        //PhotonCore.enable(); // Enable PhotonCore





        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

        Pose2d startPose = new Pose2d(35, -63, Math.toRadians(90));


        drive.setPoseEstimate(startPose);

        TrajectorySequence trajSeq = drive.trajectorySequenceBuilder(startPose)

                .forward(50)
                .turn(Math.toRadians(-90))


                .build();




        waitForStart();

        if (!isStopRequested())
            drive.followTrajectorySequence(trajSeq);



    }
}
