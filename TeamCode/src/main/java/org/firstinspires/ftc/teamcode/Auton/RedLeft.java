package org.firstinspires.ftc.teamcode.Auton;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Control.Robot;
import org.firstinspires.ftc.teamcode.Control._Autonomous;
import org.firstinspires.ftc.teamcode.Drivers._Drivetrain;
import org.firstinspires.ftc.teamcode.Drivers._TFOD;

@Autonomous(group="Auton", preselectTeleOp = "FinalTeleOp")
public class RedLeft extends _Autonomous {

    private State _state;
    private boolean _justEntered;
    private _TFOD.ValidRecognition _validRecognition;
    private int robotCount = 0;
    private int gearCount = 0;
    private int androidCount = 0;
    private int coneNum;

    @Override
    public void init() {
        Robot.setup(hardwareMap, telemetry, Robot.SetupType.AutonomousPart1);

        _validRecognition = recognition -> true;
    }

    @Override
    public void init_loop() {
        telemetry.addLine(Robot.getTFOD().getLatestRecognitions() != null ? Robot.getTFOD().getRecognitions().toString() : "NULL");
        androidCount = Robot.getTFOD().countValidLabel(_validRecognition, "android");
        robotCount = Robot.getTFOD().countValidLabel(_validRecognition, "robot");
        gearCount = Robot.getTFOD().countValidLabel(_validRecognition, "gear");
    }

    @Override
    public void start() {
        Robot.setup(hardwareMap, telemetry, Robot.SetupType.AutonomousPart2);

        Robot.getTFOD().deactivate();
        Robot.getVuforia().getVuforia().close();

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

        _state = State.right;
        _justEntered = true;
    }

    @Override
    public void loop() {
        Robot.update();

        telemetry.addLine(String.valueOf(Robot.getIMU().getYaw()));
        telemetry.addLine(String.valueOf(coneNum));

        switch (_state) {
            case right:
                if (_justEntered) {
                    Robot.getDrivetrain().runDistance(0.5, 20, _Drivetrain.Movements.forward);
                    _justEntered = false;
                }
                else if (!Robot.getDrivetrain().isBusy()) {
                    _justEntered = true;
                    _state = State.forward;
                }
                break;
            case forward:
                if (_justEntered) {
                    if (coneNum==1){
                        Robot.getDrivetrain().runDistance(0.5, 20, _Drivetrain.Movements.left);
                    }
                    else if (coneNum==3){
                        Robot.getDrivetrain().runDistance(0.5, 20, _Drivetrain.Movements.right);
                    }
                    else{
                        Robot.getDrivetrain().runDistance(0.5, 1, _Drivetrain.Movements.forward);
                    }
                    _justEntered = false;
                }
                else if (!Robot.getDrivetrain().isBusy()) {
                    _justEntered = true;
                    _state = State.tilt;
                }
                break;
//            case tilt:
//                if (_justEntered) {
//                    Robot.turn(0.25, -25);
//                    _justEntered = false;
//                }
//                else if (!Robot.isTurning()) {
//                    _justEntered = true;
//                    _state = State.tilt_back;
//                }
//                break;
//            case tilt_back:
//                if (_justEntered) {
//                    Robot.turn(0.25, 20);
//                    _justEntered = false;
//                }
//                else if (!Robot.isTurning()) {
//                    _justEntered = true;
//                    _state = State.forward_row3;
//                }
//                break;
//            case forward_row3:
//                if (_justEntered) {
//                    Robot.getDrivetrain().runDistance(0.5, 15, _Drivetrain.Movements.forward);
//                    _justEntered = false;
//                }
//                else if (!Robot.getDrivetrain().isBusy()) {
//                    _justEntered = true;
//                    _state = State.turn_left;
//                }
//                break;
//            case turn_left:
//                if (_justEntered) {
//                    Robot.turn(0.25, 80);
//                    _justEntered = false;
//                }
//                else if (!Robot.isTurning()) {
//                    _justEntered = true;
//                    _state = State.forward_col1;
//                }
//                break;
//            case forward_col1:
//                if (_justEntered) {
//                    Robot.getDrivetrain().runDistance(0.5, 30, _Drivetrain.Movements.forward);
//                    _justEntered = false;
//                }
//                else if (!Robot.getDrivetrain().isBusy()) {
//                    _justEntered = true;
//                    _state = State.stop;
//                }
//                break;
//            case stop:
//                break;
        }
    }

    private enum State {
        right,
        forward,
        tilt,
        tilt_back,
        forward_row3,
        turn_left,
        forward_col1,
        stop
    }
}
