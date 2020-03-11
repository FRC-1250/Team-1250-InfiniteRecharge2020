/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.auto_actions;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.drive.Cmd_AutoDrive;
import frc.robot.commands.drive.Cmd_AutoTurn;
import frc.robot.commands.intake.Cmd_Collect;
import frc.robot.commands.intake.Cmd_StopCollect;
import frc.robot.commands.shooter.Cmd_HoodGoToPos;
import frc.robot.commands.shooter.Cmd_ShootNTimes;
import frc.robot.subsystems.Sub_Drivetrain;
import frc.robot.subsystems.Sub_Hopper;
import frc.robot.subsystems.Sub_Intake;
import frc.robot.subsystems.Sub_Shooter;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/latest/docs/software/commandbased/convenience-features.html
public class CmdG_AutoAllianceTrench extends SequentialCommandGroup {
  /**
   * Creates a new CmdG_AutoAllianceTrench.s
   */


  public CmdG_AutoAllianceTrench(Sub_Drivetrain s_drive, Sub_Shooter s_shooter, Sub_Hopper s_hopper, Sub_Intake s_intake) {

    super(
    // new Cmd_AutoDrive(s_drive, 20, 0.8, 0.8).withTimeout(15),
    // new Cmd_HoodGoToPos(s_shooter, -127),
    // new Cmd_ShootNTimes(s_shooter, s_hopper, 3),
    // new Cmd_AutoTurn(s_drive, -30, 0.5, 0.5),
    // new Cmd_Collect(s_intake),
    // new Cmd_AutoDrive(s_drive, 80, .8, .8),
    // new Cmd_DoNothing().withTimeout(0.1),
    // new Cmd_AutoTurn(s_drive, 30, 0.5, 0.5),
    // new Cmd_DoNothing().withTimeout(0.1),
    // new Cmd_AutoDrive(s_drive, 160, 0.7, 0.7),
    // new Cmd_DoNothing().withTimeout(0.2),
    // new Cmd_AutoDrive(s_drive, -160, 0.7, 0.7),
    // new Cmd_StopCollect(s_intake, s_hopper),
    // new Cmd_HoodGoToPos(s_shooter, -150),
    // new Cmd_ShootNTimes(s_shooter, s_hopper, 5)

    new Cmd_ShootNTimes(s_shooter, s_hopper, 3),
    new Cmd_Collect(s_intake),
    new Cmd_AutoDrive(s_drive, 140, 0.7, 0.7),
    new Cmd_DoNothing().withTimeout(0.2),
    new Cmd_StopCollect(s_intake, s_hopper),
    new Cmd_AutoDrive(s_drive, -100, 0.7, 0.7),
    new Cmd_ShootNTimes(s_shooter, s_hopper, 5).alongWith(new Cmd_Collect(s_intake))
    );

  }
}
