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

                    System.out.println("Distance was found : " + value.getDistance());

                    //routerIp = RestDistanceCtrl.routerDistanceIp;

//                    try {
//                        Thread.sleep(7000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }

//                    Intent intent = new Intent();
//                    Bundle bundle = intent.getExtras();
//                    assert bundle != null;
//                    String routerDistanceIp = bundle.getString("ipAddress");
//                    Log.i("router ip address", routerDistanceIp);
//                    System.out.println("ip proper: " + routerDistanceIp);
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

        String newString;


        assert intent != null;
        Bundle extras = intent.getExtras();
            if(extras == null) {
                newString= null;
            } else {
                newString= extras.getString(Intent.EXTRA_TEXT);
            }

        System.out.println("READ ROUTER IP EXTRA = " + newString);
        Log.i("router ip","READ ROUTER IP EXTRA = " + newString);


//        Intent intent2 = new Intent();
//        assert intent2 != null;
//        Bundle bundle = intent2.getExtras();
//        assert bundle != null;
//        String routerDistanceIp = bundle.getString("ipAddress");
//        assert routerDistanceIp != null;
//        Log.i("router ip address", routerDistanceIp);
//        System.out.println("ip proper: " + routerDistanceIp);


    }

    //mTourName = intent.getStringExtra("tour_name");
}