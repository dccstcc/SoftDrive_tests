package pl.pjatk.softdrive.rest.services;

import android.app.IntentService;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.Arrays;

import pl.pjatk.softdrive.rest.IFromRestCallback;
import pl.pjatk.softdrive.rest.controllers.RestDistanceCtrl;
import pl.pjatk.softdrive.rest.controllers.RestScan2dCtrl;
import pl.pjatk.softdrive.rest.domain.Distance;
import pl.pjatk.softdrive.rest.domain.Scan2d;

public class RestScan2dService extends IntentService {

    public RestScan2dService(){
        super("RestScan2dService");
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void FindRouterIp(int ratio) {

        int startIp = 1;
        int endIp = startIp + ratio-1;

        for(;endIp<255;) {

            new RestScan2dCtrl(startIp, endIp, new IFromRestCallback() {

                @Override
                public void getScan2dResponse(Float[] value) {
                    // send broadcast scan2d data after call RestScan2dCtrl
                    // receiver in ReadDataActivity
                    System.out.println("scan2d was found : " + Arrays.toString(value));
                    sendScan2d(value);
                }

                @Override
                public void getDistanceResponse(Distance value) {


                }

                @Override
                public void getDistanceRouterIp(int partIpAddress) {

                }

                @Override
                public void getScan2dRouterIp(int partIpAddress) {
                    // send broadcast 4th part of ip address
                    // receiver in ReadDataActivity
                    System.out.println("part ip scan2d was found : " + partIpAddress);

                    sendPartIp(String.valueOf(partIpAddress));
                }

            }).prepareCall().call();

            startIp += ratio;
            endIp += ratio;
            if(endIp>255) endIp=255;
            if(startIp>254) startIp=254;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onHandleIntent(Intent intent) {

        FindRouterIp(1);
    }

    private void sendScan2d(Float[] scan2d){
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("SendScan2dDataAction");
        broadcastIntent.putExtra("Scan2dData", scan2d);
        sendBroadcast(broadcastIntent);
    }

    private void sendPartIp(String partIp){
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("SendPartIpScan2dDataAction");
        broadcastIntent.putExtra("PartIpScan2dData", partIp);
        sendBroadcast(broadcastIntent);
    }
}