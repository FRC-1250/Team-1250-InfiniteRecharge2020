/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.ColorMatch;
import com.revrobotics.ColorMatchResult;
import com.revrobotics.ColorSensorV3;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Sub_Panel extends SubsystemBase {
  /**
   * Creates a new Sub_Panel.
   */
  CANSparkMax spark = new CANSparkMax(Constants.PANEL_MOTOR, MotorType.kBrushless);
    final I2C.Port i2cPort = I2C.Port.kOnboard;
    
    ColorSensorV3 m_colorSensor = new ColorSensorV3(i2cPort);
    ColorMatch m_colorMatcher = new ColorMatch();

    Color kBlueTarget = ColorMatch.makeColor(0.143, 0.427, 0.429);
    Color kGreenTarget = ColorMatch.makeColor(0.197, 0.561, 0.240);
    Color kRedTarget = ColorMatch.makeColor(0.561, 0.232, 0.114);
    Color kYellowTarget = ColorMatch.makeColor(0.361, 0.524, 0.113);

    // Our colors vvv
    /*
    // Without LED
    private final Color kBlueTarget = ColorMatch.makeColor(0.18, 0.47, 0.34);
    private final Color kGreenTarget = ColorMatch.makeColor(0.23, 0.57, 0.19);
    private final Color kRedTarget = ColorMatch.makeColor(0.59, 0.08, 0.32);
    private final Color kYellowTarget = ColorMatch.makeColor(0.40, 0.51, 0.08);
  */
  /*
    // With LED
    private final Color kBlueTarget = ColorMatch.makeColor(0.17, 0.45, 0.34);
    private final Color kGreenTarget = ColorMatch.makeColor(0.21, 0.55, 0.24);
    private final Color kRedTarget = ColorMatch.makeColor(0.42, 0.39, 0.16);
    private final Color kYellowTarget = ColorMatch.makeColor(0.33, 0.53, 0.13);

    */
  public Sub_Panel() {    
    configureColors();
  }

  public void spinMotor() {
    spark.set(0.1);
  }

  public void stopMotor() {
    spark.set(0);
  }

  public void configureColors() {
    m_colorMatcher.addColorMatch(kBlueTarget);
    m_colorMatcher.addColorMatch(kGreenTarget);
    m_colorMatcher.addColorMatch(kRedTarget);
    m_colorMatcher.addColorMatch(kYellowTarget);
  }
  
  public void senseColors() {

    final Color detectedColor = m_colorSensor.getColor();

    String colorString;
    final ColorMatchResult match = m_colorMatcher.matchClosestColor(detectedColor);

    if (match.color == kBlueTarget) {
      colorString = "Blue";
    } else if (match.color == kRedTarget) {
      colorString = "Red";
    } else if (match.color == kGreenTarget) {
      colorString = "Green";
    } else if (match.color == kYellowTarget) {
      colorString = "Yellow";
    } else {
      colorString = "Unknown";
    }

    /**
     * Open Smart Dashboard or Shuffleboard to see the color detected by the 
     * sensor.
     */
    
    SmartDashboard.putNumber("Confidence", match.confidence);
    SmartDashboard.putString("Detected Color", colorString);

    /**
     * In addition to RGB IR values, the color sensor can also return an 
     * infrared proximity value. The chip contains an IR led which will emit
     * IR pulses and measure the intensity of the return. When an object is 
     * close the value of the proximity will be large (max 2047 with default
     * settings) and will approach zero when the object is far away.
     * 
     * Proximity can be used to roughly approximate the distance of an object
     * or provide a threshold for when an object is close enough to provide
     * accurate color values.
     */
    final int proximity = m_colorSensor.getProximity();

    SmartDashboard.putNumber("Proximity", proximity);

  }

  public String getSensorColor() {
    final Color detectedColor = m_colorSensor.getColor();

    String colorString;
    final ColorMatchResult match = m_colorMatcher.matchClosestColor(detectedColor);

    if (match.color == kBlueTarget) {
      colorString = "Blue";
    } else if (match.color == kRedTarget) {
      colorString = "Red";
    } else if (match.color == kGreenTarget) {
      colorString = "Green";
    } else if (match.color == kYellowTarget) {
      colorString = "Yellow";
    } else {
      colorString = "Unknown";
    }
    return colorString;
  }

  /* TODO: make this
  public Color getFieldColor() {
  }
  */

  public boolean stopOnColor(String stopColor) {
    String color = getSensorColor();
    if (color == stopColor) {
      return true;
    }
    return false;
  }


  public void periodic() {
    // This method will be called once per scheduler run
    senseColors();
  }
}