package com.example.covid19.network.webservices;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TrackingWebService extends WebService {


    @GET("api")
    Observable<ResponseBody> trackingDataService(@Query("date_from") String dateFrom, @Query("date_to") String dateTo);

    @GET("api/country/{country_name}")
    Observable<ResponseBody> trackingServiceByCountry(@Path(value = "country_name", encoded = true) String countryName,
                                                      @Query("date_from") String dateFrom, @Query("date_to") String dateTo);
}
