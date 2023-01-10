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

package org.firstinspires.ftc.teamcode.Testing;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.outoftheboxrobotics.photoncore.PhotonCore;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.opmodes.subsystems.Intake;
import org.firstinspires.ftc.teamcode.opmodes.subsystems.Outtake;

@Config
@Autonomous
public class CycleTest extends LinearOpMode
{




    // ROBOT STUFF

    public static double expansionDelay = 1;

    public static int cycles = 6;
    public static double cycleDelay = 0.25;


    int currentCycle = 0;


    // States

    public enum ScoreState {
        READY,
        DEPOSIT,
        PREPARE,
        GRAB,
        RETRACT_INTAKE,
        FLIP,
        EXTEND_INTAKE,
        EXTEND_OUTTAKE

    }

    ScoreState scoreState = ScoreState.READY;





    ElapsedTime scoreTimer = new ElapsedTime();
    //ElapsedTime readyTimer = new ElapsedTime();

    //Drivetrain drive = new Drivetrain();
    Intake intake = new Intake();
    Outtake outtake = new Outtake();





    @Override
    public void runOpMode()
    {
        PhotonCore.enable(); // Enable PhotonCore


        // Telemetry
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());


        // Declare our motors
        // Make sure your ID's match your configuration
        intake.init(hardwareMap);
        outtake.init(hardwareMap);


        telemetry.addLine("Ready to start");

        outtake.transferDeposit();
        waitForStart();

        scoreTimer.reset();
        while (scoreTimer.seconds() <= expansionDelay && opModeIsActive()) {
            // Expand
            intake.readyPosition();
            intake.openClaw();
            intake.dropArmAuto(2); //5 cone

            outtake.extendSlideLeft();
            outtake.setTurretAutoLeft();
            outtake.midDeposit();
        }



        scoreTimer.reset();
        while (currentCycle < cycles && opModeIsActive()) {
            switch (scoreState) {
                case READY:
                    if (scoreTimer.seconds() >= cycleDelay) {

                        outtake.scoreDeposit();

                        scoreTimer.reset();
                        scoreState = ScoreState.DEPOSIT;
                        currentCycle++;
                    }
                    break;
                case DEPOSIT:
                    if (scoreTimer.seconds() >= .7) {
                        outtake.transferDeposit();
                        outtake.retractSlide();
                        outtake.setTurretMiddle();

                        intake.openClaw();
                        intake.autoStackPosition();


                        scoreState = ScoreState.PREPARE;
                    }
                    break;
                case PREPARE:
                    if (intake.intakeOutDiff() < 20) {
                        intake.closeClaw();


                        scoreTimer.reset();
                        scoreState = ScoreState.GRAB;
                    }
                    break;
                case GRAB:
                    if (scoreTimer.seconds() >= .5) {
                        intake.transferPositionAuto();
                        intake.flipArm();

                        scoreTimer.reset();
                        scoreState = ScoreState.RETRACT_INTAKE;
                    }
                    break;
                case RETRACT_INTAKE:
                    if (scoreTimer.seconds() >= .7) {
                        intake.openClaw();

                        scoreTimer.reset();
                        scoreState = ScoreState.FLIP;
                    }
                    break;
                case FLIP:
                    if (scoreTimer.seconds() >= .75) {
                        intake.readyPosition();
                        intake.dropArmAuto(currentCycle + 2); // Starts at 2

                        scoreTimer.reset();
                        scoreState = ScoreState.EXTEND_INTAKE;
                    }
                    break;
                case EXTEND_INTAKE:
                    if (scoreTimer.seconds() >= .25) {
                        outtake.midDeposit();
                        outtake.setTurretAutoLeft();
                        outtake.extendSlideLeft();

                        scoreState = ScoreState.EXTEND_OUTTAKE;
                    }
                    break;
                case EXTEND_OUTTAKE:
                    if (outtake.slideOutDiffLeft() < 10) {

                        scoreTimer.reset();
                        scoreState = ScoreState.READY;
                    }
                    break;
                default:
                    // should never be reached, as liftState should never be null
                    scoreState = ScoreState.READY;
                    break;

            }
            telemetry.addData("Current Cycle", currentCycle);

        }




    }


}