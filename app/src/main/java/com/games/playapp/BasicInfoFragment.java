package com.games.playapp;

import android.support.v4.app.Fragment;;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by benjy on 22/08/2017.
 */

public class BasicInfoFragment extends Fragment {
    private TextView mBoardgameOverview, mBoardgameRating, mBoardgameYear, mBoardgameDuration;
    private Map<String, Object> boardgameInfo = new HashMap<>();

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_basic_info, container, false);

        mBoardgameOverview = (TextView) view.findViewById(R.id.tv_boardgame_overview);
        mBoardgameRating = (TextView) view.findViewById(R.id.tv_boardgame_rating);
        mBoardgameYear = (TextView) view.findViewById(R.id.tv_boardgame_year);
        mBoardgameDuration = (TextView) view.findViewById(R.id.tv_boardgame_duration);

        Bundle bundle = this.getArguments();
        if(bundle != null && bundle.getSerializable("boardgameInfo") != null)
            BoardgameDetailFragment.boardgameInfo = (HashMap<String,Object>)bundle.getSerializable("boardgameInfo");

        Log.d("chrissj2", ""+BoardgameDetailFragment.boardgameInfo);
        if(!BoardgameDetailFragment.boardgameInfo.isEmpty()){
            Double average = (Double) BoardgameDetailFragment.boardgameInfo.get("average");
            String yearpublished = (String) BoardgameDetailFragment.boardgameInfo.get("yearpublished");
            String playingtime =(String)  BoardgameDetailFragment.boardgameInfo.get("playingtime");
            String description = (String) BoardgameDetailFragment.boardgameInfo.get("description");

            mBoardgameRating.setText((Math.round(average*100.0)/100.0)+"");
            mBoardgameYear.setText(yearpublished);
            mBoardgameDuration.setText(playingtime+" min");

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                mBoardgameOverview.setText(Html.fromHtml(description, Html.FROM_HTML_MODE_LEGACY).toString());
            } else {
                mBoardgameOverview.setText(Html.fromHtml(description).toString());
            }
        }
        return view;
    }
}
