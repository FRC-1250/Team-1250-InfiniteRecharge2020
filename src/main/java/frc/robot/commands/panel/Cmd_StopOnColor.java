/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.panel;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Sub_Panel;

public class Cmd_StopOnColor extends CommandBase {
  /**
   * Creates a new Cmd_StopOnColor.
   */
  private final Sub_Panel s_panel;
  char color;
  public Cmd_StopOnColor(Sub_Panel subsystem, char _color) {
    color = _color;
    s_panel = subsystem;
    addRequirements(subsystem);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    s_panel.extendCylinder();
    s_panel.spinMotor(0.15 * s_panel.bestSpinDirection(color));
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    s_panel.stopMotor();
    s_panel.retractCylinders();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return s_panel.stopOnColor(color);
  }
}
