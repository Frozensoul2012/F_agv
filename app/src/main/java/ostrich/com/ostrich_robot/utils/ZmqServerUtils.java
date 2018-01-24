package ostrich.com.ostrich_robot.utils;

import android.util.Log;

import org.zeromq.ZContext;
import org.zeromq.ZMQ;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by chenjy on 2017/10/26.
 */

public class ZmqServerUtils {


    private ZMQ.Socket receiveSocket;
    private volatile boolean stopReceiving;

    private ExecutorService receiveService = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());

    private Runnable receiveTask = new Runnable() {

        @Override
        public void run() {
            Log.i("zmq", "Trying to receive");
            try {
                stopReceiving = false;
                while (!stopReceiving) {
                    final byte[] bytes = receiveSocket.recv();
                    if (bytes != null) {
                        Log.i("zmq", bytes.toString());
                    }
                    // todo test
                    /*if (bytes != null) {
                        byte[] data = new byte[2 + bytes.length];
                        System.arraycopy(bytes, 0, data, 2, bytes.length);

                        Stm32Utils.stm32Send(data);
                    }*/
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };


    public void initZmq(String ip) {
        setEthIp(ip);
    }

    /**
     * @param ip 192.168.0.3
     */

    public void setEthIp(String ip) {
        DataOutputStream dos = null;
        Process process = null;
        try {
            process = Runtime.getRuntime().exec("ifconfig eth0 " + ip + " netmask 255.255.252.0\n");
            dos = new DataOutputStream(process.getOutputStream());
            dos.writeBytes("exit\n");
            dos.flush();
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (dos != null) {
                try {
                    dos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (process != null) {
                process.destroy();
            }
        }
    }

    /**
     * @param port 7766
     */

    public void startRecive(int port) {

        ZContext context = new ZContext();
        receiveSocket = context.createSocket(ZMQ.DEALER);
        receiveSocket.setReceiveTimeOut(5000);
        receiveSocket.bind("tcp://*:" + port + "");

        receiveService.submit(receiveTask);

    }

    public void stopRecive() {
        stopReceiving = true;
    }

    public void sendTask(String task) {
        receiveSocket.send(task);
    }



}
