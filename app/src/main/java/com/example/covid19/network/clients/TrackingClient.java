package com.example.covid19.network.clients;

import com.example.covid19.BuildConfig;
import com.example.covid19.network.webservices.TrackingWebService;

public class TrackingClient extends ApiClient {

    private static TrackingClient trackingClient;

    private TrackingClient() {

    }

    public static TrackingClient getInstance(){
        if (trackingClient == null) {
            synchronized (TrackingClient.class) {
                if (trackingClient == null) {
                    trackingClient = new TrackingClient();
                }
            }
        }
       return trackingClient;
    }

    @Override
    public TrackingWebService provideCall() {
        if (mRetrofit == null) {
            mRetrofit = getRetrofit();
        }
        return mRetrofit.create(TrackingWebService.class);

    }


    @Override
    protected String getBaseUrl() {
        return BuildConfig.TRACKING_BASE_URL;
    }


}
