/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.utilities;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.RobotContainer;

/**
 * Add your docs here.
 */
public class StateTrigger {

    private final JoystickButton button;
    private final String mode;
    public Trigger trigger = new Trigger() {
        @Override
        public boolean get() {
            return (RobotContainer.s_stateManager.getRobotState() == mode) && button.get();
        }
    };

    public StateTrigger(RobotContainer.RobotState mode, JoystickButton button) {
        this.mode = mode.toString();
        this.button = button;
    }

    public boolean get() {
        return trigger.get();
    }

    public void whileActiveOnce(Command cmd, boolean interruptible) {
        trigger.whileActiveOnce(cmd, interruptible);
    }
}
