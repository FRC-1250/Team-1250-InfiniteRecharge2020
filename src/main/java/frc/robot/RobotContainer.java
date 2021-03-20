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
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import frc.robot.commands.panel.Cmd_DeployCylinder;
import frc.robot.commands.panel.Cmd_SpinThrice;
import frc.robot.commands.panel.Cmd_StopOnColor;
import frc.robot.commands.auto_actions.CmdG_AutoAllianceTrench;
import frc.robot.commands.auto_actions.CmdG_AutoCrossAndShoot;
import frc.robot.commands.auto_actions.Cmd_DoNothing;
import frc.robot.commands.auto_actions.Cmd_PlayAutoRecord;
import frc.robot.commands.auto_actions.Cmd_StartAutoRecord;
import frc.robot.commands.climb.CmdI_ExtendBottomCylinder;
import frc.robot.commands.climb.CmdI_RetractBottomCylinder;
import frc.robot.commands.climb.CmdSG_ExtendPhases;
import frc.robot.commands.climb.CmdSG_RetractPhases;
import frc.robot.commands.climb.Cmd_EngagePTO;
import frc.robot.commands.drive.Cmd_NewAutoDrive;
import frc.robot.commands.hopper.Cmd_ShootCells;
import frc.robot.commands.shooter.Cmd_HoodGoToPos;
import frc.robot.commands.shooter.Cmd_ShootNTimes;
import frc.robot.commands.shooter.Cmd_SpinFlywheels;
import frc.robot.commands.shooter.Cmd_ToggleLL;
import frc.robot.commands.shooter.Cmd_Track;
import frc.robot.commands.shooter.Cmd_TurretGoHome;
import frc.robot.state.Cmd_StateChange;
import frc.robot.commands.intake.CmdI_Collect;
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
import frc.robot.subsystems.Sub_Recorder;
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
  public static final Sub_Recorder s_recorder = new Sub_Recorder();

  // Buttons
  public static Joystick Gamepad = new Joystick(0);
  private static Joystick Gamepad1 = new Joystick(1);
  private static JoystickButton x = new JoystickButton(Gamepad, 1);
  private static JoystickButton a = new JoystickButton(Gamepad, 2);
  private static JoystickButton b = new JoystickButton(Gamepad, 3);
  private static JoystickButton y = new JoystickButton(Gamepad, 4);
  private static JoystickButton back = new JoystickButton(Gamepad, 9);
  private static JoystickButton start = new JoystickButton(Gamepad, 10);
  private static JoystickButton unjam = new JoystickButton(Gamepad1, 3);
  public static JoystickButton panelMode = new JoystickButton(Gamepad1, Constants.PANEL_MODE);
  public static JoystickButton shootMode = new JoystickButton(Gamepad1, Constants.SHOOT_MODE);
  public static JoystickButton climbMode = new JoystickButton(Gamepad1, Constants.CLIMB_MODE);
  public static JoystickButton unjamMode = new JoystickButton(Gamepad1, Constants.UNJAM_MODE);


  public enum RobotState {
    SHOOT_MODE, CLIMB_MODE, COLLECT_MODE, PANEL_MODE
  }

  Trigger panel = new Trigger() {
    @Override
    public boolean get() { return !panelMode.get(); }
  };

  Trigger shoot = new Trigger() {
    @Override
    public boolean get() { return !shootMode.get(); }
  };

  Trigger lb = new Trigger() {
    @Override
    public boolean get() { return !Gamepad.getRawButton(5); }
  };

  Trigger rb = new Trigger() {
    @Override
    public boolean get() { return !Gamepad.getRawButton(6); }
  };

  Trigger dev7 = new Trigger() {
    @Override
    public boolean get() { return !Gamepad1.getRawButton(7); }
  };

  Trigger dev6 = new Trigger() {
    @Override
    public boolean get() { return !Gamepad1.getRawButton(6); }
  };

  Trigger dev8 = new Trigger() {
    @Override
    public boolean get() { return !Gamepad1.getRawButton(8); }
  };

  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    configureButtonBindings();
    s_hopper.setDefaultCommand(new Cmd_HopperManagement(s_hopper));
    s_stateManager.setDefaultCommand(new Cmd_StateChange(s_stateManager, RobotState.COLLECT_MODE.toString()));

    ShuffleboardTab recorderTab = Shuffleboard.getTab("Recorder");
    s_recorder.addFileChooserOptions();
    recorderTab.add("Start record", new Cmd_StartAutoRecord(s_recorder, s_drivetrain)).withPosition(2, 0).withSize(2, 1);
    recorderTab.add("Playback record", new Cmd_PlayAutoRecord(s_recorder, s_drivetrain)).withPosition(2, 1).withSize(2, 1);
  }

  /**
   * Use this method to define your button->command mappings. Buttons can be
   * created by instantiating a {@link GenericHID} or one of its subclasses
   * ({@link Joystick} or {@link XboxController}), and then
   * passing it to a {@link JoystickButton}.
   */
  private void configureButtonBindings() {
    /*
     * Change the allow interrupt flag to change function.
     * FALSE = First button pressed wins
     * TRUE = Last button pressed wins
     */
    panel.whileActiveOnce(new Cmd_StateChange(s_stateManager, RobotState.PANEL_MODE.toString()), false);
    shoot.whileActiveOnce(new Cmd_StateChange(s_stateManager, RobotState.SHOOT_MODE.toString()), false);
    climbMode.whileActiveOnce(new Cmd_StateChange(s_stateManager, RobotState.CLIMB_MODE.toString()), false);


    whenTriggerPressed(RobotState.SHOOT_MODE, null, new Cmd_SpinFlywheels(s_shooter, 1), true);
    whenTriggerPressed(RobotState.SHOOT_MODE, x, new Cmd_ShootCells(s_hopper), true);
    whenTriggerPressed(RobotState.SHOOT_MODE, b, new Cmd_TurretGoHome(s_shooter), true);
    // whenTriggerPressed(RobotState.CLIMB_MODE, null, new Cmd_DeployCylinder(s_panel), true);
    // whenTriggerPressed(RobotState.PANEL_MODE, x, new Cmd_SpinThrice(s_panel), false);
    // whenTriggerPressed(RobotState.PANEL_MODE, b, new Cmd_StopOnColor(s_panel), false);
    // whenTriggerPressed(RobotState.PANEL_MODE, y, new Cmd_DeployCylinder(s_panel), false);
    whenTriggerPressed(RobotState.COLLECT_MODE, x, new Cmd_Collect(s_intake), true);
    whenTriggerPressed(RobotState.COLLECT_MODE, b, new Cmd_StopCollect(s_intake, s_hopper), true);
    // whenTriggerPressed(RobotState.CLIMB_MODE, x, new CmdSG_ExtendPhases(s_climb), true);
    // whenTriggerPressed(RobotState.CLIMB_MODE, b, new CmdSG_RetractPhases(s_drivetrain, s_climb), true);
    // whenTriggerPressed(mode, button, command, interruptible);
    // dev6.whenActive(new Cmd_HoodGoToPos(s_shooter, -150), false);
    // dev7.whenActive(new Cmd_HoodGoToPos(s_shooter, -2), true);

    start.whileActiveOnce(new Cmd_SpinFlywheels(s_shooter, 1), true);

    back.whileActiveOnce(new Cmd_ShootCells(s_hopper), true);

    dev7.whenActive(new Cmd_ShootNTimes(s_shooter, s_hopper, 3));

    // x.toggleWhenActive(new Cmd_Collect(s_intake), false);
    // b.whenActive(new Cmd_ShootNTimes(s_shooter, s_hopper, 3));
    lb.toggleWhenActive(new Cmd_StartAutoRecord(s_recorder, s_drivetrain));
    rb.toggleWhenActive(new Cmd_PlayAutoRecord(s_recorder, s_drivetrain));
    
  }

  /** This method in place of the native Object toString() method because it returns crap (this only works with XABY)
   * @param btn Button to turn to string
  */
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

  String cmdToString(Command cmd) {
    return cmd.getName().substring(cmd.getName().indexOf("_") + 1); // everything after Cmd_
  }

  public void whenTriggerPressed(RobotState mode, JoystickButton btn, Command cmd, boolean interruptible) {
    StateTrigger trigger = new StateTrigger(mode, btn);
    trigger.whileActiveOnce(cmd, interruptible);
    if ((btn != null) && (btn != unjam)) {
      // if the button is null, it should be something that activates just with the mode (and null objects shouldn't be added to the array this method adds to)
      // unjam is also an exception (treated like a mode of sorts)
      s_util.setStateButton(mode.toString(), cmdToString(cmd), btnToString(btn));
    }
  }

  public Command getAutonomousCommand() {
    return new CmdG_AutoAllianceTrench(s_drivetrain, s_shooter, s_hopper, s_intake);
    // return new Cmd_ShootNTimes(s_shooter, s_hopper, 3);
    // return new CmdG_AutoCrossAndShoot(s_drivetrain, s_shooter, s_hopper);
    // return null;
  }
}
