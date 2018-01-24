package ostrich.com.ostrich_robot;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import org.zeromq.ZMQ;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnTouch;
import ostrich.com.ostrich_robot.utils.ToastUtils;

/**
 * 新麦克纳姆车手机端控制类
 */
public class MecanumControlActivity extends BaseActivity{
    private static final String TAG = MecanumControlActivity.class.getSimpleName();

    @InjectView(R.id.ll_overall)
    LinearLayout ll_overall;
    @InjectView(R.id.btn_stop)
    Button btn_stop;
    @InjectView(R.id.btn_up)
    Button btn_up;
    @InjectView(R.id.btn_down)
    Button btn_down;
    @InjectView(R.id.btn_left)
    Button btn_left;
    @InjectView(R.id.btn_right)
    Button btn_right;
    @InjectView(R.id.btn_left_up)
    Button btn_left_up;
    @InjectView(R.id.btn_left_down)
    Button btn_left_down;
    @InjectView(R.id.btn_right_up)
    Button btn_right_up;
    @InjectView(R.id.btn_right_down)
    Button btn_right_down;
    @InjectView(R.id.btn_rotate_left)
    Button btn_rotate_left;
    @InjectView(R.id.btn_rotate_right)
    Button btn_rotate_right;
    @InjectView(R.id.btn_lock)
    Button btn_lock;

    private ZMQ.Context context;
    private ZMQ.Socket publisher;
    private boolean isLocked = true;
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            hideLockButton();
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.index);//使用圆形转向盘
        setContentView(R.layout.activity_mecanum_control);
        ButterKnife.inject(this);

        //ZMQ发布初始化
        initZmqPub();
    }

    @OnTouch(R.id.ll_overall)
    public boolean onTouch(View view, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (btn_lock.getVisibility() == View.GONE) {
                Log.i(TAG, "touch, 锁原本不可见, 变为可见");
                btn_lock.setVisibility(View.VISIBLE);
            }
        }else if (event.getAction() == MotionEvent.ACTION_UP) {
            Log.i(TAG, "touch handler 5秒后发送空消息, 让锁隐藏");
            removeMsgAndResend(0, 5000);
        }else{
            //不处理
        }
        return true;
    }

    @OnClick({R.id.btn_stop, R.id.btn_up, R.id.btn_down, R.id.btn_left, R.id.btn_right, R.id.btn_left_up, R.id.btn_left_down,
            R.id.btn_right_up, R.id.btn_right_down, R.id.btn_rotate_left, R.id.btn_rotate_right, R.id.btn_lock})
    public void onClick(View view) {
        //点击按钮时显示锁按钮
        if(btn_lock.getVisibility() == View.GONE){
            btn_lock.setVisibility(View.VISIBLE);
        }
        if (isLocked) {
            //已锁屏
            if (view.getId() == R.id.btn_lock) {
                Log.i(TAG, "原本已锁屏,现解锁,5秒后隐藏");
                isLocked = false;
                btn_lock.setBackgroundResource(R.drawable.unlocked);
                ToastUtils.showShort(MecanumControlActivity.this, "已解锁");
                removeMsgAndResend(0, 5000);
            }else{
                ToastUtils.showShort(MecanumControlActivity.this, "请先解除右上角锁定按钮才能发送指令");
            }
        } else {
            //未锁屏, 允许发指令
            switch (view.getId()) {
                case R.id.btn_stop:
                    sendRemoteCommand("stop");
                    ToastUtils.showShort(MecanumControlActivity.this, "停止");
                    break;

                case R.id.btn_up:
                    sendRemoteCommand("forward");
                    ToastUtils.showShort(MecanumControlActivity.this, "前进");
                    break;

                case R.id.btn_down:
                    sendRemoteCommand("backward");
                    ToastUtils.showShort(MecanumControlActivity.this, "后退");
                    break;

                case R.id.btn_left:
                    sendRemoteCommand("left");
                    ToastUtils.showShort(MecanumControlActivity.this, "左移");
                    break;

                case R.id.btn_right:
                    sendRemoteCommand("right");
                    ToastUtils.showShort(MecanumControlActivity.this, "右移");
                    break;

                case R.id.btn_left_up:
                    sendRemoteCommand("left-forward");
                    ToastUtils.showShort(MecanumControlActivity.this, "左前");
                    break;

                case R.id.btn_right_down:
                    sendRemoteCommand("right-backward");
                    ToastUtils.showShort(MecanumControlActivity.this, "右后");
                    break;

                case R.id.btn_right_up:
                    sendRemoteCommand("right-forward");
                    ToastUtils.showShort(MecanumControlActivity.this, "右前");
                    break;

                case R.id.btn_left_down:
                    sendRemoteCommand("left-backward");
                    ToastUtils.showShort(MecanumControlActivity.this, "左后");
                    break;

                case R.id.btn_rotate_left:
                    sendRemoteCommand("rotate-left");
                    ToastUtils.showShort(MecanumControlActivity.this, "左转");
                    break;

                case R.id.btn_rotate_right:
                    sendRemoteCommand("rotate-right");
                    ToastUtils.showShort(MecanumControlActivity.this, "右转");
                    break;

                case R.id.btn_lock:
                    Log.i(TAG, "原本未锁屏,现锁定");
                    isLocked = true;
                    btn_lock.setBackgroundResource(R.drawable.locked);
                    ToastUtils.showShort(MecanumControlActivity.this, "已锁定");
                    break;

                default:
                    break;
            }
        }
        Log.i(TAG, "移除msg,5秒后隐藏,islockd=" + isLocked);
        removeMsgAndResend(0, 5000);
    }

    //移除匹配Message.what属性的消息, 并重新延迟时间发送消息
    private void removeMsgAndResend(int what, long delayMillis){
        handler.removeMessages(what);
        handler.sendEmptyMessageDelayed(what, delayMillis);
    }

    private void hideLockButton() {
        Log.i(TAG, "锁定并隐藏按钮");
        if(!isLocked) {
            isLocked = true;
            btn_lock.setBackgroundResource(R.drawable.locked);
            ToastUtils.showShort(MecanumControlActivity.this, "已自动锁定");
        }
        btn_lock.setVisibility(View.GONE);
    }

    /**
     * 远程(板控、遥控等控制方式)控制端发送命令, 包含五个轴向共10个方向的运动
     * 命令分别为stop、forward、backward、left、right、left-forward、right-backward、right-forward、left-backward、rotate-right、rotate-left
     * 其含义分别为停止、前进、后退、向左平移、向右平移、左前、右后、右前、左后、右转、左转
     *
     * @param remoteCommand 控制端发出的命令, 该命令并非麦克纳姆车的直接驱动指令
     */
    private void sendRemoteCommand(String remoteCommand) {
        //只发送指定的命令字符串, 否则不执行
        Log.i(TAG, "远程控制端发送的运动指令remoteCommand=" + remoteCommand);
        switch (remoteCommand) {
            case "stop":
            case "forward":
            case "backward":
            case "left":
            case "right":
            case "left-forward":
            case "right-backward":
            case "right-forward":
            case "left-backward":
            case "rotate-right":
            case "rotate-left":
                publisher.send(remoteCommand);
                break;

            default:
                break;
        }
    }

    //ZMQ发布消息初始化
    private void initZmqPub() {
        //发布消息
        try {
            context = ZMQ.context(1);
            publisher = context.socket(ZMQ.PUB);
            publisher.bind("tcp://*:5556");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        if (publisher != null)
            publisher.close();
        if (context != null)
            context.term();
        super.onDestroy();
    }

}
