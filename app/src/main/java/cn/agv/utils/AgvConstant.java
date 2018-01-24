package cn.agv.utils;

/**
 * Created by chenjy on 2017/10/30.
 */

public class AgvConstant {

    public static int IIC_READ_DATA_LENGTH = 37;
    public static int IIC_CONTROL_CMD_LENGTH = 12;
    public static int IIC_MECANUM_CONTROL_CMD_LENGTH = 9;
    public static int IIC_RESET_CMD_LENGTH = 9;
    public static int IIC_CHANGECONTROLLER_CMD_LENGTH = 4;

    public static short CMD_TYPE_CONTROL = 0;
    public static short CMD_TYPE_ABS = 1;
    public static short CMD_TYPE_CONTROLLER = 2;


    public static short TEST_SEQ = 1;

    public static double WHEEL_DIAMETER = 17.5;

}
