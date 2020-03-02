/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import java.util.Vector;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.RobotContainer;
import frc.robot.utilities.*;

public class Sub_Climber extends SubsystemBase implements CAN_Input {
  /**
   * Creates a new Sub_Climber.
   */
  public Sub_Climber() {
  }

  Solenoid phase1Solenoid = new Solenoid(Constants.CLM_SOL_EXTEND0);
  Solenoid phase2Solenoid = new Solenoid(Constants.CLM_SOL_EXTEND1);

  ShuffleboardTab climbTab = Shuffleboard.getTab("Climber");
  public ShuffleboardTab getTab() { return climbTab; }

  NetworkTableEntry Top = climbTab.add("Phase 1 (top)", "false").getEntry();
  NetworkTableEntry Bottom = climbTab.add("Phase 2 (btm)", "false").getEntry();

  Joystick Gamepad0 = new Joystick(0);
  Joystick Gamepad2 = new Joystick(2);

  public void extendTopCylinder() {
    phase1Solenoid.set(true);
  }

  public void retractTopCylinder() {
    phase1Solenoid.set(false);
  }

  public void extendBottomCylinder() {
    phase2Solenoid.set(true);
  }

  public void retractBottomCylinder() {
    phase2Solenoid.set(false);
  }

  public void setShuffleboard() {
    Top.setString(Boolean.toString(phase1Solenoid.get()));
    Bottom.setString(Boolean.toString(phase2Solenoid.get()));
  }

  @Override
  public void periodic() {
    // setShuffleboard();
    // if (Gamepad2.getRawButton(7)) { // LT
    //   extendTopCylinder();
    // } else if (Gamepad2.getRawButton(8)) { // RT
    //   extendBottomCylinder();
    // } else if (Gamepad2.getRawButton(5)){ // LB
    //   retractTopCylinder();
    // } else if (Gamepad2.getRawButton(6)) { // RB
    //   retractBottomCylinder();
    // } else if (Gamepad2.getRawButton(11)) { // LEFT CLICK
    //   extendTopCylinder();
    //   extendBottomCylinder();
    // } else if (Gamepad2.getRawButton(12)) { // RIGHT CLICK
    //   retractTopCylinder();
    //   retractBottomCylinder();
    // }
  }
  
  public Vector<CAN_DeviceFaults> input() {
    Vector<CAN_DeviceFaults> myCanDevices = new Vector<CAN_DeviceFaults>();
    // myCanDevices.add(new CAN_DeviceFaults(CAN_DEVICE));
    return myCanDevices;
  }
}
