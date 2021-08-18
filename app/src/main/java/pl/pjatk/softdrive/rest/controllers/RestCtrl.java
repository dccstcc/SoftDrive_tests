package pl.pjatk.softdrive.rest.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import pl.pjatk.softdrive.rest.RestApi;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Based on Retrofit v.2 library for rest service controller for read distance from remote sensor
 * @author Dominik Stec
 * Configuration pattern come from following address
 * @link https://www.vogella.com/tutorials/Retrofit/article.html [17.08.2021]
 */
public class RestCtrl {

    /**
     * Web protocol configuration
     */
    protected static final String protocol = "http://";
    /**
     * Communication port
     */
    protected static final String portDistance = ":8080";
    protected static final String portScan2d = ":5000";
    protected static String DISTANCE_URL = "";
    protected static String SCAN2D_URL = "";
    protected static String thirdPartIp = "";

    protected FindAddressIp ip;

    private Gson gson;
    private OkHttpClient.Builder clientBuilder;
    private OkHttpClient httpHeaderConf;

    protected RestApi restApiScan2d;
    protected RestApi restApiDistance;

    /**
     * Initialize all setup of Retrofit by once
     */
    public void start() {

        this.gson = initGson();
        this.clientBuilder = initLogBuilder();
        this.httpHeaderConf = initHttpHeader();
        //this.restApiScan2d = initRetrofitScan2d(httpHeaderConf, gson, clientBuilder);
        this.restApiDistance = initRetrofitDistance(httpHeaderConf, gson, clientBuilder);
    }

    public void updateDistanceRetrofit() {
        this.restApiDistance = initRetrofitDistance(httpHeaderConf, gson, clientBuilder);
    }

    public void updateScan2dRetrofit() {
        this.restApiScan2d = initRetrofitScan2d(httpHeaderConf, gson, clientBuilder);
    }

    public static String getDistanceUrl() {
        return DISTANCE_URL;
    }

    public static String getScan2dUrl() {
        return SCAN2D_URL;
    }

    /**
     * concat full version of remote sender host IP address
     * @param fourthPartIp Four byte of IP address
     */
    public static void setDistancePartialUrl(String fourthPartIp) {
        DISTANCE_URL = protocol + thirdPartIp + fourthPartIp + portDistance;
    }

    public static void setScan2dPartialUrl(String fourthPartIp) {
        SCAN2D_URL = protocol + thirdPartIp + fourthPartIp + portScan2d;
    }

    /**
     * TODO
     * @param httpHeaderConf
     * @param gson
     * @param clientBuilder
     * @return
     */
    private RestApi initRetrofitScan2d(OkHttpClient httpHeaderConf, Gson gson, OkHttpClient.Builder clientBuilder) {
        Retrofit retrofit = new Retrofit.Builder()
                .client(httpHeaderConf)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(SCAN2D_URL)
                .client(clientBuilder.build())
                .build();
        restApiScan2d = retrofit.create(RestApi.class);
        return restApiScan2d;
    }

    /**
     * read distance intializer by Retrofit
     * @param httpHeaderConf Configuration of http header
     * @param gson Configuration of JSON data flow
     * @param clientBuilder Configuration of HTTP request receiver
     * @see OkHttpClient
     * @see Gson
     * @see OkHttpClient.Builder
     * @return API with object for distance read from rest service
     */
    private RestApi initRetrofitDistance(OkHttpClient httpHeaderConf, Gson gson, OkHttpClient.Builder clientBuilder) {
        Retrofit retrofit = new Retrofit.Builder()
                .client(httpHeaderConf)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(DISTANCE_URL)
                .client(clientBuilder.build())
                .build();
        restApiDistance = retrofit.create(RestApi.class);
        return restApiDistance;
    }

    /**
     * HTTP header configuration
     * @see OkHttpClient
     * @return Configuration of HTTP request receiver
     */
    private OkHttpClient initHttpHeader() {
        OkHttpClient httpHeaderConf = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request newRequest  = chain.request().newBuilder()
                        .addHeader("Accept", "application/json")
                        .build();
                return chain.proceed(newRequest);
            }
        }).build();
        return httpHeaderConf;
    }

    /**
     * Log service for rest communication
     * @see OkHttpClient.Builder
     * @return Configuration of HTTP request receiver
     */
    private OkHttpClient.Builder initLogBuilder() {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        clientBuilder.addInterceptor(loggingInterceptor);
        return clientBuilder;
    }

    /**
     * JSON service for rest communication
     * @see Gson
     * @return configuration for rest service with Gson object
     */
    private Gson initGson() {
        return new GsonBuilder()
                .setLenient()
                .create();
    }

    public Gson getGson() {
        return gson;
    }
}
