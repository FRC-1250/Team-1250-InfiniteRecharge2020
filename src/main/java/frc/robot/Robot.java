/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.ComplexWidget;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

import java.io.File;

import com.revrobotics.CANSparkMax.IdleMode;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private Command m_autonomousCommand;

  private RobotContainer m_robotContainer;
  public static int halvesAroundPanel;

  public static boolean isItAuto;

  public static AddressableLED ledStrip;
  public static AddressableLEDBuffer ledStripBuffer;

  SendableChooser<String> autoChooser = new SendableChooser<String>();
  public static SendableChooser<String> fileChooser = new SendableChooser<>();
  ShuffleboardTab recorderTab = Shuffleboard.getTab("Recorder");
  ComplexWidget toPlayFilename = recorderTab.add("File Name (To Play Next)", Robot.fileChooser).withWidget(BuiltInWidgets.kComboBoxChooser)
  .withPosition(0, 2).withSize(3, 1);

  @Override
  public void robotInit() {
    // Instantiate our RobotContainer.  This will perform all our button bindings, and put our
    // autonomous chooser on the dashboard.
    m_robotContainer = new RobotContainer();
    RobotContainer.s_panel.retractCylinder();
    RobotContainer.s_drivetrain.disengagePTO();

    autoChooser.setDefaultOption("Straight_Shot", "Straight_Shot");
    autoChooser.addOption("Whip", "Whip");
    SmartDashboard.putData(autoChooser);

    /* Auton Playback */
    addFileChooserOptions();
    SmartDashboard.putData(fileChooser);

      /* LEDS */
    ledStrip = new AddressableLED(Constants.LED_PWM_PORT);
    // Reuse buffer
    // Default to a length of 60, start empty output
    // Length is expensive to set, so only set it once, then just update data
    ledStripBuffer = new AddressableLEDBuffer(30);
    ledStrip.setLength(ledStripBuffer.getLength());

    halvesAroundPanel = 0;
    RobotContainer.s_util.initTime = System.currentTimeMillis();

    ledStrip.start();
  }

  // Happens on startup; adds pre-existing files to Shuffleboard chooser
  public void addFileChooserOptions() {
    File folder = new File(RobotContainer.s_recorder.getDirPath());
    String[] listOfFiles = folder.list();
    for (String file : listOfFiles) {
      Robot.fileChooser.addOption(file, file);
    }
    Robot.fileChooser.addOption("manualtest", "manualtest");
  }

  // Called when StartAutoRecord ends; adds the newly recorded file as a Shuffleboard option
  public static void updateFileChooserOptions(String filename) {
    Robot.fileChooser.addOption(filename, filename);

    // If the user doesn't select a dropdown option, this should by default play the last recorded file
    Robot.fileChooser.setDefaultOption("", filename);
  }

  // Used to play back file in PlayAutoRecord; if the "toPlay" filename is left blank in Shuffleboard, the last recorded file is played
  public static String getFilenameToPlay() {
    return Robot.fileChooser.getSelected();
    //return "test0";
  }

  /**
   * This function is called every robot packet, no matter the mode. Use this for items like
   * diagnostics that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before
   * LiveWindow and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
    // Runs the Scheduler.  This is responsible for polling buttons, adding newly-scheduled
    // commands, running already-scheduled commands, removing finished or interrupted commands,
    // and running subsystem periodic() methods.  This must be called from the robot's periodic
    // block in order for anything in the Command-based framework to work.
    CommandScheduler.getInstance().run();

    if (RobotContainer.s_util.checkCAN()) {
      ledStrip.setData(ledStripBuffer);
    }
  }

  /**
   * This function is called once each time the robot enters Disabled mode.
   */
  @Override
  public void disabledInit() {
    RobotContainer.s_drivetrain.idleMode(IdleMode.kCoast);
  }

  @Override
  public void disabledPeriodic() {
  }

  /**
   * This autonomous runs the autonomous command selected by your {@link RobotContainer} class.
   */
  @Override
  public void autonomousInit() {
    isItAuto = true;
    RobotContainer.s_drivetrain.idleMode(IdleMode.kBrake);
    m_autonomousCommand = m_robotContainer.getAutonomousCommand();

    // schedule the autonomous command (example)
    if (m_autonomousCommand != null) {
      m_autonomousCommand.schedule();
    }
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
  }

  @Override
  public void teleopInit() {
    isItAuto = false;
    // This makes sure that the autonomous stops running when
    // teleop starts running. If you want the autonomous to
    // continue until interrupted by another command, remove
    // this line or comment it out.
    
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }
    RobotContainer.s_drivetrain.idleMode(IdleMode.kBrake);
  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
  }

  @Override
  public void testInit() {
    // Cancels all running commands at the start of test mode.
    CommandScheduler.getInstance().cancelAll();
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }
}
