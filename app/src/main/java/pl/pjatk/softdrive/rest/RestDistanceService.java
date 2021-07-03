package pl.pjatk.softdrive.rest;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

public class RestDistanceService extends IntentService {

    public RestDistanceService(){
        super("RestDistanceService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {

        new RestDistanceCtrl(new IFromRestCallback() {

            @Override
            public void getScan2dResponse(Scan2d value) {

            }

            @Override
            public void getDistanceResponse(Distance value) {

                System.out.println("Distance: " + value.getDistance());
            }

        });
    }
}