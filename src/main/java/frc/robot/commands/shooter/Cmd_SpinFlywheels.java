/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.shooter;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.subsystems.Sub_Shooter;

public class Cmd_SpinFlywheels extends CommandBase {
  private final Sub_Shooter s_shooter;
  double _speed;
  /**
   * @param speed Speed from 0 to 1 (1 being 100%)
   */
  public Cmd_SpinFlywheels(Sub_Shooter shooter, double speed) {
    // Use addRequirements() here to declare subsystem dependencies.
    _speed = speed;
    s_shooter = shooter;
    addRequirements(shooter);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    _speed = _speed * 22000; // converting to ticks per 100 milliseconds
    s_shooter.setFlywheelVelocityControl(_speed);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    s_shooter.track();
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    s_shooter.spinFlywheelMotors(0);
    s_shooter.spinTurretMotor(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
