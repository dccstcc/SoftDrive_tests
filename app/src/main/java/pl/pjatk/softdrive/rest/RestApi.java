package pl.pjatk.softdrive.rest;

import pl.pjatk.softdrive.rest.domain.Distance;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

/**
 * Rest controllers endpoints configuration
 */
public interface RestApi {

    /**
     * Set endpoint for get distance value from rest API
     * @param dataType HTTP header accept data type for read
     * @return object for manipulate call for distance properties
     */
    @GET("api/distance")
    Call<Distance> getDistanceEndpoint(@Header("Accept") String dataType);

    @GET("api/rplidar")
    Call<Float[]> getScan2dEndpoint(@Header("Accept") String dataType);


}