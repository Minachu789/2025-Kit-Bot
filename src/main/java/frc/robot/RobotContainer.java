package frc.robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;

import frc.robot.commands.DriveCmd;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.IntakeArmSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.PutterSubsystem;

public class RobotContainer {
        public final DriveSubsystem driveSubsystem = new DriveSubsystem();
        private final PutterSubsystem putterSubsystem = new PutterSubsystem();
        private final IntakeSubsystem intakeSubsystem = new IntakeSubsystem();
        private final IntakeArmSubsystem intakeArmSubsystem = new IntakeArmSubsystem();

        private final Controller driver = new Controller();

        private final DriveCmd driveJoystickCmd = new DriveCmd(driveSubsystem, driver);

        private final Timer timer = new Timer();

        public RobotContainer() {
                this.driveSubsystem.setDefaultCommand(driveJoystickCmd);
                this.configBindings();
        }

        public void configBindings() {
                this.driver.Putter()
                                .whileTrue(this.putterSubsystem.Cmdexecute());
                this.driver.PutterIn()
                                .whileTrue(this.putterSubsystem.CmdexecuteIn());
                this.driver.IntakeArmDown()
                                .onTrue(this.intakeArmSubsystem.Down()
                                                .alongWith(this.intakeSubsystem.Cmdexecuteback()))
                                .onFalse(this.intakeArmSubsystem.Up());
                this.driver.IntakeArmUp()
                                .onTrue(this.intakeArmSubsystem.Up()
                                                .alongWith(this.intakeSubsystem.Cmdexecute()))
                                .onFalse(this.intakeArmSubsystem.Down());
        }

        public Command getAutonomousCommand() {
                return null;
        }
}
