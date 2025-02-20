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
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.DeviceId;

public class IntakeArmSubsystem extends SubsystemBase {
    private final SparkMax motor;

    private final DutyCycleEncoder encoder = new DutyCycleEncoder(0);
    private final PIDController IntakeArmPId = new PIDController(1.15, 0, 0);

    private final double MIN_DEGREE = 0.52792321319808;
    private final double MAX_DEGREE = 0.319051807976295;
    private final double KEEP = 0.436155310903883;

    public IntakeArmSubsystem() {
        this.motor = new SparkMax(DeviceId.controller.IntakeArm, MotorType.kBrushless);
        SparkMaxConfig config = new SparkMaxConfig();
        config
                .inverted(false)
                .idleMode(IdleMode.kBrake);
        this.motor.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

    }

    public void execute(double speed) {
        if (this.MIN_DEGREE >= this.encoder.get() && this.MAX_DEGREE <=
        this.encoder.get()) {
        this.motor.set(speed);
        } else if (this.MIN_DEGREE > this.encoder.get() && this.MAX_DEGREE >=
        this.encoder.get()) {
        this.motor.set(speed);
        } else if (this.MIN_DEGREE < this.encoder.get() && this.MAX_DEGREE <=
        this.encoder.get()) {
        this.motor.set(speed);
        } else {
        this.motor.set(0);
        }
    }

    public void ArmTo(double angle) {
        double speed = MathUtil.applyDeadband(this.IntakeArmPId.calculate(this.encoder.get(), angle),
                0.01);
        this.motor.set(speed);
        SmartDashboard.putNumber("IntakeArm encoder", this.encoder.get());
    }

    public Command Up() {
        return Commands.runEnd(() -> this.ArmTo(MAX_DEGREE), this::stop, this);
    }

    public Command Down() {
        return Commands.runEnd(() -> this.ArmTo(MIN_DEGREE), this::stop, this);
    }

    public Command Keep() {
        return Commands.runEnd(() -> this.ArmTo(KEEP), this::stop, this);
    }

    public void stop() {
        this.motor.stopMotor();
    }
}
