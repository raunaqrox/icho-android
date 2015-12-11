package in.icho;

import android.app.Application;
import android.content.Context;

/**
 * Created by abs on 11/12/15.
 */
public class IchoApplication extends Application {

    public static Context applicationContext;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = getApplicationContext();
    }
}
