package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Control.Robot;
import org.firstinspires.ftc.teamcode.Control._TeleOp;
import org.firstinspires.ftc.teamcode.Drivers._Drivetrain;

@TeleOp(group="FinalTeleOp")
public class FinalTeleOp extends _TeleOp {

    @Override
    public void init() {
        Robot.setup(hardwareMap, telemetry, Robot.Mode.TeleOp, Robot.SetupType.TeleOp1);
    }

    @Override
    public void init_loop() {
        Robot.getClaw().setPosition(Robot.UPSIDEDOWN_OPEN);
        Robot.getArm().setDegree(Robot.TELEOPCOLLECT_ARM);
        Robot.getClawPivot().setDegree(Robot.TELEOPCOLLECT_PIVOT);
    }

    @Override
    public void start() {
        Robot.setup(hardwareMap, telemetry, Robot.Mode.TeleOp, Robot.SetupType.TeleOp2);
    }

    @Override
    public void loop() {
        Robot.update();
        telemetry.addLine("Claw: " + Robot.getClaw().getPosition());
        telemetry.addLine("Pivot: " + Robot.getClawPivot().getPosition());
        telemetry.addLine("Arm: " + Robot.getArm().getPosition());
        telemetry.addLine("LS: " + (Robot.getLinearslide().getCounts() / Robot.getLinearslide().getCountsPerInch()));

        if(gamepad1.left_stick_x!=0 || gamepad1.left_stick_y!=0){
            double left_stick_y = -gamepad1.left_stick_y;
            double left_stick_x = gamepad1.left_stick_x;
            double joyStickAngle = (Math.toDegrees(Math.atan2(left_stick_y, left_stick_x)) + 360) % 360;
            double speed = Math.hypot(left_stick_x, left_stick_y)/1.5;//to make faster make lower
            Robot.getDrivetrain().runSpeedAngle(speed, joyStickAngle,180);
        }
        else if(gamepad1.right_stick_x!=0 || gamepad1.right_stick_y!=0){
            double right_stick_y = -gamepad1.right_stick_y;
            double right_stick_x = gamepad1.right_stick_x;
            double joyStickAngleCrawl = (Math.toDegrees(Math.atan2(right_stick_y, right_stick_x)) + 360) % 360;
            double speedCrawl = Math.hypot(right_stick_x, right_stick_y) * 0.30;
            Robot.getDrivetrain().runSpeedAngle(speedCrawl, joyStickAngleCrawl ,180);
        }
        else if (gamepad1.left_stick_button) {
            Robot.getDrivetrain().runSpeed(0.5, _Drivetrain.Movements.ccw);
        }
        else if (gamepad1.right_stick_button) {
            Robot.getDrivetrain().runSpeed(0.5, _Drivetrain.Movements.cw);
        }
        else if (gamepad1.left_bumper) {
            Robot.getDrivetrain().runSpeed(0.1, _Drivetrain.Movements.ccw);
        }
        else if (gamepad1.right_bumper) {
            Robot.getDrivetrain().runSpeed(0.1, _Drivetrain.Movements.cw);
        }
        else if (!Robot.getDrivetrain().isBusy()) {
                Robot.getDrivetrain().stop();
        }

        if (gamepad1.dpad_left) {
            Robot.getDrivetrain().runDistance(0.1, 3, _Drivetrain.Movements.forward);
        }

        if (gamepad1.dpad_down) {
            Robot.getClaw().setPosition(Robot.UPSIDEDOWN_OPEN);
            Robot.getArm().setSlowDegree(Robot.TELEOPCOLLECT_ARM, 500);
            Robot.getClawPivot().setSlowDegree(Robot.TELEOPCOLLECT_PIVOT, 500);
            Robot.getLinearslide().runDistance(1, Robot.TELEOPCOLLECT_LS - (Robot.getLinearslide().getCounts() / Robot.getLinearslide().getCountsPerInch()));
        }
        else if (gamepad1.dpad_up) {
            Robot.getClaw().setPosition(Robot.UPSIDEDOWN_CLOSE);
            Robot.getArm().setSlowDegree(Robot.TELEOPDEPOSIT_ARM, 2000);
            Robot.getClawPivot().setSlowDegree(Robot.TELEOPDEPOSIT_PIVOT, 2000);
            Robot.getLinearslide().runDistance(1, Robot.TELEOPDEPOSIT_LS - (Robot.getLinearslide().getCounts() / Robot.getLinearslide().getCountsPerInch()));
            Robot.getDrivetrain().runDistance(0.1, 3, _Drivetrain.Movements.backward);
        }
    }
}