package pl.pjatk.softdrive.rest.controllers;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.util.concurrent.ExecutionException;
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

    private Executor executor;

    public RestDistanceCtrl() {}

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


    @RequiresApi(api = Build.VERSION_CODES.N)
    public RestDistanceCtrl prepareIp(int fourthIp) {
        ip = new FindAddressIp();
        thirdPartIp = ip.getIp();

        DISTANCE_URL = "";
        DISTANCE_URL += protocol;
        DISTANCE_URL += thirdPartIp;
        DISTANCE_URL += String.valueOf(fourthIp);
        DISTANCE_URL += portDistance;

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

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onResponse(Call<Distance> call, Response<Distance> response) {

        System.out.println("Response callback - OK\n");

        getGson().toJson(response.body());

        if (response.isSuccessful()) {

            if(executor!=null) {

                executor.execute(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void run() {

                        String ip3b = prepareIp(fourthIp).ip.getIp();

                        IFromRestCallback.getDistanceRouterIp(fourthIp);
                        try {
                            IFromRestCallback.getDistanceResponse(response.body());
                        } catch (InterruptedException | ExecutionException e) {
                            e.printStackTrace();
                        }

                        call.cancel();

                        return;
                    }
                });

            } else {

                String ip3b = prepareIp(fourthIp).ip.getIp();

                IFromRestCallback.getDistanceRouterIp(fourthIp);

                try {
                    IFromRestCallback.getDistanceResponse(response.body());
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }

                call.cancel();

                return;

            }






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

