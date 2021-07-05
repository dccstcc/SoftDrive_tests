package pl.pjatk.softdrive.rest.services;

import android.app.IntentService;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;

import pl.pjatk.softdrive.rest.IFromRestCallback;
import pl.pjatk.softdrive.rest.controllers.RestScan2dCtrl;
import pl.pjatk.softdrive.rest.domain.Distance;
import pl.pjatk.softdrive.rest.domain.Scan2d;

public class RestScan2dService extends IntentService {

    public RestScan2dService(){
        super("RestScan2dService");
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onHandleIntent(Intent intent) {

        new RestScan2dCtrl(new IFromRestCallback() {

            @Override
            public void getScan2dResponse(Scan2d value) {

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