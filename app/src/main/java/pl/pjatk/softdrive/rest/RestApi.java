package pl.pjatk.softdrive.rest;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface RestApi {

    @GET("api/distance")
    Call<Distance> getDistanceEndpoint(@Header("Accept") String dataType);

    @GET("api/rplidar")
    Call<Float[]> getScan2dEndpoint(@Header("Accept") String dataType);


}