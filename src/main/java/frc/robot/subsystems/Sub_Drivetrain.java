/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the oot directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;


public class Sub_Drivetrain extends SubsystemBase {

  CANSparkMax fRightMotor = new CANSparkMax(Constants.DRV_RIGHT_FRONT, MotorType.kBrushless);
  CANSparkMax bRightMotor = new CANSparkMax(Constants.DRV_RIGHT_BACK, MotorType.kBrushless);
  CANSparkMax fLeftMotor = new CANSparkMax(Constants.DRV_LEFT_FRONT, MotorType.kBrushless);
  CANSparkMax bLeftMotor = new CANSparkMax(Constants.DRV_LEFT_BACK, MotorType.kBrushless);

  private SpeedControllerGroup gRightSide = new SpeedControllerGroup(fRightMotor, bRightMotor);
  private SpeedControllerGroup gLeftSide = new SpeedControllerGroup(fLeftMotor, bLeftMotor);

  private DifferentialDrive diffDriveGroup = new DifferentialDrive(gLeftSide, gRightSide);

  Joystick Gamepad = new Joystick(0);


  public Sub_Drivetrain() {
    //Ramp Rates
    fRightMotor.setOpenLoopRampRate(0.2);
    bRightMotor.setOpenLoopRampRate(0.2);
    fLeftMotor.setOpenLoopRampRate(0.2);
    bLeftMotor.setOpenLoopRampRate(0.2);  
  }

  public void idleMode(IdleMode idleMode){
    //Idle Mode config
    fRightMotor.setIdleMode(idleMode);
    bRightMotor.setIdleMode(idleMode);
    fLeftMotor.setIdleMode(idleMode);
    bLeftMotor.setIdleMode(idleMode);
 }


  public void drive(double left, double right){
    diffDriveGroup.tankDrive(left, right);
  }

  public void drive(Joystick joy){
    drive(-joy.getY(), -joy.getThrottle());
  }

  


  @Override
  public void periodic() {
    //Update Later
    drive(Gamepad);
  }
}
