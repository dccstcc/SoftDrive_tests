package pl.pjatk.softdrive.rest;

import pl.pjatk.softdrive.rest.domain.Distance;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface RestApi {

    @GET("api/distance")
    Call<Distance> getDistanceEndpoint(@Header("Accept") String dataType);
}