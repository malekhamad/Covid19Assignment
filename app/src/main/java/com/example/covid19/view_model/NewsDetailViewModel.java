package com.example.covid19.view_model;

import androidx.lifecycle.ViewModel;

import com.example.covid19.model.news.Article;

public class NewsDetailViewModel extends ViewModel {
    private Article mArticle;

    public Article getArticle(){
        return mArticle ;
    }

    public void setArticle(Article article){
        this.mArticle = article ;
    }
}
