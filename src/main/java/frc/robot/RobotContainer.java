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
import frc.robot.commands.diagnostic.Cmd_RunFlywheels;
import frc.robot.commands.diagnostic.Cmd_RunHood;
import frc.robot.commands.hopper.Cmd_ShootCells;
import frc.robot.commands.shooter.Cmd_SpinFlywheels;
import frc.robot.commands.shooter.Cmd_Track;
import frc.robot.commands.intake.Cmd_Collect;
import frc.robot.commands.intake.Cmd_StopCollect;
import frc.robot.commands.intake.Cmd_UnjamHopper;
import frc.robot.subsystems.Sub_Utility;
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
  public static final Sub_Utility s_util = new Sub_Utility();

  // Buttons
  private static Joystick Gamepad = new Joystick(0);
  private static JoystickButton x = new JoystickButton(Gamepad, 1);
  private static JoystickButton a = new JoystickButton(Gamepad, 2);
  private static JoystickButton b = new JoystickButton(Gamepad, 3);
  private static JoystickButton y = new JoystickButton(Gamepad, 4);
  private static JoystickButton rb = new JoystickButton(Gamepad, 6);
  public static JoystickButton panelMode = new JoystickButton(Gamepad, Constants.PANEL_MODE); // start button
  public static JoystickButton shootMode = new JoystickButton(Gamepad, Constants.SHOOT_MODE); // back button
  public static JoystickButton climbMode = new JoystickButton(Gamepad, Constants.CLIMB_MODE); // lt button
  /**
   * The container for the robot.  Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    configureButtonBindings();        
  }

  Trigger shooter_SpinFlywheels = new Trigger() {
    @Override
    public boolean get() { return shootMode.get(); }
  };

  Trigger shooter_Fire = new Trigger() {
    @Override
    public boolean get() { return shootMode.get() && b.get(); }
  };

  Trigger shooter_Track = new Trigger() {
    @Override
    public boolean get() { return shootMode.get(); }
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

  Trigger collect_Unjam = new Trigger() {
    @Override
    public boolean get() { return !panelMode.get() && !shootMode.get() && !climbMode.get() && y.get(); }
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
    // shooter_SpinFlywheels.whenActive(new Cmd_SpinFlywheels(s_shooter, s_hopper, 1), false);
    // shooter_Track.whenActive(new Cmd_Track(s_shooter), false);
    // shooter_Fire.whenActive(new Cmd_ShootCells(s_hopper), false);
    panel_SpinThrice.whenActive(new Cmd_SpinThrice(s_panel), true);
    panel_StopOnColor.whenActive(new Cmd_StopOnColor(s_panel), true);
    panel_DeployCylinder.whenActive(new Cmd_DeployCylinder(s_panel, s_shooter), false);
    collect_Collect.whenActive(new Cmd_Collect(s_intake), true);
    collect_Unjam.whenActive(new Cmd_UnjamHopper(s_hopper), false);
    collect_StopCollect.whenActive(new Cmd_StopCollect(s_intake, s_hopper), true);
    // shootMode.and(a).whenActive(new Cmd_RunFlywheels(s_shooter));
    // climb_Extend.whenActive();
    // climb_Retract.whenActive();
    // climb_EngagePTO.whenActive();
  }

  public Command getAutonomousCommand() {
    return null;
  }
}
