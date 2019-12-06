package com.example.gomoku;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;

public class GameActivity extends AppCompatActivity {

    private Board boardObject = null;
    private GridViewAdapter gvAdapter = null;

    // 0: Player1, 1: Player2
    static int currentPlayer = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        this.currentPlayer = 0;

        // Bundle extra "Players" has value 0 for Human vs AI game, or 1 for Human vs Human
        Bundle b = getIntent().getExtras();
        int numPlayers = b.getInt("Players");

        boardObject = new Board(numPlayers);

        // Define gridview & parameters
        GridView gv = findViewById(R.id.gridView);
        gv.setVerticalScrollBarEnabled(false);
        gv.setHorizontalScrollBarEnabled(false);
        gv.setVerticalSpacing(0);
        gv.setHorizontalSpacing(0);
        gv.setStretchMode(GridView.NO_STRETCH);

        gvAdapter = new GridViewAdapter(this, boardObject);
        gv.setAdapter(gvAdapter);
    }

    public void resetGame(View view) {
        this.recreate();
    }
}
