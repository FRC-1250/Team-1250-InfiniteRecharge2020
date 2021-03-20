/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.auto_actions;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import com.revrobotics.CANSparkMax;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Sub_Drivetrain;
import frc.robot.subsystems.Sub_Recorder;

public class Cmd_PlayAutoRecord extends CommandBase {
  /**
   * Creates a new Cmd_PlayAutoRecord.
   */
  private final Sub_Drivetrain s_drive;
  private final Sub_Recorder s_recorder;

  private BufferedReader reader;
  private String line;
  private double[] motorVoltages = {0, 0, 0, 0};
  private CANSparkMax[] motors;

  private long startTime;
  private double nextMillis;
  private boolean onTime;
  private double t_delta;

  private String fullfile;

  public Cmd_PlayAutoRecord(Sub_Recorder recorder, Sub_Drivetrain drive) {
    addRequirements(recorder, drive);
    s_drive = drive;
    s_recorder = recorder;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    motors = s_drive.getMotors();
    fullfile = s_recorder.getDirPath() + s_recorder.getFilenameToPlay();

    onTime = true;
    startTime = System.currentTimeMillis();

    try {
      // Working with files necessitates try/catches basically everywhere
      reader = new BufferedReader(new FileReader(fullfile));
    } catch (Exception e) {
      end(true);
    }
  }

  // https://github.com/DennisMelamed/FRC-Play-Record-Macro/blob/master/FRC2220-Play-Record-Macro-DM/src/BTMacroPlay.java
  @Override
  public void execute() {
    if (onTime) {
      try {
        line = reader.readLine();
        if (line != null) {
          nextMillis = Double.parseDouble(line.split(",")[4]);
          for (int i = 0; i < motors.length; i++) {
            motorVoltages[i] = Double.parseDouble(line.split(",")[i]);
          }
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    
    t_delta = System.currentTimeMillis() - (startTime + nextMillis);

    for (int i = 0; i < motors.length; i++) {
      s_drive.setMotorSpeed(motors[i], motorVoltages[i]);
    }

    if (t_delta >= 0) {
      onTime = true;

    } else {
      onTime = false;
    }
  
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    s_recorder.setLastPlayed(s_recorder.getFilenameToPlay());
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
