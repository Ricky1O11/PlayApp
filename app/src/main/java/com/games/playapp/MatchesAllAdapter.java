package com.games.playapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.LinkedList;

public class MatchesAllAdapter extends RecyclerView.Adapter<MatchesAllAdapter.BoardgamesPlayedAdapterViewHolder> {

    private final BoardgamesPlayedAdapterOnClickHandler mClickHandler;
    private Context mContext;

    public interface BoardgamesPlayedAdapterOnClickHandler {
        void onClick(String id);
    }

    private LinkedList mBoardgamesPlayed;

    public MatchesAllAdapter(@NonNull Context context, BoardgamesPlayedAdapterOnClickHandler clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;
    }

    public class BoardgamesPlayedAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final ImageView mThumbnailField;

        public BoardgamesPlayedAdapterViewHolder(View view) {
            super(view);
            mThumbnailField = (ImageView) view.findViewById(R.id.iv_bg_thumbnail);
            view.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            BgModel boardgame = (BgModel) mBoardgamesPlayed.get(adapterPosition);
            String id = boardgame.getBggId();
            mClickHandler.onClick(id);
        };
    }

    @Override
    public BoardgamesPlayedAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        int layoutIdForListItem = R.layout.matches_list_item;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new BoardgamesPlayedAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BoardgamesPlayedAdapterViewHolder boardgamesAdapterViewHolder, int position) {
        BgModel boardgame = (BgModel) mBoardgamesPlayed.get(position);
        String image = boardgame.getImage();
        Log.d("thumbnail", ""+image);
        Picasso.with(mContext).load(image).into(boardgamesAdapterViewHolder.mThumbnailField);
    }

    @Override
    public int getItemCount() {
        if (null == mBoardgamesPlayed) return 0;
        return mBoardgamesPlayed.size();
    }

    void swapArrayList(LinkedList boardgames) {
        mBoardgamesPlayed = boardgames;
        if (boardgames != null) {
            // Force the RecyclerView to refresh
            this.notifyDataSetChanged();
        }
    }

}