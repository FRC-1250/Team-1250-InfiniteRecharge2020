/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.hopper;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.subsystems.Sub_Hopper;

public class Cmd_HopperManagement extends CommandBase {
  
  private final Sub_Hopper s_hopper;
  private Joystick Gamepad0 = RobotContainer.Gamepad;
  public Cmd_HopperManagement(Sub_Hopper hopper) {
    s_hopper = hopper;
    addRequirements(hopper);
  }

  @Override
  public void initialize() {
  }

  @Override
  public void execute() {
    if (Gamepad0.getRawButton(1)) {
      if (!s_hopper.getSensor()) {
        s_hopper.spinHopperMotors(0.4);
        s_hopper.spinUptakeMotor(0.4);
      } else {
        s_hopper.spinHopperMotors(0.2);
        s_hopper.spinUptakeMotor(0);
      }
    }
  }

  @Override
  public void end(boolean interrupted) {
    s_hopper.spinHopperMotors(0);
    s_hopper.spinUptakeMotor(0);
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}
