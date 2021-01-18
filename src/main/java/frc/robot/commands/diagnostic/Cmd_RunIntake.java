/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.diagnostic;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Sub_Intake;

public class Cmd_RunIntake extends CommandBase {
  /**
   * Creates a new Cmd_RunIntake.
   */
  private final Sub_Intake s_intake;
  public Cmd_RunIntake(Sub_Intake intake) {
    // Use addRequirements() here to declare subsystem dependencies.
    withTimeout(1);
    s_intake = intake;
    addRequirements(intake);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    s_intake.spinIntakeMotor(0.3);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    s_intake.spinIntakeMotor(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
