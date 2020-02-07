/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import java.util.Vector;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.commands.hopper.Cmd_HopperManagement;
import frc.robot.utilities.CAN_DeviceFaults;
import frc.robot.utilities.CAN_Input;

public class Sub_Hopper extends SubsystemBase implements CAN_Input {
  /**
   * Creates a new Sub_Hopper.
   */

  WPI_TalonFX leftMotor = new WPI_TalonFX(Constants.HOP_FALCON_0);
  WPI_TalonFX rightMotor = new WPI_TalonFX(Constants.HOP_FALCON_1);
  WPI_TalonFX uptakeMotor = new WPI_TalonFX(Constants.HOP_ELE_MOTOR);
  AnalogInput uptakeSensor = new AnalogInput(Constants.HOP_ELE_SENS);

  // Shuffleboard
  ShuffleboardTab hopperTab = Shuffleboard.getTab("Hopper");
  NetworkTableEntry lRPM = hopperTab.add("L RPM", 0)
    .withPosition(0, 0)
    .getEntry();
  NetworkTableEntry rRPM = hopperTab.add("R RPM", 0)
    .withPosition(2, 0)
    .getEntry();
  NetworkTableEntry lCurrentDraw = hopperTab.add("L Current Draw", 0)
    .withPosition(0, 1)
    .getEntry();
  NetworkTableEntry rCurrentDraw = hopperTab.add("R Current Draw", 0)
    .withPosition(2, 1)
    .getEntry();

  public ShuffleboardTab getTab() { return hopperTab; }
  //

  public Sub_Hopper() {
  }

  public void setShuffleboard() {
    lRPM.setDouble(getVelocity(leftMotor));
    rRPM.setDouble(getVelocity(rightMotor));
    lCurrentDraw.setDouble(leftMotor.getSupplyCurrent());
    rCurrentDraw.setDouble(rightMotor.getSupplyCurrent());
  }

  public double getVelocity(WPI_TalonFX motor){
    return motor.getSelectedSensorVelocity();
  }

  public void spinHopperMotors() {
    leftMotor.set(0.1);
    rightMotor.set(-0.1);
  }

  // might need to get flipped (and sped up)
  public void reverseHopperMotors() {
    leftMotor.set(-0.1);
    rightMotor.set(0.1);
  }

  public void stopHopperMotors() {
    leftMotor.set(0);
    rightMotor.set(0);
  }

  public void spinUptakeMotor() {
    uptakeMotor.set(0.1);
  }

  public void stopUptakeMotor() {
    uptakeMotor.set(0);
  }

  public void uptakeGo() {
    uptakeMotor.set(0.5);
  }

  public void uptakeReverse() {
    uptakeMotor.set(-0.5);
  }

  public boolean getSensor() {
    if (uptakeSensor.getValue() > 1000) {
      return true;
    }
    return false;
  }

  @Override
  public void periodic() {
    setShuffleboard();
  }

  public void initDefaultCommand() {
    setDefaultCommand(new Cmd_HopperManagement(this));
  }

  public Vector<CAN_DeviceFaults> input() {
    Vector<CAN_DeviceFaults> myCanDevices = new Vector<CAN_DeviceFaults>();
    myCanDevices.add(new CAN_DeviceFaults(leftMotor));
    myCanDevices.add(new CAN_DeviceFaults(rightMotor));
    myCanDevices.add(new CAN_DeviceFaults(uptakeMotor));
    return myCanDevices;
  }
}
