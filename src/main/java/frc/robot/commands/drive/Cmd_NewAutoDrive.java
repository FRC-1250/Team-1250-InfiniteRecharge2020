/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.drive;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.Sub_Drivetrain;

public class Cmd_NewAutoDrive extends CommandBase {
  private final Sub_Drivetrain s_drivetrain;
  double distToGo;

  public Cmd_NewAutoDrive(Sub_Drivetrain subsystem, double dist) {
    s_drivetrain = subsystem;
    addRequirements(subsystem);
    distToGo = dist;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    s_drivetrain.drivePosReset();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    s_drivetrain.newDriveTopos(distToGo);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    s_drivetrain.driveStop();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    double currentPos = Math.abs(s_drivetrain.leftDriveTicks());
    double correctedDist = Math.abs(distToGo * Constants.DRV_TICKS_TO_INCH);

    return((correctedDist - currentPos) <= 1);
    // return false;
  }
}
