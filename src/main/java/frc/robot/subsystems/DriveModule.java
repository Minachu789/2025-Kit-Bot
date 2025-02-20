
package frc.robot.subsystems;

import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

import frc.robot.DeviceId;

import com.revrobotics.spark.config.SparkMaxConfig;

import com.revrobotics.spark.SparkLowLevel.MotorType;

public class DriveModule {
    private final SparkMax motor;
    private final SparkMax leftmotor;
    private final RelativeEncoder encoder;

    public DriveModule(int port, boolean reverse, int leftport, boolean leftreverse) {
        this.motor = new SparkMax(port, MotorType.kBrushless);
        SparkMaxConfig config = new SparkMaxConfig();
        config
                .inverted(reverse)
                .idleMode(IdleMode.kBrake);
        this.motor.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
        this.encoder = this.motor.getEncoder();
        this.leftmotor = new SparkMax(leftport, MotorType.kBrushless);
        SparkMaxConfig leftConfig = new SparkMaxConfig();
        leftConfig
                .inverted(leftreverse)
                .idleMode(IdleMode.kBrake)
                .follow(motor);
        this.leftmotor.configure(leftConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    }

    public double getPosition() {
        return this.encoder.getPosition();
    }

    public double getVelocity() {
        return this.encoder.getVelocity();
    }

    public void setVoltage(double speed) {
        this.motor.setVoltage(speed);
    }

    public void stop() {
        this.motor.stopMotor();
    }
}
