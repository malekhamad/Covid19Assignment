package com.example.covid19.utils;

import android.os.Build;
import android.util.Log;

import com.example.covid19.model.tracking.CountryDetail;
import com.example.covid19.model.tracking.CountryStatus;
import com.example.covid19.model.tracking.CountryTracking;
import com.example.covid19.model.tracking.CountryRootData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class JsonMapper {


    public static CountryTracking mapTrackingFromJson(String json) throws JSONException {


        CountryTracking mCountryTracking = new CountryTracking();


        JSONObject mainJSONObj = new JSONObject(json);


        // get dates JSONObject from mainJSONObj
        JSONObject datesJSONObj=mainJSONObj.optJSONObject("dates");

        // get all dates from datesJSONObj
        Iterator<String> iterator = datesJSONObj.keys();
        while (iterator.hasNext()) {
            String date = iterator.next();

            // get Countries Object
            JSONObject dynamicDataObj =datesJSONObj.optJSONObject(date);
            JSONObject countriesJSONObj = dynamicDataObj.optJSONObject("countries");

            // iterate to all countries
            Iterator<String> countryIterator = countriesJSONObj.keys();
            while (countryIterator.hasNext()) {
                String countryKey = countryIterator.next();
                // get country object
                JSONObject country = countriesJSONObj.optJSONObject(countryKey);

                CountryDetail mCountryDetail = new CountryDetail();
                mCountryDetail.setCountryName(country.optString("name"));
                mCountryDetail.setCases(country.optLong("today_open_cases"));
                mCountryDetail.setDeaths(country.optLong("today_deaths"));
                mCountryDetail.setConfirmed(country.optLong("today_confirmed"));
                mCountryDetail.setSource(country.optString("source"));
                mCountryDetail.setRecovered(country.optLong("today_recovered"));
                mCountryDetail.setDate(country.optString("date"));



                mCountryTracking.addCountryDetails(mCountryDetail);

            }
        }
        return mCountryTracking ;
    }

    public static CountryRootData mapAllJson(String json) throws JSONException {

        Map<String,CountryStatus> mMap = new HashMap<>();

        CountryRootData mCountryRootData;

        JSONObject mainJSONObj = new JSONObject(json);

        // get Total Data ;
        JSONObject totalJSONObj=mainJSONObj.optJSONObject("total");
        long wCases = totalJSONObj.optLong("today_open_cases");
        long wRecovered = totalJSONObj.optLong("today_recovered");
        long wConfirmed = totalJSONObj.optLong("today_confirmed");
        long wDeath = totalJSONObj.optLong("today_deaths");
        mCountryRootData = new CountryRootData(wCases,wRecovered,wConfirmed,wDeath);


        // get dates JSONObject from mainJSONObj
        JSONObject datesJSONObj=mainJSONObj.optJSONObject("dates");

        // get all dates from datesJSONObj
        Iterator<String> iterator = datesJSONObj.keys();
        while (iterator.hasNext()) {
            String date = iterator.next();

            // get Countries Object
            JSONObject dynamicDataObj =datesJSONObj.optJSONObject(date);
            JSONObject countriesJSONObj = dynamicDataObj.optJSONObject("countries");

            // iterate to all countries
            Iterator<String> countryIterator = countriesJSONObj.keys();
            while (countryIterator.hasNext()) {
                String countryKey = countryIterator.next();
                // get country object
                JSONObject country = countriesJSONObj.optJSONObject(countryKey);


                String name = country.optString("name");
                long cases = country.optLong("today_open_cases");

                // check if key(countryName) already exist then append it
//                if(mMap.get(name) == null){
                    mMap.put(name,new CountryStatus(cases,name));
          /*      }else {
                   long previousCaseNum = mMap.get(name).getCasesNumber() ;
                    mMap.put(name,new CountryStatus(cases+previousCaseNum,name));

                    Log.i("MapJson", "previousName : "+name+"    previousNumber: "+previousCaseNum);
                    Log.i("MapJson", "newCases :"+cases);

                }*/
            }
        }


        List<CountryStatus> mCountryStatusList = new ArrayList<>(mMap.values());

        // sort data ascending before return them
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Collections.sort(mCountryStatusList, Comparator.comparing(obj -> Long.valueOf(obj.getCasesNumber())));
        }

        mCountryRootData.setCountryStatusList(mCountryStatusList);

        return mCountryRootData;
    }

}
