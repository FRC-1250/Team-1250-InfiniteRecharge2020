/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.auto_actions;

import java.io.FileWriter;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Sub_Drivetrain;
import frc.robot.subsystems.Sub_Recorder;

public class Cmd_StartAutoRecord extends CommandBase {
  /**
   * Creates a new Cmd_StartAutoRecord.
   */
  private final Sub_Recorder s_recorder;
  private final Sub_Drivetrain s_drive;
  private FileWriter file;
  private long startTime;
  private String filename;

  public Cmd_StartAutoRecord(Sub_Recorder recorder, Sub_Drivetrain drive) {
    addRequirements(recorder);
    s_recorder = recorder;
    s_drive = drive;
  }
  
  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    filename = s_recorder.getFilenameToMake();
    startTime = System.currentTimeMillis();
    file = s_recorder.makeFile(filename);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    s_recorder.record(file, s_drive.getMotors(), startTime);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    s_recorder.closeFile(file);
    s_recorder.updateFileChooserOptions(filename);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
