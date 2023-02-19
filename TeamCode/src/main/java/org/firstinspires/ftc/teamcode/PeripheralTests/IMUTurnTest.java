package org.firstinspires.ftc.teamcode.PeripheralTests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Control.Robot;
import org.firstinspires.ftc.teamcode.Control._Autonomous;

@Autonomous(group="PeripheralTests")
public class IMUTurnTest extends _Autonomous {

    private State _state;
    private boolean _justEntered;

    @Override
    public void init() {
        Robot.setup(hardwareMap, telemetry, Robot.Mode.Auton, Robot.SetupType.AutonomousPart1);
    }

    @Override
    public void init_loop() {
        Robot.getClaw().setPosition(Robot.UPSIDEDOWN_CLOSE);
        Robot.getArm().setDegree(Robot.AUTONSTART_ARM);
        Robot.getClawPivot().setDegree(Robot.AUTONSTART_PIVOT);
    }

    @Override
    public void start() {
        Robot.setup(hardwareMap, telemetry, Robot.Mode.Auton, Robot.SetupType.AutonomousPart2);
        _state = State.turn;
        _justEntered = true;
    }

    @Override
    public void loop() {
        Robot.update();

        telemetry.addLine(String.valueOf(Robot.getIMU().getYaw()));
        telemetry.addLine(_state.name());

        switch (_state) {
            case turn:
                if (_justEntered) {
                    Robot.turn(0.25, 30);
                    _justEntered = false;
                }
                else if (!Robot.isTurning()) {
                    _justEntered = true;
                    _state = State.stop;
                }
                break;
            case stop:
                break;
        }
    }

    private enum State {
        turn,
        stop
    }
}
