/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.state;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.state.Sub_StateManager;

public class Cmd_StateChange extends CommandBase {

  private final String state;
  private final Sub_StateManager s_stateManager;

  public Cmd_StateChange(Sub_StateManager s_stateManager, String state) {
    this.state = state;
    this.s_stateManager = s_stateManager;
    addRequirements(this.s_stateManager);
  }

  @Override
  public void initialize() {
    this.s_stateManager.setRobotSubsystemState(this.state);
  }

}
