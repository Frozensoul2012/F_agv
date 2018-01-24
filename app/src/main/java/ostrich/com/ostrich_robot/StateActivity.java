package ostrich.com.ostrich_robot;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.agv.bean.AgvState;
import cn.agv.utils.AgvConstant;
import cn.agv.utils.AgvManager;
import ostrich.com.ostrich_robot.utils.StateConstant;
import ostrich.com.ostrich_robot.utils.ToastUtils;
import ostrich.com.ostrich_robot.utils.ZmqClientUtils;
import ostrich.com.ostrich_robot.utils.ZmqServerUtils;
import ostrich.com.ostrich_robot.utils.ostrichConstants;

/**
 * Created by chenjy on 2017/10/24.
 */

public class StateActivity extends BaseActivity {

    @InjectView(R.id.tv_cmdTypeSg)
    ImageView tv_cmdTypeSg;
    @InjectView(R.id.tv_pkgLengthSg)
    ImageView tv_pkgLengthSg;
    @InjectView(R.id.tv_seqSg)
    ImageView tv_seqSg;
    @InjectView(R.id.tv_xaxisSg)
    ImageView tv_xaxisSg;
    @InjectView(R.id.tv_yaxisSg)
    ImageView tv_yaxisSg;
    @InjectView(R.id.tv_xaxisPSg)
    ImageView tv_xaxisPSg;
    @InjectView(R.id.tv_yaxisPSg)
    ImageView tv_yaxisPSg;
    @InjectView(R.id.tv_oriSg)
    ImageView tv_oriSg;
    @InjectView(R.id.tv_infralSg)
    ImageView tv_infralSg;
    @InjectView(R.id.tv_infrarSg)
    ImageView tv_infrarSg;
    @InjectView(R.id.tv_motoStateSg)
    ImageView tv_motoStateSg;
    @InjectView(R.id.tv_rfidSg)
    ImageView tv_rfidSg;
    @InjectView(R.id.tv_magnSg)
    ImageView tv_magnSg;
    @InjectView(R.id.tv_utfSg)
    ImageView tv_utfSg;
    @InjectView(R.id.tv_utbSg)
    ImageView tv_utbSg;

    @InjectView(R.id.tv_cmdTypeValue)
    TextView tv_cmdTypeValue;
    @InjectView(R.id.tv_pkgLengthValue)
    TextView tv_pkgLengthValue;
    @InjectView(R.id.tv_seqValue)
    TextView tv_seqValue;
    @InjectView(R.id.tv_xaxisValue)
    TextView tv_xaxisValue;
    @InjectView(R.id.tv_yaxisValue)
    TextView tv_yaxisValue;
    @InjectView(R.id.tv_xaxisPValue)
    TextView tv_xaxisPValue;
    @InjectView(R.id.tv_yaxisPValue)
    TextView tv_yaxisPValue;
    @InjectView(R.id.tv_oriValue)
    TextView tv_oriValue;
    @InjectView(R.id.tv_infralValue)
    TextView tv_infralValue;
    @InjectView(R.id.tv_infrarValue)
    TextView tv_infrarValue;
    @InjectView(R.id.tv_motoStateValue)
    TextView tv_motoStateValue;
    @InjectView(R.id.tv_rfidValue)
    TextView tv_rfidValue;
    @InjectView(R.id.tv_magnValue)
    TextView tv_magnValue;
    @InjectView(R.id.tv_utfValue)
    TextView tv_utfValue;
    @InjectView(R.id.tv_utbValue)
    TextView tv_utbValue;


    private AgvManager agvManager = AgvManager.getInstance(this);
    private Handler stateHandler = new Handler();
    private Runnable stateRunnable = null;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_state);
        context = this;
        ButterKnife.inject(this);

        stateRunnable = new Runnable() {
            @Override
            public void run() {

                //// TODO: 2017/11/4  两个电机 
                AgvState agvState = agvManager.getAgvState();

                int cmdType = agvState.getCmdtype();
                int pkglength = agvState.getPkgLength();
                int seq = agvState.getSeq();
                int xspeed = agvState.getVx();
                int yspeed = agvState.getVy();
                double xp = agvState.getAxisx();
                double yp = agvState.getAxisy();
                double ori = agvState.getOri();
                boolean infral = agvState.getInfral();
                boolean infrar = agvState.getInfrar();
                boolean motoState = agvState.getMotostate();
                String rfid = agvState.getRfid();
                String magn = agvState.getMagn();
                String utf = agvState.getUtf();
                String utb = agvState.getUtb();

                tv_cmdTypeValue.setText(""+cmdType);
                tv_pkgLengthValue.setText(""+pkglength);
                tv_seqValue.setText(""+seq);
                tv_xaxisValue.setText(""+xspeed);
                tv_yaxisValue.setText(""+yspeed);
                tv_xaxisPValue.setText(""+xp);
                tv_yaxisPValue.setText(""+yp);
                tv_oriValue.setText(""+ori);
                tv_infralValue.setText(""+infral);
                tv_infrarValue.setText(""+infrar);
                tv_motoStateValue.setText(""+motoState);
                tv_rfidValue.setText(""+rfid);
                tv_magnValue.setText(""+magn);
                tv_utfValue.setText(""+utf);
                tv_utbValue.setText(""+utb);

                tv_cmdTypeSg.setImageDrawable(cmdType == StateConstant.STATE_UPTYPE? ContextCompat.getDrawable(context,R.drawable.nss):ContextCompat.getDrawable(context,R.drawable.nas));
                tv_pkgLengthSg.setImageDrawable(pkglength == StateConstant.STATE_PKGLENGTH? ContextCompat.getDrawable(context,R.drawable.nss):ContextCompat.getDrawable(context,R.drawable.nas));
                tv_seqSg.setImageDrawable((seq >0)? ContextCompat.getDrawable(context,R.drawable.nss):ContextCompat.getDrawable(context,R.drawable.nas));
                tv_xaxisSg.setImageDrawable((xspeed < StateConstant.STATE_MAX_SPEED && xspeed > StateConstant.STATE_MIN_SPEED)? ContextCompat.getDrawable(context,R.drawable.nss):ContextCompat.getDrawable(context,R.drawable.nas));
                tv_yaxisSg.setImageDrawable((yspeed < StateConstant.STATE_MAX_SPEED && yspeed > StateConstant.STATE_MIN_SPEED)? ContextCompat.getDrawable(context,R.drawable.nss):ContextCompat.getDrawable(context,R.drawable.nas));

                tv_seqSg.setImageDrawable((seq == 0)? ContextCompat.getDrawable(context,R.drawable.zeros):ContextCompat.getDrawable(context,R.drawable.nss));
                tv_xaxisPSg.setImageDrawable((xp == 0)? ContextCompat.getDrawable(context,R.drawable.zeros):ContextCompat.getDrawable(context,R.drawable.nss));
                tv_yaxisPSg.setImageDrawable((yp == 0)? ContextCompat.getDrawable(context,R.drawable.zeros):ContextCompat.getDrawable(context,R.drawable.nss));


                tv_oriSg.setImageDrawable((ori == 0)? ContextCompat.getDrawable(context,R.drawable.zeros):ContextCompat.getDrawable(context,R.drawable.nss));

                tv_infralSg.setImageDrawable((infral)? ContextCompat.getDrawable(context,R.drawable.nas):ContextCompat.getDrawable(context,R.drawable.nss));
                tv_infrarSg.setImageDrawable((infrar)? ContextCompat.getDrawable(context,R.drawable.nas):ContextCompat.getDrawable(context,R.drawable.nss));

                tv_motoStateSg.setImageDrawable((motoState)? ContextCompat.getDrawable(context,R.drawable.nss):ContextCompat.getDrawable(context,R.drawable.zeros));

                tv_rfidSg.setImageDrawable((rfid.equals(StateConstant.EMPTY_RFID))? ContextCompat.getDrawable(context,R.drawable.zeros):ContextCompat.getDrawable(context,R.drawable.nss));
                tv_magnSg.setImageDrawable((magn.equals(StateConstant.EMPTY_MAGN))? ContextCompat.getDrawable(context,R.drawable.zeros):ContextCompat.getDrawable(context,R.drawable.nss));
                tv_utfSg.setImageDrawable((utf.equals(StateConstant.EMPTY_UT))? ContextCompat.getDrawable(context,R.drawable.zeros):ContextCompat.getDrawable(context,R.drawable.nss));
                tv_utbSg.setImageDrawable((utb.equals(StateConstant.EMPTY_UT))? ContextCompat.getDrawable(context,R.drawable.zeros):ContextCompat.getDrawable(context,R.drawable.nss));



                stateHandler.postDelayed(this,100);
            }
        };
        stateHandler.postDelayed(stateRunnable,100);
    }


    @OnClick({R.id.ll_navigation_movement,R.id.ll_navigation_local})
    void onMainViewClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.ll_navigation_movement:
                intent = new Intent(StateActivity.this,MainActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_navigation_local:
                break;
            default:;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        stateHandler.removeCallbacks(stateRunnable);
    }
}
