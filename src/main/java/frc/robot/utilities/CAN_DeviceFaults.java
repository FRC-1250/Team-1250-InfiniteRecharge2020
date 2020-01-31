package frc.robot.utilities;

import com.ctre.phoenix.motorcontrol.StickyFaults;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.revrobotics.CANSparkMax;

public class CAN_DeviceFaults {
    public String stickyfault;
    public int can_id;
    private StickyFaults fault = new StickyFaults();

    public CAN_DeviceFaults(String stickyFault, int can_id){
        this.stickyfault = stickyFault;
        this.can_id = can_id;
    }

    public CAN_DeviceFaults(short stickyFault, int can_id){
        // TODO: FOR CAN SPARK MAX
        this.stickyfault = "SPARK";
        this.can_id = can_id;
    }

    public CAN_DeviceFaults(WPI_TalonSRX motor) {
        this.stickyfault = motor.getStickyFaults(fault).toString();
        this.can_id = motor.getDeviceID();
    }

    public CAN_DeviceFaults(WPI_TalonFX motor) {
        this.stickyfault = motor.getStickyFaults(fault).toString();
        this.can_id = motor.getDeviceID();
    }

    public CAN_DeviceFaults(CANSparkMax motor) {
        this.stickyfault = "SPARK";
        this.can_id = motor.getDeviceId();
    }
}