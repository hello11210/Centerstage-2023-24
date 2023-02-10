package org.firstinspires.ftc.teamcode.PeripheralTests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.Control.Robot;
import org.firstinspires.ftc.teamcode.Control._Autonomous;
import org.firstinspires.ftc.teamcode.Drivers._Motor;

@Autonomous(name="OdometryTest", group="DriverTest")
public class OdometryTest extends _Autonomous {

    private _Motor _right;
    private _Motor _left;
    private _Motor _back;
    private int _rightStart;
    private int _leftStart;
    private int _backStart;

    @Override
    public void init() {
        Robot.setup(hardwareMap, telemetry);
        _right = new _Motor("odoRight", _Motor.Type.GOBILDA_312_RPM, DcMotorSimple.Direction.FORWARD,
                DcMotor.ZeroPowerBehavior.BRAKE, true);
        _left = new _Motor("odoLeft", _Motor.Type.GOBILDA_312_RPM, DcMotorSimple.Direction.FORWARD,
                DcMotor.ZeroPowerBehavior.BRAKE, true);
        _back = new _Motor("odoBack", _Motor.Type.GOBILDA_312_RPM, DcMotorSimple.Direction.FORWARD,
                DcMotor.ZeroPowerBehavior.BRAKE, true);
    }

    @Override
    public void start() {
        _rightStart = _right.getCounts();
        _leftStart = _left.getCounts();
        _backStart = _back.getCounts();
    }

    @Override
    public void loop() {
        _right.update();
        _left.update();
        _back.update();

        int rightCount = _right.getCounts() - _rightStart;
        int leftCount = _left.getCounts() - _leftStart;
        int backCount = _back.getCounts() - _backStart;

        telemetry.addLine("Right counts: " + rightCount);
        telemetry.addLine("Right degrees: " + (rightCount / _right.getCountsPerDegree()));
        telemetry.addLine("Left counts: " + leftCount);
        telemetry.addLine("Left degrees: " + (leftCount / _left.getCountsPerDegree()));
        telemetry.addLine("Back counts: " + backCount);
        telemetry.addLine("Back degrees: " + (backCount / _back.getCountsPerDegree()));
    }
}