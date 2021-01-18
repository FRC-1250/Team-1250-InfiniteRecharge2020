/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.intake;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.subsystems.Sub_Intake;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// https://docs.wpilib.org/en/latest/docs/software/commandbased/convenience-features.html
public class CmdI_Collect extends InstantCommand {
  private final Sub_Intake s_intake;
  public CmdI_Collect(Sub_Intake intake) {
    s_intake = intake;
    addRequirements(intake);
    }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    s_intake.spinIntakeMotor(0.8);
    s_intake.extendCylinder();
  }
}
