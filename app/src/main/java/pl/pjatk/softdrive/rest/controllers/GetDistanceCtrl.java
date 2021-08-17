package pl.pjatk.softdrive.rest.controllers;

import android.app.Application;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import pl.pjatk.softdrive.database.DbManager;
import pl.pjatk.softdrive.rest.IFromRestCallback;
import pl.pjatk.softdrive.rest.domain.Distance;

@RequiresApi(api = Build.VERSION_CODES.N)
public class GetDistanceCtrl extends Application {

    private int byteIp4;
    boolean isDone;
    DbManager db;

    private static GetDistanceCtrl singleton;

    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;
    }

    public static synchronized GetDistanceCtrl getInstance() {return singleton;}

    @RequiresApi(api = Build.VERSION_CODES.N)
    public GetDistanceCtrl() {}

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void findRtrIpB4(int ratio) {

        ExecutorService executorService = Executors.newCachedThreadPool();
        isDone = false;

        int startIp = 1;
        int endIp = startIp + ratio - 1;

        db = new DbManager(this);

        isDone = false;

        for (; endIp < 255 || ! isDone; ) {

            new RestDistanceCtrl(executorService, startIp, endIp, new IFromRestCallback() {

                @Override
                public void getScan2dResponse(Float[] value) {}

                @Override
                public void getDistanceResponse(Distance value) {}

                @Override
                public void getDistanceRouterIp(int partIpAddress) {

                    byteIp4 = partIpAddress;
                    executorService.shutdownNow();

                    ScheduledExecutorService schedExecutor = Executors.newScheduledThreadPool(3);

                    Runnable rtask = new Runnable() {
                        @Override
                        public void run() {

                                new RestDistanceCtrl(null, partIpAddress, partIpAddress, new IFromRestCallback() {

                                    @Override
                                    public void getScan2dResponse(Float[] value) {}

                                    @Override
                                    public void getDistanceResponse(Distance value) {

                                        db = new DbManager(getInstance());

                                        db.setDistance(value.getDistance());
                                        db.dbCommit();

                                    }

                                    @Override
                                    public void getDistanceRouterIp(int partIpAddress) {}

                                    @Override
                                    public void getScan2dRouterIp(int partIpAddress) {}

                                }).prepareCall().call();
                        }
                    };

                    schedExecutor.scheduleAtFixedRate(rtask, 0, 250, TimeUnit.MILLISECONDS);
                    isDone = true;
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
}
