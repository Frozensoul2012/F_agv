package ostrich.com.ostrich_robot;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.zeromq.ZContext;
import org.zeromq.ZMQ;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class ZMQActivity extends AppCompatActivity{
    @InjectView(R.id.btn)
    Button button;
    @InjectView(R.id.btn_start_sub)
    Button startSub;
    @InjectView(R.id.btn_stop_sub)
    Button stopSub;

    private boolean rece = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zmq);
        ButterKnife.inject(this);

    }

    @OnClick({R.id.btn, R.id.btn_start_sub, R.id.btn_stop_sub})
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn:
                zmqRequest();
                break;

            case R.id.btn_start_sub:
                System.out.println("点击start sub");
                zmqSub();
                break;

            case R.id.btn_stop_sub:
                System.out.println("点击stop sub");
                zmqStopSub();
                break;
        }
    }

    private void zmqRequest(){
        ZContext context = new ZContext();
        final ZMQ.Socket sendSocket = context.createSocket(ZMQ.REQ);
        sendSocket.connect("tcp://192.168.99.243:10000");
//        sendSocket.bind("tcp://*:10000");
        sendSocket.send("hello!");
        new Thread(new Runnable() {
            @Override
            public void run() {
                byte[] reply = sendSocket.recv(0);
                final String str = new String(reply);
                System.out.println(Thread.currentThread().getName() + " 收到回复-->" + str);
                ZMQActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ZMQActivity.this, "收到服务端回复:" + str, Toast.LENGTH_SHORT).show();
                        sendSocket.close();
                    }
                });
            }
        }).start();
    }

    //订阅消息
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
                subscriber.subscribe("".getBytes());
                while (rece) {
                    String string = subscriber.recvStr();
                    System.out.println("订阅端接收到:" + string);
                }
                subscriber.close();
                context.term();
            }
        }).start();
    }

    //ZMQ停止订阅,并使订阅端关闭
    private void zmqStopSub(){
        rece = false;
    }

    //发布消息
    public static void zmqPub(){
        //地址 : 192.168.99.243
        ZMQ.Context context = ZMQ.context(1);
        ZMQ.Socket publisher = context.socket(ZMQ.PUB);
        publisher.bind("tcp://*:5556");
        int pubCount = 0;
        while (!Thread.currentThread ().isInterrupted ()) {
            pubCount++;
            String msg = "发布端发布的消息内容[" + pubCount + "]";
            publisher.send(msg);
            System.out.println("发布端发布了消息-->" + msg);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        publisher.close ();
        context.term ();
    }

}
