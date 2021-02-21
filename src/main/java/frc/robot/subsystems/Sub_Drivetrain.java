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
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.RobotContainer;
import frc.robot.commands.drive.Cmd_TankDrive;
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

  CANPIDController driveLeftPID = new CANPIDController(bLeftMotor);
  CANPIDController driveRightPID = new CANPIDController(bRightMotor);


  //Other devices
  // AnalogGyro gyro = new AnalogGyro(1);
  AnalogGyro gyro = new AnalogGyro(1);
  // Gyro gyro = new ADXRS450_Gyro();
  // PigeonIMU pigeon = new PigeonIMU(1);
  Joystick Gamepad = new Joystick(0);
  Joystick Gamepad2 = new Joystick(2);

  String mode;
  public static double accumError = 0;
	//private final double AUTO_TURN_RATE = 0.3;
  //private final double KI_SIMPLE = 0.03;
  public double driveSetpoint = 0;

  public boolean isPTOEngaged = false;

  // Shuffleboard
  ShuffleboardTab driveTab = Shuffleboard.getTab("Drive");
  NetworkTableEntry lRPM = driveTab.add("Left RPM", 0).withPosition(0, 1).getEntry();
  NetworkTableEntry rRPM = driveTab.add("Right RPM", 0).withPosition(2, 1).getEntry();
  NetworkTableEntry leftTicks = driveTab.add("Left Ticks", -1).withPosition(1, 1).getEntry();
  NetworkTableEntry lCurrentDraw = driveTab.add("Left Cur Draw", 0)
    .withWidget(BuiltInWidgets.kNumberBar)
    .withProperties(Map.of("min", 0, "max", 100))
    .withPosition(0, 0)
    .getEntry();
  NetworkTableEntry rCurrentDraw = driveTab.add("Right Cur Draw", 0)
    .withWidget(BuiltInWidgets.kNumberBar)
    .withProperties(Map.of("min", 0, "max", 100))
    .withPosition(2, 0)
    .getEntry();
  NetworkTableEntry gyroValue = driveTab.add("Gyro Angle", -1)
    .withPosition(3, 0)
    .getEntry();
  
  public ShuffleboardTab getTab() {
     return driveTab; 
  }
  //

  public Sub_Drivetrain(){
    //Ramp Rates
    setRampRate(0.8);
    setCLRampRate(0.6);
    fRightMotor.follow(bRightMotor);
    fLeftMotor.follow(bLeftMotor);

    driveRightPID.setP(0.05);
    driveRightPID.setI(0.0);
    driveRightPID.setD(0.0);

    driveLeftPID.setP(0.05);
    driveLeftPID.setI(0.0);
    driveLeftPID.setD(0.0);

    setDefaultCommand(new Cmd_TankDrive(this));

  }

  public void setShuffleboard() {
    lRPM.setDouble(getVelocity(fLeftMotor));
    rRPM.setDouble(getVelocity(fRightMotor));
    leftTicks.setDouble(fLeftMotor.getEncoder().getPosition());
    lCurrentDraw.setDouble(fLeftMotor.getOutputCurrent());
    rCurrentDraw.setDouble(fRightMotor.getOutputCurrent());
    gyroValue.setDouble(getGyroAngle());
  }

  public void setRampRate(double rate) {
    fRightMotor.setOpenLoopRampRate(rate);
    bRightMotor.setOpenLoopRampRate(rate);
    fLeftMotor.setOpenLoopRampRate(rate);
    bLeftMotor.setOpenLoopRampRate(rate);
  }

  public void setCLRampRate(double rate) {
    fRightMotor.setClosedLoopRampRate(rate);
    bRightMotor.setClosedLoopRampRate(rate);
    fLeftMotor.setClosedLoopRampRate(rate);
    bLeftMotor.setClosedLoopRampRate(rate);
  }
  
  public void idleMode(IdleMode idleMode){
    //Idle Mode config
    fRightMotor.setIdleMode(idleMode);
    bRightMotor.setIdleMode(idleMode);
    fLeftMotor.setIdleMode(idleMode);
    bLeftMotor.setIdleMode(idleMode);
  }

  public void engagePTO() {
    solPTO.set(true);
  }

  public void disengagePTO() {
    solPTO.set(false);
  }

  //Actual Drive Method
  public void drive(double left, double right){
      diffDriveGroup.tankDrive(left, right);
  }

  //The drive method that passes the joystick values (Overloaded)
  public void drive(Joystick joy){
      drive(-joy.getRawAxis(3), -joy.getY());
  }

  //Arcade drive method
  public void driveArcade(Joystick joy){
		diffDriveGroup.arcadeDrive(-joy.getY(),-joy.getZ() * 0.8);
  }

  //Get velocity for any CANSparkMax in this subsys
  public double getVelocity(CANSparkMax motor){
    return motor.getEncoder().getVelocity();
  }

  //Get position for any CANSparkMax in this subsys
  public double getPosition(CANSparkMax motor){
    return -motor.getEncoder().getPosition();
  }

  //Resets position of the motors referenced for auton
  public void drivePosReset(){
    fLeftMotor.getEncoder().setPosition(0);
    fRightMotor.getEncoder().setPosition(0);
    bLeftMotor.getEncoder().setPosition(0);
    bRightMotor.getEncoder().setPosition(0);
  }

  public double getGyroAngle() {
    return gyro.getAngle();
  }

  public void resetGyro() {
    gyro.reset(); 
  }

  //Configures the maximum amp draw of the drive motors based on temperature of the motors
  //Linear correct in accordance to some motor law
  public void linearDrivingAmpControl(){
    double leftTemp = fLeftMotor.getMotorTemperature();
    double rightTemp = fRightMotor.getMotorTemperature();
    double currentTemp = Math.max(leftTemp, rightTemp);
    int linearCorrect = (-4 * (int)currentTemp) + 220;
  
    if (currentTemp < 80){
      motorCurrentConfig(60);
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

  public double leftDriveTicks(){
    return bLeftMotor.getEncoder().getPosition();
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
    return Constants.DRV_KP_SIMPLE * error;
  }

  //Linear ramp control for driving, accepts an upper and lower value
  private double linearRamp(double upperSpeed, double lowerSpeed){
    double diff = (driveSetpoint - (double)Math.abs(getPosition(fLeftMotor)));
    double corrected = .05 * diff;
    double upperBound = Math.min(upperSpeed , corrected);
    double lowerBound = Math.max(lowerSpeed , upperBound);
  
    return lowerBound;
  }

  //Executes the driving of the robot for auton
  public void driveToPos( double upperSpeed, double lowerSpeed){	
    double offset = getGainP(0,this.getGyroAngle(),Constants.DRV_KP_SIMPLE_STRAIT);
    double sign = Math.signum(driveSetpoint);

    diffDriveGroup.arcadeDrive(-linearRamp(upperSpeed,lowerSpeed) * sign, 0 + -offset);
  }

  public void newDriveTopos(double dist){
    double ticks = dist * Constants.DRV_TICKS_TO_INCH;
    driveRightPID.setReference(ticks, ControlType.kPosition);
    driveLeftPID.setReference(-ticks, ControlType.kPosition);
  }

  //Executes the turning of the robot for auton
  public void turn (double angle, double upperSpeed, double lowerSpeed){
    double corrected;
    double rotation = angle - getGyroAngle();
    double sign = Math.signum(rotation);      
    corrected = 0.05 * rotation;
          
    if (sign > 0) {
      corrected = Math.min(upperSpeed * sign, corrected);
      corrected = Math.max(lowerSpeed * sign, corrected);
    } 
    else {
      corrected = Math.max(upperSpeed * sign, corrected);
      corrected = Math.min(lowerSpeed * sign, corrected);                    
    }
    diffDriveGroup.arcadeDrive(0, -corrected);
  }
  
  //Stops driving---------------------
  public void driveStop(){
    diffDriveGroup.arcadeDrive(0, 0);
  }

  public void pause(){
    drive(0,0);
  }
  //----------------------------------

  //2020 Robot specific 
  //Actiates feet
  public void defenseMode() {
    engagePTO();
    drive(0, 0);
  }

  @Override
  public void setDefaultCommand(Command defaultCommand) {
    super.setDefaultCommand(defaultCommand);
  }

  public double getVoltage(CANSparkMax spark) {
    return spark.getBusVoltage();
  }

  public void setVoltage(CANSparkMax spark, double voltage) {
    spark.setVoltage(voltage);
  }

  public CANSparkMax[] getMotors() {
    CANSparkMax[] motors = {fRightMotor, bRightMotor, fLeftMotor, bLeftMotor};
    return motors;
  }

  public void setMotorSpeed(CANSparkMax spark, double speed) {
    spark.set(speed);
  }

  @Override
  public void periodic(){
    mode = RobotContainer.s_stateManager.getRobotState();
    Joystick Gamepad = new Joystick(0);
    linearDrivingAmpControl();
    /*
    if (Gamepad.getRawButton(12)) {
      driveArcade(Gamepad);
    } else if (isPTOEngaged){
      diffDriveGroup.arcadeDrive(-Math.abs(Gamepad.getY()), 0);
    }
    else{
      drive(Gamepad);
    }
    */
    //Climber Logic
    if (RobotContainer.s_stateManager.getRobotState() == "CLIMB_MODE") {
      RobotContainer.s_panel.extendCylinder();
      if (Gamepad.getRawButton(Constants.BTN_X)) {
        RobotContainer.s_climb.extendBottomCylinder();
        RobotContainer.s_climb.extendTopCylinder();
      }
      else if(Gamepad.getRawButton(Constants.BTN_Y)){
        RobotContainer.s_climb.retractBottomCylinder();
        RobotContainer.s_climb.retractTopCylinder();
      }
       else if (Gamepad.getRawButton(Constants.BTN_B)) {
        RobotContainer.s_climb.retractBottomCylinder();
        RobotContainer.s_climb.retractTopCylinder();
        engagePTO();
        isPTOEngaged = true;
      }
    } 
    //Logic to deploy feet for defense
    else if (Gamepad.getRawButton(Constants.LCLICK)) {
      defenseMode();
    } 
    else {
      disengagePTO();
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
