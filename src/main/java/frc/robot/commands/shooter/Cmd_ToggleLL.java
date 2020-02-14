/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.shooter;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Sub_Shooter;

public class Cmd_ToggleLL extends CommandBase {
  /**
   * Creates a new Cmd_ToggleLL.
   */
  private final Sub_Shooter s_shooter;
  public Cmd_ToggleLL(Sub_Shooter shooter) {
    // Use addRequirements() here to declare subsystem dependencies.
    s_shooter = shooter;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    s_shooter.table.getEntry("ledMode").setNumber(3);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    s_shooter.table.getEntry("ledMode").setNumber(1);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
