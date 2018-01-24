package ostrich.com.ostrich_robot;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.zeromq.ZMQ;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.agv.utils.AgvManager;

/**
 * Created by chenjy on 2017/10/24.
 */

public class MecanumMainActivity extends BaseActivity {
    private static final String TAG = MecanumMainActivity.class.getSimpleName();

    @InjectView(R.id.stop)
    Button stop;
    @InjectView(R.id.forward)
    Button forward;
    @InjectView(R.id.backward)
    Button backward;
    @InjectView(R.id.left)
    Button left;
    @InjectView(R.id.right)
    Button right;
    @InjectView(R.id.left_forward)
    Button left_forward;
    @InjectView(R.id.right_backward)
    Button right_backward;
    @InjectView(R.id.right_forward)
    Button right_forward;
    @InjectView(R.id.left_backward)
    Button left_backward;
    @InjectView(R.id.rotate_left)
    Button rotate_left;
    @InjectView(R.id.rotate_right)
    Button rotate_right;

    private AgvManager agvManager = AgvManager.getInstance(this);
    private int seq = 0;
    private boolean enableReceive = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mecanum_main);
        ButterKnife.inject(this);

        zmqSub();
    }

    @OnClick({R.id.stop, R.id.forward, R.id.backward, R.id.left, R.id.right, R.id.left_forward, R.id.right_backward,
            R.id.right_forward, R.id.left_backward, R.id.rotate_left, R.id.rotate_right})
    void onAddViewClick(View view) {
        switch (view.getId()) {
            case R.id.stop:
                //停止, v=0
                Log.i("Meca", "stop,v=0");
                sendCommand("stop", 0);
//                agvManager.sendMecanumControlCmd(seq, 0, 0, 0, false);
                break;

            case R.id.forward:
                //向前, idx=0
                Log.i("Meca", "前: idx=0, v>0");
                sendCommand("forward", 0);
//                agvManager.sendMecanumControlCmd(seq, 0, 0, 30, false);
                break;

            case R.id.backward:
                //向后, idx=0
                Log.i("Meca", "后,idx=0,v<0");
                sendCommand("backward", 0);
//                agvManager.sendMecanumControlCmd(seq, 0, 0, -30, false);
                break;

            case R.id.left:
                //向左平移, idx=2
                Log.i("Meca", "左,idx=2, v>0");
                sendCommand("left", 0);
//                agvManager.sendMecanumControlCmd(seq, 2, 0, 30, false);
                break;

            case R.id.right:
                //向右平移, idx=2
                Log.i("Meca", "右,idx=2, v<0");
                sendCommand("right", 0);
//                agvManager.sendMecanumControlCmd(seq, 2, 0, -30, false);
                break;

            case R.id.left_forward:
                //左上, idx=3
                Log.i("Meca", "左上,idx=3, v>0");
                sendCommand("left-forward", 0);
//                agvManager.sendMecanumControlCmd(seq, 3, 0, 30, false);
                break;

            case R.id.right_backward:
                //右下, idx=3
                Log.i("Meca", "右下,idx=3,v<0");
                sendCommand("right-backward", 0);
//                agvManager.sendMecanumControlCmd(seq, 3, 0, -30, false);
                break;

            case R.id.right_forward:
                //右上, idx=4
                Log.i("Meca", "右上,idx=4,v>0");
                sendCommand("right-forward", 0);
//                agvManager.sendMecanumControlCmd(seq, 4, 0, 30, false);
                break;

            case R.id.left_backward:
                //左下, idx=4
                Log.i("Meca", "左下,idx=4,v<0");
                sendCommand("left-backward", 0);
//                agvManager.sendMecanumControlCmd(seq, 4, 0, -30, false);
                break;

            case R.id.rotate_left:
                //左转, idx=1
                Log.i("Meca", "左转,idx=1,v<0");
                sendCommand("rotate-left", 0);
//                agvManager.sendMecanumControlCmd(seq, 1, 0, -30, false);
                break;

            case R.id.rotate_right:
                //右转, idx=1
                Log.i("Meca", "右转,idx=1, v>0");
                sendCommand("rotate-right", 0);
//                agvManager.sendMecanumControlCmd(seq, 1, 0, 30, false);
                break;
        }
        seqIncrement();
    }

    //指令序号自增,如果超过255则重置为0
    private void seqIncrement(){
        seq++;
        if(seq == 256){
            seq = 0;
        }
        Log.i("Mecanum", "自增后seq=" + seq);
    }

    //订阅
    private void zmqSub(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                ZMQ.Context context = ZMQ.context(1);
                ZMQ.Socket subscriber = context.socket(ZMQ.SUB);
                //连接到发布端的ip和端口
                subscriber.connect("tcp://192.168.99.143:5556");
                //设置订阅的topic,只接收发布的以topic内容开头的消息,如果是空字符串表示全部接收
                //可设置多个订阅
                subscriber.subscribe("".getBytes());//接收所有消息
                while (enableReceive) {
                    String msg = subscriber.recvStr();
                    System.out.println("订阅端接收到:" + msg);
                    //遥控
                    sendCommand(msg, 1);
                    seqIncrement();
                }
                subscriber.close();
                context.term();
            }
        }).start();
    }

    /**
     * 发送麦克纳姆车指令
     * @param command 远程端设备发送的控制命令字符串,根据该命令字符串发送相应的控制指令
     * @param controlType 控制方式 0:板控 1:遥控
     */
    private void sendCommand(String command, int controlType){
        switch (command){
            case "stop":
                //停止, v = 0
                Log.i(TAG, "停止");
                agvManager.sendMecanumControlCmd(seq, 0, 0, 0, false);
                break;

            case "forward":
                //前进, idx = 0, v > 0
                Log.i(TAG, "前进");
                if(controlType == 0){
                    agvManager.sendMecanumControlCmd(seq, 0, 10, 30, false);
                }else if(controlType == 1){
                    agvManager.sendMecanumControlCmd(seq, 0, 0, 30, false);
                }else{

                }
                break;

            case "backward":
                //后退, idx = 0, v < 0
                Log.i(TAG, "后退");
                if(controlType == 0){
                    agvManager.sendMecanumControlCmd(seq, 0, 10, -30, false);
                }else if(controlType == 1){
                    agvManager.sendMecanumControlCmd(seq, 0, 0, -30, false);
                }else{

                }
                break;

            case "rotate-right":
                //右转, idx = 1, v > 0
                Log.i(TAG, "右转");
                if(controlType == 0){
                    agvManager.sendMecanumControlCmd(seq, 1, 10, 30, false);
                }else if(controlType == 1){
                    agvManager.sendMecanumControlCmd(seq, 1, 0, 30, false);
                }else{

                }
                break;

            case "rotate-left":
                //左转, idx = 1, v < 0
                Log.i(TAG, "左转");
                if(controlType == 0){
                    agvManager.sendMecanumControlCmd(seq, 1, 10, -30, false);
                }else if(controlType == 1){
                    agvManager.sendMecanumControlCmd(seq, 1, 0, -30, false);
                }else{

                }
                break;

            case "left":
                //向左平移, idx = 2, v > 0
                Log.i(TAG, "左移");
                if(controlType == 0){
                    agvManager.sendMecanumControlCmd(seq, 2, 10, 30, false);
                }else if(controlType == 1){
                    agvManager.sendMecanumControlCmd(seq, 2, 0, 30, false);
                }else{

                }
                break;

            case "right":
                //向右平移, idx = 2, v < 0
                Log.i(TAG, "右移");
                if(controlType == 0){
                    agvManager.sendMecanumControlCmd(seq, 2, 10, -30, false);
                }else if(controlType == 1){
                    agvManager.sendMecanumControlCmd(seq, 2, 0, -30, false);
                }else{

                }
                break;

            case "left-forward":
                //左上/左前, idx = 3, v > 0
                Log.i(TAG, "左上");
                if(controlType == 0){
                    agvManager.sendMecanumControlCmd(seq, 3, 10, 30, false);
                }else if(controlType == 1){
                    agvManager.sendMecanumControlCmd(seq, 3, 0, 30, false);
                }else{

                }
                break;

            case "right-backward":
                //右下/右后, idx = 3, v < 0
                Log.i(TAG, "右下");
                if(controlType == 0){
                    agvManager.sendMecanumControlCmd(seq, 3, 10, -30, false);
                }else if(controlType == 1){
                    agvManager.sendMecanumControlCmd(seq, 3, 0, -30, false);
                }else{

                }
                break;

            case "right-forward":
                //右上/右前, idx = 4, v > 0
                Log.i(TAG, "右上");
                if(controlType == 0){
                    agvManager.sendMecanumControlCmd(seq, 4, 10, 30, false);
                }else if(controlType == 1){
                    agvManager.sendMecanumControlCmd(seq, 4, 0, 30, false);
                }else{

                }
                break;

            case "left-backward":
                //左下/左后, idx = 4, v < 0
                Log.i(TAG, "左下");
                if(controlType == 0){
                    agvManager.sendMecanumControlCmd(seq, 4, 10, -30, false);
                }else if(controlType == 1){
                    agvManager.sendMecanumControlCmd(seq, 4, 0, -30, false);
                }else{

                }
                break;

            default:
                break;
        }
    }



}
