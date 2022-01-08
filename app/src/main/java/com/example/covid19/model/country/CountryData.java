package com.example.covid19.model.country;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CountryData implements Parcelable {

    @SerializedName("latlng")
    private List<Double> mLatlng;
    @SerializedName("flags")
    private Flags mFlags ;
    @SerializedName("cca2")
    private String mCca2;
    @SerializedName("name")
    private Name mName;


    protected CountryData(Parcel in) {
        mFlags = in.readParcelable(Flags.class.getClassLoader());
        mCca2 = in.readString();
        mName = in.readParcelable(Name.class.getClassLoader());
    }

    public static final Creator<CountryData> CREATOR = new Creator<CountryData>() {
        @Override
        public CountryData createFromParcel(Parcel in) {
            return new CountryData(in);
        }

        @Override
        public CountryData[] newArray(int size) {
            return new CountryData[size];
        }
    };

    public List<Double> getLatlng() {
        return mLatlng;
    }

    public Flags getFlags() {
        return mFlags;
    }


    public String getCca2() {
        return mCca2;
    }

    public Name getName() {
        return mName;
    }

    public String getOfficialName(){
        return mName.getOfficial() ;
    }

    public String getSvgFlag(){
        return mFlags.getSvg();
    }

      public String getPngFlag(){
        return mFlags.getPng();
    }

    public void setLatlng(List<Double> latlng) {
        mLatlng = latlng;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(mFlags, i);
        parcel.writeString(mCca2);
        parcel.writeParcelable(mName, i);
        parcel.writeList(mLatlng);
    }
}
