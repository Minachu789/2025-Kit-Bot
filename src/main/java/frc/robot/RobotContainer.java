package frc.robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;

import frc.robot.commands.DriveCmd;
import frc.robot.commands.IntakeArmCmd;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.IntakeArmSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.PutterSubsystem;

public class 
RobotContainer {
        public final DriveSubsystem driveSubsystem = new DriveSubsystem();
        private final PutterSubsystem putterSubsystem = new PutterSubsystem();
        private final IntakeSubsystem intakeSubsystem = new IntakeSubsystem();
        private final IntakeArmSubsystem intakeArmSubsystem = new IntakeArmSubsystem();

        private final Controller controller = new Controller();
        private final Driver driver = new Driver();

        private final DriveCmd driveJoystickCmd = new DriveCmd(driveSubsystem, driver);
        private final IntakeArmCmd controllJoystickCmd = new IntakeArmCmd(intakeArmSubsystem, controller);

        private final Timer timer = new Timer();

        public RobotContainer() {
                this.driveSubsystem.setDefaultCommand(driveJoystickCmd);
                this.intakeArmSubsystem.setDefaultCommand(controllJoystickCmd);
                this.configBindings();
        }

        public void configBindings() {
                this.controller.Putter()
                                .whileTrue(this.putterSubsystem.Cmdexecute());
                this.controller.PutterIn()
                                .whileTrue(this.putterSubsystem.CmdexecuteIn());
                this.controller.IntakeArmDown()
                                .onTrue(this.intakeArmSubsystem.Down()
                                                .alongWith(this.intakeSubsystem.Cmdexecuteback()))
                                .onFalse(this.intakeArmSubsystem.Keep());
                this.controller.IntakeArmUp()
                                .onTrue(this.intakeArmSubsystem.Down()
                                                .alongWith(this.intakeSubsystem.Cmdexecute()))
                                .onFalse(this.intakeArmSubsystem.Up());
        }

        public Command getAutonomousCommand() {
                return null;
        }
}
