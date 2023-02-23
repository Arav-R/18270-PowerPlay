/*
 * Copyright (c) 2021 OpenFTC Team
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.firstinspires.ftc.teamcode.oldRobot;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
//import com.outoftheboxrobotics.photoncore.PhotonCore;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gyroscope;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.RoadRunner.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.RoadRunner.trajectorysequence.TrajectorySequence;
import org.firstinspires.ftc.teamcode.opmodes.auto.vision.AprilTagDetectionPipeline;
import org.openftc.apriltag.AprilTagDetection;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

import java.util.ArrayList;

@Disabled
@Autonomous
public class rr2 extends LinearOpMode
{


    ElapsedTime armTimer = new ElapsedTime();

    OpenCvCamera camera;
    AprilTagDetectionPipeline aprilTagDetectionPipeline;

    static final double FEET_PER_METER = 3.28084;

    // Lens intrinsics
    // UNITS ARE PIXELS
    // NOTE: this calibration is for the C920 webcam at 800x448.
    // You will need to do your own calibration for other configurations!
    double fx = 578.272;
    double fy = 578.272;
    double cx = 402.145;
    double cy = 221.506;

    // UNITS ARE METERS
    double tagsize = 0.166;

    // Tag ID 1,2,3 from the 36h11 family
    int LEFT = 1;
    int MIDDLE = 2;
    int RIGHT = 3;

    AprilTagDetection tagOfInterest = null;

    private DcMotor armMotor;
    private DcMotor backleft;
    private DcMotor backright;
    private DcMotor frontleft;
    private DcMotor frontright;
    private Gyroscope imu;
    private CRServo intake1;
    private CRServo intake2;
    private DcMotor slide1;
    private DcMotor slide2;

    private CRServo frontIntakeWheels = null;
    private CRServo backIntakeWheels  = null;

    double LFPower, RFPower, LBPower, RBPower;
    double LFClip, RFClip, LBClip, RBClip;
    ElapsedTime runtime=new ElapsedTime();
    double slope, POS1, POS2, POS3, POS4, lfstart, rfstart, lbstart, rbstart;
    double d_f, d_s;




    @Override
    public void runOpMode()
    {
        //PhotonCore.enable(); // Enable PhotonCore


        // with live view port
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        camera = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);

        // no live viewport
        //camera = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"));

        aprilTagDetectionPipeline = new AprilTagDetectionPipeline(tagsize, fx, fy, cx, cy);

        camera.setPipeline(aprilTagDetectionPipeline);
        camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
        {
            @Override
            public void onOpened()
            {
                camera.startStreaming(1280,720, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode)
            {

            }
        });

        telemetry.setMsTransmissionInterval(50);



        // Declare our motors
        // Make sure your ID's match your configuration
        frontleft=hardwareMap.dcMotor.get("frontleft");
        frontright=hardwareMap.dcMotor.get("frontright");
        backleft=hardwareMap.dcMotor.get("backleft");
        backright=hardwareMap.dcMotor.get("backright");

        slide1=hardwareMap.dcMotor.get("slide1");
        slide2=hardwareMap.dcMotor.get("slide2");
        armMotor=hardwareMap.dcMotor.get("arm");
        intake1=hardwareMap.crservo.get("intake1");
        intake2=hardwareMap.crservo.get("intake2");

        intake1=hardwareMap.crservo.get("intake1");
        intake2=hardwareMap.crservo.get("intake2");
        // Reverse the right side motors
        // Reverse left motors if you are using NeveRests


        frontleft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontright.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backleft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backright.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontleft.setDirection(DcMotor.Direction.REVERSE);
        backleft.setDirection(DcMotor.Direction.REVERSE);


        armMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slide1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slide2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);




        armMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        slide1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        slide2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);



        armMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        slide1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        slide2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);



        // Road Runner Code

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

                .forward(59) //.forward(52)
                .back(7)
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

                .back(14)
                //score cone
                .waitSeconds(1)

                .UNSTABLE_addTemporalMarkerOffset(.1, () -> {
                    slide1.setTargetPosition(0); //intake pos
                    slide2.setTargetPosition(0); //intake pos

                    slide1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    slide2.setMode(DcMotor.RunMode.RUN_TO_POSITION);

                    slide1.setPower(0.9);
                    slide2.setPower(-0.9);
                })

                .splineTo(new Vector2d(58, -11), Math.toRadians(-2))
                //.waitSeconds(1.5)//intake cone


                .UNSTABLE_addTemporalMarkerOffset(0, () -> {
                    armMotor.setTargetPosition(1250); //intake pos


                    armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);


                    armMotor.setPower(0.5);

                    intake1.setPower(1);
                    intake2.setPower(-1);

                })
                .waitSeconds(1.5)


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
                //.waitSeconds(1)
                .setReversed(true)
                .splineTo(new Vector2d(24, -1), Math.toRadians(135))

                .waitSeconds(.5)

                .UNSTABLE_addTemporalMarkerOffset(0, () -> {
                    slide1.setTargetPosition(0); //intake pos
                    slide2.setTargetPosition(0); //intake pos

                    slide1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    slide2.setMode(DcMotor.RunMode.RUN_TO_POSITION);

                    slide1.setPower(1);
                    slide2.setPower(-1);
                })





                // before park
                .forward(14)
                .UNSTABLE_addTemporalMarkerOffset(0, () -> {
                    armMotor.setTargetPosition(0); //intake pos


                    armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);


                    armMotor.setPower(0.5);


                })

                //.setReversed(false)

                //.waitSeconds(1)


                //.lineToLinearHeading(new Pose2d(6, -14, Math.toRadians(0)))




/*
                .turn(Math.toRadians(45))
                //1 cycle
                .forward(25)

 */

                .build();

        Trajectory leftApril = drive.trajectoryBuilder(trajSeq.end())
                .lineToLinearHeading(new Pose2d(7, -11, Math.toRadians(0)))
                .build();
        Trajectory midApril = drive.trajectoryBuilder(trajSeq.end())
                .lineToLinearHeading(new Pose2d(35.4, -12.5, Math.toRadians(0)))
                .build();
        Trajectory rightApril = drive.trajectoryBuilder(trajSeq.end())
                .lineToLinearHeading(new Pose2d(58, -12.5, Math.toRadians(0)))
                .build();














        /*
         * The INIT-loop:
         * This REPLACES waitForStart!
         */
        while (!isStarted() && !isStopRequested())
        {
            ArrayList<AprilTagDetection> currentDetections = aprilTagDetectionPipeline.getLatestDetections();

            if(currentDetections.size() != 0)
            {
                boolean tagFound = false;

                for(AprilTagDetection tag : currentDetections)
                {
                    if(tag.id == LEFT || tag.id == MIDDLE || tag.id == RIGHT)
                    {
                        tagOfInterest = tag;
                        tagFound = true;
                        break;
                    }
                }

                if(tagFound)
                {
                    telemetry.addLine("Tag of interest is in sight!\n\nLocation data:");
                    tagToTelemetry(tagOfInterest);
                }
                else
                {
                    telemetry.addLine("Don't see tag of interest :(");

                    if(tagOfInterest == null)
                    {
                        telemetry.addLine("(The tag has never been seen)");
                    }
                    else
                    {
                        telemetry.addLine("\nBut we HAVE seen the tag before; last seen at:");
                        tagToTelemetry(tagOfInterest);
                    }
                }

            }
            else
            {
                telemetry.addLine("Don't see tag of interest :(");

                if(tagOfInterest == null)
                {
                    telemetry.addLine("(The tag has never been seen)");
                }
                else
                {
                    telemetry.addLine("\nBut we HAVE seen the tag before; last seen at:");
                    tagToTelemetry(tagOfInterest);
                }

            }

            telemetry.update();
            sleep(20);
        }

        /*
         * The START command just came in: now work off the latest snapshot acquired
         * during the init loop.
         */

        /* Update the telemetry */
        if(tagOfInterest != null)
        {
            telemetry.addLine("Tag snapshot:\n");
            tagToTelemetry(tagOfInterest);
            telemetry.update();
        }
        else
        {
            telemetry.addLine("No tag snapshot available, it was never sighted during the init loop :(");
            telemetry.update();
        }

        /* Actually do something useful */

        drive.followTrajectorySequence(trajSeq);


        if(tagOfInterest == null || tagOfInterest.id == LEFT){
            //trajectory

            drive.followTrajectory(leftApril);

        }else if(tagOfInterest.id == MIDDLE){
            //trajectory

            drive.followTrajectory(midApril);

        }else{
            //trajectory

            drive.followTrajectory(rightApril);

        }

        /*
        //place
        slide1.setTargetPosition(0); //intake pos
        slide2.setTargetPosition(0); //intake pos

        slide1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        slide2.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        slide1.setPower(0.5);
        slide2.setPower(-0.5);

        sleep(200);
        //reset arm
        armTimer.reset();
        while (armTimer.milliseconds() < 3000) {
            armMotor.setPower(-0.2);
        }
        armTimer.reset();
        while (armTimer.milliseconds() < 100) {
            armMotor.setPower(0.1);
        }

         */





        /* You wouldn't have this in your autonomous, this is just to prevent the sample from ending */
        //while (opModeIsActive()) {sleep(20);}
    }

    void tagToTelemetry(AprilTagDetection detection)
    {
        telemetry.addLine(String.format("\nDetected tag ID=%d", detection.id));
        telemetry.addLine(String.format("Translation X: %.2f feet", detection.pose.x*FEET_PER_METER));
        telemetry.addLine(String.format("Translation Y: %.2f feet", detection.pose.y*FEET_PER_METER));
        telemetry.addLine(String.format("Translation Z: %.2f feet", detection.pose.z*FEET_PER_METER));
        telemetry.addLine(String.format("Rotation Yaw: %.2f degrees", Math.toDegrees(detection.pose.yaw)));
        telemetry.addLine(String.format("Rotation Pitch: %.2f degrees", Math.toDegrees(detection.pose.pitch)));
        telemetry.addLine(String.format("Rotation Roll: %.2f degrees", Math.toDegrees(detection.pose.roll)));
    }

    public void moveMec(double forward, double strafe, double turn){
        LFPower=forward+turn+strafe;
        RFPower=forward-turn-strafe;
        LBPower=forward+turn-strafe;
        RBPower=forward-turn+strafe;
        frontleft.setPower(LFPower);
        frontright.setPower(RFPower);
        backleft.setPower(LBPower);
        backright.setPower(RBPower);
    }

    public void PIDMotors(double forward, double strafe, double turn){
        runtime.reset();
        do{
            POS1=forward+turn+strafe;
            POS2=forward-turn-strafe;
            POS3=forward+turn-strafe;
            POS4=forward-turn+strafe;
            LFPower=(POS1-frontleft.getCurrentPosition()+lfstart)/500;
            RFPower=(POS2-frontright.getCurrentPosition()+rfstart)/500;
            LBPower=(POS3-backleft.getCurrentPosition()+lbstart)/500;
            RBPower=(POS4-backright.getCurrentPosition()+rbstart)/500;

            frontleft.setPower((POS1-frontleft.getCurrentPosition()+lfstart)/1000);
            frontright.setPower((POS2-frontright.getCurrentPosition()+rfstart)/1000);
            backleft.setPower((POS3-backleft.getCurrentPosition()+lbstart)/1000);
            backright.setPower((POS4-backright.getCurrentPosition()+rbstart)/1000);
        }while ((Math.abs(LFPower)>0.1 || Math.abs(RFPower)>0.1 ||
                Math.abs(LBPower)>0.1 || Math.abs(RBPower)>0.1) &&
                opModeIsActive());
        moveMec(0, 0, 0);
    }


    public void PIDMotorsClip(double forward, double strafe, double turn){
        runtime.reset();
        do{
            POS1=forward+turn+strafe;
            POS2=forward-turn-strafe;
            POS3=forward+turn-strafe;
            POS4=forward-turn+strafe;
            LFPower= (POS1-frontleft.getCurrentPosition()+lfstart)/500;
            RFPower= (POS2-frontright.getCurrentPosition()+rfstart)/500;
            LBPower= (POS3-backleft.getCurrentPosition()+lbstart)/500;
            RBPower= (POS4-backright.getCurrentPosition()+rbstart)/500;

            LFClip = Math.max(-.5, Math.min(.5, (POS1-frontleft.getCurrentPosition()+lfstart)/1000));
            RFClip = Math.max(-.5, Math.min(.5, (POS2-frontright.getCurrentPosition()+rfstart)/1000));
            LBClip = Math.max(-.5, Math.min(.5, (POS3-backleft.getCurrentPosition()+lbstart)/1000));
            RBClip = Math.max(-.5, Math.min(.5, (POS3-backleft.getCurrentPosition()+lbstart)/1000) );



            frontleft.setPower(LFClip);
            frontright.setPower(RFClip);
            backleft.setPower(LBClip);
            backright.setPower(RBClip);
        }while ((Math.abs(LFPower)>0.1 || Math.abs(RFPower)>0.1 ||
                Math.abs(LBPower)>0.1 || Math.abs(RBPower)>0.1) &&
                opModeIsActive());
        moveMec(0, 0, 0);
    }
    public void moveticks(double forwardspeed, double strafespeed, double dist){
        if (forwardspeed==0){
            d_f=0;
        }else{
            slope=strafespeed/forwardspeed;
            d_f=Math.sqrt((dist*dist)/((slope*slope)+1));
        }
        d_s=Math.sqrt((dist*dist)-(d_f*d_f));
    }
    public void resetencoders(){
        lfstart=frontleft.getCurrentPosition();
        rfstart=frontright.getCurrentPosition();
        lbstart=backleft.getCurrentPosition();
        rbstart=backright.getCurrentPosition();
    }

}