/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import java.util.Map;
import java.util.Vector;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import frc.robot.Robot;


import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.RobotContainer;
import frc.robot.utilities.CAN_DeviceFaults;
import frc.robot.utilities.CAN_Input;

public class Sub_Shooter extends SubsystemBase implements CAN_Input {
  // Speed controllers created
  WPI_TalonSRX turretTalon = new WPI_TalonSRX(Constants.SHOOT_TURRET);
  CANSparkMax hoodNeo = new CANSparkMax(Constants.SHOOT_HOOD, MotorType.kBrushless);
  WPI_TalonFX flywheelFalconLeft = new WPI_TalonFX(Constants.SHOOT_FALCON_0);
  WPI_TalonFX flywheelFalconRight = new WPI_TalonFX(Constants.SHOOT_FALCON_1);

  public CANPIDController hoodPID = new CANPIDController(hoodNeo);

  Joystick Gamepad0 = new Joystick(0); // LOGITECH CONTROLLER
  Joystick Gamepad1 = new Joystick(1); // BUTTON BOARD
  Joystick Gamepad2 = new Joystick(2); // DEV
  
  double turretP = Constants.SHOOT_TURRET_P;
  double turretD = Constants.SHOOT_TURRET_D;
  PIDController turretPIDController = new PIDController(turretP, 0, turretD);

  public double turretCurrentPos;
  public double turretHome = Constants.SHOOT_TURRET_HOME;
  public double turretLeftStop = Constants.SHOOT_TURRET_LEFT_BOUND;
  public double turretRightStop = Constants.SHOOT_TURRET_RIGHT_BOUND;

  //Bools for hardstop config
  boolean goLeft = true;
  boolean goRight = true;

  //Bool that determines whether robot is ready to fire
  public boolean readyToFire;

  //Limelight data, used for Limelight methods
  public NetworkTable table;
  NetworkTableEntry tableTx, tableTy, tableTv;
  double tx, ty, tv;

  // Shuffleboard position and data config
  ShuffleboardTab shooterTab = Shuffleboard.getTab("Shooter");
  NetworkTableEntry turPos = shooterTab.add("Turret Position (ticks)", 0)
    .withWidget(BuiltInWidgets.kNumberBar)
    .withProperties(Map.of("min", turretLeftStop, "max", turretRightStop))
    .withSize(2, 1)
    .withPosition(0, 0).getEntry();
  NetworkTableEntry distFromHome = shooterTab.add("Turret Distance from Home (ticks)", 0)
    .withSize(2, 1)
    .withPosition(2, 0).getEntry();
  NetworkTableEntry hoodTemp = shooterTab.add("Hood Temp", 0)
    .withPosition(4, 0).getEntry();
  NetworkTableEntry hoodTicks = shooterTab.add("Hood Ticks", 0)
    .withPosition(4, 2).getEntry();
  NetworkTableEntry hoodCurrent = shooterTab.add("Hood Current", 0)
    .withPosition(4, 1).getEntry();
  NetworkTableEntry shootRPM = shooterTab.add("Shooter RPM", 0)
    .withWidget(BuiltInWidgets.kGraph)
    .withPosition(5, 0).getEntry();
  NetworkTableEntry distFromPort = shooterTab.add("Distance from Outer Port", 0)
    .withWidget(BuiltInWidgets.kNumberBar)
    .withProperties(Map.of("min", 12, "max", 629))
    .withSize(3, 1)
    .withPosition(5, 2).getEntry();
  NetworkTableEntry homeFound = shooterTab.add("Home Found", "false")
    .withPosition(8, 1).getEntry();
  NetworkTableEntry turretSpeed = shooterTab.add("Turrent Percent", -1)
    .withPosition(8, 2).getEntry();
    NetworkTableEntry wantedDistane = shooterTab.add("WantedDis", 0)
    .withPosition(8, 3).getEntry();
  
  public ShuffleboardTab getTab() {
    return shooterTab;
  }

  //Hood Neo control
  boolean wasHomeFound = false; //Starts robot in a "no home found" state
  int hoodCollisionAmps = 22; //x amps to determine when a collision or home is hit
  double interpolatedHoodPosition; //Deprecated for now

  //Hood neo PID values
  double hoodP = Constants.SHOOT_HOOD_P;
  double hoodI = Constants.SHOOT_HOOD_I;
  double hoodD = Constants.SHOOT_HOOD_D;

  //Flywheel PIDF values
  double flywheelP = Constants.SHOOT_FLYWHEEL_P;
  double flywheelI = Constants.SHOOT_FLYWHEEL_I;
  double flywheelD = Constants.SHOOT_FLYWHEEL_D;
  double flywheelF = Constants.SHOOT_FLYWHEEL_F;

  public Sub_Shooter() {
   //Sets the right falcon to follow the opposite of that the left falcon is doing
   //Right = follower Left = leader
   flywheelFalconRight.follow(flywheelFalconLeft);
   flywheelFalconRight.setInverted(InvertType.OpposeMaster);

   //Stops flywheels from ever back driving
   flywheelFalconLeft.configPeakOutputReverse(0);
   flywheelFalconRight.configPeakOutputReverse(0);

   //Configures PID for hood position control
   hoodPID.setP(hoodP);
   hoodPID.setI(hoodI);
   hoodPID.setD(hoodD);

   //Configures PIDF for flywheel velocity control
   //F VALUE WILL WORK FOR ALL FALCON 500s
   flywheelFalconLeft.config_kP(0, flywheelP);
   flywheelFalconLeft.config_kI(0, flywheelI);
   flywheelFalconLeft.config_kD(0, flywheelD);
   flywheelFalconLeft.config_kF(0, flywheelF);

   //Configures the turret encoder to absolute (Armabot Turret 240)
   turretTalon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute);
   turretTalon.configFeedbackNotContinuous(true, 10); // Important for absolute encoders not to jump ticks randomly

   //Ramp rate to make sure hood neo finds home cleanly
   hoodNeo.setOpenLoopRampRate(0.4);
  }
  
  //Shuffleboard value update method
  public void setShuffleboard() {
    turPos.setDouble(turretTalon.getSelectedSensorPosition());
    hoodTicks.setDouble(hoodNeo.getEncoder().getPosition());
    shootRPM.setDouble(flywheelFalconLeft.getSelectedSensorVelocity());
    distFromHome.setDouble(turretDistFromHome());
    wantedDistane.setDouble(amazingQuadRegression());
    hoodTemp.setDouble(hoodNeo.getMotorTemperature());
    distFromPort.setDouble(getPortDist());
    hoodCurrent.setDouble(hoodNEOCurrentDraw());
    homeFound.setString(Boolean.toString(wasHomeFound));
    turretSpeed.setDouble(turretTalon.getMotorOutputPercent());
  }

  // Basic methods
  public void spinTurretMotor(double speed) {
    if (goLeft && speed < 0) {
      turretTalon.set(speed);
    } else if (goRight && speed > 0) {
      turretTalon.set(speed);
    } else {
      turretTalon.set(0);
    }
  }

  //Returns flywheel speed in ticks/100ms
  public int getFlyWheelSpeed() {
    return flywheelFalconLeft.getSelectedSensorVelocity();
  }

  //Percent control of flywheel motors
  public void spinFlywheelMotors(double speed) {
    flywheelFalconLeft.set(speed);
  }

  //Percent control of hood neo
  public void spinHoodMotor(double speed) {
    hoodNeo.set(speed);
  }
  //Velocity control of flywheel
  public void setFlywheelVelocityControl(double rpm) {
    flywheelFalconLeft.set(ControlMode.Velocity, rpm);
  }

  //Reurns position of the turret in ticks
  public double getTurretTicks() {
    return turretTalon.getSelectedSensorPosition();
  }

  //Position control method of hood neo
  public void hoodGoToPos(double ticks){
    hoodPID.setReference(ticks, ControlType.kPosition);
  }

  //Returns hood postion in ticks (Raw rotations of neo)
  public double hoodPos(){
    return hoodNeo.getEncoder().getPosition();
  }

  // Void to updates the limelight values
  public void updateLimelight() {
    table = NetworkTableInstance.getDefault().getTable("limelight");
    tableTx = table.getEntry("tx");
    tableTy = table.getEntry("ty");
    tableTv = table.getEntry("tv");
    tx = tableTx.getDouble(-1);
    ty = tableTy.getDouble(-1);
    tv = tableTv.getDouble(-1);
  }

  //Tracking method that points the turret towards the target, using a PID controller
  public void track() {
    if (limelightSeesTarget()) {
      double heading_error = -tx + 0; // in order to change the target offset (in degrees), add it here
      // How much the limelight is looking away from the target (in degrees)

      double steering_adjust = turretPIDController.calculate(heading_error);
      // Returns the next output of the PID controller (where it thinks the turret should go)
      
      double xDiff = 0 - steering_adjust;
      double xCorrect = 0.05 * xDiff;
      spinTurretMotor(xCorrect);
    } else {
      goHome();
    }
  }

  //Simple position control to send the turret home
  public void goHome() {
    if ((turretCurrentPos > turretHome) && (turretCurrentPos - turretHome > 50)) {
      // If you're to the right of the center, move left until you're within 50 ticks (turret deadband)
      spinTurretMotor(0.3);
    } else if ((turretCurrentPos < turretHome) && (turretCurrentPos - turretHome < -50)) {
      // If you're to the left of the center, move right until you're within 50 ticks
      spinTurretMotor(-0.3);
    } else {
      spinTurretMotor(0);
    }
  }

  //Based on tv from the limelight, returns a bool of whether a target is seen
  public boolean limelightSeesTarget() {
    return tv == 1;
  }

  //Returns a string that determines whether or not a target is seen
  public String isTarget() {
    if (limelightSeesTarget()) {
      return "SEES TARGET";
    }
    return "NO TARGET";
  }
  
  //Returns how far the turret is from it's home position
  public double turretDistFromHome() {
    return Math.abs(turretCurrentPos - turretHome);
  }

  //Converts ty of limelight to return distance to the outer port in inches
  public double getPortDist() {
    return 60.25/(Math.tan(Math.toRadians(26.85) + Math.toRadians(ty)));
  }

  //Manual percent control of the hood neo
  public void hoodNEOPercentControl(double percent){
    hoodNeo.set(percent);
  }

  //Returns current draw of the hood neo
  public double hoodNEOCurrentDraw(){
    return hoodNeo.getOutputCurrent();
  }

  //Resets the bool that determines whether or not home was found
  public void resetHomeHood(){
   wasHomeFound = false;
  }

  //Sets the tick count of the internal encoder of the neo to 0
  //0 = home
  public void hoodNEOResetPos(){
    hoodNeo.getEncoder().setPosition(0);
  }

  //Determines based on boundries whether or not the turret is allowed to turn to the left or right
  public void hardStopConfiguration() {
    if (turretTalon.getSelectedSensorPosition() > turretRightStop) {
      // turretTalon.configPeakOutputReverse(0, 10);
      goRight = false;
    } else {
      // turretTalon.configPeakOutputReverse(-1, 10);
      goRight = true;
    }
    if (turretTalon.getSelectedSensorPosition() < turretLeftStop) {
      // turretTalon.configPeakOutputForward(0, 10);
      goLeft = false;
    } else {
      // turretTalon.configPeakOutputForward(1, 10);
      goLeft = true;
    }
  }

  //Finds home of the hood by colliding the hood with the end of the track
  //Looks at amount of amps drawn. When amps spike, home is assumed to be at that position
  public void hoodNEOGoHome() {
    if(!wasHomeFound) {
      if (hoodNEOCurrentDraw() < hoodCollisionAmps) {
        hoodNEOPercentControl(0.35);
      } else if (hoodNEOCurrentDraw() >= hoodCollisionAmps) {
        hoodNEOPercentControl(0);
        hoodNEOResetPos();
        wasHomeFound = true;
      }
    } else if (wasHomeFound) {
      hoodNeo.set(Gamepad1.getX() * 0.2);
    }
  }

  //Test method to configure rumble
  public void rumble(double intensity) {
    Gamepad0.setRumble(RumbleType.kLeftRumble, intensity);
    Gamepad0.setRumble(RumbleType.kRightRumble, intensity);
  }

  //Quad equation to determin hood position 
  //Deprecated
  public double amazingQuadRegression(){
    return((0.00575313 * Math.pow(getPortDist(), 2)) - (1.65056 * getPortDist()) + 39.357);
  }

  //Method to set position to ticks determined by the quad equation
  //Deprecated
  public void hoodGoToCorrectPos() {
    hoodGoToPos(amazingQuadRegression());
  }

  @Override
  public void periodic() {
    //Periodic methods that are always needed for shooter to work-----------------------------
    hoodNEOGoHome();
    updateLimelight();
    hardStopConfiguration();
    setShuffleboard();
    turretCurrentPos = turretTalon.getSelectedSensorPosition();
    NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").setNumber(3);
    String mode = RobotContainer.s_stateManager.getRobotState();
    //----------------------------------------------------------------------------------------

    //Sets the bool readyToFire to true or false
    //Determined by RPM and whether or not a ball is loaded
    // if(getFlyWheelSpeed() > 19000 && RobotContainer.s_hopper.getSensor()){
    //   readyToFire = true;
    // }
    // else{
    //   readyToFire = false;
    // }

    //only works during teleop
    if (!Robot.isItAuto){
      // Resets the home found variable (so that button can work again)
      if (!Gamepad1.getRawButton(6)) {
       wasHomeFound = false;
      }
      //When the shoot mode button is pressed
      // if (mode == "SHOOT_MODE") {
      //   setFlywheelVelocityControl(20000);
      //   //track();
      //   hoodGoToPos(-68);
      //   System.out.println("SHOOTER CURRENT DRAW =" + flywheelFalconLeft.getSupplyCurrent());
      // } 
      // //When shootmode button is not pressed
      // else {
      //   spinFlywheelMotors(0);
      //   goHome();
      //   if (wasHomeFound){
      //   hoodGoToPos(-5);
      //   }
      // }
    }
  }

  //Adding CAN devices for diagnostic LEDs
  public Vector<CAN_DeviceFaults> input() {
    Vector<CAN_DeviceFaults> myCanDevices = new Vector<CAN_DeviceFaults>();
    myCanDevices.add(new CAN_DeviceFaults(turretTalon));
    myCanDevices.add(new CAN_DeviceFaults(hoodNeo));
    myCanDevices.add(new CAN_DeviceFaults(flywheelFalconLeft));
    myCanDevices.add(new CAN_DeviceFaults(flywheelFalconRight));
    return myCanDevices;
  }
}
