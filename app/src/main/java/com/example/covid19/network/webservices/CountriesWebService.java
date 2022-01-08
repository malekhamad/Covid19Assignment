package com.example.covid19.network.webservices;

import com.example.covid19.model.country.CountryData;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.internal.operators.observable.ObservableAll;
import okhttp3.ResponseBody;
import retrofit2.http.GET;

public interface CountriesWebService extends WebService {

    @GET("v3.1/all")
    Single<List<CountryData>> countryService();

}
