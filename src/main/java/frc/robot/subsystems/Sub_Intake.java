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
import frc.robot.RobotContainer;

public class Sub_Intake extends SubsystemBase {
  /**
   * Creates a new Sub_Intake.
   */
  public Sub_Intake() {

  }

  WPI_TalonFX intakeMotor = new WPI_TalonFX(Constants.INT_COL_MOTOR);
  Solenoid intakeSol = new Solenoid(Constants.INT_COL_SOL);

  public void spinIntake() {
    intakeMotor.set(0.5);
  }

  public void stopIntake() {
    intakeMotor.set(0);
  }

  public void reverseIntake() {
    intakeMotor.set(-0.5);
  }

  public void extendCylinder() {
    intakeSol.set(true);
  }

  public void retractCylinder() {
    intakeSol.set(false);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    RobotContainer.configureCollector();
  }
}
