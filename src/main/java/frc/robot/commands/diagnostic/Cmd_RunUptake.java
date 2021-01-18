/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.diagnostic;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Sub_Hopper;

public class Cmd_RunUptake extends CommandBase {
  /**
   * Creates a new Cmd_RunUptake.
   */
  private final Sub_Hopper s_hopper;
  public Cmd_RunUptake(Sub_Hopper hopper) {
    // Use addRequirements() here to declare subsystem dependencies.
    withTimeout(1);
    addRequirements(hopper);
    s_hopper = hopper;
  }
  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    s_hopper.spinUptakeMotor(0.2);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    s_hopper.spinUptakeMotor(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
