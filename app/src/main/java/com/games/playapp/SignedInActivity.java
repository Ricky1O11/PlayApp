package com.games.playapp;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TableLayout;

import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;
import com.squareup.picasso.Picasso;

import java.util.Map;

public class SignedInActivity extends AppCompatActivity implements
BoardgamesFragment.BoardgamesFragmentListener, BoardgameDetailFragment.OnBoardgameDetailFragmentListener{

    public static FirebaseDatabase database = FirebaseDatabase.getInstance();
    public static FirebaseAuth mAuth;
    public static DatabaseReference profileRef;
    public static DatabaseReference gamesRef;
    public static DatabaseReference friendsRef;
    public static DataSnapshot favouritesSnap;
    public static FirebaseUser user;
    private ActionBar actionBar;


    private FrameLayout main_image_container;
    private ImageView iv_boardgame_img;
    private AppBarLayout appBar;
    private Toolbar myToolbar;
    private TabLayout mTabLayout;
    private CollapsingToolbarLayout collapsingToolbar;
  //  private FloatingActionButton favouriteFab;
    private ImageView boardgame_img;


    public static Map<String, Object> favourites;
    public TabLayout getTabLayout() {
        return mTabLayout;
    }
    public CollapsingToolbarLayout getCollapsingToolbar() {
        return collapsingToolbar;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signed_in);

        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                FrameLayout fragment_container = (FrameLayout) findViewById(R.id.fragment_container);
                if (fragment_container != null) {
                    fragment_container.removeAllViews();
                }

                switch (tabId) {
                    case R.id.navigation_home:
                        launchMatchesFragment();
                        break;
                    case R.id.navigation_dashboard:
                        launchBoardgamesFragment();
                        break;
                    case R.id.navigation_notifications:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, new ProfileFragment(), "ProfileFragment").commit();
                        break;
                }
            return;
            }
        });

        myToolbar = (Toolbar) findViewById(R.id.toolbar);
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        setSupportActionBar(myToolbar);
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        appBar = (AppBarLayout) findViewById(R.id.appbar);
     //   favouriteFab = (FloatingActionButton) findViewById(R.id.fabFav);
        boardgame_img = (ImageView) findViewById(R.id.backdrop);
        main_image_container = (FrameLayout) findViewById(R.id.main_image_container);
        actionBar = getSupportActionBar();

        mAuth = FirebaseAuth.getInstance();

        user = mAuth.getCurrentUser();
        profileRef = database.getReference().child("profiles/" + user.getUid());
        friendsRef = database.getReference().child("friends/" + user.getUid());
        gamesRef = database.getReference().child("user_played_matches/" + user.getUid());

        profileRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Object> user_profile = (Map<String, Object>) dataSnapshot.getValue();
                DataSnapshot favouritesSnap = dataSnapshot.child("favourites");
                setFavourites(favouritesSnap);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("chrissj2", "Failed to read value.", error.toException());
            }
        });

        launchMatchesFragment();
    }

    public static void createIntent(
            Context context,
            IdpResponse idpResponse) {

        Intent startIntent = new Intent();
        if (idpResponse != null) {
            startIntent.putExtra("aaa", idpResponse);
        }
    }

    public static void setFavourites(DataSnapshot fav){
        favouritesSnap = fav;
        favourites = (Map<String,Object>) fav.getValue();
        if (BoardgamesFragment.mAdapter != null){
            BoardgamesFragment.mAdapter.notifyDataSetChanged();
        }
    }

    public static Map<String,Object> getFavourites(){
        return favourites;
    }

    public void loadImage(String image) {
        Picasso.with(this).load(image).into(boardgame_img);
    }

    @Override
    public void enableCollapse() {
        main_image_container.setVisibility(View.VISIBLE);
        mTabLayout.setVisibility(View.VISIBLE);
        collapsingToolbar.setTitleEnabled(true);
    }

    @Override
    public void disableCollapse() {
        main_image_container.setVisibility(View.GONE);
        mTabLayout.setVisibility(View.GONE);

        collapsingToolbar.setTitleEnabled(false);
    }

    private void launchBoardgamesFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, BoardgamesFragment.newInstance(), "BoardgamesFragment");
        ft.addToBackStack(null);
        ft.commit();
    }

    private void launchMatchesFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, MatchesFragment.newInstance(), "MatchesFragment");
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public void onBackPressed() {

        int count = getFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
            //additional code
        } else {
            getFragmentManager().popBackStack();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        if(fragment instanceof MatchesFragment)
            actionBar.setDisplayHomeAsUpEnabled(false);
        else
            actionBar.setDisplayHomeAsUpEnabled(true);
    }
}
