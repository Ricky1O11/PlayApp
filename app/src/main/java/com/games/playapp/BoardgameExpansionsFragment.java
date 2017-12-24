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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.LinkedList;
import java.util.Map;

import static com.games.playapp.BoardgameDetailFragment.bgDetRef;

public class BoardgameExpansionsFragment extends Fragment implements BoardgamesAdapter.BoardgamesAdapterOnClickHandler{
    public static BoardgamesAdapter mBoardgamesAdapter;
    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerView mRecycler;
    private String query;
    private BgModel lastBg;

    private static final int LIMIT = 20;
    private Double averageEndsAt= 10.0;
    private String endsAtKey = "";


    private LinkedList<BgModel> boardgames = new LinkedList();
    private EndlessRecyclerViewScrollListener scrollListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_boardgame_expansions, container, false);

        mRecycler = (RecyclerView) view.findViewById(R.id.recyclerview_boardgame_expansions);
        mBoardgamesAdapter = new BoardgamesAdapter(getContext(), this);

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

        loadData("", "average", averageEndsAt, endsAtKey, true); // load data here for first time launch app

        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
        mRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh(){
                        Log.i("chris", "onRefresh called from SwipeRefreshLayout");

                        boardgames.clear();
                        averageEndsAt = 10.0;
                        endsAtKey = "";
                        loadData("", "average", averageEndsAt, endsAtKey, false);
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

    /*
    * Loads data from firebase
    * @search_field: name of the game as typed by the user in the search box
    * @orderingField: type of ordering: by average or by name
    * @endAt: value of the last retrieved game
    * @endAtKey: id of the last retrieved game: used in case of clashing values
    */
    private void loadData(String search_field, String orderingField, final Double endAt, final String endAtKey, final boolean override) { // "", average, 1, 18
        Query query;
        Log.d("datachanged", "IN"+search_field);
        Log.d("datachanged", "IN"+orderingField);
        Log.d("datachanged", "IN"+endAt);
        Log.d("datachanged", "IN"+endAtKey);
        Log.d("datachanged", "IN"+override);
        if(search_field!=""){
            String nameEndsAt = search_field.substring(0, search_field.length()-1) +
                    Utils.changeLetter(search_field.substring(search_field.length()-1, search_field.length()));
            query =bgDetRef.child("is_expanded_by").orderByChild("search_name").startAt(search_field).endAt(nameEndsAt).limitToFirst(LIMIT);
        }
        else{
            if(orderingField == "search_name") {
                query = bgDetRef.child("is_expanded_by").orderByChild(orderingField).limitToFirst(LIMIT);
            }
            else{
                if(endAtKey == "") {
                    query = bgDetRef.child("is_expanded_by").orderByChild(orderingField).endAt(endAt).limitToLast(LIMIT);
                }
                else {
                    query = bgDetRef.child("is_expanded_by").orderByChild(orderingField).endAt(endAt, endAtKey).limitToLast(LIMIT);
                }
            }
        }

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                LinkedList<BgModel> tempBoardgames = new LinkedList();
                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                    Map<String, Object> game = (Map<String, Object>) childSnapshot.getValue();
                    BgModel boardgame = new BgModel(game, childSnapshot.getKey());
                    tempBoardgames.addFirst(boardgame);
                }
                if(tempBoardgames.size() > 0) {
                    if(override){
                        boardgames.clear();
                    }
                    lastBg = tempBoardgames.getLast();
                    boardgames.addAll(tempBoardgames);
                    mBoardgamesAdapter.swapArrayList(boardgames);
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
            loadData(query, "average", averageEndsAt, endsAtKey, false);
    }
}
