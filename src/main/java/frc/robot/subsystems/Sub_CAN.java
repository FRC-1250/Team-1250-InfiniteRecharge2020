/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import java.util.Vector;

import com.ctre.phoenix.motorcontrol.StickyFaults;

import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Robot;
import frc.robot.RobotContainer;
import frc.robot.utilities.CAN_DeviceFaults;
import frc.robot.utilities.CAN_Input;

public class Sub_CAN extends SubsystemBase implements CAN_Input {
  /**
   * Creates a new Sub_CAN.
   */
  private final AddressableLED ledStrip = Robot.ledStrip;
  private final AddressableLEDBuffer ledStripBuffer = Robot.ledStripBuffer;
  public int can_length;
  
  public Sub_CAN() {
  }

  public Vector<CAN_DeviceFaults> input() {
    Vector<CAN_DeviceFaults> masterCanDevices = new Vector<CAN_DeviceFaults>();
    // inputs from each subsystem with CAN
    masterCanDevices.addAll(RobotContainer.s_panel.input());
    masterCanDevices.addAll(RobotContainer.s_drivetrain.input());
    masterCanDevices.addAll(RobotContainer.s_shooter.input());
    masterCanDevices.addAll(RobotContainer.s_hopper.input());
    masterCanDevices.addAll(RobotContainer.s_intake.input());
    return masterCanDevices;
  }

  @Override
  public void periodic() {
    String msg = "CAN_MSG_NOT_FOUND";
    Vector<CAN_DeviceFaults> can_devices = input();
    can_length = can_devices.size();

    for (int i = 0; i < can_devices.size(); i++) {
      if (can_devices.get(i).stickyfault == msg) {
        // bad
        ledStripBuffer.setRGB(i, 255, 0, 0);
      } else if (can_devices.get(i).stickyfault == "SPARK") {
        // not sure about CAN for sparks (sticky faults are shorts and don't follow a visible pattern)
        ledStripBuffer.setRGB(i, 255, 165, 0);
      } else {
        // good
        ledStripBuffer.setRGB(i, 0, 255, 0);
      }
      System.out.println("CANCANCANCAN ###########" + i + " " + can_devices.get(i).stickyfault);
    }
    ledStrip.setData(ledStripBuffer);
  }
}
