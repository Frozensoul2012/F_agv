package ostrich.com.ostrich_robot.utils;

import cn.rayland.library.stm32.IIC;

/**
 * Created by chenjy on 2017/10/24.
 */

public class Stm32Utils {


    public static void stm32Send(byte[] datas){
        IIC.getInstance().sendData(datas);
    }

    public static byte[] stm32Read(int length){
        return IIC.getInstance().readData(length);
    }

}
