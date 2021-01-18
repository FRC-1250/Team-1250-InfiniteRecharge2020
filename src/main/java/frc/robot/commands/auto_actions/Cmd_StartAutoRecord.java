/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.auto_actions;

import java.io.FileWriter;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Sub_Recorder;

public class Cmd_StartAutoRecord extends CommandBase {
  /**
   * Creates a new Cmd_StartAutoRecord.
   */
  private Joystick Gamepad = new Joystick(0);
  private final Sub_Recorder s_recorder;
  private FileWriter file;
  public Cmd_StartAutoRecord(Sub_Recorder recorder) {
    addRequirements(recorder);
    s_recorder = recorder;
    // Use addRequirements() here to declare subsystem dependencies.
  }
  
  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    file = s_recorder.makeFile();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    s_recorder.record(file, Gamepad);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    s_recorder.closeFile(file);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
