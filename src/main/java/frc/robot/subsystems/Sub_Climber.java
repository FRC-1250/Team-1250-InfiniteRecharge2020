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
import frc.robot.utilities.*;

public class Sub_Climber extends SubsystemBase implements CAN_Input {
  /**
   * Creates a new Sub_Climber.
   */
  public Sub_Climber() {
  }

  Solenoid phaseOneSolenoid = new Solenoid(Constants.CLM_SOL_EXTEND0);
  Solenoid phaseTwoSolenoid = new Solenoid(Constants.CLM_SOL_EXTEND1);

  ShuffleboardTab climbTab = Shuffleboard.getTab("Climber");
  public ShuffleboardTab getTab() { return climbTab; }
  NetworkTableEntry phase1 = climbTab.add("Phase 1", "false").getEntry();
  NetworkTableEntry phase2 = climbTab.add("Phase 2", "false").getEntry();
  Joystick Gamepad2 = new Joystick(2);

  public void extendPhase1Cylinder() {
    phaseOneSolenoid.set(true);
  }

  public void retractPhase1Cylinder() {
    phaseOneSolenoid.set(false);
  }

  public void extendPhase2Cylinder() {
    phaseTwoSolenoid.set(true);
  }

  public void retractPhase2Cylinder() {
    phaseTwoSolenoid.set(false);
  }

  public void setShuffleboard() {
    phase1.setString(Boolean.toString(phaseOneSolenoid.get()));
    phase2.setString(Boolean.toString(phaseTwoSolenoid.get()));
    // next stage
  }

  @Override
  public void periodic() {
    setShuffleboard();
    if (Gamepad2.getRawButton(7)) {
      extendPhase1Cylinder();
    } else if (Gamepad2.getRawButton(8)) {
      extendPhase2Cylinder();
    } else if (Gamepad2.getRawButton(5)){
      retractPhase1Cylinder();
    } else if (Gamepad2.getRawButton(6)) {
      retractPhase2Cylinder();
    }
  }
  
  public Vector<CAN_DeviceFaults> input() {
    Vector<CAN_DeviceFaults> myCanDevices = new Vector<CAN_DeviceFaults>();
    // myCanDevices.add(new CAN_DeviceFaults(CAN_DEVICE));
    return myCanDevices;
  }
}
