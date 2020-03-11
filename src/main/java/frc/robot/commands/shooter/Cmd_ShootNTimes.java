/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.shooter;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Sub_Hopper;
import frc.robot.subsystems.Sub_Shooter;

public class Cmd_ShootNTimes extends CommandBase {
  double timesToShoot;
  double timesSeen;
  int speed = 20000; //speed = ticks/100ms
  boolean lastHopSens;
  boolean withinDeadband;
  boolean changeSensor = false;
  private final Sub_Shooter s_shooter;
  private final Sub_Hopper s_hopper;
  public Cmd_ShootNTimes(Sub_Shooter shooter, Sub_Hopper hopper, int times) {
    s_shooter = shooter;
    s_hopper = hopper;
    addRequirements(shooter, hopper);  
    timesToShoot = times;
  }

  @Override
  public void initialize() {
    //Determines whether one started in chamber if so, adds one pass
    if(s_hopper.getSensor()){
     timesSeen = 1;
    }
    else{
     timesSeen = 0;
    }
    lastHopSens = s_hopper.getSensor();

     withinDeadband = false;
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    s_shooter.hoodGoToPos(-68);
    s_shooter.track();
    s_shooter.setFlywheelVelocityControl(speed);

    if (s_shooter.getFlyWheelSpeed() > 19000){
      withinDeadband = true;
    }


    if (changeSensor) {
      lastHopSens = s_hopper.getSensor();
      changeSensor = false;
    }
    //Value is later divided by 2 to account for ball entering and leaving chamber
    if(s_hopper.getSensor() != lastHopSens){
      //Value is double the true times shot
      timesSeen++;
      changeSensor = true;
    }
    if(withinDeadband && ((timesSeen / 2) < timesToShoot - 1)){
      s_hopper.spinUptakeMotor(1);
      s_hopper.spinHopperMotors(1);
    }
    else{
      s_hopper.spinUptakeMotor(0);
      s_hopper.spinHopperMotors(0);
    }
    // System.out.println("############## TIMES SEEN " + timesSeen);
  }
  
  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    //Flywheel is left running to maintain velocity, end at the end of auton with CmdI_StopFlyWheels
    // s_shooter.hoodGoToPos(-5);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return((timesSeen / 2) >= timesToShoot - 1);
  }
}
