/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Sub_Recorder extends SubsystemBase {
  /**
   * Creates a new Sub_Recorder.
   */
  public Sub_Recorder() {

  }

  public FileWriter makeFile() {
    LocalDateTime date = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    String formattedDate = date.format(formatter);
    try {
      FileWriter file = new FileWriter("Recording-" + formattedDate + ".txt");
      return file;
    } catch (IOException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    }
    return null;
  }
  
  public void record(FileWriter file, Joystick joy) {
    double lValue = -joy.getRawAxis(3);
    double rValue = -joy.getY();
    try {
      file.write(lValue + "," + rValue + "\n");
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
