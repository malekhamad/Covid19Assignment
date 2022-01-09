package com.example.covid19.ui.adapters;

import static com.example.covid19.ui.activities.NewsDetailActivity.ARTICLE_ITEM_KEY;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.covid19.R;
import com.example.covid19.databinding.NewsItemCellBinding;
import com.example.covid19.model.news.Article;
import com.example.covid19.ui.activities.NewsDetailActivity;

import java.util.List;

public class RecyclerNewsAdapter extends RecyclerView.Adapter<RecyclerNewsAdapter.RecyclerNewsVH> {
   private Context mContext ;
   private List<Article> mArticleList ;
   public RecyclerNewsAdapter(Context context){
       this.mContext = context ;
   }

    @NonNull
    @Override
    public RecyclerNewsVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        NewsItemCellBinding mNewsItemCellBinding = DataBindingUtil.
                inflate(LayoutInflater.from(parent.getContext())
                        , R.layout.news_item_cell,parent,false);

        return new RecyclerNewsVH(mNewsItemCellBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerNewsVH holder, int position) {
         Article mArticle = mArticleList.get(position);
        holder.mNewsItemCellBinding.setArticle(mArticle);

        holder.mNewsItemCellBinding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mIntent = new Intent(mContext, NewsDetailActivity.class);
                mIntent.putExtra(ARTICLE_ITEM_KEY,mArticle);
                mContext.startActivity(mIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mArticleList == null ? 0 : mArticleList.size();
    }

    public class RecyclerNewsVH extends RecyclerView.ViewHolder {
        NewsItemCellBinding mNewsItemCellBinding ;
        public RecyclerNewsVH(@NonNull NewsItemCellBinding newsItemCellBinding) {
            super(newsItemCellBinding.getRoot());
            this.mNewsItemCellBinding = newsItemCellBinding ;
        }
    }

    public void setArticleList(List<Article> articleList) {
        mArticleList = articleList;
        notifyDataSetChanged();
    }
}
