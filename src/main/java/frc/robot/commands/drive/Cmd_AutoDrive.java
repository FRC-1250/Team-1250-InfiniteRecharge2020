/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.drive;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Sub_Drivetrain;

public class Cmd_AutoDrive extends CommandBase {
  private final Sub_Drivetrain s_drivetrain;
  int distance = 0;
  double upperSpeed;
  double lowerSpeed;
  float sign;
  public Cmd_AutoDrive(Sub_Drivetrain subsystem, int distance, double upperSpeed, double lowerSpeed) {
    s_drivetrain = subsystem;
    addRequirements(subsystem);
    this.distance = distance;
    this.upperSpeed = upperSpeed;
    this.lowerSpeed = lowerSpeed;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    s_drivetrain.drivePosReset();
    s_drivetrain.resetGyro();
    s_drivetrain.setSetpointPos(distance);
    //Note in discord
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    s_drivetrain.driveToPos(upperSpeed, lowerSpeed);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    s_drivetrain.driveStop();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    sign = Math.signum(distance);
        
    if (sign == 1){
        return s_drivetrain.isDoneDriving();
    }
    if (sign == -1){
        return s_drivetrain.isDoneDrivingBack();
    }
    else{
        return false;
    }
    
  }
}
