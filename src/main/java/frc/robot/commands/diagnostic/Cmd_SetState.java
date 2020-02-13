/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.diagnostic;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;

public class Cmd_SetState extends CommandBase {
  /**
   * Creates a new Cmd_SetState.
   */
  String state;

  public Cmd_SetState(String state) {
    this.state = state;
  }

  @Override
  public void initialize() {
    RobotContainer.robotState = this.state;
  }

  @Override
  public void end(boolean interrupted) {
    RobotContainer.robotState = "collect";
  }
}
