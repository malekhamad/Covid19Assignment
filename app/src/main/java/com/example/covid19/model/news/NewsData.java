package com.example.covid19.model.news;

import com.example.covid19.model.FailureRes;

import java.util.ArrayList;

public class NewsData extends FailureRes {

    public NewsData(){

    }

    public NewsData(boolean hasError, String errorMsg){
        super(hasError,errorMsg);
    }

    public String status;
    public int totalResults;
    public ArrayList<Article> articles;

    public String getStatus() {
        return status;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public ArrayList<Article> getArticles() {
        return articles;
    }
}
