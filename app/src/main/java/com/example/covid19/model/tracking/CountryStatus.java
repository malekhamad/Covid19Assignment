package com.example.covid19.model.tracking;

public class CountryStatus {

    protected long cases ;
    protected String countryName ;



    public CountryStatus() {
    }

    public CountryStatus(long cases, String countryName) {
        this.cases = cases;
        this.countryName = countryName;
    }

    public long getCasesNumber() {
        return cases;
    }


    public String getCountryName() {
        return countryName;
    }

    public void setCases(long cases) {
        this.cases = cases;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }



}
