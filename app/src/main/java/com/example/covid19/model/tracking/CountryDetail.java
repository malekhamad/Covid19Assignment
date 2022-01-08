package com.example.covid19.model.tracking;

public class CountryDetail extends CountryStatus {

    private String source ;
    private long confirmed ;
    private long deaths ;
    private long recovered ;
    private String date ;

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getCases() {
        return String.valueOf(cases);
    }

    public void setCases(long cases) {
        this.cases = cases;
    }

    public String getConfirmed() {
        return String.valueOf(confirmed);
    }

    public void setConfirmed(long confirmed) {
        this.confirmed = confirmed;
    }

    public String getDeaths() {
        return String.valueOf(deaths);
    }

    public void setDeaths(long deaths) {
        this.deaths = deaths;
    }

    public String getRecovered() {
        return String.valueOf(recovered);
    }

    public void setRecovered(long recovered) {
        this.recovered = recovered;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
