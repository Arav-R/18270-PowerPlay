package org.firstinspires.ftc.teamcode.opmodes.auto.vision;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.ArrayList;
import java.util.List;

public class ConePipeline extends OpenCvPipeline {

    Mat mat = new Mat();
    String color = "blue";
    double error = 0;

    Telemetry telemetry;
    public ConePipeline(Telemetry t) {
//        if(color.toLowerCase() != "blue" || color.toLowerCase() != "red"){
//            throw new IllegalArgumentException("bro check your code");
//        }else{
//            this.color = color.toLowerCase();
//        }
        telemetry = t;
    }

    @Override
    public Mat processFrame(Mat input){
        Imgproc.cvtColor(input, mat, Imgproc.COLOR_RGBA2RGB);
        Imgproc.cvtColor(mat,mat, Imgproc.COLOR_RGB2HSV);

        if(color == "blue") {
            Scalar lowHSV = new Scalar(90, 50, 70);
            Scalar highHSV = new Scalar(128, 255, 255);
            Core.inRange(mat, lowHSV, highHSV, mat);

        }else {
            Mat a = new Mat();
            Mat b = new Mat();

            Scalar lowHSVa = new Scalar(0, 50, 50);
            Scalar highHSVa = new Scalar(10, 255, 255);
            Core.inRange(mat,lowHSVa,highHSVa,a);

            Scalar lowHSVb = new Scalar(170, 50, 50);
            Scalar highHSVb = new Scalar(180, 255, 255);
            Core.inRange(mat,lowHSVb,highHSVb,b);

            Core.bitwise_or(a,b,mat);
            a.release();
            b.release();

        }
        List<MatOfPoint> cnList = new ArrayList<>();
        Imgproc.findContours(mat, cnList, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);

        Rect closestRect = new Rect(new Point(0,0), new Point(1,1));
        for (MatOfPoint cn : cnList)
        {

            Rect rect = Imgproc.boundingRect(cn);
            if (rect.area() > closestRect.area()) {
                closestRect = rect;
            }

        }
        Imgproc.cvtColor(mat,mat,Imgproc.COLOR_GRAY2RGB);
        Imgproc.rectangle(mat, closestRect, new Scalar(255,255,255));
        Imgproc.line(mat, new Point(mat.width()/2, 0), new Point(mat.width()/2,mat.height()), new Scalar(0,255,0));

        error = mat.width()/2.0 - (closestRect.x + closestRect.width/2.0);

        telemetry.addData("Cone Area: ", closestRect.area());

        return mat;
    }

    public String getColor() {
        return color;
    }

    public double getError() {
        return error;
    }
}
