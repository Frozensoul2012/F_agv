package cn.agv.utils;

import java.math.BigDecimal;

/**
 * Created by chenjy on 2017/11/3.
 */

public class AgvStateUtils {

    public static double RpmToMs(int rmpSpeed){
        double ms = rmpSpeed*AgvConstant.WHEEL_DIAMETER*Math.PI/60000;
        return new BigDecimal(""+ms).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static double RToM(double r){
        double m = AgvConstant.WHEEL_DIAMETER*Math.PI/1000;
        return new BigDecimal(""+m).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static double dataToAngle(double data){
        double angle = data/10;
        return new BigDecimal(""+angle).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

}
