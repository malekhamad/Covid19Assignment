package com.example.covid19.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.covid19.model.news.NewsData;
import com.example.covid19.network.clients.NewsClient;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class NewsRepository {

    private static final String TAG = "NewsRepository";

    private static CompositeDisposable disposable = new CompositeDisposable();


    public static MutableLiveData<NewsData> getNewsDataRemote(String countryCode){

        MutableLiveData<NewsData> mNewsDataMutableLiveData = new MutableLiveData<>();


        NewsClient.getInstance().provideCall().newsService(countryCode)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<NewsData>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(NewsData value) {
                       mNewsDataMutableLiveData.setValue(value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(TAG, "onError: "+e.getMessage());
                    }
                });

        return mNewsDataMutableLiveData ;
    }

    public static void clearDisposable() {
        disposable.clear();
    }

}
