package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Control.Robot;
import org.firstinspires.ftc.teamcode.Control._TeleOp;
import org.firstinspires.ftc.teamcode.Drivers._Drivetrain;

@TeleOp(group="FinalTeleOp")
public class FinalTeleOp extends _TeleOp {

    private final double _LOW_POLE = 0.25;
    private final double _MID_POLE = 0.6;
    private final double _HIGH_POLE = 0.5;

    private boolean _triggerPressed = false;
    private int _height = 0;

    @Override
    public void init() {
        Robot.setup(hardwareMap, telemetry, Robot.SetupType.TeleOp1);
    }

    @Override
    public void start() {
        Robot.setup(hardwareMap, telemetry, Robot.SetupType.TeleOp2);
    }

    @Override
    public void loop() {
        Robot.update();
        Robot.telemetry.addLine("" + Robot.getArm().getPosition());
//        Robot.telemetry.addLine("" + Robot.getLinearslide().isBusy());
//
//        if(gamepad1.left_stick_x!=0 || gamepad1.left_stick_y!=0){
//            double left_stick_y = -gamepad1.left_stick_y;
//            double left_stick_x = gamepad1.left_stick_x;
//            double joyStickAngle = (Math.toDegrees(Math.atan2(left_stick_y, left_stick_x)) + 360) % 360;
//            double speed = Math.hypot(left_stick_x, left_stick_y)/1.5;//to make faster make lower
//            Robot.getDrivetrain().runSpeedAngle(speed, joyStickAngle,0);
//        }
//        else if(gamepad1.right_stick_x!=0 || gamepad1.right_stick_y!=0){
//            double right_stick_y = -gamepad1.right_stick_y;
//            double right_stick_x = gamepad1.right_stick_x;
//            double joyStickAngleCrawl = (Math.toDegrees(Math.atan2(right_stick_y, right_stick_x)) + 360) % 360;
//            double speedCrawl = Math.hypot(right_stick_x, right_stick_y)/2.5;
//            Robot.getDrivetrain().runSpeedAngle(speedCrawl, joyStickAngleCrawl ,0);
//        }
//
//        else if (gamepad1.left_stick_button) {
//            Robot.getDrivetrain().runSpeed(0.5, _Drivetrain.Movements.ccw);
//        }
//        else if (gamepad1.right_stick_button) {
//            Robot.getDrivetrain().runSpeed(0.5, _Drivetrain.Movements.cw);
//        } else {
//            Robot.getDrivetrain().stop();
//        }
//
//        //left trigger up and right trigger down
//        // assume encoder count increase as linear slide moves up
//        //change 5
//        if (gamepad2.right_trigger != 0) { //&& Robot.getLinearslide().getCounts()/Robot.getLinearslide().getCountsPerInch() >0
//            _triggerPressed = true;
//            Robot.getLinearslide().runSpeed(0.6);
//        }
//        else if (gamepad2.left_trigger != 0 && Robot.getLinearslide().getCounts()/Robot.getLinearslide().getCountsPerInch()>0) {//&& Robot.getLinearslide().getCounts()/Robot.getLinearslide().getCountsPerInch()<95
//            _triggerPressed = true;
//            Robot.getLinearslide().runSpeed(-0.6);
//        }
//        else if (_triggerPressed) {
//            _triggerPressed = false;
//            Robot.getLinearslide().stop();
//        }
//        else {
//            if (gamepad2.dpad_left) {
//                Robot.getArm().setPosition(_LOW_POLE);
//                Robot.getClawPivot().setPosition(.3);
//
//            }
//            else if (gamepad2.dpad_up){
//                Robot.getArm().setPosition(_MID_POLE);
//                Robot.getClawPivot().setPosition(0.1);
//
//            }
//            else if (gamepad2.dpad_right){
//                Robot.getLinearslide().runDistance(0.5, 9);
//                Robot.getArm().setPosition(_HIGH_POLE);
//                Robot.getClawPivot().setPosition(0.2);
//                _height = 9;
//            }
//            else if (gamepad2.dpad_down){
//                Robot.getArm().setPosition(0.06);
//                Robot.getLinearslide().runDistance(-0.5, _height);
//                Robot.getClawPivot().setPosition(0.52);
//
//                _height = 0;
//            }
//        }
//
//        if (gamepad1.b){
//            Robot.getClaw().setPosition(0.725);
//        }
//        else if (gamepad1.a){
//            Robot.getClaw().setPosition(0.885);
//
//        }
//        else {
//            Robot.getClawPivot().resetForNextRun();
//        }
////        if (gamepad2.b){
////            Robot.getArm().setPosition(0.1);
////        }
////        else if (gamepad2.x){
////            Robot.getArm().setPosition(0.9);
////        }
////        else{
////            Robot.getArm().resetForNextRun();
////        }
//        if (gamepad1.a){
//            //assume 0 is open
//            Robot.getClaw().setPosition(0.725);
//        }
//        else if (gamepad1.y){
//            //assume 1 is close
//            Robot.getClaw().setPosition(0.885);
//        }
//        else{
//            Robot.getClaw().resetForNextRun();
//        }
//        if (gamepad2.a){
//            Robot.getClawPivot().setPosition(0.55);
//        }
//        else if (gamepad2.y){
//            Robot.getClawPivot().setPosition(0.65);
//        }
//        else {
//            Robot.getClawPivot().resetForNextRun();
//        }

        if (gamepad1.right_trigger > 0) {
            Robot.getArm().setPosition(Robot.getArm().getPosition() + 0.001);
        }
        else if (gamepad1.left_trigger > 0) {
            Robot.getArm().setPosition(Robot.getArm().getPosition() - 0.001);
        }
    }
}