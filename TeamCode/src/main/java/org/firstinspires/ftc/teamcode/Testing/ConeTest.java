///*
// * Copyright (c) 2021 OpenFTC Team
// *
// * Permission is hereby granted, free of charge, to any person obtaining a copy
// * of this software and associated documentation files (the "Software"), to deal
// * in the Software without restriction, including without limitation the rights
// * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// * copies of the Software, and to permit persons to whom the Software is
// * furnished to do so, subject to the following conditions:
// *
// * The above copyright notice and this permission notice shall be included in all
// * copies or substantial portions of the Software.
// * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
// * SOFTWARE.
// */
//
//package org.firstinspires.ftc.teamcode.Testing;
//
//import com.acmerobotics.dashboard.FtcDashboard;
//import com.acmerobotics.dashboard.config.Config;
//import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
//import com.acmerobotics.roadrunner.geometry.Pose2d;
//import com.acmerobotics.roadrunner.trajectory.Trajectory;
//import com.outoftheboxrobotics.photoncore.PhotonCore;
//import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
//import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
//import com.qualcomm.robotcore.util.ElapsedTime;
//
//import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
//import org.firstinspires.ftc.teamcode.RoadRunner.drive.SampleMecanumDrive;
//import org.firstinspires.ftc.teamcode.RoadRunner.trajectorysequence.TrajectorySequence;
//import org.firstinspires.ftc.teamcode.opmodes.auto.vision.AprilTagDetectionPipeline;
//import org.firstinspires.ftc.teamcode.opmodes.subsystems.Intake;
//import org.firstinspires.ftc.teamcode.opmodes.subsystems.Outtake;
//import org.openftc.apriltag.AprilTagDetection;
//import org.openftc.easyopencv.OpenCvCamera;
//import org.openftc.easyopencv.OpenCvCameraFactory;
//import org.openftc.easyopencv.OpenCvCameraRotation;
//
//import java.util.ArrayList;
//
//@Config
//@Autonomous
//public class ConeTest extends LinearOpMode
//{
//
//
//
//    public static double flip1_5 = 0.25;
//    public static double flip1_4 = 0.22;
//    public static double flip1_3 = 0.19;
//    public static double flip1_2 = 0.13;
//    public static double flip1_1 = 0.08;
//
//    // ROBOT STUFF
//
//    public static double expansionDelay = 1.5;
//
//    public static int cycles = 3;
//    public static double cycleDelay = 0.5;
//
//
//    int currentCycle = 0;
//
//
//    // States
//
//    public enum ScoreState {
//        READY,
//        UNFLIP,
//        OUT,
//        GRAB,
//        FLIP,
//        RETRACT,
//        DROP
//
//    }
//
//    ScoreState scoreState = ScoreState.READY;
//
//
//
//
//
//    ElapsedTime scoreTimer = new ElapsedTime();
//
//    Intake intake = new Intake();
//
//
//
//
//
//    @Override
//    public void runOpMode()
//    {
//        PhotonCore.enable(); // Enable PhotonCore
//
//
//        // Telemetry
//        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
//
//
//        // Declare our motors
//        // Make sure your ID's match your configuration
//        intake.init(hardwareMap);
//
//
//
//        telemetry.addLine("Ready to start");
//        waitForStart();
//
//        scoreTimer.reset();
//        while (gamepad1.a != true) {
//            // Stuck Loop
//        }
//
//
//
//        scoreTimer.reset();
//        while (currentCycle < cycles) {
//            switch (scoreState) {
//                case READY:
//
//                    scoreState = ScoreState.UNFLIP
//                    break;
//                case UNFLIP:
//
//
//
//            }
//            telemetry.addData("Current Cycle", currentCycle);
//
//        }
//
//
//
//
//    }
//
//
//}