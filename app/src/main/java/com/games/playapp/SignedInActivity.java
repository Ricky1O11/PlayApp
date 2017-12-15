package com.games.playapp;

import android.app.SearchManager;
import android.graphics.Typeface;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.elmargomez.typer.Font;
import com.elmargomez.typer.Typer;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
BoardgamesFragment.BoardgamesFragmentListener, BoardgameDetailFragment.OnBoardgameDetailFragmentListener, ProfileFragment.OnProfileFragmentListener{

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
    private FloatingActionButton favouriteFab;
    private ImageView boardgame_img;

    private TextView mMatchPlayed, mMatchWon, mDuration;

    private String query = "";

    public static Map<String, Object> favourites;
    public TabLayout getTabLayout() {
        return mTabLayout;
    }
    public CollapsingToolbarLayout getCollapsingToolbar() {
        return collapsingToolbar;
    }
    public FloatingActionButton getFavouriteFab() {
        return favouriteFab;
    }
    public String getQuery() {return query;}


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
                    case R.id.navigation_dashboard:
                        launchMatchesFragment();
                        break;
                    case R.id.navigation_boardgames:
                        query = "";
                        launchBoardgamesFragment();
                        break;
                    case R.id.navigation_profile:
                        launchProfileFragment();
                        break;
                }
            return;
            }
        });

        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            query = intent.getStringExtra(SearchManager.QUERY);
            launchBoardgamesFragment();
        }

        myToolbar = (Toolbar) findViewById(R.id.toolbar);
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        setSupportActionBar(myToolbar);
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        Typeface font = Typer.set(this).getFont(Font.ROBOTO_MEDIUM);
        collapsingToolbar.setExpandedTitleTypeface(font);
        appBar = (AppBarLayout) findViewById(R.id.appbar);

        favouriteFab = (FloatingActionButton) findViewById(R.id.fabFav);
        boardgame_img = (ImageView) findViewById(R.id.backdrop);
        main_image_container = (FrameLayout) findViewById(R.id.main_image_container);
        actionBar = getSupportActionBar();

        mMatchPlayed = (TextView) findViewById(R.id.tv_match_played);
        mMatchWon = (TextView) findViewById(R.id.tv_match_won);
        mDuration = (TextView) findViewById(R.id.tv_duration);

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
            }
        });
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

    public void loadBasicStats(int match_played, int match_won, String duration) {
        mMatchPlayed.setText(""+match_played);
        mMatchWon.setText(""+match_won);
        mDuration.setText("250");
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

    private void launchProfileFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, ProfileFragment.newInstance(), "ProfileFragment");
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
            case R.id.search:
                onSearchRequested();
                return true;
            case R.id.logout:
                AuthUI.getInstance()
                        .signOut(this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            public void onComplete(@NonNull Task<Void> task) {
                                // user is now signed out
                                startActivity(new Intent(SignedInActivity.this, MainActivity.class));
                            }
                        });
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.app_bar_menu, menu);
        return true;
    }


}
