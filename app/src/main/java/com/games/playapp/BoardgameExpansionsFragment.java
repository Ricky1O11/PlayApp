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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.games.playapp.BoardgameDetailFragment.bgDetRef;
import static com.games.playapp.SignedInActivity.database;
import static com.games.playapp.SignedInActivity.favouritesSnap;
import static com.games.playapp.SignedInActivity.getFavourites;

public class BoardgameExpansionsFragment extends Fragment {
    public static DatabaseReference bgRef;
    public static FirebaseRecyclerAdapter<BgModel, BgHolder> mAdapter;
    private LinearLayoutManager mManager;
    private SwipeRefreshLayout mRefreshLayout;
    private ImageView mThumbnailField;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_boardgames, container, false);

        final RecyclerView mRecycler = (RecyclerView) view.findViewById(R.id.recyclerview_boardgames);
        mRecycler.setHasFixedSize(true);

        mManager = new LinearLayoutManager(getActivity());
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecycler.setLayoutManager(mManager);
        final Map<String, Object> favourites;


        Query query = bgDetRef.child("is_expanded_by").limitToFirst(20);
        mAdapter = new FirebaseRecyclerAdapter<BgModel, BgHolder>(
                BgModel.class,
                R.layout.boardgames_list_item,
                BgHolder.class,
                query
        ) {
            @Override
            public void populateViewHolder(final BgHolder holder, final BgModel bgm, final int position) {
                Log.d("aaa", "||"+bgm);
                Map<String, Object> favourites = getFavourites();
                if(favourites != null) {
                    Map<String, Object> favourited_game = (Map<String, Object>) favourites.get("" + bgm.getBggId());
                    holder.bind(bgm, favourited_game);
                }
            }

            @Override
            public BgHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                BgHolder viewHolder = super.onCreateViewHolder(parent, viewType);
                viewHolder.setOnClickListener(new BgHolder.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position, String name) {
                        String key = mAdapter.getRef(position).getKey(); //chiave = id bg in firebase

                        BoardgameDetailFragment bgdet= new BoardgameDetailFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("BG_KEY", key);
                        bgdet.setArguments(bundle);

                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, bgdet,null)
                                .addToBackStack(null)
                                .commit();
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {
                        Toast.makeText(getActivity(), "Item long clicked at " + position, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFavclick(View view, BgModel bgm, boolean isFav) {
                        if(isFav){
                            favouritesSnap.getRef().child(""+bgm.getBggId()).removeValue();
                        }
                        else{
                            Map<String, Object> favBg = new HashMap<String, Object>();
                            favBg.put("image", bgm.getImage());
                            favBg.put("name", bgm.getName());
                            favBg.put("key", bgm.getBggId());
                            favBg.put("bggId", bgm.getBggId());
                            favBg.put("inserted_at", new Date().getTime());
                            favouritesSnap.getRef().child(""+bgm.getBggId()).setValue(favBg);
                        }
                    }
                });
                return viewHolder;
            }
        };

        mRecycler.setAdapter(mAdapter);

        mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int friendlyMessageCount = mAdapter.getItemCount();
                int lastVisiblePosition =
                        mManager.findLastCompletelyVisibleItemPosition();
                // If the recycler view is initially being loaded or the
                // user is at the bottom of the list, scroll to the bottom
                // of the list to show the newly added message.
                if (lastVisiblePosition == -1 ||
                        (positionStart >= (friendlyMessageCount - 1) &&
                                lastVisiblePosition == (positionStart - 1))) {
                    mRecycler.scrollToPosition(positionStart);
                }
            }
        });

        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
        mRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh(){
                        Log.i("chris", "onRefresh called from SwipeRefreshLayout");

                        // This method performs the actual data-refresh operation.
                        // The method calls setRefreshing(false) when it's finished.
                        mAdapter.notifyDataSetChanged();
                        mRefreshLayout.setRefreshing(false);
                    }
                }
        );

        return view;
    }
}
