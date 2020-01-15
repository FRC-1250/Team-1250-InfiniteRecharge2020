/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the oot directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Sub_Drivetrain extends SubsystemBase {

  CANSparkMax spark = new CANSparkMax(Constants.PANEL_MOTOR, MotorType.kBrushless);
  /**
   * Creates a new Sub_Drivetrain.
   */
  public CANPIDController pid = new CANPIDController(spark);
  public double turnSetpoint = 0;

  public Sub_Drivetrain() {
    pid.setP(1);
    pid.setI(0);
    pid.setD(0);
  }

  public void turnGo() {
    pid.setReference(128, ControlType.kPosition);
  }

  public void spinMotor() {
    spark.set(1);
  }

  public void stopMotor() {
    spark.set(0);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
