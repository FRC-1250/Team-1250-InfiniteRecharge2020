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
import frc.robot.commands.panel.Cmd_SpinThrice;
import frc.robot.commands.panel.Cmd_StopOnColor;
import frc.robot.commands.intake.Cmd_SpinMotor;
import frc.robot.subsystems.Sub_Climber;
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
  // Subsystems
  private static final Sub_Drivetrain s_drivetrain = new Sub_Drivetrain();
  public static final Sub_Panel s_panel = new Sub_Panel();
  private static final Sub_Intake s_intake = new Sub_Intake();
  private static final Sub_Limelight s_limelight = new Sub_Limelight();
  private static final Sub_Shooter s_shooter = new Sub_Shooter();
  private static final Sub_Climber s_climb = new Sub_Climber();

  // Buttons
  private static Joystick Gamepad = new Joystick(0);
  private static JoystickButton x = new JoystickButton(Gamepad, 1);
  private static JoystickButton a = new JoystickButton(Gamepad, 2);
  private static JoystickButton b = new JoystickButton(Gamepad, 3);
  private static JoystickButton y = new JoystickButton(Gamepad, 4);
  private static JoystickButton rb = new JoystickButton(Gamepad, 6);
  private static JoystickButton panelMode = new JoystickButton(Gamepad, Constants.PANEL_MODE); // start button
  private static JoystickButton shootMode = new JoystickButton(Gamepad, Constants.SHOOT_MODE); // back button
  private static JoystickButton climbMode = new JoystickButton(Gamepad, Constants.CLIMB_MODE); // lt button
  /**
   * The container for the robot.  Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    configureButtonBindings();        
  }

  public static void configurePanel() {
    panelMode.and(x).whenActive(new Cmd_StopOnColor(s_panel, 'B'));
    panelMode.and(a).whenActive(new Cmd_StopOnColor(s_panel, 'G'));
    panelMode.and(b).whenActive(new Cmd_StopOnColor(s_panel, 'R'));
    panelMode.and(y).whenActive(new Cmd_StopOnColor(s_panel, 'Y'));
    panelMode.and(rb).and(x).whenActive(new Cmd_SpinThrice(s_panel), true);
    panelMode.and(rb).and(y).whenActive(new Cmd_StopOnColor(s_panel, s_panel.getDataFromField()));
  }

  public static void configureCollector() {
    // default buttons
    x.whileHeld(new Cmd_SpinMotor(s_intake));
  }

  public static void configureShooter() {
    // ex: shootMode.and(x).whenActive(new Cmd_Shoot());
  }

  public static void configureClimber() {
    // ex: climbMode.and(x).whenActive(new Cmd_Climb());
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
