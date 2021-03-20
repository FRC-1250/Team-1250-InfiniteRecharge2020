/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.drive;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.subsystems.Sub_Drivetrain;

public class Cmd_TankDrive extends CommandBase {
  /**
   * Creates a new Cmd_TankDrive.
   */
  private final Sub_Drivetrain s_drivetrain;
  private Joystick Gamepad = new Joystick(0);
  public Cmd_TankDrive(Sub_Drivetrain drivetrain) {
    // Use addRequirements() here to declare subsystem dependencies.
    s_drivetrain = drivetrain;
    addRequirements(s_drivetrain);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    //s_drivetrain.drive(Gamepad);
    s_drivetrain.drive(Gamepad);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    s_drivetrain.drive(0, 0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }

  @Override
  public boolean runsWhenDisabled() {
    return false;
  }
}
