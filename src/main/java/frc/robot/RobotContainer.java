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
import frc.robot.commands.panel.Cmd_DeployCylinder;
import frc.robot.commands.panel.Cmd_SpinThrice;
import frc.robot.commands.panel.Cmd_StopOnColor;
import frc.robot.commands.shooter.Cmd_SpinFlywheels;
import frc.robot.commands.intake.Cmd_Collect;
import frc.robot.commands.intake.Cmd_StopCollect;
import frc.robot.commands.intake.Cmd_UnjamIntake;
import frc.robot.subsystems.Sub_CAN;
import frc.robot.subsystems.Sub_Climber;
import frc.robot.subsystems.Sub_Drivetrain;
import frc.robot.subsystems.Sub_Hopper;
import frc.robot.subsystems.Sub_Intake;
import frc.robot.subsystems.Sub_Panel;
import frc.robot.subsystems.Sub_Shooter;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;

/**
 * This class is where the bulk of the robot should be declared.  Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls).  Instead, the structure of the robot
 * (including subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...
  // Subsystems
  public static final Sub_Drivetrain s_drivetrain = new Sub_Drivetrain();
  public static final Sub_Panel s_panel = new Sub_Panel();
  public static final Sub_Intake s_intake = new Sub_Intake();
  public static final Sub_Shooter s_shooter = new Sub_Shooter();
  public static final Sub_Climber s_climb = new Sub_Climber();
  public static final Sub_Hopper s_hopper = new Sub_Hopper();
  public static final Sub_CAN s_can = new Sub_CAN();

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
  private static JoystickButton collectMode = new JoystickButton(Gamepad, Constants.COLLECT_MODE);
  /**
   * The container for the robot.  Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    configureButtonBindings();        
  }

  // Configure modes
  // TODO: add LEDs to indicate what mode the robot is in?
  // ^some sign so software can know the code is being reached and operator knows buttons are ready to be pressed

  public static void configurePanel() {
    x.whenActive(new Cmd_StopOnColor(s_panel, 'B'));
    x.whenActive(new Cmd_StopOnColor(s_panel, 'G'));
    x.whenActive(new Cmd_StopOnColor(s_panel, 'R'));
    x.whenActive(new Cmd_StopOnColor(s_panel, 'Y'));
  }

  public static void configureCollector() {
    x.whenPressed(new Cmd_Collect(s_intake, s_hopper));
    b.whenPressed(new Cmd_StopCollect(s_intake, s_hopper));
    a.whenPressed(new Cmd_UnjamIntake(s_intake, s_hopper));
  }

  public static void configureShooter() {
    // when mode button is pressed, start flywheels. in order to stop flywheels, change modes (how to stop a command when changing modes?)
    shootMode.whenPressed(new Cmd_SpinFlywheels(s_shooter, s_hopper));
    // lockTrack could be an empty command; as long it's still considered "running", other tracking methods would rely on it
    // pseudocode: if (!lockTrack.active()) { stopTurretCommands(); }
  }

  public static void configureClimber() {
    // ex: climbMode.and(x).whenActive(new Cmd_Climb());
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */

  Trigger shooter_SpinFlywheels = new Trigger() {
    @Override
    public boolean get() { return shootMode.get() && x.get(); }
  };

  Trigger shooter_Fire = new Trigger() {
    @Override
    public boolean get() { return shootMode.get() && b.get(); }
  };

  Trigger panel_SpinThrice = new Trigger() {
    @Override
    public boolean get() { return panelMode.get() && x.get(); }
  };

  Trigger panel_StopOnColor = new Trigger() {
    @Override
    public boolean get() { return panelMode.get() && b.get(); }
  };

  Trigger panel_DeployCylinder = new Trigger() {
    @Override
    public boolean get() { return panelMode.get() && y.get(); }
  };

  Trigger collect_Collect = new Trigger() {
    @Override
    public boolean get() { return !panelMode.get() && !shootMode.get() && !climbMode.get() && x.get(); }
  };

  Trigger collect_StopCollect = new Trigger() {
    @Override
    public boolean get() { return !panelMode.get() && !shootMode.get() && !climbMode.get() && b.get(); }
  };

  Trigger climb_Extend = new Trigger() {
    @Override
    public boolean get() { return climbMode.get() && x.get(); }
  };

  Trigger climb_Retract = new Trigger() {
    @Override
    public boolean get() { return climbMode.get() && y.get(); }
  };

  Trigger climb_EngagePTO = new Trigger() {
    @Override
    public boolean get() { return climbMode.get() && b.get(); }
  };

  /**
   * Use this method to define your button->command mappings.  Buttons can be created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a
   * {@link edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {
    shooter_SpinFlywheels.toggleWhenActive(new Cmd_SpinFlywheels(s_shooter, s_hopper), false);
    // shooter_Fire.whenActive();
    panel_SpinThrice.whenActive(new Cmd_SpinThrice(s_panel), true);
    panel_StopOnColor.whenActive(new Cmd_StopOnColor(s_panel, s_panel.getDataFromField()), true);
    panel_DeployCylinder.whenActive(new Cmd_DeployCylinder(s_panel, s_shooter), false);
    collect_Collect.toggleWhenActive(new Cmd_Collect(s_intake, s_hopper), false);
    collect_StopCollect.whenActive(new Cmd_StopCollect(s_intake, s_hopper), false);
    // climb_Extend.whenActive();
    // climb_Retract.whenActive();
    // climb_EngagePTO.whenActive();
  }

  public Command getAutonomousCommand() {
    return null;
  }
}
