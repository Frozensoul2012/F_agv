package cn.agv.utils;

import android.content.Context;
import android.util.Log;
import cn.agv.IIC.IICUtils;
import cn.agv.bean.AgvState;

/**
 * Created by chenjy on 2017/10/30.
 */

public class AgvManager {


    private volatile AgvState agvState;
    private static AgvManager instance;
    private Context context;


    public static AgvManager getInstance(Context context) {
        if(instance == null) {
            synchronized(AgvManager.class) {
                if(instance == null) {
                    instance = new AgvManager(context);
                }
            }
        }

        return instance;
    }

    private AgvManager(Context context) {
        this.context = context;
        this.agvState = new AgvState();
    }

    public AgvState getAgvState(){

        final byte[] datas = IICUtils.IICRead(AgvConstant.IIC_READ_DATA_LENGTH);
        Log.i("IIC",SerialDataUtils.ByteArrToHex(datas));

        byte[] rfid = new byte[4];
        byte[] magn = new byte[2];
        byte[] utf = new byte[8];
        byte[] utb = new byte[8];

        agvState.setCmdtype(datas[0]&0XFF);
        agvState.setPkgLength(datas[1]&0XFF);
        agvState.setSeq(datas[2]&0XFF);
        agvState.setVx((datas[3]&0XFF) | ((datas[4])<<8));
        agvState.setVy((datas[5]&0XFF) | ((datas[6])<<8));
        agvState.setAxisx((datas[7]&0XFF) | ((datas[8])<<8));
        agvState.setAxisy((datas[9]&0XFF) | ((datas[10])<<8));
        agvState.setOri((datas[11]&0XFF) | ((datas[12])<<8));
        System.arraycopy(datas,13,rfid,0,rfid.length);
        agvState.setRfid(SerialDataUtils.ByteArrToHex(rfid));
        System.arraycopy(datas,17,magn,0,magn.length);
        agvState.setMagn(SerialDataUtils.ByteArrToHex(magn));
        agvState.setInfral((datas[19] & (1 << 1))!=0);
        agvState.setInfrar((datas[19] & (1 << 2))!=0);
        System.arraycopy(datas,20,utf,0,utf.length);
        agvState.setUtf(SerialDataUtils.ByteArrToHex(utf));
        System.arraycopy(datas,28,utb,0,utb.length);
        agvState.setUtb(SerialDataUtils.ByteArrToHex(utb));
        agvState.setMotostate((datas[36] & (1 << 1))!=0);
        return agvState;
    }

    public void sendControlCmd(int req,int vx,int vy,int sx,int sy,boolean isreset){

        byte[] cmd = new byte[AgvConstant.IIC_CONTROL_CMD_LENGTH];

        cmd[0] = (byte)AgvConstant.CMD_TYPE_CONTROL;
        cmd[1] = (byte)AgvConstant.IIC_CONTROL_CMD_LENGTH;
        cmd[2] = (byte)(req & 0xFF);
        cmd[3] = (byte)(isreset?1:0);
        cmd[4] = (byte)(vx & 0xFF);
        cmd[5] = (byte)((vx >>8 ) & 0xFF);
        cmd[6] = (byte)(vy & 0xFF);
        cmd[7] = (byte)((vy >>8 ) & 0xFF);
        cmd[8] = (byte)(sx & 0xFF);
        cmd[9] = (byte)((sx >>8 ) & 0xFF);
        cmd[10] = (byte)(sy & 0xFF);
        cmd[11] = (byte)((sy >>8 ) & 0xFF);
        IICUtils.IICSend(cmd);
    }

    /**
     * 控制麦克纳姆车五轴运动的指令
     * @param seq 指令序号, 从0～255自增, 不断重复
     * @param idx 运动方向, 0:前后 1:旋转 2:平移 3:左上和右下 4:右上和左下
     * @param s 行驶里程, 1 = (1/100)r
     * @param v 行驶速度, 范围-500～500, (1/10)r/min
     * @param isreset 是否抢占
     */
    public void sendMecanumControlCmd(int seq, int idx, int s, int v, boolean isreset){
        Log.i("cmd", "seq=" + seq + ", idx=" + idx + ",s=" + s + ",v=" + v + ",isreset=" + isreset);
        byte[] cmd = new byte[AgvConstant.IIC_MECANUM_CONTROL_CMD_LENGTH];//9
        cmd[0] = (byte)AgvConstant.CMD_TYPE_CONTROL;//0
        cmd[1] = (byte)AgvConstant.IIC_MECANUM_CONTROL_CMD_LENGTH;//9
        cmd[2] = (byte)(seq & 0xFF);
        cmd[3] = (byte)(isreset ? 1 : 0);
        cmd[4] = (byte)(idx & 0xFF);
        cmd[5] = (byte)(s & 0xFF);
        cmd[6] = (byte)((s >> 8) & 0xFF);
        cmd[7] = (byte)(v & 0xFF);
        cmd[8] = (byte)((v >> 8) & 0xFF);
        IICUtils.IICSend(cmd);
    }

    public void sendAbsSystem(int req,int axisx,int axisy,int ori){

        byte[] cmd = new byte[AgvConstant.IIC_RESET_CMD_LENGTH];
        cmd[0] = (byte)AgvConstant.CMD_TYPE_ABS;
        cmd[1] = (byte)AgvConstant.IIC_RESET_CMD_LENGTH;
        cmd[2] = (byte)(req & 0xFF);
        cmd[3] = (byte)(axisx & 0xFF);
        cmd[4] = (byte)((axisx >>8 ) & 0xFF);
        cmd[5] = (byte)(axisy & 0xFF);
        cmd[6] = (byte)((axisy >>8 ) & 0xFF);
        cmd[7] = (byte)(ori & 0xFF);
        cmd[8] = (byte)((ori >>8 ) & 0xFF);

        IICUtils.IICSend(cmd);
    }

    public void sendChange(int req,int model){

        byte[] cmd = new byte[AgvConstant.IIC_CHANGECONTROLLER_CMD_LENGTH];
        cmd[0] = (byte)AgvConstant.CMD_TYPE_CONTROLLER;
        cmd[1] = (byte)AgvConstant.IIC_CHANGECONTROLLER_CMD_LENGTH;
        cmd[2] = (byte)(req & 0xFF);
        cmd[3] = (byte)(model & 0xFF);

        IICUtils.IICSend(cmd);
    }
}
