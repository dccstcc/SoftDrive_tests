package pl.pjatk.softdrive.rest.controllers;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import pl.pjatk.softdrive.rest.IFromRestCallback;
import pl.pjatk.softdrive.rest.domain.Distance;
import pl.pjatk.softdrive.rest.domain.Scan2d;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestScan2dCtrl extends RestCtrl implements Callback<Float[]> {

    Scan2d scan2d;

    pl.pjatk.softdrive.rest.IFromRestCallback IFromRestCallback;

    private int fourthIp = 2;
    private int ipAddrStart;
    private int ipAddrEnd;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public RestScan2dCtrl(int ipAddrStart, int ipAddrEnd, IFromRestCallback IFromRestCallback) {

        this.IFromRestCallback = IFromRestCallback;

        this.ipAddrStart = ipAddrStart;
        this.ipAddrEnd = ipAddrEnd;
        fourthIp = ipAddrStart;

        prepareIp(fourthIp);

        scan2d = new Scan2d();

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public RestScan2dCtrl prepareIp(int fourthIp) {
        ip = new FindAddressIp();
        thirdPartIp = ip.getIp();

//        DISTANCE_URL = "";
//        DISTANCE_URL += protocol;
//        DISTANCE_URL += thirdPartIp;
//        DISTANCE_URL += String.valueOf(fourthIp);
//        DISTANCE_URL += portDistance;

        SCAN2D_URL = "";
        SCAN2D_URL += protocol;
        SCAN2D_URL += thirdPartIp;
        SCAN2D_URL += String.valueOf(fourthIp);
        SCAN2D_URL += portScan2d;

        return this;
    }

    public RestScan2dCtrl prepareCall() {
        start();
        return this;
    }

    public void call() {
        Call<Float[]> call = restApiScan2d.getScan2dEndpoint("application/json");
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<Float[]> call, Response<Float[]> response) {

        if (response.isSuccessful()) {

            prepareIp(fourthIp).ip.getIp();

            IFromRestCallback.getScan2dRouterIp(fourthIp);
            IFromRestCallback.getScan2dResponse(response.body());

            call.cancel();

            return;

        }
    }

    @Override
    public void onFailure(Call<Float[]> call, Throwable t){
        Log.e("REST error for Scan2d", "onFailure method - error Scan2d. IP addr is not exist");

        call = call.clone();

        setScan2dPartialUrl(String.valueOf(fourthIp));
        updateScan2dRetrofit();

        fourthIp++;

        if(fourthIp > this.ipAddrEnd) {
            call.cancel();
            return;
        }

        this.prepareIp(fourthIp).prepareCall().call();
    }
}

