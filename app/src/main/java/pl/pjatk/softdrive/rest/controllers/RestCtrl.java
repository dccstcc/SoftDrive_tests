package pl.pjatk.softdrive.rest.controllers;

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

public class RestCtrl {


    protected final String protocol = "http://";
    protected final String portDistance = ":8080";
    protected String distanceUrl = "";
    protected String thirdPartIp = "";
    protected FindAddressIp ip;
    private Gson gson;
    private OkHttpClient.Builder clientBuilder;
    private OkHttpClient httpHeaderConf;
    protected RestApi restApi;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public String getPreparedUrl(int hostPartIp) {
        FindAddressIp ipCtrl = new FindAddressIp();
        String networkIp = ipCtrl.getNetworkIpWithoutHost();

        String url = "";
        url += protocol;
        url += networkIp;
        url += String.valueOf(hostPartIp);
        url += portDistance;

        return url;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    protected RestApi initRetrofitByUrl(String url) {
        return initRetrofit(initHttpHeader(),
                initGson(),
                initLogBuilder(),
                url);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    protected RestApi initRetrofitByIp(int ip4Byte) {
        return initRetrofit(initHttpHeader(),
                initGson(),
                initLogBuilder(),
                getPreparedUrl(ip4Byte));
    }

    protected RestApi initRetrofit(OkHttpClient httpHeaderConf, Gson gson, OkHttpClient.Builder clientBuilder, String url) {
        Retrofit retrofit = new Retrofit.Builder()
                .client(httpHeaderConf)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(url)
                .client(clientBuilder.build())
                .build();
        RestApi restApi = retrofit.create(RestApi.class);
        return restApi;
    }

    protected OkHttpClient initHttpHeader() {
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

    protected OkHttpClient.Builder initLogBuilder() {
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

    public Gson getGson() {
        return gson;
    }
}
