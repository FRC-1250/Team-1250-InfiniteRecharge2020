/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Sub_Hopper extends SubsystemBase {
  /**
   * Creates a new Sub_Hopper.
   */

  WPI_TalonFX leftMotor = new WPI_TalonFX(Constants.HOP_FALCON_0);
  WPI_TalonFX rightMotor = new WPI_TalonFX(Constants.HOP_FALCON_1);
  WPI_TalonFX uptakeMotor = new WPI_TalonFX(Constants.HOP_ELE_MOTOR);
  DigitalInput uptakeSensor = new DigitalInput(Constants.HOP_ELE_SENS);

  public Sub_Hopper() {
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

  public void uptakeGo() {
    uptakeMotor.set(0.5);
  }

  public void uptakeReverse() {
    uptakeMotor.set(-0.5);
  }

  public boolean getSensor() {
    return uptakeSensor.get();
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}