package com.example.covid19.network.clients;

import com.example.covid19.BuildConfig;
import com.example.covid19.network.webservices.CountriesWebService;
import com.example.covid19.network.webservices.TrackingWebService;
import com.example.covid19.network.webservices.WebService;

import retrofit2.Retrofit;

public class CountryClient extends ApiClient<CountriesWebService> {

    private static CountryClient countryClient ;
    private CountryClient() {

    }

    public static CountryClient getInstance(){
        if (countryClient == null) {
            synchronized (TrackingClient.class) {
                if (countryClient == null) {
                    countryClient = new CountryClient();
                }
            }
        }
        return countryClient;
    }

    @Override
    public CountriesWebService provideCall(){
        if(mRetrofit == null){
            mRetrofit = getRetrofit() ;
        }
        return mRetrofit.create(CountriesWebService.class);

    }

    @Override
    protected String getBaseUrl() {
        return BuildConfig.COUNTRIES_BASE_URL;
    }
}
