package pl.pjatk.softdrive.rest;

import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestDistanceCtrl extends RestCtrl implements Callback<Integer> {

    Distance distance;

    IFromRestCallback IFromRestCallback;

    public RestDistanceCtrl(IFromRestCallback IFromRestCallback) {

        super.start();

        this.IFromRestCallback = IFromRestCallback;

        distance = new Distance();

        Call<Integer> call = restApi.getDistanceEndpoint("application/json");

        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<Integer> call, Response<Integer> response) {

        if (response.isSuccessful()) {

            distance.setDistance(response.body());

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }



        }
    }

    @Override
    public void onFailure(Call<Integer> call, Throwable t){
        Log.e("REST error Scan2d", "onFailure method error Distance");
        t.printStackTrace();
    }
}

