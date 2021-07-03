package pl.pjatk.softdrive.rest;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

public class RestDistanceService extends IntentService {

    public RestDistanceService(){
        super("RestDistanceService");
    }

    public static Context context;

    public static Context getContext() {
        return context;
    }

    int count;

    @Override
    protected void onHandleIntent(Intent intent) {

        new RestDistanceCtrl(new IFromRestCallback() {

            @Override
            public void getScan2dResponse(Scan2d value) {

            }

            @Override
            public void getDistanceResponse(Distance value) {

                context = getApplicationContext();

                System.out.println("Distance: " + value.getDistance());
            }

        });
    }
}