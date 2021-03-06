/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.intake;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Sub_Hopper;

public class Cmd_UnjamIntake extends CommandBase {
  /**
   * Creates a new Cmd_UnjamIntake.
   */
  private final Sub_Hopper s_hopper;
  public Cmd_UnjamIntake(Sub_Hopper hopper) {
    s_hopper = hopper;
    addRequirements(hopper);
  }

  @Override
  public void initialize() {
    s_hopper.reverseHopperMotors();
  }

  @Override
  public void execute() {
  }

  @Override
  public void end(boolean interrupted) {
    s_hopper.stopHopperMotors();
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}
