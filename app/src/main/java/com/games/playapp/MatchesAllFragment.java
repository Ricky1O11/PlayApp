package com.games.playapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.LinkedList;
import java.util.Map;

/**
 * Created by benjy on 27/12/2017.
 */

public class MatchesAllFragment extends Fragment implements MatchesAllAdapter.BoardgamesPlayedAdapterOnClickHandler{
    public static MatchesAllAdapter mMatchesAdapter;
    private RecyclerView mRecycler;


    private LinkedList<BgModel> matches = new LinkedList();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_matches_all, container, false);

        mRecycler = (RecyclerView) view.findViewById(R.id.recyclerview_matches_all);
        mMatchesAdapter = new MatchesAllAdapter(getContext(), this);

        mRecycler.setAdapter(mMatchesAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRecycler.setLayoutManager(linearLayoutManager);
        mRecycler.setHasFixedSize(true);

        matches.clear();
        //if(SignedInActivity.favourites!=null){
        //    for (Map.Entry<String, Object> game : SignedInActivity.favourites.entrySet()) {
        //        BgModel boardgame = new BgModel((Map) game.getValue());
        //        matches.addFirst(boardgame);
        //    }
        //}
        //else{
        //    Log.i("null", "ciao");
        //}
        mMatchesAdapter.swapArrayList(matches);

        return view;
    }

    @Override
    public void onClick(String id) {
        BoardgameDetailFragment bgdet= new BoardgameDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString("BG_KEY", id);
        bgdet.setArguments(bundle);

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, bgdet,null)
                .addToBackStack(null)
                .commit();
    }
}
