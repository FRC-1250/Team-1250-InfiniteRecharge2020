/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import java.util.Vector;

import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.RobotContainer;
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
  Joystick Gamepad1 = new Joystick(1);

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
  NetworkTableEntry uptakeAmps = hopperTab.add("Uptake Amps", 0)
    .withPosition(5, 0)
    .getEntry();

  public ShuffleboardTab getTab() { return hopperTab; }
  //

  public Sub_Hopper() {
    SupplyCurrentLimitConfiguration limit = new SupplyCurrentLimitConfiguration(true, 20, 20, 1);
    leftMotor.configSupplyCurrentLimit(limit);
    rightMotor.configSupplyCurrentLimit(limit);
  }

  public void setShuffleboard() {
    lRPM.setDouble(getVelocity(leftMotor));
    rRPM.setDouble(getVelocity(rightMotor));
    lCurrentDraw.setDouble(leftMotor.getSupplyCurrent());
    rCurrentDraw.setDouble(rightMotor.getSupplyCurrent());
    sensorValue.setDouble(uptakeSensor.getValue());
    uptakeAmps.setDouble(getUptakeAmps());
  }

  public void spinHopperMotors(double speed) {
    leftMotor.set(speed);
    rightMotor.set(speed * 0.5);
  }

  public void spinUptakeMotor(double speed) {
    uptakeMotor.set(speed);
  }

  public void alwaysSpinHopper() {
    leftMotor.set(0.1);
    rightMotor.set(0.1);
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

  public double getUptakeAmps() {
    return uptakeMotor.getSupplyCurrent();
  }

  @Override
  public void periodic() {
    String mode = RobotContainer.s_stateManager.getRobotState();

    setShuffleboard();
    // if (mode == "SHOOT_MODE" && Gamepad.getRawButton(Constants.LT) && (RobotContainer.s_shooter.getFlyWheelSpeed() > 1)) {
    //   spinHopperMotors(1);
    //   spinUptakeMotor(1);
    // } else {

    //   if (!Gamepad1.getRawButton(Constants.UNJAM_MODE)) {
    //     if (!getSensor()) {
    //       spinUptakeMotor(0.4);
    //       spinHopperMotors(0.4);
    //     } else {
    //       spinUptakeMotor(0);
    //       spinHopperMotors(0.4);
    //     }
    //   } 
    //   else if(!Gamepad1.getRawButton(4)){
    //     if (!getSensor()) {
    //       spinUptakeMotor(0.4);
    //       spinHopperMotors(-0.4);
    //     } else {
    //       spinUptakeMotor(0);
    //       spinHopperMotors(-0.4);
    //     }
    //   }
    //   else {
    //     leftMotor.set(0.1);
    //     rightMotor.set(0.1);
    //     spinUptakeMotor(0);
    //   }

    // }
  }

  public Vector<CAN_DeviceFaults> input() {
    Vector<CAN_DeviceFaults> myCanDevices = new Vector<CAN_DeviceFaults>();
    myCanDevices.add(new CAN_DeviceFaults(leftMotor));
    myCanDevices.add(new CAN_DeviceFaults(rightMotor));
    myCanDevices.add(new CAN_DeviceFaults(uptakeMotor));
    return myCanDevices;
  }
}
