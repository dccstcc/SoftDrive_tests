package pl.pjatk.softdrive.rest.services;

import android.app.IntentService;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.concurrent.Executors;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import pl.pjatk.softdrive.rest.IFromRestCallback;
import pl.pjatk.softdrive.rest.controllers.RestDistanceCtrl;
import pl.pjatk.softdrive.rest.domain.Distance;
import pl.pjatk.softdrive.rest.domain.Scan2d;

public class RestDistanceService extends IntentService {

    public RestDistanceService(){
        super("RestDistanceService");
    }

    Observable<Integer> ipFrom2to20;
    Observable<Integer> ipFrom21to40;

    private Disposable subscription;

    private Scheduler scheduler = scheduler(2);

    Scheduler scheduler(int n) {
        return Schedulers.from(Executors.newFixedThreadPool(n));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onHandleIntent(Intent intent) {

        new RestDistanceCtrl(2, 20, new IFromRestCallback() {

            @Override
            public void getScan2dResponse(Scan2d value) {

            }

            @Override
            public void getDistanceResponse(Distance value) {

                System.out.println("Distance 1 : " + value.getDistance());
            }


        }).prepareIp(1).prepareCall().call();

        new RestDistanceCtrl(21, 40, new IFromRestCallback() {

            @Override
            public void getScan2dResponse(Scan2d value) {

            }

            @Override
            public void getDistanceResponse(Distance value) {
                System.out.println("Distance 2 : " + value.getDistance());
            }

        }).prepareIp(21).prepareCall().call();
    }
}