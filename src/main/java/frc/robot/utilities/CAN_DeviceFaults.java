package frc.robot.utilities;

import com.ctre.phoenix.motorcontrol.StickyFaults;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.FaultID;

/** Class to handle the faults of every CAN item on the bus. Go to definition to see notes and tutorial on implementation. */
public class CAN_DeviceFaults {
    public String stickyfault = "true";
    public int can_id;
    private StickyFaults fault = new StickyFaults();

    public CAN_DeviceFaults(WPI_TalonSRX motor) {
        this.stickyfault = motor.getStickyFaults(fault).toString();
        this.can_id = motor.getDeviceID();
    }

    public CAN_DeviceFaults(WPI_TalonFX motor) {
        this.stickyfault = motor.getStickyFaults(fault).toString();
        this.can_id = motor.getDeviceID();
    }

    public CAN_DeviceFaults(CANSparkMax motor) {
        this.stickyfault = Boolean.toString(motor.getFault(FaultID.kHasReset));
        this.can_id = motor.getDeviceId();
    }

    public CAN_DeviceFaults(VictorSPX motor) {
        this.stickyfault = motor.getStickyFaults(fault).toString();
        this.can_id = motor.getDeviceID();
    }

    // Add more overloaded methods as needed for newer CAN devices

    public int getCanID() {
        return can_id;
    }
}

/**                     FRC 1250 INFINITE RECHARGE 2020
 *     HOW TO IMPLEMENT THIS CAN DIAGNOSTIC SYSTEM IN ANY COMMAND-BASED ROBOT:
 * 
 *      For every subsystem that has CAN devices in it, implement the interface "CAN_Input".
   ex: "public class Sub_Drivetrain extends SubsystemBase implements CAN_Input {"
 * 
 *      This makes it so that each of these subsystems is required to have a method "input()". Otherwise, the subsystem will give an error saying:
   "The type SUBSYSTEM must implement the inherited abstract method CAN_Input.input()"
 * 
 *      Within "input()", the programmer must manually add each CAN device using the below template:
   public Vector<CAN_DeviceFaults> input() {
    Vector<CAN_DeviceFaults> myCanDevices = new Vector<CAN_DeviceFaults>();
    myCanDevices.add(new CAN_DeviceFaults(CAN_DEVICE)); // If the name of a TalonFX object is "intakeMotor", CAN_DEVICE would be "intakeMotor"
    // Add as many "myCanDevices.add()" as necessary until all CAN devices in this subsystem are accounted for
    return myCanDevices;
  }
 *      It's also highly recommended to have a subsystem dedicated to parsing the CAN inputs. This subsystem acts as the "master" as it grabs
 *      the input() method from every other subsystem and uses it to create a variable "masterCanDevices". (This "master" subsystem also has to implement CAN_Input).
 *      For example, the master subsystem Sub_CAN would have the following method:
 * 
   public Vector<CAN_DeviceFaults> input() {
    Vector<CAN_DeviceFaults> masterCanDevices = new Vector<CAN_DeviceFaults>();
    // Inputs from each subsystem that implemented CAN_Input
    masterCanDevices.addAll(RobotContainer.s_intake.input());
    masterCanDevices.addAll(RobotContainer.s_drivetrain.input());
    return masterCanDevices;
  }
  *                       NOTES TO KEEP IN MIND:
  *     THE ORDER IN WHICH YOU ADD CAN DEVICES IS UNIMPORTANT AS LONG AS YOU USE THE SORT
  *     METHOD WITHIN THE CAN MASTER SUBSYSTEM **AND** PHYSICALLY ORDER YOUR DEVICES BY ID.
  *
  *     IF YOU DO NOT USE THE SORTER AND DO NOT PHYSICALLY ORDER YOUR DEVICES AND USE LED'S, THE ORDER OF THE LED'S
  *     WILL BE RELIANT UPON THE ORDER THAT YOU ADDED EACH DEVICE IN THE INPUT() METHODS.
  *     (THAT IS NOT GOOD AND WOULD REQUIRE A LOT OF TEDIOUS LINE MOVING AS WELL AS UNNECESSARY BRAIN POWER)
  *     
  *     FOR LED'S:
    for (int i = 0; i < Robot.ledStripBuffer.getLength(); i++) {
      if ((can_devices.get(i).stickyfault == msg) || (can_devices.get(i).stickyfault == "true")) {
        // bad (red)
        Robot.ledStripBuffer.setRGB(i, 255, 0, 0);
        
      } else {
        // good (green)
        Robot.ledStripBuffer.setRGB(i, 0, 255, 0);
      }
      // System.out.println("CANCANCANCAN ###########" + i + " " + can_devices.get(i).getCanID() + " " + can_devices.get(i).stickyfault);
    }
*/