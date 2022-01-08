package com.example.covid19.model.tracking;

import com.example.covid19.model.FailureRes;

import java.util.List;

public class CountryRootData extends FailureRes {
    private long worldCases ;
    private long worldDeath ;
    private long worldRecovered ;
    private long worldConfirmed ;

    private List<CountryStatus> mCountryStatusList ;

    public CountryRootData(boolean hasError, String errorMsg){
        super(hasError, errorMsg);
    }

    public CountryRootData(long worldCases, long worldDeath, long worldRecovered, long worldConfirmed) {
        this.worldCases = worldCases;
        this.worldDeath = worldDeath;
        this.worldRecovered = worldRecovered;
        this.worldConfirmed = worldConfirmed;
    }

    public String getWorldCases() {
        return String.valueOf(worldCases);
    }

    public String getWorldDeath() {
        return String.valueOf(worldDeath);
    }

    public String getWorldRecovered() {
        return String.valueOf(worldRecovered);
    }


    public String getWorldConfirmed() {
        return String.valueOf(worldConfirmed);
    }

    public void setCountryStatusList(List<CountryStatus> countryStatusList) {
        mCountryStatusList = countryStatusList;
    }

    public List<CountryStatus> getCountryStatusList() {
        return mCountryStatusList;
    }
}
