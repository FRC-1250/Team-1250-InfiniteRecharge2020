/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.commands.panel.Cmd_SpinThrice;
import frc.robot.commands.panel.Cmd_StopOnColor;
import frc.robot.subsystems.Sub_Drivetrain;
import frc.robot.subsystems.Sub_Intake;
import frc.robot.subsystems.Sub_Limelight;
import frc.robot.subsystems.Sub_Panel;
import frc.robot.subsystems.Sub_Shooter;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

/**
 * This class is where the bulk of the robot should be declared.  Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls).  Instead, the structure of the robot
 * (including subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...
  private static final Sub_Drivetrain s_drivetrain = new Sub_Drivetrain();
  public static final Sub_Panel s_panel = new Sub_Panel();
  private static final Sub_Intake s_intake = new Sub_Intake();
  private static final Sub_Limelight s_limelight = new Sub_Limelight();
  private static final Sub_Shooter s_shooter = new Sub_Shooter();
  private static Joystick Gamepad = new Joystick(0);
  private static JoystickButton x = new JoystickButton(Gamepad, 1);
  private static JoystickButton a = new JoystickButton(Gamepad, 2);
  private static JoystickButton b = new JoystickButton(Gamepad, 3);
  private static JoystickButton y = new JoystickButton(Gamepad, 4);
  private static JoystickButton start = new JoystickButton(Gamepad, 10);
  private static JoystickButton leftClick = new JoystickButton(Gamepad, 11);
  private static JoystickButton rightClick = new JoystickButton(Gamepad, 12);
  static Command spinThrice = new Cmd_SpinThrice(s_panel);
  /**
   * The container for the robot.  Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    configureButtonBindings();        
  }

  public static void configurePanel() {
    JoystickButton x = new JoystickButton(Gamepad, 1);
    JoystickButton a = new JoystickButton(Gamepad, 2);
    JoystickButton b = new JoystickButton(Gamepad, 3);
    JoystickButton y = new JoystickButton(Gamepad, 4);
    if (s_panel.getDataFromField() == 'N') {
      if (leftClick.get()) {
        x.whenPressed(new Cmd_StopOnColor(s_panel, 'B'));
        a.whenPressed(new Cmd_StopOnColor(s_panel, 'G'));
        b.whenPressed(new Cmd_StopOnColor(s_panel, 'R'));
        y.whenPressed(new Cmd_StopOnColor(s_panel, 'Y'));
        SmartDashboard.putString("Mode", "STOP ON COLOR");
      } else {
        x.whenPressed(spinThrice);
        SmartDashboard.putString("Mode", "SPIN THRICE");
        // maybe instead, do if (x.get()) { execute spinthrice }
        // or x.and(a).whenActive(command, interruptible);
      }
    } else {
      x.whenPressed(new Cmd_StopOnColor(s_panel, s_panel.getDataFromField()));
      SmartDashboard.putString("Cmd", "data from field != 'N'");
    }
  }

  /**
   * Use this method to define your button->command mappings.  Buttons can be created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a
   * {@link edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {
    Joystick Gamepad = new Joystick(0);
    JoystickButton x = new JoystickButton(Gamepad, 1);
    JoystickButton b = new JoystickButton(Gamepad, 3);
  }


  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    return Cmd_SpinMotor();
  }

  private Command Cmd_SpinMotor() {
    return null;
  }
}
