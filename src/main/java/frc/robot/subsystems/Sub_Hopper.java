/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import java.util.Vector;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
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

  ShuffleboardTab hopperTab = Shuffleboard.getTab("Hopper");

  public Sub_Hopper() {
    setShuffleboard();
  }

  public void setShuffleboard() {
    hopperTab.add("Left RPM", getVelocity(leftMotor));
    hopperTab.add("Right RPM", getVelocity(rightMotor));
    hopperTab.add("Left Current Draw", leftMotor.getSupplyCurrent());
    hopperTab.add("Right Current Draw", rightMotor.getSupplyCurrent());
  }

  public double getVelocity(WPI_TalonFX motor){
    return motor.getSelectedSensorVelocity();
  }

  public void spinHopperMotors() {
    leftMotor.set(0.5);
    rightMotor.set(-0.5);
  }
  // might need to get flipped
  public void reverseHopperMotors() {
    leftMotor.set(-0.5);
    rightMotor.set(0.5);
  }

  public void stopHopperMotors() {
    leftMotor.set(0);
    rightMotor.set(0);
  }

  public void uptakeGo() {
    uptakeMotor.set(0.5);
  }

  public void uptakeReverse() {
    uptakeMotor.set(-0.5);
  }

  @Override
  public void periodic() {
    /* if (Gamepad.getSomeButton()) {
      // unjam intake
    } else {
      // background management stuff
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
