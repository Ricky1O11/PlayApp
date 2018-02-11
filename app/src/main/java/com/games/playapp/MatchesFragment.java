package com.games.playapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import static com.games.playapp.SignedInActivity.actionBar;
import static com.games.playapp.SignedInActivity.gamesRef;
import static com.games.playapp.SignedInActivity.profileRef;
import static com.games.playapp.SignedInActivity.user;

/**
 * Created by benjy on 27/12/2017.
 */

public class MatchesFragment extends Fragment {

    private MatchesFragment.OnMatchesFragmentListener matchesListener;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_matches, container, false);
        actionBar.setTitle("Matches");
        final ViewPager viewPager=(ViewPager) view.findViewById(R.id.viewPagerMatches);
        ((SignedInActivity) getActivity()).getTabLayout().setupWithViewPager(viewPager);
        final MatchesTabPageAdapter viewPagerAdapter = new MatchesTabPageAdapter(getActivity().getSupportFragmentManager(), getContext());
        viewPager.setAdapter(viewPagerAdapter);

        gamesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.w("MATCHES", "MAMAMATCHEEEESSSSS");

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("chrissj2", "Failed to read value.", error.toException());
            }

        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        matchesListener.disableCollapse();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MatchesFragment.OnMatchesFragmentListener) {
            matchesListener = (MatchesFragment.OnMatchesFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnCheeseDetailFragmentListener");
        }
    }

    public interface OnMatchesFragmentListener {
        void disableCollapse();
    }

    public static MatchesFragment newInstance(){
        Bundle args = new Bundle();
        MatchesFragment fragment = new MatchesFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
