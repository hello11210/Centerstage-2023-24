package org.firstinspires.ftc.teamcode.Auton;

        import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

        import org.firstinspires.ftc.teamcode.Control.Robot;
        import org.firstinspires.ftc.teamcode.Control._Autonomous;
        import org.firstinspires.ftc.teamcode.Drivers._Drivetrain;

        import java.util.concurrent.TimeUnit;

@Autonomous(group="Auton", preselectTeleOp = "FinalTeleOp")
public class RedLeft extends _Autonomous {

    private State _state;
    private boolean _justEntered;
    private int _parkingSpot;
    double t2;
    private double _hue;
    private String _color;

    @Override
    public void init() {
        Robot.setup(hardwareMap, telemetry, Robot.SetupType.AutonomousPart1);
    }

    @Override
    public void init_loop() {
        telemetry.addLine(Robot.getWebcam().getName());
        telemetry.addLine("" + Robot.getPipeline().getConeHue());

        _hue = Robot.getPipeline().getConeHue();
    }

    @Override
    public void start() {
        Robot.getWebcam().stopStreaming();
        Robot.getWebcam().closeCameraDevice();

        if (_hue <= 25) {
            _color = "Red";
        }
        else if (_hue <= 75) {
            _color = "Green";
        }
        else {
            _color = "Blue";
        }

        Robot.setup(hardwareMap, telemetry, Robot.SetupType.AutonomousPart2);

        _state = State.moveleftlil;
        _justEntered = true;
    }

    @Override
    public void loop() {
        telemetry.addLine(_color);

        switch (_state) {
            case moveleftlil:
                if(_justEntered){
                    _justEntered=false;
                    Robot.getDrivetrain().runDistance(0.2, 3, _Drivetrain.Movements.left);
                }
                else if(!Robot.getDrivetrain().isBusy()){
                    _state = State.forward;
                    _justEntered=true;
                }
                break;


            case forward:
                if(_justEntered){
                    _justEntered=false;
                    Robot.getDrivetrain().runDistance(0.2, 24, _Drivetrain.Movements.forward);
                }
                else if(!Robot.getDrivetrain().isBusy()){
                    _state = State.turnaxis;
                    _justEntered=true;
                }
                break;
            case turnaxis:
                if (_justEntered){
                    _justEntered=false;
                    Robot.getDrivetrain().runSpeedAngle(90,0);
                }
                else if(!Robot.getDrivetrain().isBusy()){
                    _state = State.forward1;
                    _justEntered = true;
                }
                break;
            case forward1:
                if (_justEntered) {
                    _justEntered = false;
                    Robot.getDrivetrain().runDistance(0.2, 20, _Drivetrain.Movements.forward);
                }
                else if (!Robot.getDrivetrain().isBusy()) {
                    _state = State.liftlinearslide;
                    _justEntered = true;
                }
                break;
            case liftlinearslide:
                if (_justEntered) {
                    _justEntered = false;
                    Robot.getLinearslide().runDistance(0.5, 7 );
                }
                else if (!Robot.getLinearslide().isBusy()) {
                    _state = State.angleclaw;
                    _justEntered = true;
                }
                break;
            case angleclaw:
                if(_justEntered){
                    _justEntered=false;
                    Robot.getClawPivot().setPosition(0.9);
                }
                else if(!Robot.getClawPivot().isBusy()){
                    _state = State.openclaw;
                    _justEntered=true;
                }
                break;

            case openclaw:
                if(_justEntered){
                    _justEntered=false;
                    Robot.getClaw().setPosition(0.55);
                }
                else if(!Robot.getClaw().isBusy()){
                    _state = State.closeclaw;
                    _justEntered=true;
                }
                break;

            case closeclaw:
                if(_justEntered){
                    _justEntered=false;
                    Robot.getClaw().setPosition(0);
                }
                else if(!Robot.getClaw().isBusy()){
                    _state = State.anglebackclaw;
                    _justEntered=true;
                }
                break;
            case anglebackclaw:
                if(_justEntered){
                    _justEntered=false;
                    Robot.getClawPivot().setPosition(0.1);
                }
                else if(!Robot.getClawPivot().isBusy()){
                    _state = State.linslidedown;
                    _justEntered=true;
                }
                break;


            case linslidedown:
                if(_justEntered){
                    _justEntered=false;
                    Robot.getLinearslide().runDistance(-0.5, 7 );
                }
                else if (!Robot.getLinearslide().isBusy()){
                    _state=State.movebacklil;
                    _justEntered=true;
                }
                break;

            case movebacklil:
                if(_justEntered){
                    _justEntered=false;
                    Robot.getDrivetrain().runDistance(0.3, 3, _Drivetrain.Movements.backward);
                }
                else if (!Robot.getDrivetrain().isBusy()){
                    _state=State.utilizeopencv;
                    _justEntered=true;
                }
                break;


            case utilizeopencv:
                if (_justEntered) {
                    _justEntered = false;
                    if (_color.equals("Red")) {
                        Robot.getDrivetrain().runDistance(.3, 50, _Drivetrain.Movements.left);
                    } else if (_color.equals("Green")) {

                        Robot.getDrivetrain().runDistance(.55, 25, _Drivetrain.Movements.left);
                    } else {
                        Robot.getDrivetrain().runDistance(.55, 8, _Drivetrain.Movements.left);
                    }
                }

                break;
        }
    }

    private enum State {
        //opencv,
        moveleftlil,
        forward,
        turnaxis,
        forward1,
        liftlinearslide,
        angleclaw,
        openclaw,
        closeclaw,
        anglebackclaw,
        linslidedown,
        movebacklil,
        utilizeopencv

    }
}
