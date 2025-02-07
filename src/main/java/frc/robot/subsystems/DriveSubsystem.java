package frc.robot.subsystems;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.Constants.MotorReverse;
import frc.robot.DeviceId.DriveMotor;

public class DriveSubsystem extends SubsystemBase {
    private final DriveModule frontLeft;
    private final DriveModule frontRight;
    private final DriveModule backLeft;
    private final DriveModule backRight;

    private final PIDController LefPidController = new PIDController(0.01, 0.0, 0.0);
    private final PIDController RighPidController = new PIDController(0.01, 0.0, 0.0);
    private final SimpleMotorFeedforward feedforward = new SimpleMotorFeedforward(1.0, 0.5, 0.1);

    private double timestates;
    private double nowtime;
    private final double KOP_WHEEL = 0.0672;

    public DriveSubsystem() {
        this.frontLeft = new DriveModule(DriveMotor.FRONT_LEFT, MotorReverse.FRONT_LEFT);
        this.frontRight = new DriveModule(DriveMotor.FRONT_RIGHT, MotorReverse.FRONT_RIGHT);
        this.backLeft = new DriveModule(DriveMotor.BACK_LEFT, MotorReverse.BACK_LEFT);
        this.backRight = new DriveModule(DriveMotor.BACK_RIGHT, MotorReverse.BACK_RIGHT);
    }

    public void updateTime() {
        nowtime = Timer.getFPGATimestamp() - timestates;
        this.timestates = Timer.getFPGATimestamp();
    }

    public double getLeftSpeed() {
        return this.backLeft.getVelocity() / 60 / 13.5 * KOP_WHEEL * 2 * Math.PI;
    }

    public double getRightSpeed() {
        return this.backRight.getVelocity() / 60 / 13.5 * KOP_WHEEL * 2 * Math.PI;
    }

    public double leftEncoder() {
        return (this.backLeft.getVelocity() + this.frontLeft.getVelocity()) / 2;
    }

    public double rightEncoder() {
        return (this.backRight.getVelocity()) + this.frontRight.getVelocity() / 2;
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

        this.backLeft.setVoltage(leftVoltage);
        this.frontLeft.setVoltage(leftVoltage);
        this.backRight.setVoltage(rightVoltage);
        this.frontRight.setVoltage(rightVoltage);

        SmartDashboard.putNumber("leftvelocity", leftvelocity);
        SmartDashboard.putNumber("rightvelocity", rightvelocity);
        SmartDashboard.putNumber("leftSetpoint", leftSetpoint);
        SmartDashboard.putNumber("rightSetpoint", rightSetpoint);
    }

    public void stopModules() {
        this.frontLeft.stop();
        this.frontRight.stop();
        this.backLeft.stop();
        this.backRight.stop();
    }

    @Override
    public void periodic() {
        this.updateTime();
    }
}
