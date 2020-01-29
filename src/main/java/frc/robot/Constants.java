/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants.  This class should not be used for any other purpose.  All constants should be
 * declared globally (i.e. public static).  Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {

    //Robot Devices----------------------------------------
    //Sub_Drivetrain
    public static final int DRV_RIGHT_FRONT = 10;
    public static final int DRV_RIGHT_BACK = 11;
    public static final int DRV_LEFT_FRONT = 12;
    public static final int DRV_LEFT_BACK = 13;
    public static final int DRV_PIGEON = 50;
    public static final int CLM_SOL_PTO = 1;

    //Sub_Shooter
    public static final int SHOOT_FALCON_0 = 14;
    public static final int SHOOT_FALCON_1 = 15;
    public static final int SHOOT_TURRET = 16;
    public static final int SHOOT_HOOD = 17;

    //Sub_Panel
    public static final int PANEL_MOTOR = 18;
    public static final int PANEL_SOL = 0;
    public static final I2C.Port PANEL_SENSOR_PORT = I2C.Port.kOnboard;

    //Sub_Intake
    public static final int INT_COL_MOTOR = 19;
    public static final int INT_COL_SOL = 3;


    //Sub_Hopper
    public static final int HOP_FALCON_0 = 20;
    public static final int HOP_FALCON_1 = 21;
    public static final int HOP_ELE_MOTOR = 22;
    public static final int HOP_ELE_SENS = 0;

    //Sub_Climber
    public static final int CLM_SOL_EXTEND = 2;
    
    //Pigeon

    //Constants--------------------------------------------
    public static final double DRV_GEAR_RATIO = 10;
    public static final double DRV_WHEEL_SIZE = 6;
    public static final double DRV_WHEEL_CIRCUMF = DRV_WHEEL_SIZE * Math.PI;
    public static final double DRV_TICKS_TO_INCH = DRV_WHEEL_CIRCUMF / 42;

    public static final double SHOOT_TURRET_P = 0.6;
    public static final double SHOOT_TURRET_D = 0.0;

    public static final double SHOOT_TURRET_HOME = 2348;
    public static final double SHOOT_TURRET_LEFT_BOUND = 1120;
    public static final double SHOOT_TURRET_RIGHT_BOUND = 3560;

    public static final double DRV_KP_SIMPLE_STRAIT = 0.01;
    public static final double DRV_KP_SIMPLE = 0.05;


}
