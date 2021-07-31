package pl.pjatk.softdrive.rest.services;

import android.content.Context;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.Arrays;

import pl.pjatk.softdrive.rest.IFromRestCallback;
import pl.pjatk.softdrive.rest.controllers.RestScan2dCtrl;
import pl.pjatk.softdrive.rest.domain.Distance;

public class RestScan2dService extends Worker {

    public RestScan2dService(
            @NonNull Context context,
            @NonNull WorkerParameters params) {
        super(context, params);
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
//                    sendScan2d(value);
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

//                    sendPartIp(String.valueOf(partIpAddress));
                }

            }).prepareCall().call();

            startIp += ratio;
            endIp += ratio;
            if(endIp>255) endIp=255;
            if(startIp>254) startIp=254;
        }
    }

//    @RequiresApi(api = Build.VERSION_CODES.N)
//    @Override
//    protected void onHandleIntent(Intent intent) {
//
//        FindRouterIp(1);
//    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public Result doWork() {

        // Do the work here--in this case, upload the images.
        FindRouterIp(1);

        // Indicate whether the work finished successfully with the Result
        return Result.success();
    }


//    private void sendScan2d(Float[] scan2d){
//        Intent broadcastIntent = new Intent();
//        broadcastIntent.setAction("SendScan2dDataAction");
//        broadcastIntent.putExtra("Scan2dData", scan2d);
//        sendBroadcast(broadcastIntent);
//    }
//
//    private void sendPartIp(String partIp){
//        Intent broadcastIntent = new Intent();
//        broadcastIntent.setAction("SendPartIpScan2dDataAction");
//        broadcastIntent.putExtra("PartIpScan2dData", partIp);
//        sendBroadcast(broadcastIntent);
//    }
}