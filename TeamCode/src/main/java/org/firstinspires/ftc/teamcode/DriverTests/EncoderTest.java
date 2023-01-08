package org.firstinspires.ftc.teamcode.DriverTests;

import static org.firstinspires.ftc.teamcode.Control.Robot.MM_PER_INCH;

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
    }

    @Override
    public void loop() {
        _fr.update();
        _fl.update();
        _br.update();
        _bl.update();

        telemetry.addLine("FR counts: " + _fr.getCounts());
        telemetry.addLine("FR degrees: " + (_fr.getCounts() / _fr.getCountsPerDegree()));
        telemetry.addLine("FL counts: " + _fl.getCounts());
        telemetry.addLine("FL degrees: " + (_fl.getCounts() / _fl.getCountsPerDegree()));
        telemetry.addLine("BR counts: " + _br.getCounts());
        telemetry.addLine("BR degrees: " + (_br.getCounts() / _br.getCountsPerDegree()));
        telemetry.addLine("BL counts: " + _bl.getCounts());
        telemetry.addLine("BL degrees: " + (_bl.getCounts() / _bl.getCountsPerDegree()));
    }
}