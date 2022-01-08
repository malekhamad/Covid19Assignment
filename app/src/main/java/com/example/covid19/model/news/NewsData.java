package com.example.covid19.model.news;

import java.util.ArrayList;

public class NewsData {

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
