/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.I2C;

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
    public static final int DRV_RIGHT_MID = 11;
    public static final int DRV_RIGHT_BACK = 12;
    public static final int DRV_LEFT_FRONT = 13;
    public static final int DRV_LEFT_MID = 14;
    public static final int DRV_LEFT_BACK = 15;
    public static final int DRV_PIGEON = 40;

    //Sub_Shooter
    public static final int SHOOT_FALCON_0 = 16;
    public static final int SHOOT_FALCON_1 = 17;
    public static final int SHOOT_TURRET = 18;
    public static final int SHOOT_HOOD = 19;

    //Sub_Panel
    public static final int PANEL_MOTOR = 20;
    public static final int PANEL_SOL = 0;
    public static final I2C.Port PANEL_SENSOR_PORT = I2C.Port.kOnboard;

    //Sub_Intake
    public static final int INT_COL_MOTOR = 21;
    public static final int INT_HOP_0 = 22;
    public static final int INT_HOP_1 = 23;
    public static final int INT_HOP_ELE = 24;
    public static final int INT_HOP_ELE_SENS = 0;

    //Sub_Climber
    public static final int CLM_SOL_PTO = 1;
    public static final int CLM_SOL_EXTEND = 2;
    public static final int CLM_SPOOL = 25;

    //Constants--------------------------------------------
    public static final double DRV_GEAR_RATIO = 10;
    public static final double DRV_WHEEL_SIZE = 6;
    public static final double DRV_WHEEL_DIAMETER = DRV_WHEEL_SIZE * Math.PI;

}
