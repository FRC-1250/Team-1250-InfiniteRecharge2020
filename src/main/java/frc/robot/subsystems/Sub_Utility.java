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
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
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
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;

public class Sub_Utility extends SubsystemBase implements CAN_Input {

  Joystick Gamepad1 = new Joystick(1);
  Joystick Gamepad2 = new Joystick(2);
  public long initTime;

  PowerDistributionPanel pdp = new PowerDistributionPanel();
  Compressor pcm = new Compressor();
  // AnalogInput pressureSensor = new AnalogInput(1);

  ArrayList<ArrayList<String>> stateButtons = new ArrayList<ArrayList<String>>();
  public String[] buttons = {"x", "a", "b", "y"};

  /** For CAN and Shuffleboard (diagnostics in general) */
  public Sub_Utility() {
    makeModeEntries(true);
    makeCommandEntries(true);
  }

  // Self Shuffleboard
  ShuffleboardTab utilTab = Shuffleboard.getTab("Utility");
  NetworkTableEntry pdpID = utilTab.add("PDP ID", -1)
    .withPosition(0, 0).getEntry();
  NetworkTableEntry pcmID = utilTab.add("PCM ID", -1)
    .withPosition(0, 1).getEntry();
  NetworkTableEntry pdpTCurrent = utilTab.add("PDP T Current", -1)
    .withPosition(1, 0).getEntry();
  NetworkTableEntry pdpTEnergy = utilTab.add("PDP T Energy", -1)
    .withPosition(0, 0).getEntry();
  NetworkTableEntry pdpVoltage = utilTab.add("PDP Voltage", -1)
    .withPosition(1, 0).getEntry();
  NetworkTableEntry pdpTPower = utilTab.add("PDP T Power", -1)
    .withPosition(2, 0).getEntry();
  NetworkTableEntry pcmSFault = utilTab.add("PCM (Sticky) Connected", "false")
    .withPosition(1, 1).withSize(2, 1).getEntry();
  NetworkTableEntry pcmFault = utilTab.add("PCM Connected", "false")
    .withPosition(3, 1).withSize(2, 1).getEntry();
  NetworkTableEntry pcmSwitch = utilTab.add("PCM Pressure Switch", "false")
    .withPosition(5, 1).withSize(2, 1).getEntry();
  NetworkTableEntry pressure = utilTab.add("Pressure Sensor", 0)
    .withPosition(5, 2).getEntry();
  
  public ShuffleboardTab getTab() { return utilTab; }

  public void utilDiagnostic() {
    pdpTCurrent.setDouble(pdp.getTotalCurrent());
    pdpTEnergy.setDouble(pdp.getTotalEnergy());
    pdpVoltage.setDouble(pdp.getVoltage());
    pdpTPower.setDouble(pdp.getTotalPower());
    pcmFault.setString(Boolean.toString(pcm.getCompressorNotConnectedFault()));
    pcmSFault.setString(Boolean.toString(pcm.getCompressorNotConnectedStickyFault()));
    pcmSwitch.setString(Boolean.toString(pcm.getPressureSwitchValue()));
    // pressure.setDouble(getPressureSensor());
  }
  //

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

    // Palette cleanser (cleanses with the color that the color sensor sees)
    int[] rgb = new int[3];
    for (int i = 0; i < rgb.length; i++) {
      rgb[i] = (int)(RobotContainer.s_panel.getRGBValues()[i] * 120); // 255 is bright
    }
    for (int i = 0; i < Robot.ledStripBuffer.getLength(); i++) {
      if (RobotContainer.s_panel.getSensorColor() != 'U') {
        Robot.ledStripBuffer.setRGB(i, rgb[0], rgb[1], rgb[2]);
      } else {
        Robot.ledStripBuffer.setRGB(i, 255, 255, 255);
        // White; means the color sensor isn't working
      }
    }

    for (int i = 0; i < can_length; i++) {
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
                              RobotContainer.s_hopper.getTab(), RobotContainer.s_drivetrain.getTab(), RobotContainer.s_climb.getTab(), /*this.getTab()*/}; // add more if more tabs are created
  NetworkTableEntry[] allModeEntries = new NetworkTableEntry[allTabs.length];
  NetworkTableEntry[][] allCommandEntries = new NetworkTableEntry[allTabs.length][4];

  public NetworkTableEntry[][] makeCommandEntries(boolean initialize) {
    if (initialize) {
      for (int tab = 0; tab < allTabs.length; tab++) {
        for (int button = 0; button < buttons.length; button++) {
        allCommandEntries[tab][button] = allTabs[tab].add(buttons[button].toUpperCase(), "none")
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

  /**
   * Makes the entries for robot states (collect, shoot, panel, climb)
   *
   * @param initialize   Should only be true when the method is called in the constructor
   * @return             Array of NetworkTableEntry for use in conjunction with setShuffleboard()
   */
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
    // utilDiagnostic();
  }
  
  public String whatMode() {
    return RobotContainer.s_stateManager.getRobotState();
  }

  public void setStateButton(String mode, String cmd, String btn) {
    ArrayList<String> modeAndBtn = new ArrayList<String>();
    switch (btn) {
      case "x":
        btn = "0";
        break;
      case "a":
        btn = "1";
        break;
      case "b":
        btn = "2";
        break;
      case "y":
        btn = "3";
        break;
      default:
        btn = "ONLY XABY AVAILABLE";
        break;
    }
    modeAndBtn.add(mode);
    modeAndBtn.add(btn);
    modeAndBtn.add(cmd);
    stateButtons.add(modeAndBtn);
  }

  /** Iterates through all the commands created in RobotContainer and then checks if the first item
   * in every stateButton array (1st item being the mode) is equal to the current robot mode, then adds
   * to the "cmds" array with the button number. (Since 'x' is actually '1', cmds[1] would equal the command assigned to the x button)
   * 
   * @return  Array of commands to be assigned to buttons in shuffleboard
   */
  public String[] whatCommand() {
    String empty = "NONE";
    String[] cmds = {empty, empty, empty, empty};
    
    // stateButtons = [mode, button, command]
    for (int i = 0; i < stateButtons.size(); i++) {
      if (whatMode() == stateButtons.get(i).get(0)) {
        cmds[Integer.parseInt(stateButtons.get(i).get(1))] = stateButtons.get(i).get(2);
      }
      // System.out.println("### " + "i: " + i + " \nstateButtons.get(i).get(1): " + Integer.parseInt(stateButtons.get(i).get(1)) + " \nstateButtons.get(i).get(2): " + stateButtons.get(i).get(2));
    }
    return cmds;
  }

  /** Timer set to pause CAN check for 3 seconds */
  public boolean checkCAN() {
    if (System.currentTimeMillis() - initTime > 3000) {
      // RobotContainer.s_util.sortLEDByCAN();
      initTime = System.currentTimeMillis();
      return true;
    }
    return false;
  }

  public void checkSpinThrice() {
    if (Robot.halvesAroundPanel > 7) {
      Robot.halvesAroundPanel = 0;
    }
  }

  // public double getPressureSensor() {
  //   return pressureSensor.getValue();
  // }

  @Override
  public void periodic() {
    setShuffleboard();
    //checkSpinThrice();
  }
}
