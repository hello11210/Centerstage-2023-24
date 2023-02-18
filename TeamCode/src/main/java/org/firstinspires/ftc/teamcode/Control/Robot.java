package org.firstinspires.ftc.teamcode.Control;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;


import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Drivers._Drivetrain;
import org.firstinspires.ftc.teamcode.Drivers._IMU;
import org.firstinspires.ftc.teamcode.Drivers._Color;


import org.firstinspires.ftc.teamcode.Drivers._Motor;
import org.firstinspires.ftc.teamcode.Drivers._OpenCV;
import org.firstinspires.ftc.teamcode.Drivers._Servo;
import org.firstinspires.ftc.teamcode.Drivers._ServoGroup;
import org.firstinspires.ftc.teamcode.Drivers._TFOD;
import org.firstinspires.ftc.teamcode.Drivers._Vuforia;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

public final class Robot {

    public static ElapsedTime runtime;
    public static HardwareMap hardwareMap;
    public static Telemetry telemetry;

    private static _Drivetrain _drivetrain;
    private static _IMU _imu;
    private static _Color _color;
    private static _Motor _linearslide;
    private static _Servo _claw;
    private static _Servo _pivot;
    private static _ServoGroup _Arm;
    private static _OpenCV _webcam;
    private static _ProcessPipeline _pipeline;
    private static _Vuforia _vuforia;
    private static _TFOD _tfod;

    public static final double MM_PER_INCH = 25.4;

    private static final double _TURN_OFFSET_POSITIVE = 0;
    private static final double _TURN_OFFSET_NEGATIVE = 0;

    private static boolean _isTurning = false;
    private static double _startAngle;
    private static double _turnDegrees;
    private static boolean _initialized = false;

    private Robot() {};

    public static void setup(HardwareMap centralHardwareMap, Telemetry centralTelemetry, SetupType... setupTypes) {
        if (!_initialized) {
            _initialized = true;
            runtime = new ElapsedTime();
            hardwareMap = centralHardwareMap;
            telemetry = centralTelemetry;
        }

        StringBuilder setupSequence = new StringBuilder();
        for (SetupType type : setupTypes) {
            switch(type) {
                case AutonomousPart1:
                    setupAutonomousPart1();
                    break;
                case AutonomousPart2:
                    setupAutonomousPart2();
                    break;
                case TeleOp1:
                    setupTeleOp1();
                    break;
                case TeleOp2:
                    setupTeleOp2();
                    break;
                case Drivetrain:
                    setupDrivetrain();
                    break;
                case IMU:
                    setupIMU();
                    break;
                case Linearslide:
                    setupLinearslide();
                    break;
                case Claw:
                    setupClaw();
                    break;
                case ClawPivot:
                    setupClawPivot();
                    break;
                case Color:
                    setupColor();
                    break;
                case OpenCV:
                    setupOpenCV();
                    break;
                case Arm:
                    setupArm();
                    break;
                case Vuforia:
                    setupVuforia();
                    break;
                case TFOD:
                    setupTFOD();
                    break;
            }

            setupSequence.append(type.name()).append(" ");
        }

        telemetry.addLine(setupSequence.toString());
    }

    private static void setupAutonomousPart1() {
        setupClaw();
        setupClawPivot();
        setupArm();
        setupVuforia();
        setupTFOD();
    }

    private static void setupAutonomousPart2() {
        setupIMU();
        setupDrivetrain();
        setupLinearslide();
        //OpenCV is just for testing, not actual runs
    }

    private static void setupTeleOp1() {
        setupClaw();
        setupClawPivot();
        setupArm();
    }

    private static void setupTeleOp2() {
        setupIMU();
        setupDrivetrain();
        setupLinearslide();
        //OpenCV is just for testing, not actual runs
    }

    private static void setupDrivetrain() {
        double wheelDiameter = 96/MM_PER_INCH;
        _Motor fr = new _Motor("motorFR", _Motor.Type.GOBILDA_312_RPM, DcMotorSimple.Direction.FORWARD,
                DcMotor.ZeroPowerBehavior.BRAKE, wheelDiameter, true);
        _Motor fl = new _Motor("motorFL", _Motor.Type.GOBILDA_312_RPM, DcMotorSimple.Direction.FORWARD,
                DcMotor.ZeroPowerBehavior.BRAKE, wheelDiameter, true);
        _Motor br = new _Motor("motorBR", _Motor.Type.GOBILDA_312_RPM, DcMotorSimple.Direction.FORWARD,
                DcMotor.ZeroPowerBehavior.BRAKE, wheelDiameter, true);
        _Motor bl = new _Motor("motorBL", _Motor.Type.GOBILDA_312_RPM, DcMotorSimple.Direction.FORWARD,
                DcMotor.ZeroPowerBehavior.BRAKE, wheelDiameter, true);
        _drivetrain = new _Drivetrain(fr, fl, br, bl, 1.0);
    }

    private static void setupIMU() {
        _imu = new _IMU("imu", true, true);
    }

    private static void setupColor() {
        _color = new _Color("color");
    }

    private static void setupOpenCV() {
        _pipeline = new _ProcessPipeline();
        _webcam = new _OpenCV("Webcam 1", 320, 240, _pipeline);
    }

    private static void setupLinearslide() {
        double linearslideDiameter = 1.25/2; //inches
        _linearslide = new _Motor("linearslide", _Motor.Type.GOBILDA_435_RPM, DcMotorSimple.Direction.FORWARD,
                DcMotor.ZeroPowerBehavior.BRAKE, linearslideDiameter, true); //Add encoder if theres isn't already
    }

    private static void setupClaw() {
        double startPosition = 1;
        _claw = new _Servo("claw", Servo.Direction.REVERSE, 0, 1, 0.35);

    }
    private static void setupClawPivot() {
//        _pivot = new _Servo("clawPivot", Servo.Direction.FORWARD, 0, 0.7, 1);
    }

    private static void setupArm(){
        double startPosition = 0.027;
        _Servo ArmLeft = new _Servo("armLeft", Servo.Direction.FORWARD, startPosition, 0.95,0);
        _Servo ArmRight = new _Servo("armRight", Servo.Direction.REVERSE, 0, 0.95 - startPosition, 0);
        _Arm  = new _ServoGroup(ArmLeft, ArmRight);
    }

    private static void setupVuforia() {
        _vuforia = new _Vuforia("Webcam 1");
    }

    private static void setupTFOD() {
        _tfod = new _TFOD(_vuforia.getVuforia(), 0.45f, true, 320, 1.0, 16.0/9.0,
                "AGS1273.tflite", new String[] {"robot", "gear", "android"});
    }

    public static void update() {
        _imu.update();
        _drivetrain.update();
        _linearslide.update();
        _claw.update();
//        _pivot.update();
        _Arm.update();

        if (_isTurning) {
            if (Math.abs(_turnDegrees) > Math.max(_TURN_OFFSET_POSITIVE, _TURN_OFFSET_NEGATIVE)) {
                if (_turnDegrees > 0 ? _imu.getYaw() - _startAngle >= _turnDegrees - _TURN_OFFSET_POSITIVE : _imu.getYaw() - _startAngle <= _turnDegrees + _TURN_OFFSET_NEGATIVE) {
                    _isTurning = false;
                }
            }
            else {
                if (_turnDegrees > 0 ? _imu.getYaw() - _startAngle >= _turnDegrees : _imu.getYaw() - _startAngle <= _turnDegrees) {
                    _isTurning = false;
                }
            }

            if (!_isTurning) {
                _drivetrain.stop();
            }
        }
    }

    public static void turn(double speed, double degrees, TurnAxis turnAxis) {
        if (!_isTurning && degrees != 0) {
            _isTurning = true;
            _startAngle = _imu.getYaw();
            _turnDegrees = degrees;

            _Drivetrain.Movements movement = _Drivetrain.Movements.forward; // arbitrary initialization
            switch (turnAxis) {
                case Center:
                    movement = _turnDegrees > 0 ? _Drivetrain.Movements.ccw : _Drivetrain.Movements.cw;
                    break;
                case Back:
                    movement = _turnDegrees > 0 ? _Drivetrain.Movements.ccwback : _Drivetrain.Movements.cwback;
                    break;
                case Front:
                    movement = _turnDegrees > 0 ? _Drivetrain.Movements.ccwfront : _Drivetrain.Movements.cwfront;
                    break;
            }

            _drivetrain.runSpeed(speed, movement);
        }
    }

    public static void turn(double speed, double degrees) {
        turn(speed, degrees, TurnAxis.Center);
    }

    public static _Drivetrain getDrivetrain() {
        return _drivetrain;
    }

    public static _IMU getIMU() {
        return _imu;
    }

    public static _Color getColor() {
        return _color;
    }

    public static _Motor getLinearslide() {
        return _linearslide;
    }

    public static _Servo getClaw() {
        return _claw;
    }

    public static _Servo getClawPivot() {
        return _pivot;
    }
    public static _ServoGroup getArm(){
        return _Arm;
    }

    public static _OpenCV getWebcam() {
        return _webcam;
    }

    public static _ProcessPipeline getPipeline() {
        return _pipeline;
    }

    public static _Vuforia getVuforia() {
        return _vuforia;
    }

    public static _TFOD getTFOD() {
        return _tfod;
    }

    public static boolean isTurning() {
        return _isTurning;
    }

    public enum SetupType {
        AutonomousPart1,
        AutonomousPart2,
        TeleOp1,
        TeleOp2,
        Drivetrain,
        IMU,
        Linearslide,
        Claw,
        ClawPivot,
        Color,
        OpenCV,
        Arm,
        Vuforia,
        TFOD
    }

    public enum TurnAxis {
        Front,
        Center,
        Back
    }

    public static class _ProcessPipeline extends OpenCvPipeline
    {

        private double _hue;
        private Mat _processed;

        public _ProcessPipeline() {
            super();
            _processed = new Mat();
        }

        @Override
        public Mat processFrame(Mat input) {
            int x = (int) (input.cols() * 0.375);
            int y = (int) (input.rows() * 0.43);
            int w = (int) (input.cols() * 0.05);
            int h = (int) (input.rows() * 0.05);

            Imgproc.cvtColor(input, _processed, Imgproc.COLOR_RGBA2RGB);
            Imgproc.cvtColor(_processed, _processed, Imgproc.COLOR_RGB2HSV);

            Imgproc.rectangle(
                    input,
                    new Point(x, y),
                    new Point(x + w, y + h),
                    new Scalar(0, 255, 0), 4
            );

            _processed = new Mat(_processed, new Rect(x, y, w, h));

            Scalar meanHSV = Core.mean(_processed);
            _hue = meanHSV.val[0];

            return input;
        }

        public double getConeHue() {
            return _hue;
        }
    }
}