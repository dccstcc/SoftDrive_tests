package pl.pjatk.softdrive.rest.services;

import android.app.IntentService;
import android.content.Intent;
import android.os.Build;

public class RestDataSendService extends IntentService {

    public RestDataSendService(){
        super("RestDataSendService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Intent restServiceDistance = new Intent(getApplicationContext(), RestDistanceService.class);
        startService(restServiceDistance);
    }
}
