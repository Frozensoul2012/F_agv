package cn.agv.IIC;

import android.util.Log;

import cn.agv.utils.SerialDataUtils;
import cn.rayland.library.stm32.IIC;

/**
 * Created by chenjy on 2017/10/30.
 */

public class IICUtils {

    public static void IICSend(byte[] datas){
        Log.i("IIC send", SerialDataUtils.ByteArrToHex(datas));
        IIC.getInstance().sendData(datas);
    }

    public static byte[] IICRead(int length){
        return IIC.getInstance().readData(length);
    }
}
