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
    private final PIDController lifterPid = new PIDController(0.01, 0, 0);

    private final double MIN_DEGREE = -0.388300284707507;
    private final double MAX_DEGREE = 0.368803809220095;

    public IntakeArmSubsystem() {
        this.motor = new SparkMax(DeviceId.controller.IntakeArm, MotorType.kBrushless);
        SparkMaxConfig config = new SparkMaxConfig();
        config
                .inverted(true)
                .idleMode(IdleMode.kBrake);
        this.motor.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
        SmartDashboard.putNumber("IntakeArm encoder", this.encoder.get());
    }

    public void execute() {
        this.motor.set(0.1);
    }

    public void executeback() {
        this.motor.set(-0.1);
    }

    public Command Up() {
        return Commands.runEnd(this::execute, this::stop, this);
    }

    public Command Down() {
        return Commands.runEnd(this::executeback, this::stop, this);
    }

    public void stop() {
        this.motor.stopMotor();
    }
}
