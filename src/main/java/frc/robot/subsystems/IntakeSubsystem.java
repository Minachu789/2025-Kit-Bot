package frc.robot.subsystems;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.WaitCommand;

import frc.robot.Constants;
import frc.robot.DeviceId;

public class IntakeSubsystem extends SubsystemBase {
    private final SparkMax motor;

    public IntakeSubsystem() {
        this.motor = new SparkMax(DeviceId.controller.Intake, MotorType.kBrushless);
        SparkMaxConfig config = new SparkMaxConfig();
        config
                .inverted(true)
                .idleMode(IdleMode.kBrake);
        this.motor.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    }

    public void execute() {
        this.motor.set(Constants.Controller.Subsystemspeed);
    }

    public void executeback() {
        this.motor.set(-0.3);
    }

    public Command Cmdexecute() {
        return Commands.runEnd(this::execute, this::stop, this);
    }

    public Command Cmdexecuteback() {
        return Commands.runEnd(this::executeback, this::stop, this);
    }

    public void stop() {
        this.motor.stopMotor();
    }
}