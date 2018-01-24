package ostrich.com.ostrich_robot.utils;

/**
 * Created by chenjy on 2017/10/26.
 */

public class OstrichController {

    // OstrichRobot 四层结构 head(头)、neck(颈)、waist(腰)、foot(脚)

    public static int cmdId = 2;
    public static int pkgLen = 16;
    public static int defVal = 0;

    public static int angleRatio = 9;

    //foot

    /**
     *
     * @param length "1" :正向 5.53cm
     *               "-1":反向 5.53cm
     *               "10":正向 55.3cm
     */

    public static void ostrichFootForward(int length){
        packageCmd(cmdId,pkgLen,length,defVal,defVal,defVal,defVal,defVal,defVal,defVal);
    }

    public static void ostrichFootBackward(int length){
        packageCmd(cmdId,pkgLen,-length,defVal,defVal,defVal,defVal,defVal,defVal,defVal);
    }


    /**
     *
     * @param angle "1" :逆时针旋转 9°
     *              "-1":顺时针旋转 9°
     *              "20":逆时针旋转 180°
     */
    public static void ostrichFootLeft(int angle){
        packageCmd(cmdId,pkgLen,defVal,angle/angleRatio,defVal,defVal,defVal,defVal,defVal,defVal);
    }

    public static void ostrichFootRight(int angle){
        packageCmd(cmdId,pkgLen,defVal,angle/angleRatio,defVal,defVal,defVal,defVal,defVal,defVal);
    }


    //waist

    /**
     *
     * @param length "10" :4mm
     *               "-10":4mm
     */

    public static void ostrichWaistForward(int length){
        packageCmd(cmdId,pkgLen,-length,defVal,defVal,defVal,defVal,defVal,defVal,defVal);
    }

    public static void ostrichWaistBackward(int length){
        packageCmd(cmdId,pkgLen,-length,defVal,defVal,defVal,defVal,defVal,defVal,defVal);
    }

    public static void ostrichWaistLeft(int length){
        packageCmd(cmdId,pkgLen,-length,defVal,defVal,defVal,defVal,defVal,defVal,defVal);
    }

    public static void ostrichWaistRight(int length){
        packageCmd(cmdId,pkgLen,-length,defVal,defVal,defVal,defVal,defVal,defVal,defVal);
    }

    //neck

    /**
     *
     * @param length "10" :上升1cm
     *              "100":上升10cm
     *              "-10" :下降1cm
     */

    public static void ostrichNeckUp(int length){
        packageCmd(cmdId,pkgLen,-length,defVal,defVal,defVal,defVal,defVal,defVal,defVal);
    }

    public static void ostrichNecktDown(int length){
        packageCmd(cmdId,pkgLen,-length,defVal,defVal,defVal,defVal,defVal,defVal,defVal);
    }

    //head

    /**
     *
     * @param length [0,180]
     */

    public static void ostrichHeadUp(int length){
        packageCmd(cmdId,pkgLen,-length,defVal,defVal,defVal,defVal,defVal,defVal,defVal);
    }

    public static void ostrichHeadDown(int length){
        packageCmd(cmdId,pkgLen,-length,defVal,defVal,defVal,defVal,defVal,defVal,defVal);
    }

    public static void ostrichHeadLeft(int length){
        packageCmd(cmdId,pkgLen,-length,defVal,defVal,defVal,defVal,defVal,defVal,defVal);
    }

    public static void ostrichHeadRight(int length){
        packageCmd(cmdId,pkgLen,-length,defVal,defVal,defVal,defVal,defVal,defVal,defVal);
    }


    public static void ostrichStop(){
        packageCmd(cmdId,pkgLen,defVal,defVal,defVal,defVal,defVal,defVal,defVal,defVal);
    }



    public static void packageCmd(int cmdId,int pkgLen,int x,int y,int z,int a,int b,int c,int h,int v){


    }



}
