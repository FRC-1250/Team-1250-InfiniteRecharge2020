/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the oot directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Sub_Drivetrain extends SubsystemBase {

  CANSparkMax spark = new CANSparkMax(Constants.DRV_RIGHT_FRONT, MotorType.kBrushless);
  /**
   * Creates a new Sub_Drivetrain.
   */
  public Sub_Drivetrain() {
    
  }

  public void spinMotor() {
    spark.set(0.2);
  }

  public void stopMotor() {
    spark.set(0);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
