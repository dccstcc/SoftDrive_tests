package pl.pjatk.softdrive.rest.services;

import android.app.IntentService;
import android.content.Intent;

import pl.pjatk.softdrive.rest.IFromRestCallback;
import pl.pjatk.softdrive.rest.controllers.RestDistanceCtrl;
import pl.pjatk.softdrive.rest.domain.Distance;
import pl.pjatk.softdrive.rest.domain.Scan2d;

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