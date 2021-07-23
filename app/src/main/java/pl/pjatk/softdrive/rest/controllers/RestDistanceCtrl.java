package pl.pjatk.softdrive.rest.controllers;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.util.concurrent.Executor;

import pl.pjatk.softdrive.rest.IFromRestCallback;
import pl.pjatk.softdrive.rest.domain.Distance;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestDistanceCtrl extends RestCtrl implements Callback<Distance> {

    Distance distance;

    pl.pjatk.softdrive.rest.IFromRestCallback IFromRestCallback;

    private int fourthIp = 2;
    private int ipAddrStart;
    private int ipAddrEnd;

    private final Executor executor;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public RestDistanceCtrl(Executor executor, int ipAddrStart, int ipAddrEnd, IFromRestCallback IFromRestCallback) {

        this.executor = executor;

        this.IFromRestCallback = IFromRestCallback;


        this.ipAddrStart = ipAddrStart;
        this.ipAddrEnd = ipAddrEnd;
        fourthIp = ipAddrStart;


                prepareIp(fourthIp);


        distance = new Distance();

    }

//    public void runCallThreadBackdround() {
//        executor.execute(new Runnable() {
//            @Override
//            public void run() {
//                prepareCall().call();
//            }
//        });
//    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public RestDistanceCtrl prepareIp(int fourthIp) {
        ip = new FindAddressIp();
        thirdPartIp = ip.getIp();

        DISTANCE_URL = "";
        DISTANCE_URL += protocol;
        DISTANCE_URL += thirdPartIp;
        DISTANCE_URL += String.valueOf(fourthIp);
        DISTANCE_URL += portDistance;

//        SCAN2D_URL = "";
//        SCAN2D_URL += protocol;
//        SCAN2D_URL += thirdPartIp;
//        SCAN2D_URL += String.valueOf(fourthIp);
//        SCAN2D_URL += portScan2d;

        return this;
    }

    public RestDistanceCtrl prepareCall() {
        start();
        return this;
    }

    public void call() {
        Call<Distance> call = restApiDistance.getDistanceEndpoint("application/json");
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<Distance> call, Response<Distance> response) {

        if (response.isSuccessful()) {

            executor.execute(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void run() {

                    prepareIp(fourthIp).ip.getIp();

                    IFromRestCallback.getDistanceRouterIp(fourthIp);
                    IFromRestCallback.getDistanceResponse(response.body());

                    call.cancel();

                    return;
                }
            });





        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onFailure(Call<Distance> call, Throwable t){
        Log.e("REST error for Distance", "onFailure method - error Distance. IP addr is not exist");

        call = call.clone();


                setDistancePartialUrl(String.valueOf(fourthIp));
                updateDistanceRetrofit();


        fourthIp++;

        if(fourthIp > this.ipAddrEnd) {
            call.cancel();
            return;
        }

//        this.prepareIp(fourthIp).prepareCall().call();
        this.prepareIp(fourthIp).prepareCall().call();

    }
}

