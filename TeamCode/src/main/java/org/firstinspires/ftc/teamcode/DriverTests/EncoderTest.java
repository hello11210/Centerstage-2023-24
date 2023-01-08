package org.firstinspires.ftc.teamcode.DriverTests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.Control.Robot;
import org.firstinspires.ftc.teamcode.Control._Autonomous;
import org.firstinspires.ftc.teamcode.Drivers._Motor;

@Autonomous(name="EncoderTest", group="DriverTest")
public class EncoderTest extends _Autonomous {

    private _Motor _fr;
    private _Motor _fl;
    private _Motor _br;
    private _Motor _bl;
    private int _frStart;
    private int _flStart;
    private int _brStart;
    private int _blStart;
    private boolean _justEntered;
    private States _state;

    @Override
    public void init() {
        Robot.setup(hardwareMap, telemetry);
        _fr = new _Motor("motorFR", _Motor.Type.GOBILDA_312_RPM, DcMotorSimple.Direction.FORWARD,
                DcMotor.ZeroPowerBehavior.BRAKE, true);
        _fl = new _Motor("motorFL", _Motor.Type.GOBILDA_312_RPM, DcMotorSimple.Direction.FORWARD,
                DcMotor.ZeroPowerBehavior.BRAKE, true);
        _br = new _Motor("motorBR", _Motor.Type.GOBILDA_312_RPM, DcMotorSimple.Direction.FORWARD,
                DcMotor.ZeroPowerBehavior.BRAKE, true);
        _bl = new _Motor("motorBL", _Motor.Type.GOBILDA_312_RPM, DcMotorSimple.Direction.FORWARD,
                DcMotor.ZeroPowerBehavior.BRAKE, true);
        _state = States.RUN_FR;
        _justEntered = true;
    }

    @Override
    public void start() {
        _frStart = _fr.getCounts();
        _flStart = _fl.getCounts();
        _brStart = _br.getCounts();
        _blStart = _bl.getCounts();
    }

    @Override
    public void loop() {
        _fr.update();
        _fl.update();
        _br.update();
        _bl.update();

        int frCount = _fr.getCounts() - _frStart;
        int flCount = _fl.getCounts() - _flStart;
        int brCount = _br.getCounts() - _brStart;
        int blCount = _bl.getCounts() - _blStart;

        telemetry.addLine("FR counts: " + frCount);
        telemetry.addLine("FR degrees: " + (frCount / _fr.getCountsPerDegree()));
        telemetry.addLine("FL counts: " + flCount);
        telemetry.addLine("FL degrees: " + (flCount / _fl.getCountsPerDegree()));
        telemetry.addLine("BR counts: " + brCount);
        telemetry.addLine("BR degrees: " + (brCount / _br.getCountsPerDegree()));
        telemetry.addLine("BL counts: " + blCount);
        telemetry.addLine("BL degrees: " + (blCount / _bl.getCountsPerDegree()));

        switch (_state) {
            case RUN_FR:
                if (_justEntered) {
                    _justEntered = false;
                    _fr.runTime(0.2, 1000);
                }
                else if (!_fr.isBusy()) {
                    _state = States.RUN_FL;
                    _justEntered = true;
                }
                break;
            case RUN_FL:
                if (_justEntered) {
                    _justEntered = false;
                    _fl.runTime(0.2, 1000);
                }
                else if (!_fl.isBusy()) {
                    _state = States.RUN_BR;
                    _justEntered = true;
                }
                break;
            case RUN_BR:
                if (_justEntered) {
                    _justEntered = false;
                    _br.runTime(0.2, 1000);
                }
                else if (!_br.isBusy()) {
                    _state = States.RUN_BL;
                    _justEntered = true;
                }
                break;
            case RUN_BL:
                if (_justEntered) {
                    _justEntered = false;
                    _bl.runTime(0.2, 1000);
                }
                else if (!_bl.isBusy()) {
                    _state = States.STOP;
                    _justEntered = true;
                }
                break;
            case STOP:
                _fr.stop();
                _fl.stop();
                _br.stop();
                _bl.stop();
                break;
        }
    }

    private enum States {
        RUN_FR,
        RUN_FL,
        RUN_BR,
        RUN_BL,
        STOP
    }
}