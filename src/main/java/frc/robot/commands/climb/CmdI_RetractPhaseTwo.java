/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.climb;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.subsystems.Sub_Climber;

public class CmdI_RetractPhaseTwo extends InstantCommand {

  private final Sub_Climber s_climber;

  public CmdI_RetractPhaseTwo(Sub_Climber s_climber) {
    this.s_climber = s_climber;
  }

  @Override
  public void initialize() {
    s_climber.retractPhase2Cylinder();
  }
}
