/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.state;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Sub_StateManager extends SubsystemBase {

  private String robotSubsystemState;

  public Sub_StateManager() {
    robotSubsystemState = "";
  }

  public String getRobotState() {
    return robotSubsystemState;
  }

  public void setRobotSubsystemState(String robotSubsystemState) {
    this.robotSubsystemState = robotSubsystemState;
  }
  
}
