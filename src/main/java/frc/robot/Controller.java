package frc.robot;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;

public class Controller extends XboxController {
    public Controller() {
        super(1);
    }

    public Trigger Putter() {
        return new Trigger(this::getBButton);
    }

    public Trigger PutterCorrection() {
        return new Trigger(this::getAButton);
    }

    public Trigger IntakeArmUp() {
        return new Trigger(this::getRightBumperButton);
    }

    public Trigger IntakeArmDown() {
        return new Trigger(this::getLeftBumperButton);
    }

    public Trigger Intake() {
        return new Trigger(this::getYButton);
    }

    public Trigger AutoIntake() {
        return new Trigger(this::getXButton);
    }
}