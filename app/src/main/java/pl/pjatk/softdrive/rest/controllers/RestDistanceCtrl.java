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

    @RequiresApi(api = Build.VERSION_CODES.N)
    public RestDistanceCtrl(IFromRestCallback IFromRestCallback) {

        super.start();

        this.IFromRestCallback = IFromRestCallback;

        distance = new Distance();

        Call<Distance> call = restApiDistance.getDistanceEndpoint("application/json");

        call.enqueue(this);
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
            getDistanceUrl();
        }
    }

    @Override
    public void onFailure(Call<Distance> call, Throwable t){
        Log.e("REST error Distance", "onFailure method error Distance");
        t.printStackTrace();
    }
}

