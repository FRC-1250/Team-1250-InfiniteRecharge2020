/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.diagnostic;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Sub_Shooter;

public class Cmd_RunFlywheels extends CommandBase {
  /**
   * Creates a new Cmd_RunFlywheels.
   */
  private final Sub_Shooter s_shoot;
  public Cmd_RunFlywheels(Sub_Shooter shoot) {
    // Use addRequirements() here to declare subsystem dependencies.
    s_shoot = shoot;
    addRequirements(shoot);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    s_shoot.spinFlywheelMotors(0.2);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    s_shoot.spinFlywheelMotors(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
