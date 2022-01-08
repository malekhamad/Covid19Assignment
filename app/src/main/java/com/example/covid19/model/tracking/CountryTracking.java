package com.example.covid19.model.tracking;

import com.example.covid19.model.FailureRes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CountryTracking extends FailureRes {

    private boolean hasData = true ;

    public CountryTracking() {
    }
    public CountryTracking(boolean isHasError, String errorMsg) {
        super(isHasError,errorMsg);
    }

    public CountryTracking(boolean hasData) {
        this.hasData = hasData;
    }

    private List<CountryDetail> mCountryDetailList = new ArrayList<>();

    public List<CountryDetail> getCountryDetailList() {
        return mCountryDetailList;
    }

    public void addCountryDetails(CountryDetail countryDetail) {
        mCountryDetailList.add(countryDetail);
    }

    public boolean isHasData() {
        return hasData;
    }

    public void setHasData(boolean hasData) {
        this.hasData = hasData;
    }
}
