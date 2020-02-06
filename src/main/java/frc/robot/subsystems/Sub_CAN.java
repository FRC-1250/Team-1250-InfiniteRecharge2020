/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Robot;
import frc.robot.RobotContainer;
import frc.robot.utilities.CAN_DeviceFaults;
import frc.robot.utilities.CAN_Input;

public class Sub_CAN extends SubsystemBase implements CAN_Input {
  /**
   * Creates a new Sub_CAN.
   */
  public Sub_CAN() {
  }

  public Vector<CAN_DeviceFaults> input() {
    Vector<CAN_DeviceFaults> masterCanDevices = new Vector<CAN_DeviceFaults>();
    // Inputs from each subsystem with CAN
    masterCanDevices.addAll(RobotContainer.s_panel.input());
    masterCanDevices.addAll(RobotContainer.s_drivetrain.input());
    masterCanDevices.addAll(RobotContainer.s_shooter.input());
    masterCanDevices.addAll(RobotContainer.s_hopper.input());
    masterCanDevices.addAll(RobotContainer.s_intake.input());
    return masterCanDevices;
  }

  public int sortLEDByCAN(String getLength) {
    String msg = "CAN_MSG_NOT_FOUND";

    Vector<CAN_DeviceFaults> can_devices = this.input();
    int can_length = can_devices.size();

    if (getLength == "getLength") {
      return can_length;
    }

    Collections.sort(can_devices, new Comparator<CAN_DeviceFaults>() {
      public int compare(CAN_DeviceFaults c1, CAN_DeviceFaults c2) {
        return c1.getCanID() - c2.getCanID();
      }
    });

    // Palette cleanser
    for (int i = 0; i < Robot.ledStripBuffer.getLength(); i++) {
      Robot.ledStripBuffer.setRGB(i, 0, 0, 255);
    }

    for (int i = 1; i < Robot.ledStripBuffer.getLength(); i++) {
      if ((can_devices.get(i).stickyfault == msg) || (can_devices.get(i).stickyfault == "true")) {
        // bad
        Robot.ledStripBuffer.setRGB(i, 60, 0, 0);
      } else {
        // good
        Robot.ledStripBuffer.setRGB(i, 0, 60, 0);
      }
      // System.out.println("CANCANCANCAN ###########" + i + " " + can_devices.get(i).getCanID() + " " + can_devices.get(i).stickyfault);
    }
    return can_length;
  }

  @Override
  public void periodic() {
  }
}
