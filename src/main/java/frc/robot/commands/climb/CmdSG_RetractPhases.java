/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.climb;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.subsystems.Sub_Climber;
import frc.robot.subsystems.Sub_Drivetrain;

public class CmdSG_RetractPhases extends SequentialCommandGroup {

  public CmdSG_RetractPhases(Sub_Drivetrain s_drivetrain, Sub_Climber s_climber) {
    super(
      new Cmd_EngagePTO(s_drivetrain),
      new CmdI_RetractTopCylinder(s_climber),
      // new WaitCommand(0.2),
      new CmdI_RetractBottomCylinder(s_climber)
      );
  }
}
