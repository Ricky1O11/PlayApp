package com.games.playapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.LinkedList;
import java.util.Map;


public class ProfileFavouritesFragment extends Fragment implements BoardgamesFavouriteAdapter.BoardgamesFavouriteAdapterOnClickHandler{
    public static BoardgamesFavouriteAdapter mBoardgamesAdapter;
    private RecyclerView mRecycler;


    private LinkedList<BgModel> boardgames = new LinkedList();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_profile_favourites, container, false);

        mRecycler = (RecyclerView) view.findViewById(R.id.recyclerview_boardgame_favourites);
        mBoardgamesAdapter = new BoardgamesFavouriteAdapter(getContext(), this);

        mRecycler.setAdapter(mBoardgamesAdapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 4);
        mRecycler.setLayoutManager(gridLayoutManager);
        mRecycler.setHasFixedSize(true);

        boardgames.clear();
        for (Map.Entry<String, Object> game : SignedInActivity.favourites.entrySet()) {
            BgModel boardgame = new BgModel((Map) game.getValue());
            boardgames.addFirst(boardgame);
        }
        mBoardgamesAdapter.swapArrayList(boardgames);

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
