package com.games.playapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import static com.games.playapp.SignedInActivity.profileRef;
import static com.games.playapp.SignedInActivity.gamesRef;
import static com.games.playapp.SignedInActivity.user;

public class ProfileFragment extends Fragment {
    private OnProfileFragmentListener mListener;

    private TextView mMatchPlayed, mMatchWon, mDuration, mUsername, mProfileAvatar;
    private ImageView mProfileImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_profile, container, false);
        final ViewPager viewPager=(ViewPager) view.findViewById(R.id.viewPager);
        ((SignedInActivity) getActivity()).getTabLayout().setupWithViewPager(viewPager);
        final ProfileTabPageAdapter viewPagerAdapter = new ProfileTabPageAdapter(getFragmentManager(), getContext());
        viewPager.setAdapter(viewPagerAdapter);
/*
        mUsername= (TextView) view.findViewById(R.id.tv_username);
        mMatchPlayed = (TextView) view.findViewById(R.id.tv_match_played);
        mMatchWon = (TextView) view.findViewById(R.id.tv_match_won);
        mDuration = (TextView) view.findViewById(R.id.tv_duration);
        mProfileImage = (ImageView) view.findViewById(R.id.iv_profile_img);
        mProfileAvatar = (TextView) view.findViewById(R.id.tv_profile_avatar);
*/



        profileRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                setProfileInfo((Map<String, Object>) dataSnapshot.getValue());
                viewPagerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("chrissj2", "Failed to read value.", error.toException());
            }
        });
        /*
        gamesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Map<String, Object> companions;
                int match_played = 0;
                int match_finished = 0;
                int match_won = 0;
                String most_played_game = "-";
                int most_played_game_amount = 0;

                Map<String, Object> user_games = (Map<String, Object>) dataSnapshot.getValue();

                if(user_games != null && user_games.size() > 0){
                    for(Map.Entry<String, Object> game_entry : user_games.entrySet()){
                        Map<String, Object> game = (Map<String, Object>) game_entry.getValue();
                        game.put("lastMatchTime", (long) 0);

                        //get most played game
                        Map<String, Object> played_matches = (Map<String, Object>) game.get("matches");
                        if(played_matches == null) continue;
                        if (played_matches.size() > most_played_game_amount) {
                            most_played_game_amount = played_matches.size();
                            most_played_game = (String) game.get("name");
                        }

                        for (Map.Entry<String, Object> match_entry : played_matches.entrySet()) {
                            Map<String, Object> match = (Map<String, Object>) match_entry.getValue();
                            game.put("lastMatchTime", Math.max((long) game.get("lastMatchTime"), (long) match.get("time")));

                            //get finished, won and played matches
                            if ((Boolean) match.get("completed")) {
                                match_finished++;
                                HashMap winner = (HashMap) match.get("winner");

                                if (winner.containsKey(user.getUid()))
                                    match_won++;
                            }
                            match_played++;

                        }

                    }
                }
                mMatchPlayed.setText(""+match_played);
                mMatchWon.setText(""+match_won);
                mDuration.setText("250");
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("chrissj2", "Failed to read value.", error.toException());
            }

        });*/
        return view;
    }

    private void setProfileInfo(Map<String, Object> data){
        String image = (String) ""+data.get("image");
        String username = (String) data.get("username");

        ((SignedInActivity) getActivity()).getCollapsingToolbar().setTitle(username);
        ((SignedInActivity) getActivity()).loadImage(image);
        if(!("".equals(image))) {
            ((SignedInActivity) getActivity()).loadImage(image);
        }
        else{
            // ((SignedInActivity) getActivity()).loadImage(image); will need some placeholder
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mListener.enableCollapse();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnProfileFragmentListener) {
            mListener = (OnProfileFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnCheeseDetailFragmentListener");
        }
    }

    public interface OnProfileFragmentListener {
        void enableCollapse();
    }

    public static ProfileFragment newInstance(){
        Bundle args = new Bundle();
        ProfileFragment fragment = new ProfileFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
