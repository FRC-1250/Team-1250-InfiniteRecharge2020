/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.drive;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Sub_Drivetrain;

public class Cmd_AutoTurn extends CommandBase {
  private final Sub_Drivetrain s_drivetrain;

  double angle = 0;
  double upperSpeed = 0;
  double lowerSpeed = 0;
  public Cmd_AutoTurn(Sub_Drivetrain subsystem, double angle, double upperSpeed, double lowerSpeed) {
    s_drivetrain = subsystem;
    addRequirements(subsystem);
    this.angle = angle;
    this.upperSpeed = upperSpeed;
    this.lowerSpeed = lowerSpeed;
    s_drivetrain.resetGyro();
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    s_drivetrain.resetGyro();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    s_drivetrain.turn(angle, upperSpeed, lowerSpeed);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    s_drivetrain.driveStop();
    s_drivetrain.resetGyro();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return s_drivetrain.isDoneTurning(angle);
  }
}
