package org.firstinspires.ftc.teamcode.PeripheralTests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Control.Robot;
import org.firstinspires.ftc.teamcode.Control._Autonomous;

@Autonomous(name="ConeTest_OpenCV", group="PeripheralTest")
public class ConeTest_OpenCV extends _Autonomous {

    private double _hue;
    private String _color;

    @Override
    public void init() {
        Robot.setup(hardwareMap, telemetry, Robot.SetupType.OpenCV);
    }

    @Override
    public void init_loop() {
        telemetry.addLine(Robot.getWebcam().getName());
        telemetry.addLine("" + Robot.getPipeline().getConeHue());

        _hue = Robot.getPipeline().getConeHue();
    }

    @Override
    public void start() {
        Robot.getWebcam().stopStreaming();
        Robot.getWebcam().closeCameraDevice();

        if (_hue <= 25) {
            _color = "Red";
        }
        else if (_hue <= 75) {
            _color = "Green";
        }
        else {
            _color = "Blue";
        }
    }

    @Override
    public void loop() {
        telemetry.addLine(_color);
    }
}