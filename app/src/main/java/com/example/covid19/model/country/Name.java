package com.example.covid19.model.country;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Name implements Parcelable {

    public Name(){

    }

    @SerializedName("official")
    private String mOfficial;

    @SerializedName("common")
    private String mCommon;

    protected Name(Parcel in) {
        mOfficial = in.readString();
        mCommon = in.readString();
    }

    public static final Creator<Name> CREATOR = new Creator<Name>() {
        @Override
        public Name createFromParcel(Parcel in) {
            return new Name(in);
        }

        @Override
        public Name[] newArray(int size) {
            return new Name[size];
        }
    };

    public String getOfficial() {
        return mOfficial;
    }

    public String getCommon() {
        return mCommon;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mOfficial);
        parcel.writeString(mCommon);
    }
}
