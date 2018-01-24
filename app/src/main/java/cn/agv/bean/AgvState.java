package cn.agv.bean;


/**
 * Created by chenjy on 2017/10/30.
 */

public class AgvState {

    private int cmdtype;
    private int pkgLength;
    private int seq;
    private int vx;
    private int vy;
    private float axisx;
    private float axisy;
    private float ori;
    private String rfid;
    private String magn;
    private boolean infral;
    private boolean infrar;
    private String utf;
    private String utb;
    private boolean motostate;

    public AgvState(){}

    public int getCmdtype(){return this.cmdtype;}

    public int getSeq(){
        return this.seq;
    }

    public int getPkgLength(){return this.pkgLength;}

    public int getVx(){
        return this.vx;
    }

    public int getVy(){
        return this.vy;
    }

    public float getAxisx(){
        return this.axisx;
    }

    public float getAxisy(){
        return this.axisy;
    }

    public float getOri(){
        return this.ori;
    }

    public String getRfid(){
        return this.rfid;
    }

    public String getMagn(){
        return this.magn;
    }

    public boolean getInfral(){
        return this.infral;
    }

    public boolean getInfrar(){
        return this.infrar;
    }

    public String getUtf(){
        return this.utf;
    }

    public String getUtb(){
        return this.utb;
    }

    public boolean getMotostate(){
        return  this.motostate;
    }

    public void setCmdtype(int cmdtype){this.cmdtype = cmdtype;}

    public void setSeq(int seq){
        this.seq = seq;
    }

    public void setPkgLength(int pkgLength){this.pkgLength = pkgLength;}

    public void setVx(int vx){this.vx = vx;}

    public void setVy(int vy){this.vy = vy;}

    public void setAxisx(float axisx){this.axisx = axisx;}

    public void setAxisy(float axisy){this.axisy = axisy;}

    public void setOri(float ori){this.ori = ori;}

    public void setRfid(String rfid){this.rfid = rfid;}

    public void setMagn(String magn){this.magn = magn;}

    public void setInfral(boolean infral){this.infral = infral;}

    public void setInfrar(boolean infrar){this.infrar = infrar;}

    public void setUtf(String utf){this.utf = utf;}

    public void setUtb(String utb){this.utb = utb;}

    public void setMotostate(boolean motostate){
        this.motostate = motostate;
    }

}
