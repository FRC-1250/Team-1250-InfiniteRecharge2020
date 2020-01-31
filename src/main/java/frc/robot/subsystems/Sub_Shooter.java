/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Sub_Shooter extends SubsystemBase {
  //Speed Controllers created
  WPI_TalonSRX turretTalon = new WPI_TalonSRX(Constants.SHOOT_TURRET);
  CANSparkMax hoodNeo = new CANSparkMax(Constants.SHOOT_HOOD, MotorType.kBrushless);
  WPI_TalonFX flywheelFalconLeft = new WPI_TalonFX(Constants.SHOOT_FALCON_0);
  WPI_TalonFX flywheelFalconRight = new WPI_TalonFX(Constants.SHOOT_FALCON_1);

  Joystick Gamepad0 = new Joystick(0);
  Joystick Gamepad1 = new Joystick(1);

  double turretP = Constants.SHOOT_TURRET_P;
  double turretD = Constants.SHOOT_TURRET_D;

  public Sub_Shooter() {
   flywheelFalconRight.follow(flywheelFalconLeft);
   flywheelFalconLeft.setInverted(InvertType.OpposeMaster);
  }

  //TODO:
  //Config pid for hood and pidf for wheel
  //pid for hood will be realllllllly slow (config max)


  public double degToRad(double deg) {
    return deg * Math.PI / 180;
  }

  @Override
  public void periodic() {
    PIDController turretPIDController = new PIDController(turretP, 0, turretD);

    double turretCurrentPos = turretTalon.getSelectedSensorPosition();
    SmartDashboard.putNumber("Turret Position", turretCurrentPos);

    NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");
    NetworkTableEntry tx = table.getEntry("tx");
    NetworkTableEntry ty = table.getEntry("ty");
    NetworkTableEntry tv = table.getEntry("tv");

    if (Gamepad1.getRawButton(2)) {
      table.getEntry("ledMode").setNumber(1);
    }
    else {
      table.getEntry("ledMode").setNumber(3);
    }

    double x = tx.getDouble(0.0);
    double y = ty.getDouble(0);
    double v = tv.getDouble(0.0);

    SmartDashboard.putNumber("tx", x);
    SmartDashboard.putNumber("tv", v);

    double turretHome = Constants.SHOOT_TURRET_HOME;
    double turretLeftStop = Constants.SHOOT_TURRET_LEFT_BOUND;
    double turretRightStop = Constants.SHOOT_TURRET_RIGHT_BOUND;

    double distance = 60.25/(Math.tan(degToRad(26.85) + degToRad(y)));
    SmartDashboard.putNumber("Distance From Outer Port", distance);


    if (v == 1) // If you see a target
    {
      if (!Gamepad1.getRawButton(1)) // If x isn't pressed
      {
        double heading_error = -x; // in order to change the target offset (in degrees), add it here
        // How much the limelight is looking away from the target (in degrees)

        double steering_adjust = turretPIDController.calculate(heading_error);
        // Returns the next output of the PID controller (where it thinks the turret should go)
        
        double xDiff = 0 - steering_adjust;
        double xCorrect = 0.05 * xDiff;
        turretTalon.set(xCorrect);
        SmartDashboard.putNumber("xCorrect", xCorrect);
      }
    } else if ((v == 0) && (!Gamepad1.getRawButton(1))) // If you do see a target
    {
      if ((turretCurrentPos > turretHome) && (turretCurrentPos - turretHome > 50)) 
      {
        // If you're to the right of the center, move left until you're within 50 ticks
        turretTalon.set(0.3);
      } else if ((turretCurrentPos < turretHome) && (turretCurrentPos - turretHome < -50)) 
      {
        turretTalon.set(-0.3);
      }
    }

    // Hard stop configurations
    if (turretTalon.getSelectedSensorPosition() > turretRightStop)
    {
      turretTalon.configPeakOutputReverse(0, 10);
    } else
    {
      turretTalon.configPeakOutputReverse(-1, 10);
    }
    if (turretTalon.getSelectedSensorPosition() < turretLeftStop)
    {
      turretTalon.configPeakOutputForward(0, 10);
    } else
    {
      turretTalon.configPeakOutputForward(1, 10);
    }

    turretPIDController.close();
    
  }
}
