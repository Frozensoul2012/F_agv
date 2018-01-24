package ostrich.com.ostrich_robot.utils;

import android.content.Context;
import android.widget.Toast;

public class ToastUtils {
    private static Toast toast;

    /**
     * 显示持续时间短的toast
     * @param context
     * @param message   显示信息
     */
    public static void showShort(Context context, CharSequence message){
        if(toast == null){
            toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        }else{
            toast.setText(message);
            toast.setDuration( Toast.LENGTH_SHORT);
        }
        toast.show();
    }

    /**
     * 显示持续时间长的toast
     * @param context
     * @param message   显示信息
     */
    public static void showLong(Context context, CharSequence message){
        if(toast == null){
            toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        }else{
            toast.setText(message);
            toast.setDuration( Toast.LENGTH_LONG);
        }
        toast.show();
    }


}
