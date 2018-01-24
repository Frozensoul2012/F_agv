package ostrich.com.ostrich_robot.utils;


import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android_serialport_api.SerialPort;

/**
 * Created by chenjy on 2017/10/24.
 */

public class RfidSensorUtils {

    private static RfidSensorUtils instance;

    private static SerialPort serialPort;
    private static String sPort = "/dev/ttyUSB0";
    private static int iBaudRate = 9600;

    private volatile byte[] rfid = new byte[4];
    private volatile boolean isReadStop = true;
    private volatile long startReadingTime = 0;

    private static int TIMEOUT = 100;

    public static RfidSensorUtils getInstance() {
        if(instance == null) {
            synchronized(RfidSensorUtils.class) {
                if(instance == null) {
                    instance = new RfidSensorUtils();
                }
            }
        }
        return instance;
    }

    public void open(){
        isReadStop = false;
        connect();
    }

    public synchronized boolean connect() {
        try {
            serialPort = new SerialPort(new File(sPort), iBaudRate, 0);

            ThreadFactory rfidFactory = new ThreadFactoryBuilder().setNameFormat("rfid-pool-%d").build();
            ExecutorService rfidService = new ThreadPoolExecutor(1,1,0L, TimeUnit.MILLISECONDS,new LinkedBlockingDeque<Runnable>(),rfidFactory);

            rfidService.submit(rfidRunnable);

            new Thread().start();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private Runnable rfidRunnable = new Runnable() {
        @Override
        public void run() {
            InputStream is = serialPort.getInputStream();
            OutputStream os = serialPort.getOutputStream();

            while (!isReadStop) {
                try {
                    startReadingTime = System.currentTimeMillis();
                    byte[] buffer = new byte[14];
                    if (is != null) {
                        int size = is.read(buffer);
                        if(size > 0 ){
                            System.arraycopy(buffer, size-6, rfid, 0, rfid.length);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    if(serialPort!=null){
                        serialPort.close();
                        serialPort = null;
                    }
                }
            }
            return;
        }
    };

    /**
     *  获取当前rfid
     */
    public String getData(){
        if(System.currentTimeMillis() - startReadingTime > TIMEOUT){
            return "";
        }
        return SerialDataUtils.ByteArrToHex(rfid);
    }

    public boolean isConnect(){
        return !isReadStop;
    }

    public void close(){
        isReadStop = true;
        if(serialPort != null){
            serialPort.close();
        }
    }

}
