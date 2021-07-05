package pl.pjatk.softdrive.rest.controllers;

import android.app.Application;
import android.os.Build;

import androidx.annotation.RequiresApi;

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

@RequiresApi(api = Build.VERSION_CODES.N)
public class RestCtrl extends Application {

    protected static final String protocol = "http://";
    protected static final String portDistance = ":8080";
    protected static final String portScan2d = ":5000";
    protected static String DISTANCE_URL = "";
    protected static String SCAN2D_URL = "";
    protected static String thirdPartIp = "";

    protected FindAddressIp ip;

    private Gson gson;
    private OkHttpClient.Builder clientBuilder;
    private OkHttpClient httpHeaderConf;

    RestApi restApiScan2d;
    RestApi restApiDistance;

    public void start() {
        //prepareFirstIp();

        this.gson = initGson();
        this.clientBuilder = initLogBuilder();
        this.httpHeaderConf = initHttpHeader();
        this.restApiScan2d = initRetrofitScan2d(httpHeaderConf, gson, clientBuilder);
        this.restApiDistance = initRetrofitDistance(httpHeaderConf, gson, clientBuilder);
    }

    public void updateDistanceRetrofit() {
        this.restApiDistance = initRetrofitDistance(httpHeaderConf, gson, clientBuilder);
    }

    public void updateScan2dRetrofit() {
        this.restApiScan2d = initRetrofitScan2d(httpHeaderConf, gson, clientBuilder);
    }

//    public RestDistanceCtrl prepareFirstIp() {
//        ip = new FindAddressIp();
//        thirdPartIp = ip.getIp();
//
//        DISTANCE_URL = "";
//        DISTANCE_URL += protocol;
//        DISTANCE_URL += thirdPartIp;
//        DISTANCE_URL += "1";
//        DISTANCE_URL += portDistance;
//
//        SCAN2D_URL = "";
//        SCAN2D_URL += protocol;
//        SCAN2D_URL += thirdPartIp;
//        SCAN2D_URL += "1";
//        SCAN2D_URL += portScan2d;
//
//        return this;
//    }


    public static String getDistanceUrl() {
        return DISTANCE_URL;
    }

    public static String getScan2dUrl() {
        return SCAN2D_URL;
    }

    public static void setDistancePartialUrl(String fourthPartIp) {
        DISTANCE_URL = protocol + thirdPartIp + fourthPartIp + portDistance;
    }

    public static void setScan2dPartialUrl(String fourthPartIp) {
        SCAN2D_URL = protocol + thirdPartIp + fourthPartIp + portScan2d;
    }

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
