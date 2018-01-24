package ostrich.com.ostrich_robot;

import android.app.Application;
import android.content.Context;

/**
 * Created by chenjy on 2017/10/24.
 */

public class App extends Application {

    private static App app = null;

    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        if(app == null){
            app = this;
        }


    }

    public static App getInstance() {
        return app;
    }

    public static Context getContext(){
        return app.getApplicationContext();
    }

}
