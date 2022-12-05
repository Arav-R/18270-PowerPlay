package org.firstinspires.ftc.teamcode.RoadRunner.drive.opmode;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.outoftheboxrobotics.photoncore.PhotonCore;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
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
    private DcMotor armMotor;
    private CRServo intake1;
    private CRServo intake2;

    public static double MAX_VEL = 52.48180821614297;
    public static double MAX_ACCEL = 52.48180821614297;


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




        armMotor = hardwareMap.dcMotor.get("arm");
        // Reset Encoders
        armMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        // Set to RUN_USING_ENCODER
        armMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Set to BRAKE
        armMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);




        intake1 = hardwareMap.crservo.get("intake1");
        intake2 = hardwareMap.crservo.get("intake2");





        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

        Pose2d startPose = new Pose2d(35, -63, Math.toRadians(90));


        drive.setPoseEstimate(startPose);

        TrajectorySequence trajSeq = drive.trajectorySequenceBuilder(startPose)

                //.setConstraints(MAX_VEL, MAX_ACCEL)
//
                .UNSTABLE_addTemporalMarkerOffset(0, () -> {
                    armMotor.setTargetPosition(700); //intake pos


                    armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);


                    armMotor.setPower(0.5);

                })
                //.waitSeconds(2)

                .forward(52)
                .turn(Math.toRadians(-135))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> {
                    slide1.setTargetPosition(900); //intake pos
                    slide2.setTargetPosition(-900); //intake pos

                    slide1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    slide2.setMode(DcMotor.RunMode.RUN_TO_POSITION);

                    slide1.setPower(0.5);
                    slide2.setPower(-0.5);
                })
                .waitSeconds(2)

                .back(12)
                //score cone
                .waitSeconds(1)

                .UNSTABLE_addTemporalMarkerOffset(0, () -> {
                    slide1.setTargetPosition(0); //intake pos
                    slide2.setTargetPosition(0); //intake pos

                    slide1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    slide2.setMode(DcMotor.RunMode.RUN_TO_POSITION);

                    slide1.setPower(0.7);
                    slide2.setPower(-0.7);
                })

                .forward(12)

                .waitSeconds(1)
                .splineTo(new Vector2d(58, -11.9), Math.toRadians(-2))
                .waitSeconds(1.5)//intake cone


                .UNSTABLE_addTemporalMarkerOffset(0, () -> {
                    armMotor.setTargetPosition(1250); //intake pos


                    armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);


                    armMotor.setPower(0.5);

                    intake1.setPower(1);
                    intake2.setPower(-1);

                })
                .waitSeconds(2)


                .UNSTABLE_addTemporalMarkerOffset(0, () -> {
                    armMotor.setTargetPosition(0); //intake pos


                    armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);


                    armMotor.setPower(0.5);


                })
                .waitSeconds(2)
                .UNSTABLE_addTemporalMarkerOffset(0, () -> {
                    intake1.setPower(-1);
                    intake2.setPower(1);
                    armMotor.setTargetPosition(700); //intake pos


                    armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);


                    armMotor.setPower(0.5);

                })
                .waitSeconds(1)
                .UNSTABLE_addTemporalMarkerOffset(0, () -> {

                    intake1.setPower(0);
                    intake2.setPower(0);

                    slide1.setTargetPosition(900); //intake pos
                    slide2.setTargetPosition(-900); //intake pos

                    slide1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    slide2.setMode(DcMotor.RunMode.RUN_TO_POSITION);

                    slide1.setPower(0.5);
                    slide2.setPower(-0.5);
                })
                .waitSeconds(2)
                .setReversed(true)
                .splineTo(new Vector2d(26, -1), Math.toRadians(135))

                .waitSeconds(1)

                .UNSTABLE_addTemporalMarkerOffset(0, () -> {
                    slide1.setTargetPosition(0); //intake pos
                    slide2.setTargetPosition(0); //intake pos

                    slide1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    slide2.setMode(DcMotor.RunMode.RUN_TO_POSITION);

                    slide1.setPower(0.7);
                    slide2.setPower(-0.7);
                })

                .forward(14)
                .UNSTABLE_addTemporalMarkerOffset(0, () -> {
                    armMotor.setTargetPosition(0); //intake pos


                    armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);


                    armMotor.setPower(0.5);


                })
                .lineToLinearHeading(new Pose2d(6, -14, Math.toRadians(0)))




/*
                .turn(Math.toRadians(45))
                //1 cycle
                .forward(25)

 */


                .build();




        waitForStart();

        if (!isStopRequested())
            drive.followTrajectorySequence(trajSeq);





    }
}
