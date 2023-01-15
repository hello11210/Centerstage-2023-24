package org.firstinspires.ftc.teamcode.Auton;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Control.Robot;
import org.firstinspires.ftc.teamcode.Control._Autonomous;
import org.firstinspires.ftc.teamcode.Drivers._Drivetrain;

@Autonomous(group="Auton", preselectTeleOp = "RobotDrivetrainTwoStep")
public class RobotDrivetrainTwoStep extends _Autonomous {

    private State _state;
    private boolean _justEntered;

    @Override
    public void init() {
        Robot.setup(hardwareMap, telemetry, Robot.SetupType.AutonomousPart1);
    }

    @Override
    public void start() {
        Robot.setup(hardwareMap, telemetry, Robot.SetupType.AutonomousPart2);

        _state = State.forward;
        _justEntered = true;
    }

    @Override
    public void loop() {
        Robot.update();

        switch (_state) {
            case forward:
                if (_justEntered) {
                    _justEntered = false;
                    Robot.getDrivetrain().runDistance(0.3, 10, _Drivetrain.Movements.forward);
                }
                else if(!Robot.getDrivetrain().isBusy()){
                    _state = State.left;
                    _justEntered=true;
                }
                break;
            case left:
                if(_justEntered){
                    _justEntered=false;
                    Robot.getDrivetrain().runDistance(0.2,10, _Drivetrain.Movements.left);
                }
                else if(!Robot.getDrivetrain().isBusy()){
                    _state = State.stop;
                    _justEntered=true;
                }
                break;
            case stop:
                break;
        }
    }

    private enum State {
        forward,
        left,
        stop
    }
}

