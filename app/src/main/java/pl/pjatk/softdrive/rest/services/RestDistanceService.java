package pl.pjatk.softdrive.rest.services;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import pl.pjatk.softdrive.rest.IFromRestCallback;
import pl.pjatk.softdrive.rest.controllers.RestDistanceCtrl;
import pl.pjatk.softdrive.rest.domain.Distance;
import pl.pjatk.softdrive.rest.domain.Scan2d;

public class RestDistanceService extends IntentService {

    String routerIp = "no read";

    public RestDistanceService(){
        super("RestDistanceService");
    }

    private String FindRouterIp(int ratio) {

        int startIp = 1;
        int endIp = startIp + ratio-1;

        for(;endIp<255;) {

            new RestDistanceCtrl(startIp, endIp, new IFromRestCallback() {

                @Override
                public void getScan2dResponse(Scan2d value) {

                }

                @Override
                public void getDistanceResponse(Distance value) {

                }

                @Override
                public void getDistanceRouterIp(int partIpAddress) {

                    System.out.println("part ip was found : " + partIpAddress);

                }

            }).prepareCall().call();

            startIp += ratio;
            endIp += ratio;
            if(endIp>255) endIp=255;
            if(startIp>254) startIp=254;
        }

        return routerIp;
    }


    @Override
    protected void onHandleIntent(Intent intent) {

        String routerIp = FindRouterIp(1);

        System.out.println("READ ROUTER IP = " + routerIp);
        Log.i("router ip","READ ROUTER IP = " + routerIp);

    }
}