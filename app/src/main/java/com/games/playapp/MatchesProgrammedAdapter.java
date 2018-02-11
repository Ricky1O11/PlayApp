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

public class MatchesProgrammedAdapter extends RecyclerView.Adapter<MatchesProgrammedAdapter.BoardgamesFavouriteAdapterViewHolder> {

    private final BoardgamesFavouriteAdapterOnClickHandler mClickHandler;
    private Context mContext;

    public interface BoardgamesFavouriteAdapterOnClickHandler {
        void onClick(String id);
    }

    private LinkedList mBoardgames;

    public MatchesProgrammedAdapter(@NonNull Context context, BoardgamesFavouriteAdapterOnClickHandler clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;
    }

    public class BoardgamesFavouriteAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final ImageView mThumbnailField;

        public BoardgamesFavouriteAdapterViewHolder(View view) {
            super(view);
            mThumbnailField = (ImageView) view.findViewById(R.id.iv_bg_thumbnail);
            view.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            BgModel boardgame = (BgModel) mBoardgames.get(adapterPosition);
            String id = boardgame.getBggId();
            mClickHandler.onClick(id);
        };
    }

    @Override
    public BoardgamesFavouriteAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        int layoutIdForListItem = R.layout.boardgames_list_item_circle;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new BoardgamesFavouriteAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BoardgamesFavouriteAdapterViewHolder boardgamesAdapterViewHolder, int position) {
        BgModel boardgame = (BgModel) mBoardgames.get(position);
        String image = boardgame.getImage();
        Log.d("thumbnail", ""+image);
        Picasso.with(mContext).load(image).into(boardgamesAdapterViewHolder.mThumbnailField);
    }

    @Override
    public int getItemCount() {
        if (null == mBoardgames) return 0;
        return mBoardgames.size();
    }

    void swapArrayList(LinkedList boardgames) {
        mBoardgames = boardgames;
        if (boardgames != null) {
            // Force the RecyclerView to refresh
            this.notifyDataSetChanged();
        }
    }

}