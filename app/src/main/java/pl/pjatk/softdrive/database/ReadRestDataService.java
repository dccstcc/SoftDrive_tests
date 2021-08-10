package pl.pjatk.softdrive.database;

import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import pl.pjatk.softdrive.rest.IFromRestCallback;
import pl.pjatk.softdrive.rest.controllers.RestDistanceCtrl;
import pl.pjatk.softdrive.rest.domain.Distance;
import pl.pjatk.softdrive.view.MainViewActivity;


public class ReadRestDataService extends Worker {

    private final int restFrequency = 2000;

    ExecutorService executorService;

    Intent i;
    int ip;

    DbManager db;

    Intent runView;

    int distance;

    boolean runViewActivity;

    public ReadRestDataService(
            @NonNull Context context,
            @NonNull WorkerParameters params) {
        super(context, params);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public Result doWork() {


        System.out.println("readrestdata run");

        int ip = getInputData().getInt("ProperIPDistance",0);

        System.out.println("ProperIPDistance " + ip);

        executorService = Executors.newFixedThreadPool(3);

        db = new DbManager(getApplicationContext());

        runView = new Intent(getApplicationContext(), MainViewActivity.class);

        runViewActivity = true;


        while (ip != 0) {

            new RestDistanceCtrl(executorService, ip, ip, new IFromRestCallback() {

                @Override
                public void getScan2dResponse(Float[] value) {

                }

                @Override
                public void getDistanceResponse(Distance value) {

                    distance = value.getDistance();

                    System.out.println("correct distance was found : " + distance);

                    db.setDistance(distance);
                    db.dbCommit();

                }

                @Override
                public void getDistanceRouterIp(int partIpAddress) {
                    System.out.println("from ip address");
                }

                @Override
                public void getScan2dRouterIp(int partIpAddress) {

                }

            }).prepareCall().call();

            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }


        return Result.success();
    }

}

