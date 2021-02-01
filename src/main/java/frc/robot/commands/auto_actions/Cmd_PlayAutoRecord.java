/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.auto_actions;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Sub_Drivetrain;
import frc.robot.subsystems.Sub_Recorder;

public class Cmd_PlayAutoRecord extends CommandBase {
  /**
   * Creates a new Cmd_PlayAutoRecord.
   */
  private final Sub_Recorder s_recorder;
  private final Sub_Drivetrain s_drive;

  private BufferedReader reader;
  private String line;
  private double left, right;

  private long startTime;
  private double nextMillis;
  private boolean onTime;
  private double t_delta;

  public Cmd_PlayAutoRecord(Sub_Recorder recorder, Sub_Drivetrain drive) {
    addRequirements(recorder, drive);
    s_recorder = recorder;
    s_drive = drive;
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    onTime = true;
    startTime = System.currentTimeMillis();
    // Working with files necessitates try/catches basically everywhere
    try {
      reader = new BufferedReader(new FileReader("/home/lvuser/autonrecord.txt"));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }

  // https://github.com/DennisMelamed/FRC-Play-Record-Macro/blob/master/FRC2220-Play-Record-Macro-DM/src/BTMacroPlay.java
  @Override
  public void execute() {
    if (onTime) {
      try {
        line = reader.readLine();
        if (line != null) {
          nextMillis = Double.parseDouble(line.split(",")[2]);
          // Line should look like "leftValue,rightValue,ms"
        left = Double.parseDouble(line.split(",")[0]); // Splits line by comma and grabs the 0th item (which is leftValue)
        right = Double.parseDouble(line.split(",")[1]);
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    
    //t_delta = nextMillis - (System.currentTimeMillis() - startTime);
    t_delta = System.currentTimeMillis() - (startTime + nextMillis);

    s_drive.drive(left, right); // Since this command is executed every 20 ms, robot should drive based on values
    
    if (t_delta >= 0) {
      onTime = true;
      System.out.println("############ ONTIME IS TRUE " + "nextMillis: " + nextMillis + " t_delta: " + t_delta);

    } else {
      onTime = false;
      System.out.println("######################### ONTIME IS FALSE " + "nextMillis: " + nextMillis + " t_delta: " + t_delta);
    }
  
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    try {
      reader.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if (line != null) {
      return false;
    }
    return true;
  }
}
