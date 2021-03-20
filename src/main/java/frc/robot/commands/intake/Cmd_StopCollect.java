/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.intake;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Sub_Hopper;
import frc.robot.subsystems.Sub_Intake;

public class Cmd_StopCollect extends CommandBase {
  /**
   * Creates a new Cmd_StopCollect.
   */
  private final Sub_Intake s_intake;
  private final Sub_Hopper s_hopper;
  public Cmd_StopCollect(Sub_Intake intake, Sub_Hopper hopper) {
    s_intake = intake;
    s_hopper = hopper;
    addRequirements(intake, hopper);
  }

  @Override
  public void initialize() {
   
  }

  @Override
  public void execute() {
    s_intake.spinIntakeMotor(0);
    s_intake.retractCylinder();
  }

  @Override
  public void end(boolean interrupted) {
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}
