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
import frc.robot.commands.auto_actions.Cmd_DoNothing;
import frc.robot.commands.climb.CmdSG_ExtendPhases;
import frc.robot.commands.climb.CmdSG_RetractPhases;
import frc.robot.commands.climb.Cmd_EngagePTO;
import frc.robot.commands.hopper.Cmd_ShootCells;
import frc.robot.commands.shooter.Cmd_ShootNTimes;
import frc.robot.commands.shooter.Cmd_SpinFlywheels;
import frc.robot.commands.shooter.Cmd_ToggleLL;
import frc.robot.commands.shooter.Cmd_Track;
import frc.robot.state.Cmd_StateChange;
import frc.robot.commands.intake.Cmd_Collect;
import frc.robot.commands.intake.Cmd_StopCollect;
import frc.robot.commands.hopper.Cmd_UnjamHopper;
import frc.robot.commands.hopper.Cmd_HopperManagement;
import frc.robot.subsystems.Sub_Utility;
import frc.robot.subsystems.Sub_Climber;
import frc.robot.subsystems.Sub_Drivetrain;
import frc.robot.subsystems.Sub_Hopper;
import frc.robot.subsystems.Sub_Intake;
import frc.robot.subsystems.Sub_Panel;
import frc.robot.subsystems.Sub_Shooter;
import frc.robot.state.Sub_StateManager;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.utilities.StateTrigger;

/**
 * This class is where the bulk of the robot should be declared. Since
 * Command-based is a "declarative" paradigm, very little robot logic should
 * actually be handled in the {@link Robot} periodic methods (other than the
 * scheduler calls). Instead, the structure of the robot (including subsystems,
 * commands, and button mappings) should be declared here.
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
  public static final Sub_StateManager s_stateManager = new Sub_StateManager();

  // Buttons
  private static Joystick Gamepad = new Joystick(0);
  private static Joystick Gamepad1 = new Joystick(1);
  private static JoystickButton x = new JoystickButton(Gamepad, 1);
  private static JoystickButton a = new JoystickButton(Gamepad, 2);
  private static JoystickButton b = new JoystickButton(Gamepad, 3);
  private static JoystickButton y = new JoystickButton(Gamepad, 4);
  private static JoystickButton rb = new JoystickButton(Gamepad, 6);
  public static JoystickButton panelMode = new JoystickButton(Gamepad1, Constants.PANEL_MODE); // start button
  public static JoystickButton shootMode = new JoystickButton(Gamepad1, Constants.SHOOT_MODE); // back button
  public static JoystickButton climbMode = new JoystickButton(Gamepad1, Constants.CLIMB_MODE); // lt button

  public enum RobotState {
    SHOOT_MODE, CLIMB_MODE, COLLECT_MODE, PANEL_MODE
  }

  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    configureButtonBindings();
    // s_hopper.setDefaultCommand(new Cmd_HopperManagement(s_hopper));
    s_stateManager.setDefaultCommand(new Cmd_StateChange(s_stateManager, RobotState.COLLECT_MODE.toString()));
  }

  Trigger shooter_SpinFlywheels = new Trigger() {
    @Override
    public boolean get() {
      return (s_stateManager.getRobotState() == RobotState.SHOOT_MODE.toString()) && x.get();
    }
  };

  Trigger shooter_Fire = new Trigger() {
    @Override
    public boolean get() {
      return (s_stateManager.getRobotState() == RobotState.SHOOT_MODE.toString()) && b.get();
    }
  };

  Trigger shooter_Track = new Trigger() {
    @Override
    public boolean get() { return (s_stateManager.getRobotState() == RobotState.SHOOT_MODE.toString()); }
  };

  Trigger panel_SpinThrice = new Trigger() {
    @Override
    public boolean get() {
      return (s_stateManager.getRobotState() == RobotState.PANEL_MODE.toString()) && x.get();
    }
  };

  Trigger panel_StopOnColor = new Trigger() {
    @Override
    public boolean get() {
      return (s_stateManager.getRobotState() == RobotState.PANEL_MODE.toString()) && b.get();
    }
  };

  Trigger panel_DeployCylinder = new Trigger() {
    @Override
    public boolean get() {
      return (s_stateManager.getRobotState() == RobotState.PANEL_MODE.toString()) && y.get();
    }
  };

  Trigger collect_Collect = new Trigger() {
    @Override
    public boolean get() {
      return (s_stateManager.getRobotState() == RobotState.COLLECT_MODE.toString()) && x.get();
    }
  };

  Trigger collect_StopCollect = new Trigger() {
    @Override
    public boolean get() {
      return (s_stateManager.getRobotState() == RobotState.COLLECT_MODE.toString()) && b.get();
    }
  };

  Trigger collect_Unjam = new Trigger() {
    @Override
    public boolean get() {
      return (s_stateManager.getRobotState() == RobotState.COLLECT_MODE.toString()) && y.get();
    }
  };

  Trigger climb_Extend = new Trigger() {
    @Override
    public boolean get() {
      return (s_stateManager.getRobotState() == RobotState.CLIMB_MODE.toString()) && x.get();
    }
  };

  Trigger climb_Retract = new Trigger() {
    @Override
    public boolean get() {
      return (s_stateManager.getRobotState() == RobotState.CLIMB_MODE.toString()) && y.get();
    }
  };

  Trigger climb_EngagePTO = new Trigger() {
    @Override
    public boolean get() {
      return (s_stateManager.getRobotState() == RobotState.CLIMB_MODE.toString()) && b.get();
    }
  };

  /**
   * Use this method to define your button->command mappings. Buttons can be
   * created by instantiating a {@link GenericHID} or one of its subclasses
   * ({@link edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then
   * passing it to a {@link edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {
    // shooter_SpinFlywheels.whenActive(new Cmd_SpinFlywheels(s_shooter, s_hopper, 1), false);
    // shooter_Track.whenActive(new Cmd_Track(s_shooter), false);
    // shooter_Fire.whenActive(new Cmd_ShootCells(s_hopper), false);
    // shooter_Track.whenActive(new Cmd_ToggleLL(s_shooter), true);
    // panel_SpinThrice.whenActive(new Cmd_SpinThrice(s_panel), true);
    // panel_StopOnColor.whenActive(new Cmd_StopOnColor(s_panel), true);
    // panel_DeployCylinder.whenActive(new Cmd_DeployCylinder(s_panel), false);
    // collect_Collect.whenActive(new Cmd_Collect(s_intake), true);
    // collect_Unjam.whenActive(new Cmd_UnjamHopper(s_hopper), false);
    // collect_StopCollect.whenActive(new Cmd_StopCollect(s_intake, s_hopper), true);

    // climb_Extend.whenActive(new Cmd_ExtendCylinders(s_climb), true);
    // shootMode.and(a).whenActive(new Cmd_RunFlywheels(s_shooter));
    // climb_Retract.whenActive();
    // climb_EngagePTO.whenActive();

    /*
     * Change the allow interrupt flag to change function. FALSE = First button
     * pressed wins TRUE = Last button pressed wins
     */
    panelMode.whenHeld(new Cmd_StateChange(s_stateManager, RobotState.PANEL_MODE.toString()), false);
    shootMode.whenHeld(new Cmd_StateChange(s_stateManager, RobotState.SHOOT_MODE.toString()), false);
    climbMode.whenHeld(new Cmd_StateChange(s_stateManager, RobotState.CLIMB_MODE.toString()), false);

    whenTriggerPressed(RobotState.SHOOT_MODE, null, new Cmd_Track(s_shooter), false);
    whenTriggerPressed(RobotState.SHOOT_MODE, x, new Cmd_SpinFlywheels(s_shooter, 1), false);
    whenTriggerPressed(RobotState.SHOOT_MODE, b, new Cmd_ShootCells(s_hopper), false);
    whenTriggerPressed(RobotState.PANEL_MODE, x, new Cmd_SpinThrice(s_panel), false);
    whenTriggerPressed(RobotState.PANEL_MODE, b, new Cmd_StopOnColor(s_panel), false);
    whenTriggerPressed(RobotState.PANEL_MODE, y, new Cmd_DeployCylinder(s_panel), false);
    whenTriggerPressed(RobotState.COLLECT_MODE, x, new Cmd_Collect(s_intake), true);
    whenTriggerPressed(RobotState.COLLECT_MODE, b, new Cmd_StopCollect(s_intake, s_hopper), false);
    whenTriggerPressed(RobotState.COLLECT_MODE, y, new Cmd_UnjamHopper(s_hopper), false);
    whenTriggerPressed(RobotState.CLIMB_MODE, x, new CmdSG_ExtendPhases(s_climb), false);
    whenTriggerPressed(RobotState.CLIMB_MODE, b, new Cmd_EngagePTO(s_drivetrain, s_climb), false);
    whenTriggerPressed(RobotState.CLIMB_MODE, y, new CmdSG_RetractPhases(s_climb), false);
    whenTriggerPressed(RobotState.CLIMB_MODE, a, new Cmd_DoNothing(1), true);
    whenTriggerPressed(RobotState.COLLECT_MODE, a, new Cmd_ShootNTimes(s_shooter, s_hopper, 2), true);
    // whenTriggerPressed(mode, button, command, interruptible);
  }

  // This method in place of the native Object toString() method because it returns crap
  public String btnToString(JoystickButton btn) {
    String str;
    if (btn.equals(x)) {
      str = "x";
    } else if (btn.equals(a)) {
      str = "a";
    } else if (btn.equals(b)) {
      str = "b";
    } else if (btn.equals(y)) {
      str = "y";
    } else {
      str = "ERROR, ONLY XABY AVAILABLE";
    }
    return str;
  }

  String getCmdString(Command cmd) {
    return cmd.getName().substring(cmd.getName().indexOf("_") + 1); // everything after Cmd_
  }

  public void whenTriggerPressed(RobotState mode, JoystickButton btn, Command cmd, boolean interruptible) {
    StateTrigger trigger = new StateTrigger(mode, x);
    trigger.whileActiveOnce(cmd, interruptible);
    if (btn != null) { // if the button is null, it should be something that activates just with the mode (and null objects shouldn't be added to the array this method adds to)
      // System.out.println("##########" + mode.toString() + " " + btnToString(btn) + " " + getCmdString(cmd));
      s_util.setStateButton(mode.toString(), getCmdString(cmd), btnToString(btn));
    }
  }

  public Command getAutonomousCommand() {
    return null;
  }
}
