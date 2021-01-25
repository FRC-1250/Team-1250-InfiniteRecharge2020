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
  public Cmd_PlayAutoRecord(Sub_Recorder recorder, Sub_Drivetrain drive) {
    addRequirements(recorder, drive);
    s_recorder = recorder;
    s_drive = drive;
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    // Working with files necessitates try/catches basically everywhere
    try {
      reader = new BufferedReader(new FileReader("filename.txt")); // TODO: give command a file from roboRIO
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    // https://www.journaldev.com/709/java-read-file-line-by-line
    try {
      line = reader.readLine();

      System.out.println(line); // TODO: shuffleboard alternative for println?
      // Line should look like "leftValue,rightValue"
      left = Double.parseDouble(line.split(",")[0]); // Splits line by comma and grabs the 0th item (which is leftValue)
      right = Double.parseDouble(line.split(",")[1]);

      s_drive.drive(left, right); // Since this command is executed every 20 ms, robot should drive based on values
    } catch (IOException e) {
      e.printStackTrace();
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
    return false;
  }
}
