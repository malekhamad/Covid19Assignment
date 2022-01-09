package com.example.covid19.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.covid19.model.tracking.CountryTracking;
import com.example.covid19.model.tracking.CountryRootData;
import com.example.covid19.network.clients.TrackingClient;
import com.example.covid19.utils.JsonMapper;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class TrackingRepository {


    private static final String TAG = "TrackingRepository";

    private static CompositeDisposable disposable = new CompositeDisposable();

    public static MutableLiveData<CountryRootData> getTrackingAllData(String dateFrom, String dateTo) {
        MutableLiveData<CountryRootData> mTrackingDataMutableLiveData = new MutableLiveData<>();

        TrackingClient.getInstance().provideCall().trackingDataService(dateFrom, dateTo)
                .subscribeOn(Schedulers.io())
                .map(new Function<ResponseBody, CountryRootData>() {
                    @Override
                    public CountryRootData apply(ResponseBody responseBody) throws Exception {

                        return JsonMapper.mapAllJson(responseBody.string());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CountryRootData>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onNext(CountryRootData value) {
                        mTrackingDataMutableLiveData.setValue(value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mTrackingDataMutableLiveData.setValue(new CountryRootData(true,e.getMessage()));
                    }

                    @Override
                    public void onComplete() {

                    }
                });

        return mTrackingDataMutableLiveData;
    }

    public static MutableLiveData<CountryTracking> getTrackingByCountryRemote(String countryName, String dateFrom, String dateTo) {
        MutableLiveData<CountryTracking> mCountryTrackingMutableLiveData = new MutableLiveData<>();

        TrackingClient.getInstance().provideCall().trackingServiceByCountry(countryName, dateFrom, dateTo)
                .subscribeOn(Schedulers.io())
                .map(responseBody ->
                        JsonMapper.mapTrackingFromJson(responseBody.string())
                )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CountryTracking>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onNext(CountryTracking value) {
                        if (value.getCountryDetailList().size() == 0) {
                            mCountryTrackingMutableLiveData.setValue(new CountryTracking(false));
                        } else
                            mCountryTrackingMutableLiveData.setValue(value);
                    }

                    @Override
                    public void onError(Throwable e) {

                        mCountryTrackingMutableLiveData.setValue(new CountryTracking(true,e.getMessage()));

                    }

                    @Override
                    public void onComplete() {

                    }
                });

        return mCountryTrackingMutableLiveData;
    }

    public static void clearDisposable() {
            disposable.clear();
    }
}
