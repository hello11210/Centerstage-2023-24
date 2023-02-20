package org.firstinspires.ftc.teamcode.Auton;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Control.Robot;
import org.firstinspires.ftc.teamcode.Control._Autonomous;
import org.firstinspires.ftc.teamcode.Drivers._Drivetrain;
import org.firstinspires.ftc.teamcode.Drivers._TFOD;

@Autonomous(group="Auton", preselectTeleOp = "FinalTeleOp")
public class RedLeftField2 extends _Autonomous {

    private State _state;
    private boolean _justEntered;
    private _TFOD.ValidRecognition _validRecognition;
    private int robotCount = 0;
    private int gearCount = 0;
    private int androidCount = 0;
    private int coneNum;
    private final double dSpeed = 0.25;
    private double enterTime = 0;

    @Override
    public void init() {
        Robot.setup(hardwareMap, telemetry, Robot.Mode.Auton, Robot.SetupType.AutonomousPart1);

        _validRecognition = recognition -> true;
    }

    @Override
    public void init_loop() {
        telemetry.addLine(Robot.getTFOD().getLatestRecognitions() != null ? Robot.getTFOD().getRecognitions().toString() : "NULL");
        androidCount = Robot.getTFOD().countValidLabel(_validRecognition, "android");
        robotCount = Robot.getTFOD().countValidLabel(_validRecognition, "robot");
        gearCount = Robot.getTFOD().countValidLabel(_validRecognition, "gear");

        Robot.getClaw().setPosition(Robot.UPSIDEDOWN_CLOSE);
        Robot.getArm().setDegree(Robot.AUTONSTART_ARM);
        Robot.getClawPivot().setDegree(Robot.AUTONSTART_PIVOT);
    }

    @Override
    public void start() {
        Robot.setup(hardwareMap, telemetry, Robot.Mode.Auton, Robot.SetupType.AutonomousPart2);

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

        _state = State.off_the_wall;
        _justEntered = true;
    }

    @Override
    public void loop() {
        Robot.update();

        telemetry.addLine(String.valueOf(Robot.getIMU().getYaw()));
        telemetry.addLine(String.valueOf(coneNum));

        switch (_state) {
            case off_the_wall:
                if (_justEntered) {
                    Robot.getDrivetrain().runDistance(dSpeed, 2, _Drivetrain.Movements.forward);
                    _justEntered = false;
                }
                else if (!Robot.getDrivetrain().isBusy()) {
                    _justEntered = true;
                    _state = State.right;
                }
                break;
            case right:
                if (_justEntered) {
                    Robot.getDrivetrain().runDistance(dSpeed, 13, _Drivetrain.Movements.right);
                    _justEntered = false;
                }
                else if (!Robot.getDrivetrain().isBusy()) {
                    _justEntered = true;
                    _state = State.forward;
                }
                break;
            case forward:
                if (_justEntered) {
                    Robot.getDrivetrain().runDistance(dSpeed, 14, _Drivetrain.Movements.forward);
                    Robot.getLinearslide().runDistance(1, 15.5 - (Robot.getLinearslide().getCounts() / Robot.getLinearslide().getCountsPerInch()));
                    Robot.getArm().setSlowDegree(109, 1500);
                    Robot.getClawPivot().setSlowDegree(0, 1500);
                    _justEntered = false;
                }
                else if (!Robot.getDrivetrain().isBusy()) {
                    _justEntered = true;
                    _state = State.delay;
                }
                break;
            case delay:
                if (_justEntered) {
                    enterTime = Robot.runtime.milliseconds();
                    _justEntered = false;
                }
                else if (enterTime + 1500 <= Robot.runtime.milliseconds()) {
                    _justEntered = true;
                    _state = State.tilt;
                }
                break;
            case tilt:
                if (_justEntered) {
                    Robot.turn(0.3, -38 - Robot.getIMU().getYaw());
                    _justEntered = false;
                }
                else if (!Robot.isTurning()) {
                    _justEntered = true;
                    _state = State.arm_delay;
                }
                break;
            case arm_delay:
                if (_justEntered) {
                    enterTime = Robot.runtime.milliseconds();
                    _justEntered = false;
                }
                else if (enterTime + 6000 <= Robot.runtime.milliseconds()) {
                    _justEntered = true;
                    _state = State.deposit_delay;
                    Robot.getClaw().setPosition(Robot.UPSIDEDOWN_OPEN);
                }
                break;
            case deposit_delay:
                if (_justEntered) {
                    enterTime = Robot.runtime.milliseconds();
                    _justEntered = false;
                }
                else if (enterTime + 2000 <= Robot.runtime.milliseconds()) {
                    _justEntered = true;
                    _state = State.tilt_back;
                }
                break;
            case tilt_back:
                if (_justEntered) {
                    Robot.turn(0.3, -Robot.getIMU().getYaw());
                    Robot.getLinearslide().runDistance(1, Robot.TELEOPCOLLECT_LS - (Robot.getLinearslide().getCounts() / Robot.getLinearslide().getCountsPerInch()));
                    Robot.getArm().setSlowDegree(25, 500);
                    Robot.getClawPivot().setDegree(135);
                    _justEntered = false;
                }
                else if (!Robot.isTurning()) {
                    _justEntered = true;
                    _state = State.forward_row3;
                    Robot.getClaw().setPosition(Robot.UPSIDEDOWN_CLOSE);
                }
                break;
            case forward_row3:
                if (_justEntered) {
                    Robot.getDrivetrain().runDistance(dSpeed, 19, _Drivetrain.Movements.forward);
                    _justEntered = false;
                }
                else if (!Robot.getDrivetrain().isBusy()) {
                    _justEntered = true;
                    _state = State.turn_left;
                }
                break;
            case turn_left:
                if (_justEntered) {
                    Robot.turn(0.75, 180 - Robot.getIMU().getYaw());
                    _justEntered = false;
                }
                else if (!Robot.isTurning()) {
                    _justEntered = true;
                    _state = State.right_cone;
                }
                break;
            case right_cone:
                if (_justEntered) {
                    if (coneNum == 1) {
                        Robot.getDrivetrain().runDistance(dSpeed, 36, _Drivetrain.Movements.right);
                    }
                    else if (coneNum == 2) {
                        Robot.getDrivetrain().runDistance(dSpeed, 17, _Drivetrain.Movements.right);
                    }
                    _justEntered = false;
                }
                else if (!Robot.getDrivetrain().isBusy()) {
                    _justEntered = true;
                    _state = State.stop;
                }
                break;
            case stop:
                break;
        }
    }

    private enum State {
        off_the_wall,
        right,
        forward,
        delay,
        tilt,
        arm_delay,
        deposit_delay,
        tilt_back,
        forward_row3,
        turn_left,
        right_cone,
        stop
    }
}
