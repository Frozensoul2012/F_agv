package ostrich.com.ostrich_robot;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.agv.bean.AgvState;
import cn.agv.utils.AgvManager;
import ostrich.com.ostrich_robot.utils.ToastUtils;
import ostrich.com.ostrich_robot.utils.ZmqClientUtils;
import ostrich.com.ostrich_robot.utils.ZmqServerUtils;
import ostrich.com.ostrich_robot.utils.ostrichConstants;

/**
 * Created by chenjy on 2017/10/24.
 */

public class MainActivity extends BaseActivity {
    @InjectView(R.id.tv_a)
    TextView tva;
    @InjectView(R.id.tv_b)
    TextView tvb;
    @InjectView(R.id.tv_c)
    TextView tvc;
    @InjectView(R.id.tv_x)
    TextView tvx;
    @InjectView(R.id.tv_y)
    TextView tvy;
    @InjectView(R.id.tv_z)
    TextView tvz;
    @InjectView(R.id.tv_h)
    TextView tvh;
    @InjectView(R.id.tv_v)
    TextView tvv;



    @InjectView(R.id.btn_lock)
    TextView btnlock;


    private int aAxis = 0;
    private int bAxis = 0;
    private int cAxis = 0;
    private int xAxis = 0;
    private int yAxis = 0;
    private int zAxis = 0;
    private int hAxis = 0;
    private int vAxis = 0;

    private boolean isLock = false;

    private Handler uiUpdateHandler = new Handler();
    private Runnable uiUpdateRunnable =  new Runnable() {
        @Override
        public void run() {
            tva.setText(""+aAxis);
            tvb.setText(""+bAxis);
            tvc.setText(""+cAxis);
            tvx.setText(""+xAxis);
            tvy.setText(""+yAxis);
            tvz.setText(""+zAxis);
            tvh.setText(""+hAxis);
            tvv.setText(""+vAxis);
            uiUpdateHandler.postDelayed(this,2000);
        }
    };

    private ZmqServerUtils zmqServerUtils = new ZmqServerUtils();
    private ZmqClientUtils zmqClientUtils = new ZmqClientUtils();

    private AgvManager agvManager = AgvManager.getInstance(this);
    private Handler stateHandler = new Handler();
    private Runnable stateRunnable = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);



        uiUpdateHandler.postDelayed(uiUpdateRunnable,100);

        stateRunnable = new Runnable() {
            @Override
            public void run() {

                AgvState agvState = agvManager.getAgvState();

                Log.i("AgvState","cmdtype:"+agvState.getCmdtype()+",pkgLength:"+agvState.getPkgLength()+",seq:"+agvState.getSeq());
                Log.i("AgvState","vx:"+agvState.getVx()+",vy:"+agvState.getVy()+",axisx:"+agvState.getAxisx()+",axisy:"+agvState.getAxisy()+",ori:"+agvState.getOri());
                Log.i("AgvState","rfid:"+agvState.getRfid());
                Log.i("AgvState","magn:"+agvState.getMagn());
                Log.i("AgvState","infral:"+agvState.getInfral()+",infrar:"+agvState.getInfrar());
                Log.i("AgvState","utf:"+agvState.getUtf());
                Log.i("AgvState","utb:"+agvState.getUtb());
                Log.i("AgvState","motostate:"+agvState.getMotostate());

                stateHandler.postDelayed(this,100);
            }
        };
        stateHandler.postDelayed(stateRunnable,100);
    }

    @OnClick({R.id.tv_aadd,R.id.tv_badd,R.id.tv_cadd,R.id.tv_xadd,R.id.tv_yadd,R.id.tv_zadd,R.id.tv_hadd,R.id.tv_vadd})
    void onAddViewClick(View view) {
        if(isLock){return;}
        switch (view.getId()) {
            case R.id.tv_aadd:
                if(aAxis <= ostrichConstants.maxA ){
                    aAxis += 100;
                    updateUi();
                }else {
                    return;
                }
                break;
            case R.id.tv_badd:
                if(bAxis <= ostrichConstants.maxB ){
                    bAxis += 100;
                    updateUi();
                }else {
                    return;
                }
                break;
            case R.id.tv_cadd:
                if(cAxis <= ostrichConstants.maxC ){
                    cAxis += 100;
                    updateUi();
                }else {
                    return;
                }
                break;
            case R.id.tv_xadd:
                if(xAxis <= ostrichConstants.maxX ){
                    xAxis += 100;
                    updateUi();
                }else {
                    return;
                }
                break;
            case R.id.tv_yadd:
                if(yAxis <= ostrichConstants.maxY ){
                    yAxis += 100;
                    updateUi();
                }else {
                    return;
                }
                break;
            case R.id.tv_zadd:
                if(zAxis <= ostrichConstants.maxZ ){
                    zAxis += 100;
                    updateUi();
                }else {
                    return;
                }
                break;
            case R.id.tv_hadd:
                if(hAxis <= ostrichConstants.maxH ){
                    hAxis += 100;
                    updateUi();
                }else {
                    return;
                }
                break;
            case R.id.tv_vadd:
                if(vAxis <= ostrichConstants.maxV ){
                    vAxis += 100;
                    updateUi();
                }else {
                    return;
                }
                break;

            default:;
        }
    }

    @OnClick({R.id.tv_asub,R.id.tv_bsub,R.id.tv_csub,R.id.tv_xsub,R.id.tv_ysub,R.id.tv_zsub,R.id.tv_hsub,R.id.tv_vsub})
    void onSubViewClick(View view) {
        if(isLock){return;}
        switch (view.getId()) {
            case R.id.tv_asub:
                if(aAxis >= ostrichConstants.minA ){
                    aAxis -= 100;
                    updateUi();
                }else {
                    return;
                }
                break;
            case R.id.tv_bsub:
                if(bAxis >= ostrichConstants.minB ){
                    bAxis -= 100;
                    updateUi();
                }else {
                    return;
                }
                break;
            case R.id.tv_csub:
                if(cAxis >= ostrichConstants.minC ){
                    cAxis -= 100;
                    updateUi();
                }else {
                    return;
                }
                break;
            case R.id.tv_xsub:
                if(xAxis >= ostrichConstants.minX ){
                    xAxis -= 100;
                    updateUi();
                }else {
                    return;
                }
                break;
            case R.id.tv_ysub:
                if(yAxis >= ostrichConstants.minY ){
                    yAxis -= 100;
                    updateUi();
                }else {
                    return;
                }
                break;
            case R.id.tv_zsub:
                if(zAxis >= ostrichConstants.minZ ){
                    zAxis -= 100;
                    updateUi();
                }else {
                    return;
                }
                break;
            case R.id.tv_hsub:
                if(hAxis >= ostrichConstants.minH ){
                    hAxis -= 100;
                    updateUi();
                }else {
                    return;
                }
                break;
            case R.id.tv_vsub:
                if(vAxis >= ostrichConstants.minV ){
                    vAxis -= 100;
                    updateUi();
                }else {
                    return;
                }
                break;

            default:;
        }
    }

    @OnClick({R.id.tv_azero,R.id.tv_bzero,R.id.tv_czero,R.id.tv_xzero,R.id.tv_yzero,R.id.tv_zzero,R.id.tv_hzero,R.id.tv_vzero})
    void onZeroViewClick(View view) {
        if(isLock){return;}
        switch (view.getId()) {
            case R.id.tv_azero:
                aAxis = 0 ;
                updateUi();
                break;
            case R.id.tv_bzero:
                bAxis = 0;
                updateUi();
                break;
            case R.id.tv_czero:
                cAxis = 0;
                updateUi();
                break;
            case R.id.tv_xzero:
                xAxis = 0;
                updateUi();
                break;
            case R.id.tv_yzero:
                yAxis = 0;
                updateUi();
                break;
            case R.id.tv_zzero:
                zAxis = 0;
                updateUi();
                break;
            case R.id.tv_hzero:
                hAxis = 0;
                updateUi();
                break;
            case R.id.tv_vzero:
                vAxis = 0;
                updateUi();
                break;

            default:;
        }
    }

    @OnClick({R.id.tv_aup,R.id.tv_bup,R.id.tv_cup,R.id.tv_xup,R.id.tv_yup,R.id.tv_zup,R.id.tv_hup,R.id.tv_vup,R.id.tv_adown,R.id.tv_bdown,R.id.tv_cdown,R.id.tv_xdown,R.id.tv_ydown,R.id.tv_zdown,R.id.tv_hdown,R.id.tv_vdown})
    void onActionViewClick(View view) {
        if(isLock){return;}
        switch (view.getId()) {
            case R.id.tv_aup:
                agvManager.sendControlCmd(1,0,0,10,10,false);
                /*ToastUtils.showShort(MainActivity.this,"A+");*/
                ToastUtils.showShort(MainActivity.this,"Stop");
                break;
            case R.id.tv_bup:
                agvManager.sendControlCmd(1,30,30,0,0,false);
                /*ToastUtils.showShort(MainActivity.this,"B+");*/
                ToastUtils.showShort(MainActivity.this,"Forward");
                break;
            case R.id.tv_cup:
                agvManager.sendChange(1,1);
                ToastUtils.showShort(MainActivity.this,"遥控");
                /*ToastUtils.showShort(MainActivity.this,"C+");*/
                break;
            case R.id.tv_xup:
                agvManager.sendChange(1,0);
                ToastUtils.showShort(MainActivity.this,"板控");
                /*ToastUtils.showShort(MainActivity.this,"X+");*/
                break;
            case R.id.tv_yup:
                ToastUtils.showShort(MainActivity.this,"Y+");
                break;
            case R.id.tv_zup:
                ToastUtils.showShort(MainActivity.this,"Z+");
                break;
            case R.id.tv_hup:
                ToastUtils.showShort(MainActivity.this,"H+");
                break;
            case R.id.tv_vup:
                ToastUtils.showShort(MainActivity.this,"V+");
                break;

            case R.id.tv_adown:
                ToastUtils.showShort(MainActivity.this,"A-");
                break;
            case R.id.tv_bdown:
                ToastUtils.showShort(MainActivity.this,"B-");
                break;
            case R.id.tv_cdown:
                ToastUtils.showShort(MainActivity.this,"C-");
                break;
            case R.id.tv_xdown:
                ToastUtils.showShort(MainActivity.this,"X-");
                break;
            case R.id.tv_ydown:
                ToastUtils.showShort(MainActivity.this,"Y-");
                break;
            case R.id.tv_zdown:
                ToastUtils.showShort(MainActivity.this,"Z-");
                break;
            case R.id.tv_hdown:
                ToastUtils.showShort(MainActivity.this,"H-");
                break;
            case R.id.tv_vdown:
                ToastUtils.showShort(MainActivity.this,"V-");
                break;
            default:;
        }
    }

    @OnClick({R.id.tv_send,R.id.tv_stop,R.id.btn_lock})
    void onEventViewClick(View view) {
        switch (view.getId()) {
            case R.id.tv_send:
                if(isLock){return;}
                agvManager.sendControlCmd(1,20,20,10,10,false);
                ToastUtils.showShort(MainActivity.this,"send!");
                break;
            case R.id.tv_stop:
                if(isLock){return;}
                agvManager.sendAbsSystem(1,0,0,10);
                ToastUtils.showShort(MainActivity.this,"stop!");
                break;
            case R.id.btn_lock:
                if(isLock){
                    isLock = false;
                    btnlock.setBackgroundResource(R.drawable.off);
                }else {
                    isLock = true;
                    btnlock.setBackgroundResource(R.drawable.on);

                }
                break;

            default:;
        }
    }

    @OnClick({R.id.ll_navigation_movement,R.id.ll_navigation_local})
    void onMainViewClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.ll_navigation_movement:
                break;
            case R.id.ll_navigation_local:
                intent = new Intent(MainActivity.this,StateActivity.class);
                startActivity(intent);
                break;
            default:;
        }
    }

    private void updateUi(){
        tva.setText(""+aAxis);
        tvb.setText(""+bAxis);
        tvc.setText(""+cAxis);
        tvx.setText(""+xAxis);
        tvy.setText(""+yAxis);
        tvz.setText(""+zAxis);
        tvh.setText(""+hAxis);
        tvv.setText(""+vAxis);
    }



}
