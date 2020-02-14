/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.climb;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.Sub_Climber;

public class CmdSG_ExtendPhases extends SequentialCommandGroup {

  public CmdSG_ExtendPhases(Sub_Climber s_climber) {
    super(
      new CmdI_ExtendTopCylinder(s_climber), 
      new CmdI_ExtendBottomCylinder(s_climber)
      );
  }
}
