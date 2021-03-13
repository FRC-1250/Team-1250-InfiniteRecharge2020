/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.ComplexWidget;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Robot;
import frc.robot.commands.auto_actions.Cmd_DoNothing;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.revrobotics.CANSparkMax;

public class Sub_Recorder extends SubsystemBase {
  /**
   * Creates a new Sub_Recorder.
   */

  private double[] motorVoltages = {0, 0, 0, 0};

  ShuffleboardTab recorderTab = Shuffleboard.getTab("Recorder");
  NetworkTableEntry toRecordFilename = recorderTab.add("File Name (To Record Next)", "untitled")
    .withPosition(0, 0).withSize(3, 1).getEntry();
  NetworkTableEntry lastPlayedFilename = recorderTab.add("File Name (Last Played)", "")
    .withPosition(0, 1).withSize(3, 1).getEntry();
  //ComplexWidget toPlayFilename = recorderTab.add("File Name (To Play Next)", Robot.fileChooser).withWidget(BuiltInWidgets.kComboBoxChooser)
  //  .withPosition(0, 2).withSize(3, 1);
  
  public Sub_Recorder() {
  }

  public FileWriter makeFile(String filename) {
    try {
      FileWriter file = new FileWriter(getDirPath() + filename);
      return file;
    } catch (IOException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    }
    return null;
  }
  
  // https://github.com/DennisMelamed/FRC-Play-Record-Macro/blob/master/FRC2220-Play-Record-Macro-DM/src/BTMacroRecord.java
  public void record(FileWriter file, CANSparkMax[] motors, long startTime) {
    for (int i = 0; i < motors.length; i++) {
      motorVoltages[i] = motors[i].get();
    }
    try {
      // Writes motor values and millisecond difference between current and recording-start time
      file.write(motorVoltages[0] + "," + motorVoltages[1] + "," + motorVoltages[2] + "," + motorVoltages[3] + "," + (System.currentTimeMillis()-startTime) + "\n");
    } catch (IOException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    }
  }

  public void closeFile(FileWriter file) {
    try {
      file.close();
    } catch (IOException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    }
  }

  // Used to create file in StartAutoRecord
  public String getFilenameToMake() {
    return toRecordFilename.getString("untitled");
  }

  // Used to play back file in PlayAutoRecord; if the "toPlay" filename is left blank in Shuffleboard, the last recorded file is played
  // public String getFilenameToPlay() {
  //   return Robot.fileChooser.getSelected();
  //   //return "test0";
  // }

  // Updates the "File Name (Last Played)" on Shuffleboard
  public void setLastPlayed(String filename) {
    lastPlayedFilename.setString(filename);
  }

  public String getDirPath() {
    return "/home/lvuser/auton_record/";
  }

  // // Happens on startup; adds pre-existing files to Shuffleboard chooser
  // public void addFileChooserOptions() {
  //   File folder = new File(getDirPath());
  //   String[] listOfFiles = folder.list();
  //   for (String file : listOfFiles) {

  //     Robot.fileChooser.addOption(file, file);
  //   }
  // }

  // // Called when StartAutoRecord ends; adds the newly recorded file as a Shuffleboard option
  // public void updateFileChooserOptions(String filename) {
  //   Robot.fileChooser.addOption(filename, filename);

  //   // If the user doesn't select a dropdown option, this should by default play the last recorded file
  //   Robot.fileChooser.setDefaultOption("", filename);
  // }

  @Override
  public void periodic() {
    
  }
}
