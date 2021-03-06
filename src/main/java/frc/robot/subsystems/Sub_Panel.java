/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import java.util.Map;
import java.util.Vector;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ColorMatch;
import com.revrobotics.ColorMatchResult;
import com.revrobotics.ColorSensorV3;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Robot;
import frc.robot.utilities.*;

public class Sub_Panel extends SubsystemBase implements CAN_Input {
  // Speed controllers created
  CANSparkMax panelMotor = new CANSparkMax(Constants.PANEL_MOTOR, MotorType.kBrushless);
  I2C.Port i2cPort = Constants.PANEL_SENSOR_PORT;
  Solenoid panelSol = new Solenoid(Constants.PANEL_SOL);

  ColorSensorV3 m_colorSensor = new ColorSensorV3(i2cPort);
  ColorMatch m_colorMatcher = new ColorMatch();
  ColorMatchResult match;

  private final Color kBlueTarget = ColorMatch.makeColor(0.18, 0.46, 0.35);
  private final Color kGreenTarget = ColorMatch.makeColor(0.21, 0.53, 0.25);
  private final Color kRedTarget = ColorMatch.makeColor(0.41, 0.40, 0.17);
  private final Color kYellowTarget = ColorMatch.makeColor(0.31, 0.56, 0.13);

  // Shuffleboard
  ShuffleboardTab panelTab = Shuffleboard.getTab("Panel");
  NetworkTableEntry proxim = panelTab.add("Proximity", 0)
    .withWidget(BuiltInWidgets.kNumberBar)
    .withProperties(Map.of("min", 90, "max", 2047))
    .withSize(2, 1)
    .withPosition(1, 0)
    .getEntry();
  NetworkTableEntry isProximGood = panelTab.add("isProximityGood", "false")
    .withPosition(0, 0)
    .getEntry();
  NetworkTableEntry curColor = panelTab.add("Cur Color", "U")
    .withPosition(3, 0)
    .getEntry();
  NetworkTableEntry halvRoundPanel = panelTab.add("Half Rnd Panel", 0)
    .withPosition(4, 0)
    .getEntry();

  public ShuffleboardTab getTab() { return panelTab; }
  //
  
  public Sub_Panel() {
    configureColors();
    panelMotor.setIdleMode(IdleMode.kBrake);
  }

  public void setShuffleboard() {
    proxim.setDouble(getProximity());
    isProximGood.setString(Boolean.toString(isProximityGood()));
    curColor.setString(Character.toString(getSensorColor()));
    halvRoundPanel.setDouble((double)Robot.halvesAroundPanel);
  }

  public void extendCylinder() {
    panelSol.set(true);
  }

  public void retractCylinders() {
    panelSol.set(false);
  }

  public double[] getRGBValues() {
    Color detectedColor = m_colorSensor.getColor();
    double[] rgb = {detectedColor.red, detectedColor.green, detectedColor.blue};
    return rgb;
  }

  public char getDataFromField() {
    String str_gameData;
    str_gameData = DriverStation.getInstance().getGameSpecificMessage();
    if(str_gameData.length() > 0)
    {
      return str_gameData.charAt(0);
    } else {
      //Code for no data received yet
      return 'N';
    }
  }

  public static int findIndex(char arr[], char t) {
    int len = arr.length; 
    int i = 0; 
    while (i < len) { 
      if (arr[i] == t) {
        return i;
      } 
      else {
        i++; 
      } 
    }
    return -1; 
  } 

  public void spinMotor(double speed) {
    panelMotor.set(speed);
  }

  public void stopMotor() {
    panelMotor.set(0);
  }

  public void configureColors() {
    m_colorMatcher.addColorMatch(kBlueTarget);
    m_colorMatcher.addColorMatch(kGreenTarget);
    m_colorMatcher.addColorMatch(kRedTarget);
    m_colorMatcher.addColorMatch(kYellowTarget);
  }

  public double getConfidence() {
    return match.confidence;
  }

  public char getSensorColor() {
    Color detectedColor = m_colorSensor.getColor();
    char colorChar;
    match = m_colorMatcher.matchClosestColor(detectedColor);

    if (match.color == kBlueTarget) {
      colorChar = 'B';
    } else if (match.color == kRedTarget) {
      colorChar = 'R';
    } else if (match.color == kGreenTarget) {
      colorChar = 'G';
    } else if (match.color == kYellowTarget) {
      colorChar = 'Y';
    } else {
      colorChar = 'U';
    }
    return colorChar;
  }

  public char getFieldColor() {
    char[] colors = {'B', 'G', 'R', 'Y'};
    char robot_sensor = getSensorColor();
    int color_index = findIndex(colors, robot_sensor) + 2;
    char real_color = colors[color_index % 4];
    return real_color;
  }

  public int getProximity() {
    return m_colorSensor.getProximity();
  }
  
  public boolean isProximityGood() {
    if ((getProximity() > 150) && (getProximity() < 500)) {
      return true;
    }
    return false;
  }

  public boolean stopOnColor(char color) { // parameter for if you want to stop on a specific color
    if (getProximity() > 140) {
      if (color == getSensorColor()) {
        return true;
      }
    }
    return false;
  }

  public int bestSpinDirection(char desiredColor) {
    char[] colors = {'Y', 'R', 'G', 'B'};
    int colorIndex, desiredColorIndex;
    if (isProximityGood()) {
      colorIndex = findIndex(colors, getSensorColor());
      desiredColorIndex = findIndex(colors, desiredColor);
      int indexDiff = colorIndex - desiredColorIndex;
      if ((indexDiff == -3) || (indexDiff == 1)) {
        return -1; // spin CCW
      }
    }
    return 1; // spin CW
  }

  public void periodic() {
    setShuffleboard();
  }

  public Vector<CAN_DeviceFaults> input() {
    Vector<CAN_DeviceFaults> myCanDevices = new Vector<CAN_DeviceFaults>();
    myCanDevices.add(new CAN_DeviceFaults(panelMotor));
    return myCanDevices;
  }
}
