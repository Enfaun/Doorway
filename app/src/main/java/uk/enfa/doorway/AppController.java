package uk.enfa.doorway;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.preference.PreferenceManager;

public class AppController extends Application {
    private static AppController instance;

    public static AppController getInstance() {
        return instance;
    }

    public static Context getContext(){
        return instance.getApplicationContext();
    }

    public static Resources iGetResources(){
        return instance.getResources();
    }

    public static String getToken(){
        return PreferenceManager.getDefaultSharedPreferences(getContext()).getString("token", "");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
