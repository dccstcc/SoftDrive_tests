package pl.pjatk.softdrive.rest.services;

import android.app.IntentService;
import android.content.Intent;
import android.os.Build;
import androidx.annotation.RequiresApi;
import pl.pjatk.softdrive.rest.IFromRestCallback;
import pl.pjatk.softdrive.rest.controllers.RestDistanceCtrl;
import pl.pjatk.softdrive.rest.domain.Distance;
import pl.pjatk.softdrive.rest.domain.Scan2d;

public class RestDistanceService extends IntentService {

    public RestDistanceService(){
        super("RestDistanceService");
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onHandleIntent(Intent intent) {

        new RestDistanceCtrl(1, 20, new IFromRestCallback() {

            @Override
            public void getScan2dResponse(Scan2d value) {

            }

            @Override
            public void getDistanceResponse(Distance value) {

                System.out.println("Distance 1 : " + value.getDistance());
            }


        }).prepareCall().call();

        new RestDistanceCtrl(21, 40, new IFromRestCallback() {

            @Override
            public void getScan2dResponse(Scan2d value) {

            }

            @Override
            public void getDistanceResponse(Distance value) {
                System.out.println("Distance 2 : " + value.getDistance());
            }

        }).prepareCall().call();
    }
}