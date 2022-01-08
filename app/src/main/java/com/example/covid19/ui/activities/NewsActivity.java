package com.example.covid19.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.covid19.R;
import com.example.covid19.databinding.ActivityNewsBinding;
import com.example.covid19.model.country.CountryData;
import com.example.covid19.model.news.NewsData;
import com.example.covid19.network.webservices.NewsWebService;
import com.example.covid19.ui.adapters.RecyclerNewsAdapter;
import com.example.covid19.view_model.NewsViewModel;

public class NewsActivity extends BaseActivity {

    // region Variables
    public static final String COUNTRY_DATA_KEY = "country_data";
    private ActivityNewsBinding mBinding;
    private CountryData mCountryData;
    private NewsViewModel mNewsViewModel ;
    private RecyclerNewsAdapter recyclerNewsAdapter ;

    // endregion

    // region Listeners
    private Observer<NewsData> newsDataObserver = new Observer<NewsData>() {
        @Override
        public void onChanged(NewsData newsData) {
            hideProgressDialog();
            if(!newsData.isHasError()){
                if(newsData.getArticles().size() == 0){
                    mBinding.textviewNoDataFound.setVisibility(View.VISIBLE);
                }else
                    recyclerNewsAdapter.setArticleList(newsData.getArticles());
            }else {
                onFailure();
            }



        }
    };

    // endregion

    // region Override Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_news);

        init();

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    // endregion

    // region Methods
    public void init() {
        mCountryData = getIntent().getExtras().getParcelable(COUNTRY_DATA_KEY);
        mNewsViewModel = new ViewModelProvider(this).get(NewsViewModel.class);

        setSupportActionBar(mBinding.toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.news_title));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mBinding.recyclerNewsView.setLayoutManager(new LinearLayoutManager(this));


        Glide.with(this)
                .load(mCountryData.getFlags().getPng())
                .circleCrop()
                .into(mBinding.imageFlag);

        recyclerNewsAdapter = new RecyclerNewsAdapter(this);
        mBinding.recyclerNewsView.setAdapter(recyclerNewsAdapter);

        showProgressDialog();
        mNewsViewModel.getNewsData(mCountryData.getCca2()).observe(this,newsDataObserver);

    }

    @Override
    public Activity currentActivity() {
        return this;
    }

    // endregion
}