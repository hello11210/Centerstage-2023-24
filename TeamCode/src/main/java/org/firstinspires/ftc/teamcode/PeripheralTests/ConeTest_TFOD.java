package org.firstinspires.ftc.teamcode.PeripheralTests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.Control.Robot;
import org.firstinspires.ftc.teamcode.Control._Autonomous;
import org.firstinspires.ftc.teamcode.Drivers._TFOD;
import org.firstinspires.ftc.teamcode.Drivers._Vuforia;

@Autonomous(name="ConeTest_TFOD", group="DriverTest")
public class ConeTest_TFOD extends _Autonomous {

    private _Vuforia _vuforia;
    private _TFOD _tfod;
    private _TFOD.ValidRecognition _validRecognition;
    private int robotCount = 0;
    private int gearCount = 0;
    private int androidCount = 0;
    private int coneNum;

    @Override
    public void init() {
        Robot.setup(hardwareMap, telemetry);
        _vuforia = new _Vuforia("Webcam 1");
        _tfod = new _TFOD(_vuforia.getVuforia(), 0.45f, true, 320, 1.0, 16.0/9.0,
                "AGS1273.tflite", new String[] {"robot", "gear", "android"});
        _validRecognition = recognition -> true;
    }

    @Override
    public void init_loop() {
        telemetry.addLine(_tfod.getLatestRecognitions() != null ? _tfod.getRecognitions().toString() : "NULL");
        androidCount = _tfod.countValidLabel(_validRecognition, "android");
        robotCount = _tfod.countValidLabel(_validRecognition, "robot");
        gearCount = _tfod.countValidLabel(_validRecognition, "gear");
    }

    @Override
    public void start() {
        _tfod.deactivate();
        _vuforia.getVuforia().close();

        int maxCount = Math.max(androidCount, Math.max(robotCount, gearCount));
        if (maxCount == robotCount) {
            coneNum = 1;
        }
        else if (maxCount == androidCount) {
            coneNum = 2;
        }
        else {
            coneNum = 3;
        }
    }

    @Override
    public void loop() {
        telemetry.addLine(String.valueOf(coneNum));
    }
}