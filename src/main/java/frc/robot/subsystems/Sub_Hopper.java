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
import edu.wpi.first.wpilibj.Joystick;
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

  Joystick Gamepad = new Joystick(0);

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
  NetworkTableEntry sensorValue = hopperTab.add("Sensor Value", 0)
    .withPosition(4, 0)
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
    sensorValue.setDouble(uptakeSensor.getValue());
  }

  public void spinHopperMotors(double speed) {
    leftMotor.set(speed);
    rightMotor.set(speed);
  }

  public void spinUptakeMotor(double speed) {
    uptakeMotor.set(speed);
  }

  public double getVelocity(WPI_TalonFX motor){
    return motor.getSelectedSensorVelocity();
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

    /*

    TODO: Remove in future commit. The default command is now set in the RobotContainer 
    and this logic has been moved to the command Cmd_HopperManagement and Cmd_ShootCells.

    if ((Gamepad.getRawButton(Constants.SHOOT_MODE)) && (Gamepad.getRawButton(Constants.BTN_X))) {
      spinHopperMotors(0.4);
      spinUptakeMotor(1);
    } else if (!getSensor()) {
      spinHopperMotors(0.4);
      spinUptakeMotor(0.4);
    } else {
      spinHopperMotors(0);
      spinUptakeMotor(0);
    }
    */
  }

  public Vector<CAN_DeviceFaults> input() {
    Vector<CAN_DeviceFaults> myCanDevices = new Vector<CAN_DeviceFaults>();
    myCanDevices.add(new CAN_DeviceFaults(leftMotor));
    myCanDevices.add(new CAN_DeviceFaults(rightMotor));
    myCanDevices.add(new CAN_DeviceFaults(uptakeMotor));
    return myCanDevices;
  }
}
