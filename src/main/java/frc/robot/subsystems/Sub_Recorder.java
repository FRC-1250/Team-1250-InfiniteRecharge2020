/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import java.io.FileWriter;
import java.io.IOException;

public class Sub_Recorder extends SubsystemBase {
  /**
   * Creates a new Sub_Recorder.
   */
  public Sub_Recorder() {

  }

  public FileWriter makeFile() {
    try {
      FileWriter file = new FileWriter("/home/lvuser/autonrecord.txt");
      return file;
    } catch (IOException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    }
    return null;
  }
  
  // https://github.com/DennisMelamed/FRC-Play-Record-Macro/blob/master/FRC2220-Play-Record-Macro-DM/src/BTMacroRecord.java
  public void record(FileWriter file, Joystick joy, long startTime) {
    double lValue = -joy.getRawAxis(3);
    double rValue = -joy.getY();
    try {
      // Writes motor values and millisecond difference between current and recording-start time
      file.write(lValue + "," + rValue + "," + (System.currentTimeMillis()-startTime) + "\n");
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

  @Override
  public void periodic() {
    // This method will be called once per scheduler run

  }
}
