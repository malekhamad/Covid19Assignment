package com.example.covid19.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.covid19.R;
import com.example.covid19.databinding.CountryItemBinding;
import com.example.covid19.model.tracking.CountryDetail;

import java.util.List;

public class RecyclerCountryStatusAdapter extends RecyclerView.Adapter<RecyclerCountryStatusAdapter.RecyclerCountryStatusVH> {
    private Context mContext ;
    private List<CountryDetail> mCountryDetailList ;

    public RecyclerCountryStatusAdapter(Context context, List<CountryDetail> countryDetailList) {
        mContext = context;
        mCountryDetailList = countryDetailList;
    }

    @NonNull
    @Override
    public RecyclerCountryStatusVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CountryItemBinding mCountryItemBinding =  DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.country_item, parent, false);

        return new RecyclerCountryStatusVH(mCountryItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerCountryStatusVH holder, int position) {
        CountryDetail mDetail = mCountryDetailList.get(position);
        holder.mCountryItemBinding.setCountryDetail(mDetail);
    }

    @Override
    public int getItemCount() {
        return mCountryDetailList == null ? 0 : mCountryDetailList.size();
    }

    public class RecyclerCountryStatusVH extends RecyclerView.ViewHolder {
          CountryItemBinding mCountryItemBinding ;
        public RecyclerCountryStatusVH(@NonNull CountryItemBinding mCountryItemBinding) {
            super(mCountryItemBinding.getRoot());
            this.mCountryItemBinding = mCountryItemBinding;

        }
    }
}
