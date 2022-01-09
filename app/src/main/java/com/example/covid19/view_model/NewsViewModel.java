package com.example.covid19.view_model;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.covid19.model.news.NewsData;
import com.example.covid19.repository.NewsRepository;

public class NewsViewModel extends ViewModel {

    public MutableLiveData<NewsData> getNewsData(String countryCode){
         return NewsRepository.getNewsDataRemote(countryCode);
    }


    @Override
    protected void onCleared() {
        super.onCleared();
        NewsRepository.clearDisposable();
    }
}
