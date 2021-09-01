package pl.pjatk.softdrive.rest.controllers;

import android.app.Application;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import pl.pjatk.softdrive.database.DbManager;
import pl.pjatk.softdrive.rest.domain.Distance;

///**
// * Get distance from remote sender with rest service and callbacks and commit into database
// * @author Dominik Stec
// * @see RestDistanceCtrl
// * @see IFromRestCallback
// * @see DbManager
// */
@RequiresApi(api = Build.VERSION_CODES.N)
public class GetDistanceCtrl extends Application {

    private int byteIp4;
    private boolean isDone;
    private DbManager db;

    /**
     * Singleton design pattern for get instance as remote classes dependency for this controller
     */
    private static GetDistanceCtrl singleton;

    /**
     * Initialize Application super class and singleton reference
     * @see Application
     */
    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;
    }

    /**
     * singeton access getter
     * @return singleton of this object instance
     */
    public static synchronized GetDistanceCtrl getInstance() {return singleton;}

    @RequiresApi(api = Build.VERSION_CODES.N)
    public GetDistanceCtrl() {}

    /**
//     * Try to find four byte of remote sender host IP address and execute rest controller
//     * for valid IP address
//     * @see RestDistanceCtrl
//     * @see IFromRestCallback
//     * @see DbManager
//     * @param ratio determine size of range IP address one byte to find from 1 to 255,
//     *              ex. ratio=5 gives offset of 5 tries to find proper IP in one iteration
//     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void findRtrIpB4(int ratio) {

        ExecutorService executorService = Executors.newCachedThreadPool();

//        // ratio offset calculate
//        int startIp = 1;
//        int endIp = startIp + ratio - 1;

//        db = new DbManager(this);

//        isDone = false;
        int byte4Ip = 1;
        while (byte4Ip++ < 255) {

            // run rest controller
        //    searchIpRunTask(executorService, byte4Ip, byte4Ip);

//            // control main loop with ratio
//            startIp += ratio;
//            endIp += ratio;
//            if (endIp > 255) endIp = 255;
//            if (startIp > 254) startIp = 254;
        }
    }

//    public void searchIpRunTask(ExecutorService executorService, int startIp, int endIp) {
//        new RestDistanceCtrl(executorService, startIp, endIp, new IFromRestCallback() {
//
//            @Override
//            public void getScan2dResponse(Float[] value) {}
//
//            @Override
//            public void getDistanceResponse(Distance value) {}
//
//            // Connected with remote distance sender, IP address found
//            @Override
//            public void getDistanceRouterIp(int ipAddress4Byte) {
//
////                byteIp4 = ipAddress4Byte;
//                executorService.shutdownNow();
//
//                ScheduledExecutorService schedExecutor = Executors.newScheduledThreadPool(3);
//
//                // run in continues thread iteration read from rest and write into database
//                // distance value
//                Runnable readDistanceRunTask = getDistanceRunnable(ipAddress4Byte);
//
//                // Cyclic thread for distance rest read and database write
//                schedExecutor.scheduleAtFixedRate(readDistanceRunTask, 0, 250, TimeUnit.MILLISECONDS);
////                isDone = true;
//            }
//
//            @Override
//            public void getScan2dRouterIp(int partIpAddress) {}
//
//        }).prepareCall().call();
//    }
//
//    @NonNull
//    public Runnable getDistanceRunnable(int IpAddress4Byte) {
//        Runnable rtask = new Runnable() {
//            @Override
//            public void run() {
//
//                    new RestDistanceCtrl(null, IpAddress4Byte, IpAddress4Byte, new IFromRestCallback() {
//
//                        @Override
//                        public void getScan2dResponse(Float[] value) {}
//
//                        // get distance from sensor rest callback
//                        // commit distance into database
//                        @Override
//                        public void getDistanceResponse(Distance dist) {
//
//                            commitDistanceDb(dist, getInstance());
//
//                        }
//
//                        @Override
//                        public void getDistanceRouterIp(int partIpAddress) {}
//
//                        @Override
//                        public void getScan2dRouterIp(int partIpAddress) {}
//
//                    }).prepareCall().call();
//            }
//        };
//        return rtask;
//    }

    public DbManager getDb() {
        return db;
    }

    public void setDb(DbManager db) {
        this.db = db;
    }

    private void commitDistanceDb(Distance dist, GetDistanceCtrl instance) {
        initDb(instance);

        getDb().setDistance(dist.getDistance());
        getDb().dbCommit();
    }

    private void initDb(GetDistanceCtrl instance) {
        setDb(new DbManager(instance));
    }
}
