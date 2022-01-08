package com.example.covid19.model.country;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Flags implements Parcelable {

    public Flags(){

    }
    @SerializedName("png")
    private String png ;

    @SerializedName("svg")
    private String svg ;

    protected Flags(Parcel in) {
        png = in.readString();
        svg = in.readString();
    }

    public static final Creator<Flags> CREATOR = new Creator<Flags>() {
        @Override
        public Flags createFromParcel(Parcel in) {
            return new Flags(in);
        }

        @Override
        public Flags[] newArray(int size) {
            return new Flags[size];
        }
    };

    public String getPng() {
        return png;
    }


    public String getSvg() {
        return svg;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(png);
        parcel.writeString(svg);
    }
}
