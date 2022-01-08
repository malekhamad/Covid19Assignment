package com.example.covid19.network.clients;

import com.example.covid19.network.webservices.TrackingWebService;
import com.example.covid19.network.webservices.WebService;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public abstract class ApiClient<T extends WebService> {

   protected Retrofit mRetrofit ;

    protected OkHttpClient.Builder getClient(){
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.readTimeout(10, TimeUnit.SECONDS);
        client.writeTimeout(10, TimeUnit.SECONDS);
        client.connectTimeout(10, TimeUnit.SECONDS);

        return client;
    }


    protected Retrofit getRetrofit(){
        mRetrofit = new Retrofit.Builder()
                .baseUrl(getBaseUrl())
                .client(getClient().build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        return mRetrofit;
    }

    protected abstract String getBaseUrl();

    public abstract T provideCall();
}
