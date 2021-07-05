package pl.pjatk.softdrive.rest.controllers;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

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

    @RequiresApi(api = Build.VERSION_CODES.N)
    public RestDistanceCtrl(int ipAddrStart, int ipAddrEnd, IFromRestCallback IFromRestCallback) {

        super.start();

        this.IFromRestCallback = IFromRestCallback;

        this.ipAddrStart = ipAddrStart;
        this.ipAddrEnd = ipAddrEnd;
        fourthIp = ipAddrStart;

        distance = new Distance();

        Call<Distance> call = restApiDistance.getDistanceEndpoint("application/json");

        call.enqueue(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void start() {
        super.start();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onResponse(Call<Distance> call, Response<Distance> response) {

        if (response.isSuccessful()) {

            distance = response.body();

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onFailure(Call<Distance> call, Throwable t){
        Log.e("REST error Distance", "onFailure method error Distance");
//        t.printStackTrace();
//
//        setDistancePartialUrl(String.valueOf(fourthIp));
//        updateDistanceRetrofit();
//        fourthIp++;
//        if(fourthIp == 255) fourthIp = 1;
//        System.out.println("4 IP: " + fourthIp);
//
//        call.cancel();
//
//        call = restApiDistance.getDistanceEndpoint("application/json");
//
//        call.enqueue(this);

        setDistancePartialUrl(String.valueOf(fourthIp));
        updateDistanceRetrofit();
        fourthIp++;
        if(fourthIp > this.ipAddrEnd) return;
        System.out.println("4 IP: " + fourthIp);
        call.enqueue(this);
    }
}

