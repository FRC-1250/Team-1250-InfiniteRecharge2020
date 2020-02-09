/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.shooter;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Sub_Hopper;
import frc.robot.subsystems.Sub_Shooter;

public class Cmd_SpinFlywheels extends CommandBase {
  /**
   * Creates a new heels.
   */
  private final Sub_Shooter s_shooter;
  private final Sub_Hopper s_hopper;
  double _speed;
  public Cmd_SpinFlywheels(Sub_Shooter shooter, Sub_Hopper hopper, double speed) {
    // Use addRequirements() here to declare subsystem dependencies.
    _speed = speed;
    s_shooter = shooter;
    s_hopper = hopper;
    addRequirements(shooter);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    s_shooter.spinFlywheelMotors(_speed);
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
