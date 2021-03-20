/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.hopper;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Sub_Hopper;

public class Cmd_ShootCells extends CommandBase {
  /**
   * Creates a new Cmd_ShootCells.
   */
  private final Sub_Hopper s_hopper;
  public Cmd_ShootCells(Sub_Hopper hopper) {
    super();
    s_hopper = hopper;
    addRequirements(hopper);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  @Override
  public void initialize() {
    s_hopper.spinHopperMotors(0.6);
    s_hopper.spinUptakeMotor(1);
  }

  @Override
  public void execute() {
  }

  @Override
  public void end(boolean interrupted) {
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}
