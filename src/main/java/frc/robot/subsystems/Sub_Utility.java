/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Robot;
import frc.robot.RobotContainer;
import frc.robot.commands.diagnostic.Cmd_RunFlywheels;
import frc.robot.commands.diagnostic.Cmd_RunIntake;
import frc.robot.commands.diagnostic.Cmd_RunPanel;
import frc.robot.commands.diagnostic.Cmd_RunTurret;
import frc.robot.commands.diagnostic.Cmd_RunUptake;
import frc.robot.commands.shooter.Cmd_ToggleLL;
import frc.robot.commands.diagnostic.Cmd_RunHood;
import frc.robot.commands.diagnostic.Cmd_RunHopper;
import frc.robot.utilities.CAN_DeviceFaults;
import frc.robot.utilities.CAN_Input;

public class Sub_Utility extends SubsystemBase implements CAN_Input {

  Joystick Gamepad2 = new Joystick(2);

  ArrayList<ArrayList<String>> stateButtons = new ArrayList<ArrayList<String>>();
  public String[] buttons = {"X", "A", "B", "Y"};

  public Sub_Utility() {
    makeModeEntries(true);
    makeCommandEntries(true);
  }

  // CAN
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
      Robot.ledStripBuffer.setRGB(i, 0, 0, 30);
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
  //

  // Shuffleboard
  ShuffleboardTab[] allTabs = {RobotContainer.s_shooter.getTab(), RobotContainer.s_panel.getTab(), RobotContainer.s_intake.getTab(),
                                RobotContainer.s_hopper.getTab(), RobotContainer.s_drivetrain.getTab(), RobotContainer.s_climb.getTab()}; // add more if more tabs are created
  NetworkTableEntry[] allModeEntries = new NetworkTableEntry[allTabs.length];
  NetworkTableEntry[][] allCommandEntries = new NetworkTableEntry[allTabs.length][4];

  public NetworkTableEntry[][] makeCommandEntries(boolean initialize) {
    if (initialize) {
      for (int tab = 0; tab < allTabs.length; tab++) {
        for (int button = 0; button < buttons.length; button++) {
        allCommandEntries[tab][button] = allTabs[tab].add(buttons[button], "none")
          .withSize(1, 1)
          .withPosition(2+button, 3)
          .getEntry();
        }
      }
    } else {
      return allCommandEntries;
    }
    return null;
  }

  public void makeTestCommands() {
    SmartDashboard.putData("Hood Motor (F)", new Cmd_RunHood(RobotContainer.s_shooter, 0.1));
    SmartDashboard.putData("Hood Motor (B)", new Cmd_RunHood(RobotContainer.s_shooter, -0.1));
    SmartDashboard.putData("Hopper Motors", new Cmd_RunHopper(RobotContainer.s_hopper));
    SmartDashboard.putData("Intake Motor", new Cmd_RunIntake(RobotContainer.s_intake));
    SmartDashboard.putData("Turret Motor", new Cmd_RunTurret(RobotContainer.s_shooter));
    SmartDashboard.putData("Flywheel Motors", new Cmd_RunFlywheels(RobotContainer.s_shooter));
    SmartDashboard.putData("Uptake Motor", new Cmd_RunUptake(RobotContainer.s_hopper));
    SmartDashboard.putData("Panel Motor", new Cmd_RunPanel(RobotContainer.s_panel));
    SmartDashboard.putData("Limelight Toggle", new Cmd_ToggleLL(RobotContainer.s_shooter));
  }

  public NetworkTableEntry[] makeModeEntries(boolean initialize) {
    if (initialize) {
      for (int i = 0; i < allTabs.length; i++) {
        allModeEntries[i] = allTabs[i].add("Robot Mode", "default")
          .withSize(2, 1)
          .withPosition(0, 3)
          .getEntry();
      }
    } else {
      return allModeEntries;
    }
    return null;
  }

  public void setShuffleboard() {
    for (int i = 0; i < allTabs.length; i++) {
      makeModeEntries(false)[i].setString(RobotContainer.s_stateManager.getRobotState());
      for (int j = 0; j < 4; j++) {
        makeCommandEntries(false)[i][j].setString(whatCommand()[j]);
      }
    }
  }
  
  public String whatMode() {
    return RobotContainer.s_stateManager.getRobotState();
  }

  public void setStateButton(String mode, String cmd, String btn) {
    ArrayList<String> modeAndBtn = new ArrayList<String>();
    switch (btn) { // This could probably be optimized
      case "x":
        btn = "1";
        break;
      case "a":
        btn = "2";
      case "b":
        btn = "3";
      case "y":
        btn = "4";
      default:
        btn = "0";
        break;
    }
    modeAndBtn.add(mode);
    modeAndBtn.add(btn);
    modeAndBtn.add(cmd);
    stateButtons.add(modeAndBtn);
  }

  public String[] whatCommand() {
    String[] cmds = {"none", "none", "none", "none"};
    String mode = RobotContainer.s_stateManager.getRobotState();

    // iterates through all the commands created (stateButtons.size()) and then checks if the first
    // item in every stateButton array (the mode) is equal to the current robot mode
    // then adds to the cmds array with the button number (since 'x' is actually '1' cmds[1] would equal
    // the command on the x button)
    // [mode, button, command]
    for (int i = 0; i < stateButtons.size(); i++) {
      for (int j = 0; j < buttons.length; j++) {
        if (mode == stateButtons.get(i).get(0)) {
          cmds[Integer.parseInt(stateButtons.get(i).get(1))] = stateButtons.get(i).get(2);
        }
      }
    }
    return cmds;
  }

  // public String[] whatCommand() {
  //   String[] cmd = {"none", "none", "none", "none"};
  //   if (whatMode() == "PANEL_MODE") {
  //     cmd[0] = "Spin Thrice";
  //     cmd[2] = "Stop on Color";
  //     cmd[3] = "Deploy Cylinder";
  //   } else if (whatMode() == "SHOOT_MODE") {
  //     cmd[2] = "Fire";
  //   } else if (whatMode() == "CLIMB_MODE") {
  //     cmd[0] = "Extend";
  //     cmd[2] = "Engage PTO";
  //     cmd[3] = "Retract";
  //   } else if (whatMode() == "COLLECT_MODE") {
  //     cmd[0] = "Collect";
  //     cmd[3] = "Unjam";
  //   }
  //   return cmd;
  // }
  //

  @Override
  public void periodic() {
    if (Gamepad2.getRawButton(Constants.BTN_A)) {
      if (RobotContainer.s_shooter.table.getEntry("ledMode").getDouble(0) == 1) {
        RobotContainer.s_shooter.table.getEntry("ledMode").setNumber(3);
      } else {
        RobotContainer.s_shooter.table.getEntry("ledMode").setNumber(1);
      }
    }
    setShuffleboard();
    // makeTestCommands();
  }
}
