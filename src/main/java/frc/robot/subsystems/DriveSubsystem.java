package frc.robot.subsystems;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.Constants.MotorReverse;
import frc.robot.DeviceId.DriveMotor;

public class DriveSubsystem extends SubsystemBase {
    private final DriveModule Left = new DriveModule(DriveMotor.FRONT_LEFT, MotorReverse.FRONT_LEFT,
            DriveMotor.BACK_LEFT, MotorReverse.BACK_LEFT);
    private final DriveModule Right = new DriveModule(DriveMotor.FRONT_RIGHT, MotorReverse.FRONT_RIGHT,
            DriveMotor.BACK_RIGHT, MotorReverse.BACK_RIGHT);

    private final PIDController LefPidController = new PIDController(0.0005, 0.0, 0.0);
    private final PIDController RighPidController = new PIDController(0.0005, 0.0, 0.0);
    private final SimpleMotorFeedforward feedforward = new SimpleMotorFeedforward(1.0, 0.5, 0.1);

    private double timestates;
    private double nowtime;
    private final double KOP_WHEEL = 0.0672;

    public DriveSubsystem() {

    }

    public void updateTime() {
        nowtime = Timer.getFPGATimestamp() - timestates;
        this.timestates = Timer.getFPGATimestamp();
    }

    public double getLeftSpeed() {
        return this.Left.getVelocity() / 2 / 60 / 13.5 * KOP_WHEEL * 2 * Math.PI;
    }

    public double getRightSpeed() {
        return this.Right.getVelocity() / 2 / 60 / 13.5 * KOP_WHEEL * 2 * Math.PI;
    }

    public double leftEncoder() {
        return Left.getVelocity();
    }

    public double rightEncoder() {
        return Right.getVelocity();
    }

    public void execute(double leftSetpoint, double rightSetpoint) {
        double leftvelocity = this.getLeftSpeed();
        double rightvelocity = this.getRightSpeed();

        double leftPIDOutput = LefPidController.calculate(leftvelocity, leftSetpoint);
        double rightPIDOutput = RighPidController.calculate(rightvelocity, rightSetpoint);

        double leftFeedforward = feedforward.calculate(leftSetpoint);
        double rightFeedforward = feedforward.calculate(rightSetpoint);

        double leftVoltage = leftPIDOutput + leftFeedforward;
        double rightVoltage = rightPIDOutput + rightFeedforward;

        leftVoltage = MathUtil.clamp(leftVoltage, -12.0, 12.0);
        rightVoltage = MathUtil.clamp(rightVoltage, -12.0, 12.0);

        this.Left.setVoltage(leftVoltage);
        this.Right.setVoltage(rightVoltage);
    }

    public void stopModules() {
        this.Left.stop();
        this.Right.stop();

    }

    @Override
    public void periodic() {
        this.updateTime();
    }
}