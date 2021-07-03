package pl.pjatk.softdrive.rest;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

public class RestScan2dService extends IntentService {

    public RestScan2dService(){
        super("RestScan2dService");
    }

    public static Context context;

    public static Context getContext() {
        return context;
    }

    int count;

    @Override
    protected void onHandleIntent(Intent intent) {

        new RestScan2dCtrl(new IFromRestCallback() {

            @Override
            public void getScan2dResponse(Scan2d value) {

                context = getApplicationContext();

                for (float v : value.getScan2d()) {
                    System.out.println("scan2d: " + v);
                }

            }

            @Override
            public void getDistanceResponse(Distance value) {

            }

        });
    }
}