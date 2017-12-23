package com.games.playapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import java.util.Map;

/**
 * Created by benjy on 16/12/2017.
 */

public class ProfileFavouritesFragment extends Fragment {
    public static Map<String, Object> favourites;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_favourites, container, false);
    }
    public static Map<String,Object> getFavourites(){
        return favourites;
    }

}



