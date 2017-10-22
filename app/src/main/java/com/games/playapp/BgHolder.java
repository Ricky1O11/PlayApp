package com.games.playapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Map;

public class BgHolder extends RecyclerView.ViewHolder {

    private final ImageView mThumbnailField;
    private final ImageView mFavourite;
    private final TextView mNameField;
    private final TextView mAverageField;
    private Context mContext;

    private final int RES_ID = 0;

    View mView;

    public BgHolder(View itemView) {
        super(itemView);
        mView = itemView;

        mThumbnailField = (ImageView) itemView.findViewById(R.id.iv_bg_thumbnail);
        mNameField = (TextView) itemView.findViewById(R.id.tv_bg_title);
        mAverageField = (TextView) itemView.findViewById(R.id.tv_bg_average);
        mFavourite= (ImageView) itemView.findViewById(R.id.iv_bg_favourite);
        mContext = itemView.getContext();

        //listener set on ENTIRE ROW, you may set on individual components within a row.
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Click", "ok");
                mClickListener.onItemClick(v, getAdapterPosition(),(String) mNameField.getText());

            }
        });

        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.i("LongClick", "ok");
                mClickListener.onItemLongClick(v, getAdapterPosition());
                return true;
            }
        });

        mFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BgModel resource = (BgModel) mFavourite.getTag(R.string.id_bg);
                boolean isFav = (boolean) mFavourite.getTag(R.string.id_isfav);
                mClickListener.onFavclick(v, resource, isFav);

            }
        });

    }

    private BgHolder.ClickListener mClickListener;

    //Interface to send callbacks...
    public interface ClickListener{
        void onItemClick(View view, int position, String name);
        void onItemLongClick(View view, int position);
        void onFavclick(View view, BgModel bgm, boolean isFav);
    }

    public void setOnClickListener(BgHolder.ClickListener clickListener){
        mClickListener = clickListener;
    }

    public void bind(BgModel bgm, Map<String, Object> s) {

        setName(bgm.getName());
        setAverage(bgm.getAverage());
        setThumbnail(bgm.getThumbnail());
        setFavourite(bgm, s);
    }

    public void setName(String name) {
        mNameField.setText(name);

    }
    public void setThumbnail(String thumbnail) {
        Picasso.with(mContext).load(thumbnail).into(mThumbnailField);
    }

    public void setAverage(Double average) {
        if(average != null)
            mAverageField.setText(""+average);
        else
            mAverageField.setVisibility(View.GONE);
    }


    public void setFavourite(BgModel bgm, Map<String, Object> s){
        if(s != null){
            mFavourite.setImageResource(R.drawable.ic_favorite);
            mFavourite.setTag(R.string.id_isfav, true);
        }
        else{
            mFavourite.setImageResource(R.drawable.ic_favorite_border);
            mFavourite.setTag(R.string.id_isfav, false);
        }
        mFavourite.setTag(R.string.id_bg, bgm);
    }
}
