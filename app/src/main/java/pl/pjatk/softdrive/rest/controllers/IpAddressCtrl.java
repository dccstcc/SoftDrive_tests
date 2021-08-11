package pl.pjatk.softdrive.rest.controllers;

import android.app.Application;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import pl.pjatk.softdrive.database.DbManager;
import pl.pjatk.softdrive.rest.IFromRestCallback;
import pl.pjatk.softdrive.rest.domain.Distance;

@RequiresApi(api = Build.VERSION_CODES.N)
public class IpAddressCtrl extends Application {

    private int byteIp4;
    boolean isDone;
    DbManager db;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public IpAddressCtrl() {}

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void findRtrIpB4(int ratio) {

        ExecutorService executorService = Executors.newCachedThreadPool();
        isDone = false;

        int startIp = 1;
        int endIp = startIp + ratio - 1;

        db = new DbManager(this);


        for (; endIp < 255; ) {

            new RestDistanceCtrl(executorService, startIp, endIp, new IFromRestCallback() {

                @Override
                public void getScan2dResponse(Float[] value) {}

                @Override
                public void getDistanceResponse(Distance value) {}

                @Override
                public void getDistanceRouterIp(int partIpAddress) {

                    byteIp4 = partIpAddress;
                    executorService.shutdownNow();

                    ExecutorService executor = Executors.newSingleThreadExecutor();

                    executor.execute(new Runnable() {
                        @Override
                        public void run() {

                            do {

                                new RestDistanceCtrl(null, partIpAddress, partIpAddress, new IFromRestCallback() {

                                    @Override
                                    public void getScan2dResponse(Float[] value) {}

                                    @Override
                                    public void getDistanceResponse(Distance value) throws InterruptedException {

                                        System.out.print("Distance to write: " + value.getDistance() + "\n");
                                        db.setDistance(value.getDistance());
                                        db.dbCommit();
                                        Thread.sleep(1);
                                        System.out.print("Distance wrote.\n");

                                    }

                                    @Override
                                    public void getDistanceRouterIp(int partIpAddress) {}

                                    @Override
                                    public void getScan2dRouterIp(int partIpAddress) {}

                                }).prepareCall().call();


                                try{
                                    Thread.sleep(250);
                                } catch(InterruptedException exc) {
                                    exc.printStackTrace();
                                }

                            } while(true);

                        }
                    });


                }

                @Override
                public void getScan2dRouterIp(int partIpAddress) {}

            }).prepareCall().call();

            startIp += ratio;
            endIp += ratio;
            if (endIp > 255) endIp = 255;
            if (startIp > 254) startIp = 254;
        }
    }



    //    Distance distance;
//
//    pl.pjatk.softdrive.rest.IFromRestCallback IFromRestCallback;
//
//    private int fourthIp = 2;
//    private int ipAddrStart;
//    private int ipAddrEnd;
//
//
//    @RequiresApi(api = Build.VERSION_CODES.N)
//    public IpAddressCtrl(int ipAddrStart, int ipAddrEnd, pl.pjatk.softdrive.rest.IFromRestCallback IFromRestCallback) {
//
//
//        this.IFromRestCallback = IFromRestCallback;
//
//
//        this.ipAddrStart = ipAddrStart;
//        this.ipAddrEnd = ipAddrEnd;
//        fourthIp = ipAddrStart;
//
//
//        prepareIp(fourthIp);
//
//
//        distance = new Distance();
//
//    }
//
//
//    @RequiresApi(api = Build.VERSION_CODES.N)
//    public IpAddressCtrl prepareIp(int fourthIp) {
//        ip = new FindAddressIp();
//        thirdPartIp = ip.getIp();
//
//        DISTANCE_URL = "";
//        DISTANCE_URL += protocol;
//        DISTANCE_URL += thirdPartIp;
//        DISTANCE_URL += String.valueOf(fourthIp);
//        DISTANCE_URL += portDistance;
//
//        return this;
//    }
//
//    public IpAddressCtrl prepareCall() {
//        start();
//        return this;
//    }
//
//    public void call() {
//        Call<Distance> call = restApiDistance.getDistanceEndpoint("application/json");
//        call.enqueue(this);
//    }
//
//    @Override
//    public void onResponse(Call<Distance> call, Response<Distance> response) {
//
//        if (response.isSuccessful()) {
//
//            executor.execute(new Runnable() {
//                @RequiresApi(api = Build.VERSION_CODES.N)
//                @Override
//                public void run() {
//
//                    String ip3b = prepareIp(fourthIp).ip.getIp();
//
//                    IFromRestCallback.getDistanceRouterIp(fourthIp);
//                    IFromRestCallback.getDistanceResponse(response.body());
//
//                    call.cancel();
//
//                    return;
//                }
//            });
//
//
//
//
//
//        }
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.N)
//    @Override
//    public void onFailure(Call<Distance> call, Throwable t){
//        Log.e("REST error for Distance", "onFailure method - error Distance. IP addr is not exist");
//
//        call = call.clone();
//
//
//        setDistancePartialUrl(String.valueOf(fourthIp));
//        updateDistanceRetrofit();
//
//
//        fourthIp++;
//
//        if(fourthIp > this.ipAddrEnd) {
//            call.cancel();
//            return;
//        }
//
////        this.prepareIp(fourthIp).prepareCall().call();
//        this.prepareIp(fourthIp).prepareCall().call();
//
//    }
//

}
