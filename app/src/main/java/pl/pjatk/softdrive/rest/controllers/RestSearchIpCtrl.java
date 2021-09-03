package pl.pjatk.softdrive.rest.controllers;

import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.io.IOException;

import pl.pjatk.softdrive.rest.domain.Distance;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestSearchIpCtrl extends RestCtrl implements Callback<Distance> {

    private int fourthIp;
    private String ip4Byte;
    private int ip;

    public RestSearchIpCtrl() {
        fourthIp = 1;
        ip4Byte = "none";
        ip = 1;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void startSearchIp() throws IOException {
        super.restApi = initRetrofitByIp(fourthIp);
        getCall();
    }

    public void getCall() throws IOException {
        super.restApi.getDistanceEndpoint("application/json").enqueue(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void startSearchIpLoop() throws IOException {
        for(ip=1; ip<255; ip++) {
            super.restApi = initRetrofitByIp(ip);
            getCall();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onResponse(Call<Distance> call, Response<Distance> response) {

        Log.v("Response IP address", "find IP address");

        if (response.isSuccessful()) {

            setIp4Byte(String.valueOf(ip));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onFailure(@NonNull Call<Distance> call, @NonNull Throwable t){
        Log.e("try find IP", "IP not exist");
    }

    public String getIp4Byte() {
        return ip4Byte;
    }

    public void setIp4Byte(String ip4Byte) {
        this.ip4Byte = ip4Byte;
    }

    public int getFourthIp() {
        return fourthIp;
    }

    public void setFourthIp(int fourthIp) {
        this.fourthIp = fourthIp;
    }

    public int getIp() {
        return ip;
    }

    public void setIp(int ip) {
        this.ip = ip;
    }
}

