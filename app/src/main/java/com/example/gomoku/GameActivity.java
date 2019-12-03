package com.example.gomoku;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

public class GameActivity extends AppCompatActivity {





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // Add gameboard fragment to UI
        FragmentTransaction trans = getSupportFragmentManager().beginTransaction();

        GameboardFragment boardFrag = new GameboardFragment();
        boardFrag.setContainerActivity(this);
        trans.replace(R.id.grid, boardFrag);
        trans.commit();
    }
}
