/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.climber;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Sub_Climber;
import frc.robot.subsystems.Sub_Drivetrain;

public class Cmd_EngagePTO extends CommandBase {
  /**
   * Creates a new Cmd_EngagePTO.
   */
  private final Sub_Drivetrain s_drive;
  private final Sub_Climber s_climb;
  public Cmd_EngagePTO(Sub_Drivetrain drive, Sub_Climber climb) {
    // Use addRequirements() here to declare subsystem dependencies.
    s_drive = drive;
    s_climb = climb;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    s_drive.engagePTO();
    s_climb.retractTopCylinder();
    s_drive.isPTOEngaged = true;
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
