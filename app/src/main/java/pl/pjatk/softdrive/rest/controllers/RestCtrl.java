package pl.pjatk.softdrive.rest.controllers;

import android.app.Application;

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

public class RestCtrl extends Application {

    static final String DISTANCE_URL = "http://192.168.13.229:8080";
    static final String SCAN2D_URL = "http://192.168.146.82:5000";


    RestApi restApiScan2d;
    RestApi restApiDistance;

    public void start() {

        Gson gson = initGson();
        OkHttpClient.Builder clientBuilder = initLogBuilder();
        OkHttpClient httpHeaderConf = initHttpHeader();
        this.restApiScan2d = initRetrofit(httpHeaderConf, gson, clientBuilder);
        this.restApiDistance = initRetrofitDistance(httpHeaderConf, gson, clientBuilder);
    }

    private RestApi initRetrofit(OkHttpClient httpHeaderConf, Gson gson, OkHttpClient.Builder clientBuilder) {
        Retrofit retrofit = new Retrofit.Builder()
                .client(httpHeaderConf)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(SCAN2D_URL)
                .client(clientBuilder.build())
                .build();
        restApiScan2d = retrofit.create(RestApi.class);
        return restApiScan2d;
    }

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

    private OkHttpClient.Builder initLogBuilder() {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        clientBuilder.addInterceptor(loggingInterceptor);
        return clientBuilder;
    }

    private Gson initGson() {
        return new GsonBuilder()
                .setLenient()
                .create();
    }

}
