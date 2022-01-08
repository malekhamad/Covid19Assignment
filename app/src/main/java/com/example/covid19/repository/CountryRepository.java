package com.example.covid19.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.covid19.model.country.CountryData;
import com.example.covid19.network.clients.CountryClient;
import com.example.covid19.utils.JsonMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class CountryRepository {

    private static final String TAG = "CountryRepository";

    private static Disposable disposable;

    public static MutableLiveData<Map<String, CountryData>> getCountryDataRemote() {

        MutableLiveData<Map<String, CountryData>> mCountryDataMutableLiveData = new MutableLiveData<>();

        disposable = CountryClient.getInstance().provideCall().countryService()
                .subscribeOn(Schedulers.io())
                .map(dataList -> {
                    Map<String,CountryData> mMap = new HashMap<>();
                    for(CountryData countryData : dataList){
                        mMap.put(countryData.getName().getCommon(),countryData);
                    }
                    return mMap ;
                })
                .observeOn(AndroidSchedulers.mainThread())

                .subscribeWith(new DisposableSingleObserver<Map<String, CountryData>>() {
                    @Override
                    public void onSuccess(Map<String, CountryData> mapData) {
                        mCountryDataMutableLiveData.setValue(mapData);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(TAG, "onError: "+e.getMessage());
                    }
                });

        return mCountryDataMutableLiveData;
    }


    public static void disposeObservable() {
        if (disposable.isDisposed()) {
            disposable.dispose();

        }
    }

}
