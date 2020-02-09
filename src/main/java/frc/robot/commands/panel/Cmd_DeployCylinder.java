/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.panel;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Sub_Panel;
import frc.robot.subsystems.Sub_Shooter;

public class Cmd_DeployCylinder extends CommandBase {
  /**
   * Creates a new Cmd_DeployCylinder.
   */
  private final Sub_Panel s_panel;
  private final Sub_Shooter s_shooter;
  public Cmd_DeployCylinder(Sub_Panel panel, Sub_Shooter shooter) {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(panel);
    s_panel = panel;
    s_shooter = shooter;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if ((s_shooter.turretCurrentPos > s_shooter.turretHome) && (s_shooter.turretCurrentPos - s_shooter.turretHome > 50)) 
      {
        // If you're to the right of the center, move left until you're within 50 ticks
        s_shooter.spinTurretMotor(-0.3);
      } else if ((s_shooter.turretCurrentPos < s_shooter.turretHome) && (s_shooter.turretCurrentPos - s_shooter.turretHome < -50)) 
      {
        s_shooter.spinTurretMotor(0.3);
      } else
      {
        s_panel.extendCylinder();
      }
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
