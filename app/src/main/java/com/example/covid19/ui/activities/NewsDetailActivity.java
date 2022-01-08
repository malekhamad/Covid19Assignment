package com.example.covid19.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.os.Bundle;

import com.example.covid19.R;
import com.example.covid19.databinding.ActivityNewsDetailBinding;
import com.example.covid19.model.news.Article;
import com.example.covid19.view_model.NewsDetailViewModel;
import com.example.covid19.view_model.NewsViewModel;

public class NewsDetailActivity extends BaseActivity {

    // region Variables
    public static final String ARTICLE_ITEM_KEY="article_item";
    ActivityNewsDetailBinding mBinding ;
    NewsDetailViewModel mNewsDetailViewModel ;

    // endregion


    // region Override Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_news_detail);

        init();

    }

    @Override

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    @Override
    public Activity currentActivity() {
        return this;
    }
    // endregion


    // region Methods
    private void init(){
        mNewsDetailViewModel = new ViewModelProvider(this).get(NewsDetailViewModel.class);

        Article mArticleData = getIntent().getExtras().getParcelable(ARTICLE_ITEM_KEY);
        mNewsDetailViewModel.setArticle(mArticleData);

        setSupportActionBar(mBinding.toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.news_title));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mBinding.setArticle(mNewsDetailViewModel.getArticle());

    }

    // endregion


}