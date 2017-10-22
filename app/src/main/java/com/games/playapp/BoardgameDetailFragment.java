package com.games.playapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.hannesdorfmann.swipeback.Position;
import com.hannesdorfmann.swipeback.SwipeBack;
import com.hannesdorfmann.swipeback.transformer.SlideSwipeBackTransformer;
import java.util.HashMap;
import java.util.Map;

import static com.games.playapp.SignedInActivity.database;


public class BoardgameDetailFragment extends Fragment {
    private OnBoardgameDetailFragmentListener mListener;
    public static DatabaseReference bgDetRef;
    public static Map<String, Object> boardgameInfo = new HashMap<>();
    TabLayout mTabLayout;
    String value;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_boardgame_detail, container, false);

        final ViewPager viewPager=(ViewPager) view.findViewById(R.id.viewPager);

        ((SignedInActivity) getActivity()).getTabLayout().setupWithViewPager(viewPager);
        final BgTabPageAdapter viewPagerAdapter = new BgTabPageAdapter(getFragmentManager(), getContext());
        viewPager.setAdapter(viewPagerAdapter);


        Bundle bundle = this.getArguments();
        if (bundle != null) {
            value = bundle.getString("BG_KEY");
        }

        // Init the swipe back
        //SwipeBack.attach(getActivity(), Position.LEFT)
        //        .setContentView(R.layout.fragment_boardgame_detail)
        //        .setSwipeBackView(R.layout.fragment_boardgames);


        bgDetRef = database.getReference().child("boardgames/" + value);

        bgDetRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                setBoardgameInfo((Map<String, Object>) dataSnapshot.getValue());
                viewPagerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("chrissj2", "Failed to read value.", error.toException());
            }
        });

        return view;
    }

    //public static SwipeBack attach(Activity activity, SwipeBack.Type type, Position position, int dragMode, SwipeBackTransformer transformer);

    private void setBoardgameInfo(Map<String, Object> data){
        boardgameInfo = data;
        String image = (String) boardgameInfo.get("image");
        String name = (String) boardgameInfo.get("name");
        ((SignedInActivity) getActivity()).getCollapsingToolbar().setTitle(name);
        ((SignedInActivity) getActivity()).loadImage(image);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mListener.enableCollapse();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnBoardgameDetailFragmentListener) {
            mListener = (OnBoardgameDetailFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnCheeseDetailFragmentListener");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public interface OnBoardgameDetailFragmentListener {
        void enableCollapse();
    }
}
