/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import java.util.Map;
import java.util.Vector;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.Constants;
import frc.robot.commands.shooter.Cmd_TurretGoHome;
import frc.robot.utilities.CAN_DeviceFaults;
import frc.robot.utilities.CAN_Input;

public class Sub_Shooter extends SubsystemBase implements CAN_Input {
  // Speed controllers created
  WPI_TalonSRX turretTalon = new WPI_TalonSRX(Constants.SHOOT_TURRET);
  CANSparkMax hoodNeo = new CANSparkMax(Constants.SHOOT_HOOD, MotorType.kBrushless);
  WPI_TalonFX flywheelFalconLeft = new WPI_TalonFX(Constants.SHOOT_FALCON_0);
  WPI_TalonFX flywheelFalconRight = new WPI_TalonFX(Constants.SHOOT_FALCON_1);

  public CANPIDController hoodPID = new CANPIDController(hoodNeo);

  Joystick Gamepad0 = new Joystick(0);
  Joystick Gamepad2 = new Joystick(2);
  
  double turretP = Constants.SHOOT_TURRET_P;
  double turretD = Constants.SHOOT_TURRET_D;
  PIDController turretPIDController = new PIDController(turretP, 0, turretD);

  public double turretCurrentPos;
  public double turretHome = Constants.SHOOT_TURRET_HOME;
  public double turretLeftStop = Constants.SHOOT_TURRET_LEFT_BOUND;
  public double turretRightStop = Constants.SHOOT_TURRET_RIGHT_BOUND;
  double turretDeadband = 50;

  // Used for limelight methods
  public NetworkTable table;
  NetworkTableEntry tableTx, tableTy, tableTv;
  double tx, ty, tv;

  // Shuffleboard
  ShuffleboardTab shooterTab = Shuffleboard.getTab("Shooter");
  ShuffleboardLayout llLayout = shooterTab.getLayout("Limelight", BuiltInLayouts.kList).withSize(4, 2).withPosition(0, 1);
  NetworkTableEntry turPos = shooterTab.add("Turret Position (ticks)", 0)
    .withWidget(BuiltInWidgets.kNumberBar)
    .withProperties(Map.of("min", turretLeftStop, "max", turretRightStop))
    .withSize(2, 1)
    .withPosition(0, 0)
    .getEntry();
  NetworkTableEntry distFromHome = shooterTab.add("Turret Distance from Home (ticks)", 0)
    .withSize(2, 1)
    .withPosition(2, 0)
    .getEntry();
  NetworkTableEntry hoodTemp = shooterTab.add("Hood Temp", 0)
    .withPosition(4, 0)
    .getEntry();
  NetworkTableEntry hoodTicks = shooterTab.add("Hood Ticks", 0)
    .withPosition(4, 1)
    .getEntry();
  NetworkTableEntry hoodCurrent = shooterTab.add("Hood Current", 0)
    .withPosition(4, 2)
    .getEntry();
  NetworkTableEntry shootRPM = shooterTab.add("Shooter RPM", 0)
    .withWidget(BuiltInWidgets.kGraph)
    .withPosition(5, 0)
    .getEntry();
  NetworkTableEntry distFromPort = shooterTab.add("Distance from Outer Port", 0)
    .withWidget(BuiltInWidgets.kNumberBar)
    .withProperties(Map.of("min", 12, "max", 629))
    .withSize(3, 1)
    .withPosition(7, 0)
    .getEntry();
  NetworkTableEntry xOffset = llLayout.add("X Offset Angle (degrees)", 0)
    .withWidget(BuiltInWidgets.kDial)
    .getEntry();
  NetworkTableEntry seeTarget = llLayout.add("Sees Target", "false").getEntry();
  NetworkTableEntry homeFound = shooterTab.add("Home Found", "false").getEntry();

  public ShuffleboardTab getTab() { return shooterTab; }
  //

  boolean wasHomeFound = false;
  int hoodCollisionAmps = 15;
  double interpolatedHoodPosition;

  double hoodP = 0.1;
  double hoodI = 0;
  double hoodD = 0;

  //TODO: Config pid for hood and pidf for wheel
  //pid for hood will be realllllllly slow (config max)
  public Sub_Shooter() {
   flywheelFalconRight.follow(flywheelFalconLeft);
   flywheelFalconRight.setInverted(InvertType.OpposeMaster);

   hoodPID.setP(hoodP);
   hoodPID.setI(hoodI);
   hoodPID.setD(hoodD);

   turretTalon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute);
   turretTalon.configFeedbackNotContinuous(true, 10);

   hoodNeo.setOpenLoopRampRate(0.4);


  }

  public void setShuffleboard() {
    turPos.setDouble(turretTalon.getSelectedSensorPosition());
    hoodTicks.setDouble(hoodNeo.getEncoder().getPosition());
    shootRPM.setDouble(flywheelFalconLeft.getSelectedSensorVelocity());
    distFromHome.setDouble(turretDistFromHome());
    seeTarget.setString(Boolean.toString(limelightSeesTarget()));
    xOffset.setDouble(tx);
    hoodTemp.setDouble(hoodNeo.getMotorTemperature());
    distFromPort.setDouble(getPortDist());
    hoodCurrent.setDouble(hoodNEOCurrentDraw());
    homeFound.setString(Boolean.toString(wasHomeFound));
  }

  // Diagnostic methods
  public void spinTurretMotor(double speed) {
    turretTalon.set(speed);
  }

  public void spinFlywheelMotors(double speed) {
    flywheelFalconLeft.set(speed);
  }

  public void spinHoodMotor(double speed) {
    hoodNeo.set(speed);
  }
  //

  public void updateLimelight() {
    table = NetworkTableInstance.getDefault().getTable("limelight");
    tableTx = table.getEntry("tx");
    tableTy = table.getEntry("ty");
    tableTv = table.getEntry("tv");
    tx = tableTx.getDouble(-1);
    ty = tableTy.getDouble(-1);
    tv = tableTv.getDouble(-1);
  }

  public void track() {
    if (limelightSeesTarget()) {
      double heading_error = -tx; // in order to change the target offset (in degrees), add it here
      // How much the limelight is looking away from the target (in degrees)

      double steering_adjust = turretPIDController.calculate(heading_error);
      // Returns the next output of the PID controller (where it thinks the turret should go)
      
      double xDiff = 0 - steering_adjust;
      double xCorrect = 0.05 * xDiff;
      turretTalon.set(xCorrect);
    } else {
      goHome();
    }
  }

  public void goHome() {
    if ((turretCurrentPos > turretHome) && (turretCurrentPos - turretHome > turretDeadband)) {
      // If you're to the right of the center, move left until you're within 50 ticks
      turretTalon.set(0.3);
    } else if ((turretCurrentPos < turretHome) && (turretCurrentPos - turretHome < -turretDeadband)) {
      turretTalon.set(-0.3);
    } else {
      turretTalon.set(0);
    }
  }

  public boolean limelightSeesTarget() {
    return tv == 1;
  }
  
  public double turretDistFromHome() {
    return Math.abs(turretCurrentPos - turretHome);
  }

  public double getPortDist() {
    return 60.25/(Math.tan(Math.toRadians(26.85) + Math.toRadians(ty)));
  }

  public void hoodNEOPercentControl(double percent){
    hoodNeo.set(percent);
  }

  public double hoodNEOCurrentDraw(){
    return hoodNeo.getOutputCurrent();
  }

  public void hoodNEOResetPos(){
    hoodNeo.getEncoder().setPosition(0);
  }

  public void hardStopConfiguration() {
    if (turretTalon.getSelectedSensorPosition() > turretRightStop) {
      turretTalon.configPeakOutputReverse(0, 10);
    } else {
      turretTalon.configPeakOutputReverse(-1, 10);
    }
    if (turretTalon.getSelectedSensorPosition() < turretLeftStop) {
      turretTalon.configPeakOutputForward(0, 10);
    } else {
      turretTalon.configPeakOutputForward(1, 10);
    }
  }

  public void hoodNEOGoHome() {
    //New Hood Stuff
    //Auto Home Detect TODO: Find the value for hoodCollisionAmps
    //TODO: Create lookup table for interpolatedHoodPosition
    if(!wasHomeFound) {
      if (hoodNEOCurrentDraw() < hoodCollisionAmps) {
        hoodNEOPercentControl(0.4);
      } else if (hoodNEOCurrentDraw() >= hoodCollisionAmps) {
        hoodNEOPercentControl(0);
        hoodNEOResetPos();
        wasHomeFound = true;
      }
    } else if (wasHomeFound) {
      // hoodPID.setReference(interpolatedHoodPosition, ControlType.kPosition);
      hoodNEOPercentControl(0);
    }
  }

  @Override
  public void periodic() {
    // Controls hood
    spinHoodMotor(Gamepad2.getThrottle() * 0.4);

    // Sets hood home
    if (Gamepad2.getRawButton(Constants.BTN_X)) {
      hoodNEOGoHome();
    } // Resets the home found variable (so that^ button can work again)
    if (Gamepad2.getRawButton(Constants.BTN_Y)) {
      wasHomeFound = false;
    }

    updateLimelight();

    hardStopConfiguration();
    setShuffleboard();

    turretCurrentPos = turretTalon.getSelectedSensorPosition();

    if (Gamepad0.getRawButton(Constants.SHOOT_MODE)) {
      track();
      spinFlywheelMotors(1);
    } else {
      spinFlywheelMotors(0);
      goHome();
    }
  }

  public Vector<CAN_DeviceFaults> input() {
    Vector<CAN_DeviceFaults> myCanDevices = new Vector<CAN_DeviceFaults>();
    myCanDevices.add(new CAN_DeviceFaults(turretTalon));
    myCanDevices.add(new CAN_DeviceFaults(hoodNeo));
    myCanDevices.add(new CAN_DeviceFaults(flywheelFalconLeft));
    myCanDevices.add(new CAN_DeviceFaults(flywheelFalconRight));
    return myCanDevices;
  }
}
