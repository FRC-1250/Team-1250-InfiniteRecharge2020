/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import java.util.Vector;

import com.ctre.phoenix.motorcontrol.StickyFaults;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.utilities.*;

public class Sub_Climber extends SubsystemBase implements CAN_Input {
  /**
   * Creates a new Sub_Climber.
   */
  public Sub_Climber() {
  }

  Solenoid solenoid = new Solenoid(Constants.CLM_SOL_EXTEND);

  public void extendCylinder() {
    solenoid.set(true);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
  
  public Vector<CAN_DeviceFaults> input() {
    StickyFaults fault = new StickyFaults();
    Vector<CAN_DeviceFaults> myCanDevices = new Vector<CAN_DeviceFaults>();
    // myCanDevices.add(new CAN_DeviceFaults(CAN_DEVICE.getStickyFaults(fault).toString(), CAN_DEVICE.getDeviceID()));
    return myCanDevices;
  }
}
