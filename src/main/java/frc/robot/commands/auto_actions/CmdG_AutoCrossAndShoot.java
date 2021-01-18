/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.auto_actions;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.drive.Cmd_AutoDrive;
import frc.robot.commands.shooter.Cmd_HoodGoToPos;
import frc.robot.commands.shooter.Cmd_ShootNTimes;
import frc.robot.subsystems.Sub_Drivetrain;
import frc.robot.subsystems.Sub_Hopper;
import frc.robot.subsystems.Sub_Shooter;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/latest/docs/software/commandbased/convenience-features.html
public class CmdG_AutoCrossAndShoot extends SequentialCommandGroup {
  /**
   * Creates a new CmdG_AutoCrossAndShoot.
   */
  public CmdG_AutoCrossAndShoot(Sub_Drivetrain s_drive, Sub_Shooter s_shooter, Sub_Hopper s_hopper) {
    // Add your commands in the super() call, e.g.
    // super(new FooCommand(), new BarCommand());
    super
    (
      new Cmd_ShootNTimes(s_shooter, s_hopper, 3),
      new Cmd_HoodGoToPos(s_shooter, -5),
      new Cmd_AutoDrive(s_drive, 20, 0.8, 0.8).withTimeout(15)
    );
  }
}
