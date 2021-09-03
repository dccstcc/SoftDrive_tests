package pl.pjatk.softdrive.rest.controllers;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import pl.pjatk.softdrive.database.DbManager;
import pl.pjatk.softdrive.rest.domain.Distance;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestGetDistanceCtrl extends RestCtrl implements Callback<Distance> {

    private Context context;
    private DbManager db;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public RestGetDistanceCtrl() {
        context = ActivitySingleton.getInstance();
        initDb(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public String getFullIpWithUrl(int ip4Byte) {
        ip = new FindAddressIp();
        String ipNetworkAddress = ip.getNetworkIpWithoutHost();

        super.distanceUrl = "";
        super.distanceUrl += protocol;
        super.distanceUrl += ipNetworkAddress;
        super.distanceUrl += String.valueOf(ip4Byte);
        super.distanceUrl += portDistance;

        return super.distanceUrl;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void startRest(int ip4Byte) {
        String url = getFullIpWithUrl(ip4Byte);
        super.restApi = super.initRetrofitByUrl(url);
        call();
    }

    public void call() {
        super.restApi.getDistanceEndpoint("application/json").enqueue(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onResponse(Call<Distance> call, Response<Distance> response) {

        Log.v("Response callback", "call callback");

        if (response.isSuccessful()) {

            assert response.body() != null;
            commitDistanceDb(response.body());

            call.cancel();

        }
    }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onFailure (Call <Distance> call, Throwable t){
            Log.e("rest read error","read distance failure");
        }

    public DbManager getDb () {
        return db;
    }

    public void setDb (DbManager db){
        this.db = db;
    }

    private void commitDistanceDb(Distance dist){

        getDb().setDistance(dist.getDistance());
        getDb().dbCommit();
    }

    private void initDb(Context context){
        setDb(new DbManager(context));
    }
}

