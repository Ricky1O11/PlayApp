package com.games.playapp;


import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

import static com.games.playapp.SignedInActivity.favourites;
import static com.games.playapp.SignedInActivity.profileRef;

public class BgModel {
    private Float mAverage;
    private String mName;
    private String mThumbnail;
    private String mImage;
    private int mBggId;

    public BgModel() {
        // Needed for Firebase
    }

    public BgModel(int bggId, Float average, String name, String thumbnail, String image) {
        mAverage = average;
        mName = name;
        mThumbnail = thumbnail;
        mBggId = bggId;
        mImage = image;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public Float getAverage() {
        return mAverage;
    }

    public void setImage(String image) {
        mImage = image;
    }

    public String getImage() {
        return mImage;
    }

    public void setAverage(Float average) {
        mAverage = average;
    }

    public String getThumbnail() {
        return mThumbnail;
    }

    public void setThumbnail(String thumbnail) {
        mThumbnail = thumbnail;
    }

    public int getBggId() {
        return mBggId;
    }

    public void setBggId(int bggId) {
        mBggId = bggId;
    }
}
