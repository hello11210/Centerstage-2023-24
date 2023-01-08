package org.firstinspires.ftc.teamcode.PeripheralTests;

import static org.firstinspires.ftc.teamcode.Control.Robot.MM_PER_INCH;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.Control.Robot;
import org.firstinspires.ftc.teamcode.Control._Autonomous;
import org.firstinspires.ftc.teamcode.Drivers._Motor;

@Autonomous(name="IndividualMotorTest", group="DriverTest")
public class IndividualMotorTest extends _Autonomous {

    private _Motor _fr;
    private _Motor _fl;
    private _Motor _br;
    private _Motor _bl;
    private States _state;
    private boolean _justEntered;

    @Override
    public void init() {
        Robot.setup(hardwareMap, telemetry);
        double wheelDiameter = 96/MM_PER_INCH;
        _fr = new _Motor("motorFR", _Motor.Type.GOBILDA_312_RPM, DcMotorSimple.Direction.FORWARD,
                DcMotor.ZeroPowerBehavior.BRAKE, wheelDiameter, true);
        _fl = new _Motor("motorFL", _Motor.Type.GOBILDA_312_RPM, DcMotorSimple.Direction.FORWARD,
                DcMotor.ZeroPowerBehavior.BRAKE, wheelDiameter, true);
        _br = new _Motor("motorBR", _Motor.Type.GOBILDA_312_RPM, DcMotorSimple.Direction.FORWARD,
                DcMotor.ZeroPowerBehavior.BRAKE, wheelDiameter, true);
        _bl = new _Motor("motorBL", _Motor.Type.GOBILDA_312_RPM, DcMotorSimple.Direction.FORWARD,
                DcMotor.ZeroPowerBehavior.BRAKE, wheelDiameter, true);
        _state = States.RUN_FR;
        _justEntered = true;
    }

    @Override
    public void loop() {
        _fr.update();
        _fl.update();
        _br.update();
        _bl.update();

        telemetry.addLine("FR counts: " + _fr.getCounts());
        telemetry.addLine("FL counts: " + _fl.getCounts());
        telemetry.addLine("BR counts: " + _br.getCounts());
        telemetry.addLine("BL counts: " + _bl.getCounts());

        switch (_state) {
            case RUN_FR:
                if (_justEntered) {
                    _justEntered = false;
                    _fr.runDistance(0.5, 10);
                }
                else if (!_fr.isBusy()) {
                    _state = States.RUN_FL;
                    _justEntered = true;
                }
                break;
            case RUN_FL:
                if (_justEntered) {
                    _justEntered = false;
                    _fl.runDistance(0.5, 10);
                }
                else if (!_fl.isBusy()) {
                    _state = States.RUN_BR;
                    _justEntered = true;
                }
                break;
            case RUN_BR:
                if (_justEntered) {
                    _justEntered = false;
                    _br.runDistance(0.5, 10);
                }
                else if (!_br.isBusy()) {
                    _state = States.RUN_BL;
                    _justEntered = true;
                }
                break;
            case RUN_BL:
                if (_justEntered) {
                    _justEntered = false;
                    _bl.runDistance(0.5, 10);
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