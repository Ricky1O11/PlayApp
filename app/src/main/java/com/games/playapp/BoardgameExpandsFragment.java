package com.games.playapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import static com.games.playapp.BoardgameDetailFragment.bgDetRef;
import static com.games.playapp.SignedInActivity.actionBar;
import static com.games.playapp.SignedInActivity.collapsingToolbar;
import static com.games.playapp.SignedInActivity.database;
import static com.games.playapp.SignedInActivity.favouritesSnap;
import static com.games.playapp.SignedInActivity.getFavourites;

public class BoardgameExpandsFragment extends Fragment implements BoardgamesAdapter.BoardgamesAdapterOnClickHandler{
    public static DatabaseReference bgRef;
    public static BoardgamesAdapter mBoardgamesAdapter;
    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerView mRecycler;
    private String query;
    private BgModel lastBg;

    private static final int LIMIT = 20;
    private int currentPage = 0;
    private Double averageEndsAt= 10.0;
    private String endsAtKey = "";


    private LinkedList<BgModel> boardgames = new LinkedList();
    private BoardgamesFragment.BoardgamesFragmentListener mListener;
    private EndlessRecyclerViewScrollListener scrollListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_boardgames, container, false);

        mRecycler = (RecyclerView) view.findViewById(R.id.recyclerview_boardgames);
        mBoardgamesAdapter = new BoardgamesAdapter(getContext(), this);
        collapsingToolbar.setTitleEnabled(false);

        query = ((SignedInActivity) getActivity()).getQuery();

        mRecycler.setAdapter(mBoardgamesAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRecycler.setLayoutManager(linearLayoutManager);
        mRecycler.setHasFixedSize(true);



        mRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                loadMoreData();
            }
        };

        mRecycler.addOnScrollListener(scrollListener);

        loadData(query, "average", averageEndsAt, endsAtKey); // load data here for first time launch app

        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
        mRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh(){
                        Log.i("chris", "onRefresh called from SwipeRefreshLayout");

                        boardgames.clear();
                        averageEndsAt = 10.0;
                        endsAtKey = "";
                        loadData(query, "average", averageEndsAt, endsAtKey); // load data here for first time launch app
                        mBoardgamesAdapter.swapArrayList(boardgames);
                        mRefreshLayout.setRefreshing(false);
                    }
                }
        );

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

    private void loadData(String search_field, String orderingField, final Double endAt, final String endAtKey) { // "", average, 1, 18

        Query query;
        if(search_field!=""){
            String nameEndsAt = search_field.substring(0, search_field.length()-1) +
                    Utils.changeLetter(search_field.substring(search_field.length()-1, search_field.length()));
            query = bgDetRef.child("expands").orderByChild("search_name").startAt(search_field).endAt(nameEndsAt).limitToFirst(LIMIT);
        }
        else{
            if(orderingField == "search_name") {
                query = bgDetRef.child("expands").orderByChild(orderingField).limitToFirst(LIMIT);
            }
            else{
                if(endAtKey == "") {
                    query = bgDetRef.child("expands").orderByChild(orderingField).endAt(endAt).limitToLast(LIMIT);
                }
                else {
                    query = bgDetRef.child("expands").orderByChild(orderingField).endAt(endAt, endAtKey).limitToLast(LIMIT);
                }
            }
        }

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                LinkedList<BgModel> tempBoardgames = new LinkedList();
                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                    Map<String, Object> game = (Map<String, Object>) childSnapshot.getValue();
                    Log.d("gamename", ""+game);
                    BgModel boardgame = new BgModel(game);
                    tempBoardgames.addFirst(boardgame);

                }
                if(tempBoardgames.size() > 0) {
                    lastBg = tempBoardgames.getLast();
                    boardgames.addAll(tempBoardgames);
                    mBoardgamesAdapter.swapArrayList(boardgames);

                    averageEndsAt = lastBg.getAverage();
                    endsAtKey = lastBg.getBggId();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void loadMoreData(){
        if(boardgames.size() >= 20)
            loadData(query, "average", averageEndsAt, endsAtKey);
    }
}
