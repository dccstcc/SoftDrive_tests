package pl.pjatk.softdrive.rest.controllers;

import android.app.Application;
import android.os.Build;

import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.N)
public class ActivitySingleton extends Application {

    private static ActivitySingleton singleton;

    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;
    }

    public static synchronized ActivitySingleton getInstance() {
        return singleton;
    }
}

