package com.example.gomoku;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;

public class GameActivity extends AppCompatActivity {

    private Board boardObject = null;
    private Activity containerActivity = null;
    private View view = null;
    private GridViewAdapter gvAdapter = null;

    // 0: Player1, 1: Player2
    static int currentPlayer = 0;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // TODO: Param is 0 for AI or 1 for Human, shouldnt be static
        boardObject = new Board(1);

        GridView gv = findViewById(R.id.gridView);

        gvAdapter = new GridViewAdapter(this, boardObject);
        gv.setAdapter(gvAdapter);
    }


}
