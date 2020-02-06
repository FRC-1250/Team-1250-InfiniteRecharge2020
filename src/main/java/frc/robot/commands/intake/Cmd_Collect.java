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

public class Cmd_Collect extends CommandBase {
  /**
   * Creates a new Cmd_Collect.
   */
  private final Sub_Intake s_intake;
  private final Sub_Hopper s_hopper;
  public Cmd_Collect(Sub_Intake intake, Sub_Hopper hopper) {
    // Use addRequirements() here to declare subsystem dependencies.
    s_intake = intake;
    s_hopper = hopper;
    addRequirements(intake, hopper);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    s_intake.spinIntake();
    s_intake.extendCylinder();
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
