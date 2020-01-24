/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the oot directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.sensors.PigeonIMU;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
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

  PigeonIMU pigeon = new PigeonIMU(Constants.DRV_PIGEON);

  Joystick Gamepad = new Joystick(0);

  public static double accumError = 0;
	//private final double AUTO_TURN_RATE = 0.3;
	private final double KP_SIMPLE_STRAIT = 0.01;
	private final double KP_SIMPLE = 0.05;
  //private final double KI_SIMPLE = 0.03;
  public double driveSetpoint = 0;

  public Sub_Drivetrain(){
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

  public void driveArcade(Joystick joy){
		diffDriveGroup.arcadeDrive(-joy.getThrottle(),joy.getZ());
  }
  
  public double getVelocity(CANSparkMax motor){
    return motor.getEncoder().getVelocity();
  }

  public double getPosition(CANSparkMax motor){
    return motor.getEncoder().getPosition();
  }

  public void drivePosReset(){
    fLeftMotor.getEncoder().setPosition(0);
    fRightMotor.getEncoder().setPosition(0);
  }

  public double getGyroAngle(){
    double[] ypr = new double[3];
    pigeon.getYawPitchRoll(ypr);
    return ypr[0];
  }

  public void resetGyro(){
    pigeon.addYaw(-getGyroAngle());
  }

  public void linearDrivingAmpControl(){
    double leftTemp = fLeftMotor.getMotorTemperature();
    double rightTemp = fRightMotor.getMotorTemperature();
    double currentTemp = Math.max(leftTemp, rightTemp);
    int linearCorrect = (-4 * (int)currentTemp) + 220;
  
    if (currentTemp < 80){
      motorCurrentConfig(100);
    }
    else if (currentTemp > 100){
      motorCurrentConfig(20);    
    }
    else if (currentTemp >= 80){
      motorCurrentConfig(linearCorrect);
    }
  }

  public void motorCurrentConfig(int limit){
    fRightMotor.setSmartCurrentLimit(limit);
    bRightMotor.setSmartCurrentLimit(limit);
    fLeftMotor.setSmartCurrentLimit(limit);
    bLeftMotor.setSmartCurrentLimit(limit);
  }

  public void setSetpointPos(double distance){
    driveSetpoint = (Constants.DRV_TICKS_TO_INCH * distance);
  }   

  public boolean isDoneDriving(){    
    double currVal = this.getPosition(fLeftMotor);
    double distToPos = currVal - driveSetpoint;
    SmartDashboard.putNumber("DistToPos", distToPos);
    return (distToPos >= 0);
  }

  public boolean isDoneDrivingBack(){   
    double currVal = this.getPosition(fLeftMotor);
    double distToPos = currVal - driveSetpoint;
    SmartDashboard.putNumber("DistToPosBack", distToPos);
    return (distToPos <= 0);
  }

  public boolean isDoneTurning(double angle){
    return (Math.abs(angle - this.getGyroAngle()) < 2);
  }

  private double getGainP(double setpoint, double current, double kP){ 	
    double error = setpoint - current;  		
    return KP_SIMPLE * error;
  }

  private double linearRamp(double upperSpeed, double lowerSpeed){
    double diff = (driveSetpoint - (double)Math.abs(getPosition(fLeftMotor)));
    double corrected = .05 * diff;
    double upperBound = Math.min(upperSpeed , corrected);
    double lowerBound = Math.max(lowerSpeed , upperBound);
  
    SmartDashboard.putNumber("correctedoutput", corrected);
    return lowerBound;
  }

  public void driveToPos( double upperSpeed, double lowerSpeed){	
    double offset = getGainP(0,this.getGyroAngle(),KP_SIMPLE_STRAIT);
    double sign = Math.signum(driveSetpoint);
    
    diffDriveGroup.arcadeDrive(linearRamp(upperSpeed,lowerSpeed) * sign, 0 + offset);
    
  }

  public void turn (double angle, double upperSpeed, double lowerSpeed){
    double corrected;
    double rotation = angle - getGyroAngle();
    double sign = Math.signum(rotation);      
    corrected = 0.05 * rotation;
          
    if (sign > 0){
            corrected = Math.min(upperSpeed * sign, corrected);
            corrected = Math.max(lowerSpeed * sign, corrected);
          }
              
    else{
            corrected = Math.max(upperSpeed * sign, corrected);
            corrected = Math.min(lowerSpeed * sign, corrected);                    
          }
          diffDriveGroup.arcadeDrive(0, corrected);
  }

  public void driveStop(){
    diffDriveGroup.arcadeDrive(0, 0);
  }

  public void pause(){
    drive(0,0);
  }

  @Override
  public void periodic(){
    Joystick Gamepad = new Joystick(0);
    linearDrivingAmpControl();
    if (Gamepad.getRawButton(12)){
      driveArcade(Gamepad);
    }
    else{
      drive(Gamepad);
    }
  }
}
