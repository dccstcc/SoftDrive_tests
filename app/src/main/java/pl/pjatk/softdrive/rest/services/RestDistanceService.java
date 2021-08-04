package pl.pjatk.softdrive.rest.services;

import android.content.Context;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import pl.pjatk.softdrive.database.ReadRestDataService;
import pl.pjatk.softdrive.rest.IFromRestCallback;
import pl.pjatk.softdrive.rest.controllers.RestDistanceCtrl;
import pl.pjatk.softdrive.rest.domain.Distance;


public class RestDistanceService extends Worker {

    public RestDistanceService(
            @NonNull Context context,
            @NonNull WorkerParameters params) {
        super(context, params);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void FindRouterIp(int ratio) {

        ExecutorService executorService = Executors.newFixedThreadPool(1);

        int startIp = 1;
        int endIp = startIp + ratio - 1;

        for (; endIp < 255; ) {

            new RestDistanceCtrl(executorService, startIp, endIp, new IFromRestCallback() {

                @Override
                public void getScan2dResponse(Float[] value) {

                }

                @Override
                public void getDistanceResponse(Distance value) {

                }

                @Override
                public void getDistanceRouterIp(int partIpAddress) {
                    // send broadcast 4th part of ip address
                    // receiver in ReadDataActivity
                    System.out.println("part ip was found : " + partIpAddress);

                    sendPartIp(partIpAddress);
                }

                @Override
                public void getScan2dRouterIp(int partIpAddress) {

                }

            }).prepareCall().call();

            startIp += ratio;
            endIp += ratio;
            if (endIp > 255) endIp = 255;
            if (startIp > 254) startIp = 254;
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public Result doWork() {

        FindRouterIp(1);

        // Indicate whether the work finished successfully with the Result
        return Result.success();
    }


    // check names !!!
    private void sendPartIp(int partIp) {

        OneTimeWorkRequest.Builder readRestData = new OneTimeWorkRequest.Builder(ReadRestDataService.class);

        Data.Builder ip = new Data.Builder();
        ip.putInt("ProperIPDistance", partIp);

        readRestData.setInputData(ip.build());

        WorkManager
                .getInstance(getApplicationContext())
                .enqueue(readRestData.build());

    }
}


