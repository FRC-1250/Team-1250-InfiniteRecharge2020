/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Sub_Intake extends SubsystemBase {
  /**
   * Creates a new Sub_Intake.
   */
  public Sub_Intake() {

  }

  WPI_TalonFX intakeMotor = new WPI_TalonFX(Constants.INT_COL_MOTOR);
  // Solenoid intakeSol = new Solenoid(0);

  public void collect() {
    intakeMotor.set(0.5);
  }

  public void collectStop() {
    intakeMotor.set(0);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
