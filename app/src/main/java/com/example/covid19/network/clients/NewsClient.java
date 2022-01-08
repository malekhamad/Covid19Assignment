package com.example.covid19.network.clients;

import com.example.covid19.BuildConfig;
import com.example.covid19.network.webservices.NewsWebService;

public class NewsClient extends ApiClient {

     private static NewsClient newsClient ;

    private NewsClient() {

    }

    public static NewsClient getInstance(){
        if (newsClient == null) {
            synchronized (NewsClient.class) {
                if (newsClient == null) {
                    newsClient = new NewsClient();
                }
            }
        }
        return newsClient;
    }

    @Override
    public NewsWebService provideCall(){
        if(mRetrofit == null){
            mRetrofit = getRetrofit() ;
        }
        return mRetrofit.create(NewsWebService.class);

    }

    @Override
    protected String getBaseUrl() {
        return BuildConfig.NEWS_BASE_URL;
    }
}
