package frc.robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;

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

        private final Driver driver = new Driver();

        private final DriveCmd driveJoystickCmd = new DriveCmd(driveSubsystem, driver);

        private final Timer timer = new Timer();

        public RobotContainer() {
                this.driveSubsystem.setDefaultCommand(driveJoystickCmd);
                this.configBindings();
        }

        public void configBindings() {
                this.driver.Putter()
                                .whileTrue(this.putterSubsystem.Cmdexecute());
                this.driver.PutterCorrection()
                                .whileTrue(this.putterSubsystem.CmdexecuteCorrection());
                this.driver.Intake()
                                .whileTrue(this.intakeSubsystem.Cmdexecute());
                this.driver.AutoIntake()
                                //.onTrue(Commands
                                                //.runOnce(this.intakeArmSubsystem::AutoDown,
                                                  //              this.intakeArmSubsystem)
                                                //.andThen(this.intakeSubsystem.autoCmdexecute()))
                               // .onFalse(new ParallelRaceGroup(
                                 //               Commands.runEnd(this.intakeSubsystem::Cmdexecuteback,
                                     //                           this.intakeSubsystem::stop,
                                   //                             this.intakeSubsystem),
                                       //         new WaitCommand(1.5))
                                         //       .andThen(Commands.runOnce(this.intakeArmSubsystem::AutoUp,
                                           //                     this.intakeArmSubsystem)));
                                           .whileTrue(this.intakeSubsystem.Cmdexecuteback());
                this.driver.IntakeArmUp()
                                .whileTrue(this.intakeArmSubsystem.Up());
                this.driver.IntakeArmDown()
                                .whileTrue(this.intakeArmSubsystem.Down());
        }

        public Command getAutonomousCommand() {
                return null;
        }
}
