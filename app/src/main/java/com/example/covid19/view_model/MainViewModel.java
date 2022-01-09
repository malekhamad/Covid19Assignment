package com.example.covid19.view_model;

import android.app.Application;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.covid19.model.country.CountryData;
import com.example.covid19.model.tracking.CountryTracking;
import com.example.covid19.model.tracking.CountryRootData;
import com.example.covid19.repository.CountryRepository;
import com.example.covid19.repository.TrackingRepository;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainViewModel extends AndroidViewModel {

    private static final String TAG = "MainViewModel";
    // region Variables
    private Calendar selectedStartCalendar;
    private Calendar selectedEndCalendar;
    private Calendar selectedStartCalendarShown;
    private CountryTracking mCountryTracking;
    private Application mApplication;

    private Map<String,CountryData> mCountryData ;

    MutableLiveData<Boolean> showCountryBottomSheetMutable = new MutableLiveData<>();

    // endregion

    public MainViewModel(@NonNull Application application) {
        super(application);
        this.mApplication = application;
    }

    // region MutableLiveData Methods

    public MutableLiveData<CountryRootData> getTrackingAllData(String dateFrom, String dateTo) {
        return TrackingRepository.getTrackingAllData(dateFrom, dateTo);
    }

    public MutableLiveData<Boolean> getTrackingDataByCountry(String countryName, String dateFrom, String dateTo) {
        TrackingRepository.getTrackingByCountryRemote(countryName, dateFrom, dateTo).observeForever(countryTracking -> {
            if(!countryTracking.isHasError()){
                mCountryTracking = countryTracking;
                showCountryBottomSheetMutable.setValue(true);
            }else{
                showCountryBottomSheetMutable.setValue(false);
            }

        });
        return showCountryBottomSheetMutable;
    }

    public void getAllCountryData(){
      CountryRepository.getCountryDataRemote().observeForever(countryDataMap -> mCountryData = countryDataMap);
    };

    public Map<String, CountryData> getCountryData() {
        return mCountryData;
    }

    public MutableLiveData<String> getCountryNameFromLocation(LatLng latLng) {
        MutableLiveData<String> countryNameMutableLiveData = new MutableLiveData<>();

        Observable.defer((Callable<ObservableSource<String>>) () -> Observable.just(getCountryName(latLng)))
                .debounce(2, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String value) {
                        countryNameMutableLiveData.setValue(value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        countryNameMutableLiveData.setValue(null);
                        Log.i(TAG, "onError: "+e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });

        return countryNameMutableLiveData;

    }

    // endregion

    // region Methods

    public void setSelectedStartCalendar(int year, int month, int date) {
        this.selectedStartCalendar = Calendar.getInstance();
        this.selectedStartCalendar.set(year, month + 1, date);
    }

    public Long getSelectedStartCalendar() {
        return this.selectedStartCalendar.getTimeInMillis();
    }

    public String getSelectedStartDateWithFormat() {

        int year = selectedStartCalendar.get(Calendar.YEAR);
        int month = selectedStartCalendar.get(Calendar.MONTH);
        int day = selectedStartCalendar.get(Calendar.DAY_OF_MONTH);

        return year + "-" + month + "-" + day;
    }

    /*
     * this method just for set max and min value on date picker
     * because month returned as index not value on calendar
     * */
    public void setSelectedStartCalendarShown(int year, int month, int date) {
        this.selectedStartCalendarShown = Calendar.getInstance();
        this.selectedStartCalendarShown.set(year, month, date);

    }

    public long getSelectedStartCalendarShown() {
        return selectedStartCalendarShown.getTimeInMillis();
    }


    public void setSelectedEndCalendar(int year, int month, int date) {
        this.selectedEndCalendar = Calendar.getInstance();
        this.selectedEndCalendar.set(year, month + 1, date);

    }

    public boolean isDateFieldFilled() {
        return selectedEndCalendar == null ? false : true;
    }

    public Long getSelectedEndCalendar() {
        return selectedEndCalendar.getTimeInMillis();
    }

    public String getSelectedEndDateWithFormat() {
        int year = selectedEndCalendar.get(Calendar.YEAR);
        int month = selectedEndCalendar.get(Calendar.MONTH);
        int day = selectedEndCalendar.get(Calendar.DAY_OF_MONTH);

        return year + "-" + month + "-" + day;
    }

    public CountryTracking getCountryTracking() {
        return mCountryTracking;
    }

    private String getCountryName(@NonNull LatLng latLng) throws IOException {
        // get Name from Location geocoder
        Geocoder geocoder = new Geocoder(mApplication.getApplicationContext(), Locale.getDefault());
        List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
        if (addresses.size() > 0) {
            return addresses.get(0).getCountryName();


        }
        return "";
    }

    public void setShowCountryBottomSheetMutableLiveData(Boolean showDialog) {
        this.showCountryBottomSheetMutable.setValue(showDialog);
    }

    // endregion

    @Override
    protected void onCleared() {
        super.onCleared();
        CountryRepository.disposeObservable();
        TrackingRepository.clearDisposable();
    }
}
