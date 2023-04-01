package org.firstinspires.ftc.teamcode.PeripheralTests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.Control.Robot;
import org.firstinspires.ftc.teamcode.Control._Autonomous;
import org.firstinspires.ftc.teamcode.Drivers._Motor;

@Autonomous(group="PeripheralTests")
public class MotorProgressiveTest extends _Autonomous {

    private _Motor _motor;
    private State _state;
    private boolean _justEntered;

    @Override
    public void init() {
        Robot.setup(hardwareMap, telemetry);
        _motor = new _Motor("motorFR", _Motor.Type.GOBILDA_312_RPM, DcMotorSimple.Direction.FORWARD,
                DcMotor.ZeroPowerBehavior.BRAKE, 96/Robot.MM_PER_INCH, true);
    }

    @Override
    public void start() {
        _state = State.drive;
        _justEntered = true;
    }

    @Override
    public void loop() {
        _motor.update();

        telemetry.addLine(String.valueOf(_motor.getCounts() / _motor.getCountsPerInch()));
        telemetry.addLine(_state.name());

        switch (_state) {
            case drive:
                if (_justEntered) {
                    _motor.runDistProgressive(0.5, 20);
                    _justEntered = false;
                }
                else if (!_motor.isBusy()) {
                    _justEntered = true;
                    _state = State.stop;
                }
                break;
            case stop:
                break;
        }
    }

    private enum State {
        drive,
        stop
    }
}
