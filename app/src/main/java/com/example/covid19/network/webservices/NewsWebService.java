package com.example.covid19.network.webservices;

import com.example.covid19.model.news.NewsData;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NewsWebService extends WebService {

    @GET("v2/top-headlines?category=health&apiKey=0a8d0dcdcc3148ab9a4ed742a3a6a2e3")
    Single<NewsData> newsService(@Query("country") String countryCode);


}
