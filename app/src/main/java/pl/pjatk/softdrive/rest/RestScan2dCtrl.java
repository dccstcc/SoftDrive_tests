package pl.pjatk.softdrive.rest;

import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestScan2dCtrl extends RestCtrl implements Callback<Float[]> {

    Scan2d scan2d;

    IFromRestCallback IFromRestCallback;

    public RestScan2dCtrl(IFromRestCallback IFromRestCallback) {

        super.start();

        this.IFromRestCallback = IFromRestCallback;

        scan2d = new Scan2d();

        Call<Float[]> call = restApi.getScan2dEndpoint("application/json");

        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<Float[]> call, Response<Float[]> response) {

        if (response.isSuccessful()) {

            scan2d.setScan2d(response.body());

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }



        }
    }

    @Override
    public void onFailure(Call<Float[]> call, Throwable t){
        Log.e("REST error Scan2d", "onFailure method error Scan2d");
        t.printStackTrace();
    }
}

