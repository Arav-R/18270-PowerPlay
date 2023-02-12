package org.firstinspires.ftc.teamcode.Testing;


import com.ThermalEquilibrium.homeostasis.Controllers.Feedback.PIDEx;
import com.ThermalEquilibrium.homeostasis.Parameters.PIDCoefficients;
import com.ThermalEquilibrium.homeostasis.Controllers.Feedback.BasicPID;
import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;


@Config
@TeleOp
public class PIDTest extends LinearOpMode {

    private DcMotor turret, intakeSlide, outtakeSlide1, outtakeSlide2;



    // PID

    public static double Kp = 0.001;
    public static double Ki = 0;
    public static double Kd = 0;


    public static int targetPosition = 200;


    // creation of the PID object
    PIDCoefficients coefficients = new PIDCoefficients(Kp, Ki, Kd);
    BasicPID controller = new BasicPID(coefficients);

    //PIDEx controller = new BasicPID(coefficients);

    @Override
    public void runOpMode() throws InterruptedException {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());


        //turret = hardwareMap.dcMotor.get("turret");
        intakeSlide = hardwareMap.dcMotor.get("intakeslide");

        intakeSlide.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        // use braking to slow the motor down faster
        intakeSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        //outtakeSlide1 = hardwareMap.dcMotor.get("outtake1");
        //outtakeSlide2 = hardwareMap.dcMotor.get("outtake2");

        //outtakeSlide2.setDirection(DcMotor.Direction.REVERSE);




        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {


            // PID
            // update pid controller
            double command = controller.calculate(targetPosition, intakeSlide.getCurrentPosition());
            // assign motor the PID output
            intakeSlide.setPower(command);


            telemetry.addData("Target Position: ", targetPosition);
            telemetry.addData("Command: ", command);
            telemetry.addData("Current Position: ", intakeSlide.getCurrentPosition());

            telemetry.update();
        }
    }
}
