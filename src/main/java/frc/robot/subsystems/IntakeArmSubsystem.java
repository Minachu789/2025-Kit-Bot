package frc.robot.subsystems;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.DutyCycleEncoder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.WaitUntilCommand;

import frc.robot.Constants;
import frc.robot.DeviceId;

public class IntakeArmSubsystem extends SubsystemBase {
    private final SparkMax motor;

    private final DutyCycleEncoder encoder = new DutyCycleEncoder(0);
    private final PIDController Pid = new PIDController(1.15, 0, 0);

    private final double MIN_DEGREE = -0.388300284707507;
    private final double MAX_DEGREE = 0.368803809220095;

    public IntakeArmSubsystem() {
        this.motor = new SparkMax(DeviceId.controller.IntakeArm, MotorType.kBrushless);
        SparkMaxConfig config = new SparkMaxConfig();
        config
                .inverted(true)
                .idleMode(IdleMode.kBrake);
        this.motor.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

    }

    public void execute(double speed) {
        if (this.MIN_DEGREE >= this.encoder.get() && this.MAX_DEGREE <= this.encoder.get()) {
            this.motor.set(speed);
            SmartDashboard.putNumber("IntakeArm encoder", this.encoder.get());
        } else if (this.MIN_DEGREE > this.encoder.get() && this.MAX_DEGREE >= this.encoder.get()) {
            this.motor.set(speed);
            SmartDashboard.putNumber("IntakeArm encoder", this.encoder.get());
        } else if (this.MIN_DEGREE < this.encoder.get() && this.MAX_DEGREE <= this.encoder.get()) {
            this.motor.set(speed);
            SmartDashboard.putNumber("IntakeArm encoder", this.encoder.get());
        } else {
            this.motor.set(0);
            SmartDashboard.putNumber("IntakeArm encoder", this.encoder.get());
        }
    }

    public void ArmTo(double angle) {
        double speed = MathUtil.applyDeadband((this.Pid.calculate(angle, this.encoder.get())),
                Constants.Drive.DEAD_BAND);
        this.execute(speed);
    }

    public Command Up() {
        return new WaitUntilCommand(() -> this.encoder.get() <= this.MIN_DEGREE + 0.05);
        // WaitCommand 條件判斷式，直到 true 才會結束。"->"Lambda 表達式
    }

    public Command Down() {
        return new WaitUntilCommand(() -> this.encoder.get() >= this.MAX_DEGREE + 0.05);
    }

    public Command AutoUp() {
        return new ParallelDeadlineGroup(this.AutoUp(),
                Commands.runEnd(() -> this.ArmTo(MAX_DEGREE), this::stop, this));
    }

    public Command AutoDown() {
        return new ParallelDeadlineGroup(this.AutoDown(),
                Commands.runEnd(() -> this.ArmTo(MIN_DEGREE), this::stop, this));
    }

    public void stop() {
        this.motor.stopMotor();
    }
}
