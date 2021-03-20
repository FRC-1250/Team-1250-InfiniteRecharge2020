/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.intake;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Sub_Intake;

public class Cmd_Collect extends CommandBase {
  /**
   * Creates a new Cmd_Collect.
   */
  private final Sub_Intake s_intake;
  public Cmd_Collect(Sub_Intake intake) {
    s_intake = intake;
    addRequirements(intake);
  }

  @Override
  public void initialize() {
 
  }

  @Override
  public void execute() {
    s_intake.spinIntakeMotor(1);
    s_intake.extendCylinder();
  }

  @Override
  public void end(boolean interrupted) {
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}
