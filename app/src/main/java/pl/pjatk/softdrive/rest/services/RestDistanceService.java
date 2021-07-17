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

    private void FindRouterIp(int ratio) {

        int startIp = 1;
        int endIp = startIp + ratio-1;

        for(;endIp<255;) {

            new RestDistanceCtrl(startIp, endIp, new IFromRestCallback() {

                @Override
                public void getScan2dResponse(Float[] value) {

                }

                @Override
                public void getDistanceResponse(Distance value) {
                    // send broadcast distance data after call RestDistanceCtrl
                    // receiver in ReadDataActivity
                    System.out.println("distance was found : " + value.getDistance());
                    sendDistance(String.valueOf(value.getDistance()));

                }

                @Override
                public void getDistanceRouterIp(int partIpAddress) {
                    // send broadcast 4th part of ip address
                    // receiver in ReadDataActivity
                    System.out.println("part ip was found : " + partIpAddress);

                    sendPartIp(String.valueOf(partIpAddress));
                }

                @Override
                public void getScan2dRouterIp(int partIpAddress) {

                }

            }).prepareCall().call();

            startIp += ratio;
            endIp += ratio;
            if(endIp>255) endIp=255;
            if(startIp>254) startIp=254;
        }
    }


    @Override
    protected void onHandleIntent(Intent intent) {

        FindRouterIp(1);

    }

    private void sendDistance(String distance){
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("SendDistanceDataAction");
        broadcastIntent.putExtra("DistanceData", distance);
        sendBroadcast(broadcastIntent);
    }

    // check names !!!
    private void sendPartIp(String partIp){
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("SendPartIpDataAction");
        broadcastIntent.putExtra("PartIpData", partIp);
        sendBroadcast(broadcastIntent);
    }

}


