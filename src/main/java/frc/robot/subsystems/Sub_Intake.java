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
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.RobotContainer;
import frc.robot.utilities.CAN_DeviceFaults;
import frc.robot.utilities.CAN_Input;

public class Sub_Intake extends SubsystemBase implements CAN_Input {
  /**
   * Creates a new Sub_Intake.
   */
  WPI_TalonFX intakeMotor = new WPI_TalonFX(Constants.INT_COL_MOTOR);
  Solenoid intakeSol = new Solenoid(Constants.INT_COL_SOL);

  ShuffleboardTab intakeTab = Shuffleboard.getTab("Intake");
  NetworkTableEntry curDraw = intakeTab.add("Intake Current Draw", 0).getEntry();

  public Sub_Intake() {
  }

  public void setShuffleboard() {
    curDraw.setDouble(intakeMotor.getSupplyCurrent());
  }

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
    setShuffleboard();
  }

  public Vector<CAN_DeviceFaults> input() {
    Vector<CAN_DeviceFaults> myCanDevices = new Vector<CAN_DeviceFaults>();
    myCanDevices.add(new CAN_DeviceFaults(intakeMotor));
    return myCanDevices;
  }
}
