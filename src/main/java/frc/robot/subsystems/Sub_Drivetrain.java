/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the oot directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import java.util.Map;
import java.util.Vector;

import com.ctre.phoenix.sensors.PigeonIMU;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.utilities.CAN_DeviceFaults;
import frc.robot.utilities.CAN_Input;


public class Sub_Drivetrain extends SubsystemBase implements CAN_Input {

  CANSparkMax fRightMotor = new CANSparkMax(Constants.DRV_RIGHT_FRONT, MotorType.kBrushless);
  CANSparkMax bRightMotor = new CANSparkMax(Constants.DRV_RIGHT_BACK, MotorType.kBrushless);
  CANSparkMax fLeftMotor = new CANSparkMax(Constants.DRV_LEFT_FRONT, MotorType.kBrushless);
  CANSparkMax bLeftMotor = new CANSparkMax(Constants.DRV_LEFT_BACK, MotorType.kBrushless);

  //Drive groups
  private SpeedControllerGroup gRightSide = new SpeedControllerGroup(fRightMotor, bRightMotor);
  private SpeedControllerGroup gLeftSide = new SpeedControllerGroup(fLeftMotor, bLeftMotor);

  //Diff Drive
  private DifferentialDrive diffDriveGroup = new DifferentialDrive(gLeftSide, gRightSide);
  Solenoid solPTO = new Solenoid(Constants.CLM_SOL_PTO);

  //Other devices
  PigeonIMU pigeon = new PigeonIMU(Constants.DRV_PIGEON);
  Joystick Gamepad = new Joystick(0);



  public static double accumError = 0;
	//private final double AUTO_TURN_RATE = 0.3;
	private final double pSimpleStraight = Constants.DRV_KP_SIMPLE_STRAIT;
	private final double pSimple = Constants.DRV_KP_SIMPLE;
  //private final double KI_SIMPLE = 0.03;
  public double driveSetpoint = 0;

  // Shuffleboard
  ShuffleboardTab driveTab = Shuffleboard.getTab("Drive");
  NetworkTableEntry lRPM = driveTab.add("Left RPM", 0).getEntry();
  NetworkTableEntry rRPM = driveTab.add("Right RPM", 0).getEntry();
  NetworkTableEntry lCurrentDraw = driveTab.add("Left Cur Draw", 0)
    .withWidget(BuiltInWidgets.kNumberBar)
    .withProperties(Map.of("min", 0, "max", 100))
    .getEntry();
  NetworkTableEntry rCurrentDraw = driveTab.add("Right Cur Draw", 0)
    .withWidget(BuiltInWidgets.kNumberBar)
    .withProperties(Map.of("min", 0, "max", 100))
    .getEntry();
  
  public ShuffleboardTab getTab() { return driveTab; }
  //

  public Sub_Drivetrain(){
    //Ramp Rates
    fRightMotor.setOpenLoopRampRate(0.2);
    bRightMotor.setOpenLoopRampRate(0.2);
    fLeftMotor.setOpenLoopRampRate(0.2);
    bLeftMotor.setOpenLoopRampRate(0.2);
  }

  public void setShuffleboard() {
    lRPM.setDouble(getVelocity(fLeftMotor));
    rRPM.setDouble(getVelocity(fRightMotor));
    lCurrentDraw.setDouble(fLeftMotor.getOutputCurrent());
    rCurrentDraw.setDouble(fRightMotor.getOutputCurrent());
  }
  
  public void idleMode(IdleMode idleMode){
    //Idle Mode config
    fRightMotor.setIdleMode(idleMode);
    bRightMotor.setIdleMode(idleMode);
    fLeftMotor.setIdleMode(idleMode);
    bLeftMotor.setIdleMode(idleMode);
  }

  //Actual Drive Method
  public void drive(double left, double right){
    diffDriveGroup.tankDrive(left, right);
  }

  //The drive method that passes the joystick values (Overloaded)
  public void drive(Joystick joy){
    drive(-joy.getY(), -joy.getThrottle());
  }

  //Arcade drive method
  public void driveArcade(Joystick joy){
		diffDriveGroup.arcadeDrive(-joy.getThrottle(),joy.getZ());
  }

  //Get velocity for any CANSparkMax in this subsys
  public double getVelocity(CANSparkMax motor){
    return motor.getEncoder().getVelocity();
  }

  //Get position for any CANSparkMax in this subsys
  public double getPosition(CANSparkMax motor){
    return motor.getEncoder().getPosition();
  }

  //Resets position of the motors referenced for auton
  public void drivePosReset(){
    fLeftMotor.getEncoder().setPosition(0);
    fRightMotor.getEncoder().setPosition(0);
  }

  //Returns the yaw from the pigeon IMU
  public double getGyroAngle(){
    double[] ypr = new double[3];
    pigeon.getYawPitchRoll(ypr);
    return ypr[0];
  }

  //Resets the angle of the gyro to 0
  //TODO: Test this
  public void resetGyro(){
    pigeon.addYaw(-getGyroAngle());
  }

  //Configures the maximum amp draw of the drive motors based on temperature of the motors
  //Linear correct in accordance to some motor law
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

  //Used in linearDrivingAmpControl() to set the current limit of the drive motors
  public void motorCurrentConfig(int limit){
    fRightMotor.setSmartCurrentLimit(limit);
    bRightMotor.setSmartCurrentLimit(limit);
    fLeftMotor.setSmartCurrentLimit(limit);
    bLeftMotor.setSmartCurrentLimit(limit);
  }

  //Configures the setpoint var for auton
  public void setSetpointPos(double distance){
    driveSetpoint = (Constants.DRV_TICKS_TO_INCH * distance);
  } 

  //Checks if auto drive command is complete
  public boolean isDoneDriving(){    
    double currVal = this.getPosition(fLeftMotor);
    double distToPos = currVal - driveSetpoint;
    SmartDashboard.putNumber("DistToPos", distToPos);
    return (distToPos >= 0);
  }

  //Checks if auto drive command is complete for when robot runs backwards
  public boolean isDoneDrivingBack(){   
    double currVal = this.getPosition(fLeftMotor);
    double distToPos = currVal - driveSetpoint;
    SmartDashboard.putNumber("DistToPosBack", distToPos);
    return (distToPos <= 0);
  }

  //Checks if auto turn command is complete
  public boolean isDoneTurning(double angle){
    return (Math.abs(angle - this.getGyroAngle()) < 2);
  }

  //Configures a proportional gain for the driving stright during auto
  private double getGainP(double setpoint, double current, double kP){ 	
    double error = setpoint - current;  		
    return pSimple * error;
  }

  //Linear ramp control for driving, accepts an upper and lower value
  private double linearRamp(double upperSpeed, double lowerSpeed){
    double diff = (driveSetpoint - (double)Math.abs(getPosition(fLeftMotor)));
    double corrected = .05 * diff;
    double upperBound = Math.min(upperSpeed , corrected);
    double lowerBound = Math.max(lowerSpeed , upperBound);
  
    SmartDashboard.putNumber("correctedoutput", corrected);
    return lowerBound;
  }

  //Executes the driving of the robot for auton
  public void driveToPos( double upperSpeed, double lowerSpeed){	
    double offset = getGainP(0,this.getGyroAngle(),pSimpleStraight);
    double sign = Math.signum(driveSetpoint);
    
    diffDriveGroup.arcadeDrive(linearRamp(upperSpeed,lowerSpeed) * sign, 0 + offset);
    
  }

  //Executes the turning of the robot for auton
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
  
  //Stops driving---------------------
  public void driveStop(){
    diffDriveGroup.arcadeDrive(0, 0);
  }

  public void pause(){
    drive(0,0);
  }
  //----------------------------------

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
    setShuffleboard();
  }

  public Vector<CAN_DeviceFaults> input() {
    Vector<CAN_DeviceFaults> myCanDevices = new Vector<CAN_DeviceFaults>();
    myCanDevices.add(new CAN_DeviceFaults(fRightMotor));
    myCanDevices.add(new CAN_DeviceFaults(fLeftMotor));
    myCanDevices.add(new CAN_DeviceFaults(bRightMotor));
    myCanDevices.add(new CAN_DeviceFaults(bLeftMotor));
    return myCanDevices;
  }
}
