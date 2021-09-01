package pl.pjatk.softdrive.rest.controllers;

import android.app.Application;
import android.os.Build;

import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.N)
public class ActivitySingleton extends Application {

    private static ActivitySingleton singleton;

    /**
     * Initialize Application super class and singleton reference
     *
     * @see Application
     */
    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;
    }

    /**
     * singeton access getter
     *
     * @return singleton of this object instance
     */
    public static synchronized ActivitySingleton getInstance() {
        return singleton;
    }
}

