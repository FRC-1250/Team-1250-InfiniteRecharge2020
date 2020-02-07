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

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Robot;
import frc.robot.RobotContainer;
import frc.robot.utilities.CAN_DeviceFaults;
import frc.robot.utilities.CAN_Input;

public class Sub_CAN extends SubsystemBase implements CAN_Input {

  public Sub_CAN() {
    makeModeEntries(true);
    makeCommandEntries(true);
  }

  public Vector<CAN_DeviceFaults> input() {
    Vector<CAN_DeviceFaults> masterCanDevices = new Vector<CAN_DeviceFaults>();
    // Grabs inputs from each subsystem that has CAN (manually add)
    masterCanDevices.addAll(RobotContainer.s_panel.input());
    masterCanDevices.addAll(RobotContainer.s_drivetrain.input());
    masterCanDevices.addAll(RobotContainer.s_shooter.input());
    masterCanDevices.addAll(RobotContainer.s_hopper.input());
    masterCanDevices.addAll(RobotContainer.s_intake.input());
    return masterCanDevices;
  }

  public int sortLEDByCAN() {
    String msg = "CAN_MSG_NOT_FOUND";

    Vector<CAN_DeviceFaults> can_devices = this.input();
    int can_length = can_devices.size();

    Collections.sort(can_devices, new Comparator<CAN_DeviceFaults>() {
      public int compare(CAN_DeviceFaults c1, CAN_DeviceFaults c2) {
        return c1.getCanID() - c2.getCanID();
      }
    });

    // Palette cleanser
    for (int i = 0; i < Robot.ledStripBuffer.getLength(); i++) {
      Robot.ledStripBuffer.setRGB(i, 0, 0, 255);
    }

    for (int i = 1; i < can_length; i++) {
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

  // Shuffleboard
  ShuffleboardTab[] allTabs = {RobotContainer.s_shooter.getTab(), RobotContainer.s_panel.getTab(), RobotContainer.s_intake.getTab(),
                                RobotContainer.s_hopper.getTab(), RobotContainer.s_drivetrain.getTab(), RobotContainer.s_climb.getTab()}; // add more if more tabs are created
  NetworkTableEntry[] allModeEntries = new NetworkTableEntry[allTabs.length];
  NetworkTableEntry[][] allCommandEntries = new NetworkTableEntry[allTabs.length][4];

  public NetworkTableEntry[][] makeCommandEntries(boolean initialize) {
    if (initialize) {
      String[] buttons = {"X", "A", "B", "Y"};
      for (int i = 0; i < allTabs.length; i++) {
        for (int j = 0; j < 4; j++) {
        allCommandEntries[i][j] = allTabs[i].add(buttons[j], "none")
          .withSize(1, 1)
          .withPosition(2+j, 4)
          .getEntry();
        }
      }
    } else {
      return allCommandEntries;
    }
    return null;
  }

  public NetworkTableEntry[] makeModeEntries(boolean initialize) {
    if (initialize) {
      for (int i = 0; i < allTabs.length; i++) {
        allModeEntries[i] = allTabs[i].add("Robot Mode", "default")
          .withSize(2, 1)
          .withPosition(0, 4)
          .getEntry();
      }
    } else {
      return allModeEntries;
    }
    return null;
  }

  public void setShuffleboard() {
    for (int i = 0; i < allTabs.length; i++) {
      makeModeEntries(false)[i].setString(whatMode());
      for (int j = 0; j < 4; j++) {
        makeCommandEntries(false)[i][j].setString(whatCommand()[j]);
      }
    }
  }
  
  public String whatMode() {
    String mode;
    if (RobotContainer.panelMode.get()) {
      mode = "Panel";
    } else if (RobotContainer.shootMode.get()) {
      mode = "Shoot";
    } else if (RobotContainer.climbMode.get()) {
      mode = "Climb";
    } else {
      mode = "Collect";
    }
    return mode;
  }

  public String[] whatCommand() {
    String[] cmd = {"none", "none", "none", "none"};
    if (whatMode() == "Panel") {
      cmd[0] = "Spin Thrice";
      cmd[2] = "Stop on Color";
      cmd[3] = "Deploy Cylinder";
    } else if (whatMode() == "Shoot") {
      cmd[0] = "Spin Flywheels";
      cmd[2] = "Fire";
    } else if (whatMode() == "Climb") {
      cmd[0] = "Extend";
      cmd[2] = "Engage PTO";
      cmd[3] = "Retract";
    } else {
      cmd[0] = "Collect";
      cmd[2] = "Stop Collect";
      cmd[3] = "Unjam";
    }
    return cmd;
  }
  //

  @Override
  public void periodic() {
    setShuffleboard();
  }
}
