package com.games.playapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity implements
        BoardgamesFragment.BoardgamesFragmentListener, BoardgameDetailFragment.OnBoardgameDetailFragmentListener, ProfileFragment.OnProfileFragmentListener{

    private TextView mTextMessage;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signed_in);
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d("Ricky", "onAuthStateChanged:signed_in:" + user.getUid());
                    Intent intent = new Intent(getBaseContext(), SignedInActivity.class);
                    startActivity(intent);
                    // User is signed in
                } else {
                    Log.d("Ricky", "onAuthStateChanged:signed_out");
                    Intent intent = new Intent(getBaseContext(), LoginAndRegister.class);
                    startActivity(intent);
                    // User is signed out
                }
                // ...
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
    @Override
    public void enableCollapse() {
    }

    @Override
    public void disableCollapse() {
    }
}
