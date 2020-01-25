/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.panel;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Sub_Panel;

public class Cmd_SpinThrice extends CommandBase {
  /**
   * Creates a new Cmd_SpinThrice.
   */
  private final Sub_Panel s_panel;
  char desiredColor;
  int i = 0;
  char pastColor = 'N';
  public Cmd_SpinThrice(Sub_Panel subsystem) {
    s_panel = subsystem;
    addRequirements(subsystem);
    desiredColor = s_panel.getSensorColor();
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    s_panel.spinMotor();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    s_panel.stopMotor();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    char currentColor = s_panel.getSensorColor();
    if (currentColor == desiredColor && currentColor != pastColor) {
      i++;
    }
    if (i == 3) {
      return true;
    }
    pastColor = currentColor;
    return false;
  }
}
