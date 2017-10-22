package com.games.playapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;

public class BoardgamesAdapter extends RecyclerView.Adapter<BoardgamesAdapter.BoardgamesAdapterViewHolder> {

    private final BoardgamesAdapterOnClickHandler mClickHandler;
    private Context mContext;

    public interface BoardgamesAdapterOnClickHandler {
        void onClick(String id);
    }

    private LinkedList mBoardgames;

    public BoardgamesAdapter(@NonNull Context context, BoardgamesAdapterOnClickHandler clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;
    }

    public class BoardgamesAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final ImageView mThumbnailField;
        public final TextView mTitleTextView;
        public final TextView mAverageTextView;
        public final ImageView mFavourite;

        public BoardgamesAdapterViewHolder(View view) {
            super(view);
            mThumbnailField = (ImageView) view.findViewById(R.id.iv_bg_thumbnail);
            mTitleTextView = (TextView) view.findViewById(R.id.tv_bg_title);
            mAverageTextView =(TextView) view.findViewById(R.id.tv_bg_average);
            mFavourite= (ImageView) itemView.findViewById(R.id.iv_bg_favourite);
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
    public BoardgamesAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        int layoutIdForListItem = R.layout.boardgames_list_item;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new BoardgamesAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BoardgamesAdapterViewHolder boardgamesAdapterViewHolder, int position) {
        BgModel boardgame = (BgModel) mBoardgames.get(position);
        String thumbnail = boardgame.getThumbnail();
        Picasso.with(mContext).load(thumbnail).into(boardgamesAdapterViewHolder.mThumbnailField);

        final String name = boardgame.getName();
        final Double average = boardgame.getAverage();
        final String bggId = boardgame.getBggId();
        boardgamesAdapterViewHolder.mTitleTextView.setText(name);
        boardgamesAdapterViewHolder.mAverageTextView.setText((Math.round(average*100.0)/100.0)+"/10");

        if(boardgame.isFavourite()){
            boardgamesAdapterViewHolder.mFavourite.setImageResource(R.drawable.ic_favorite);
            boardgamesAdapterViewHolder.mFavourite.setTag(R.string.id_isfav, true);
        }
        else{
            boardgamesAdapterViewHolder.mFavourite.setImageResource(R.drawable.ic_favorite_border);
            boardgamesAdapterViewHolder.mFavourite.setTag(R.string.id_isfav, false);
        }
        boardgamesAdapterViewHolder.mFavourite.setTag(R.string.id_bg, boardgame);
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