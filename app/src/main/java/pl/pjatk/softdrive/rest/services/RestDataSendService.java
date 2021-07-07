package pl.pjatk.softdrive.rest.services;

import android.app.IntentService;
import android.content.Intent;
import android.os.Build;

import pl.pjatk.softdrive.rest.controllers.RestScan2dCtrl;

public class RestDataSendService extends IntentService {

    public RestDataSendService(){
        super("RestDataSendService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Intent restServiceDistance = new Intent(getApplicationContext(), RestDistanceService.class);
        startService(restServiceDistance);
        Intent restServiceScan2d = new Intent(getApplicationContext(), RestScan2dService.class);
        startService(restServiceScan2d);
    }
}
