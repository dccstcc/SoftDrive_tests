package pl.pjatk.softdrive.rest.services;

import android.app.IntentService;
import android.content.Intent;
import pl.pjatk.softdrive.rest.IFromRestCallback;
import pl.pjatk.softdrive.rest.controllers.RestDistanceCtrl;
import pl.pjatk.softdrive.rest.domain.Distance;
import pl.pjatk.softdrive.rest.domain.Scan2d;

public class RestDistanceService extends IntentService {

    int distance;

    public RestDistanceService(){
        super("RestDistanceService");
    }

    private void FindRouterIp(int ratio) {
        //distance = -1;
        //int ipAddrRatio = ratio;
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

                }

            }).prepareCall().call();

            startIp += ratio;
            endIp += ratio;
            if(endIp>255) endIp=255;
            if(startIp>254) startIp=254;
        }

        //return distance;
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        FindRouterIp(20);

//        new RestDistanceCtrl(1, 20, new IFromRestCallback() {
//
//            @Override
//            public void getScan2dResponse(Scan2d value) {
//
//            }
//
//            @Override
//            public void getDistanceResponse(Distance value) {
//
//                System.out.println("Distance 1 : " + value.getDistance());
//            }
//
//
//        }).prepareCall().call();
//
//        new RestDistanceCtrl(21, 40, new IFromRestCallback() {
//
//            @Override
//            public void getScan2dResponse(Scan2d value) {
//
//            }
//
//            @Override
//            public void getDistanceResponse(Distance value) {
//                System.out.println("Distance 2 : " + value.getDistance());
//            }
//
//        }).prepareCall().call();
//
//        new RestDistanceCtrl(41, 60, new IFromRestCallback() {
//
//            @Override
//            public void getScan2dResponse(Scan2d value) {
//
//            }
//
//            @Override
//            public void getDistanceResponse(Distance value) {
//                System.out.println("Distance 2 : " + value.getDistance());
//            }
//
//        }).prepareCall().call();
//
//        new RestDistanceCtrl(61, 80, new IFromRestCallback() {
//
//            @Override
//            public void getScan2dResponse(Scan2d value) {
//
//            }
//
//            @Override
//            public void getDistanceResponse(Distance value) {
//                System.out.println("Distance 2 : " + value.getDistance());
//            }
//
//        }).prepareCall().call();
//
//        new RestDistanceCtrl(81, 100, new IFromRestCallback() {
//
//            @Override
//            public void getScan2dResponse(Scan2d value) {
//
//            }
//
//            @Override
//            public void getDistanceResponse(Distance value) {
//                System.out.println("Distance 2 : " + value.getDistance());
//            }
//
//        }).prepareCall().call();
    }
}