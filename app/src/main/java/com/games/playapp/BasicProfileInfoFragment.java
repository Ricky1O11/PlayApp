package com.games.playapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import static com.games.playapp.SignedInActivity.gamesRef;
;

import static com.games.playapp.SignedInActivity.user;

/**
 * Created by benjy on 22/08/2017.
 */

public class BasicProfileInfoFragment extends Fragment {
    private TextView mMatchPlayed, mMatchWon, mDuration, mUsername, mProfileAvatar;
    private ImageView mProfileImage;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_basic_profile_info, container, false);
        /*mMatchPlayed = (TextView) view.findViewById(R.id.tv_match_played);
        mMatchWon = (TextView) view.findViewById(R.id.tv_match_won);
        mDuration = (TextView) view.findViewById(R.id.tv_duration);
*/

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
                /*
                mMatchPlayed.setText(""+match_played);
                mMatchWon.setText(""+match_won);
                mDuration.setText("250");
                */
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("chrissj2", "Failed to read value.", error.toException());
            }

        });
        return view;
    }
}
