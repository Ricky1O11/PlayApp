package com.games.playapp;


import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

import static com.games.playapp.SignedInActivity.favourites;
import static com.games.playapp.SignedInActivity.getFavourites;
import static com.games.playapp.SignedInActivity.profileRef;

public class BgModel {
    private Double mAverage;
    private String mName;
    private String mThumbnail;
    private String mImage;
    private String mBggId;

    public BgModel() {
        // Needed for Firebase
    }

    public BgModel(Map<String, Object> game) {
        mAverage = (Double) game.get("average");
        mName = (String) game.get("name");
        mThumbnail = (String) game.get("thumbnail");
        mBggId = game.get("bggId").toString();
        mImage = (String) game.get("image");
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public Double getAverage() {
        return mAverage;
    }

    public void setImage(String image) {
        mImage = image;
    }

    public String getImage() {
        return mImage;
    }

    public void setAverage(Double average) {
        mAverage = average;
    }

    public String getThumbnail() {
        return mThumbnail;
    }

    public void setThumbnail(String thumbnail) {
        mThumbnail = thumbnail;
    }

    public String getBggId() {
        return mBggId;
    }

    public void setBggId(String bggId) {
        mBggId = bggId;
    }

    public void printGame(){
        Log.d("chrissj2", mName);
    }

    public Boolean isFavourite(){
        Map<String, Object> favourites = getFavourites();
        if(favourites != null) {
            Map<String, Object> favourited_game = (Map<String, Object>) favourites.get("" + mBggId);
            return (favourited_game != null);
        }
        return false;
    }
}
