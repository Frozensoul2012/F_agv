package cn.agv.utils;

import android.util.Log;

import java.math.BigDecimal;

/**
 * Created by chenjy on 2017/11/3.
 */

public class test {
    public static void main(String args[]){

//       System.out.println(AgvStateUtils.RToM(10.0));
        int vx = 30;
        byte[] cmd = new byte[6];
        cmd[4] = (byte)(vx & 0xFF);
        cmd[5] = (byte)((vx >>8 ) & 0xFF);
        System.out.println("cmd[4]=" + cmd[4]);
        System.out.println("cmd[5]=" + cmd[5]);
    }
}
