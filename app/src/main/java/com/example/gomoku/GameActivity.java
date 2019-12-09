package com.example.gomoku;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Intent;
import android.widget.EditText;
import android.widget.TextView;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;

public class GameActivity extends AppCompatActivity {

    private Board boardObject = null;
    private GridViewAdapter gvAdapter = null;

    // 0: Player1, 1: Player2
    static int currentPlayer = 0;
    public static String name1;
    public static String name2;

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

        //If names changed from MainMenu settings, keep same name
        name1 = MainMenu.name1;
        name2 = MainMenu.name2;
    }

    public void resetGame(View view) {
        this.recreate();
    }

    public void openHelp(View view) {
        HelpFragment hf = new HelpFragment();
        hf.setContainerActivity(this);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.outer, hf);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void openSettings(View view) {
        SettingsFragment sf = new SettingsFragment();
        sf.setContainerActivity(this);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.outer, sf);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void changeNames(View view) {
        TextView tv1 = findViewById(R.id.player1TV);
        TextView tv2 = findViewById(R.id.player2TV);
        EditText e1 = findViewById(R.id.player1edit);
        EditText e2 = findViewById(R.id.player2edit);

        String name1 = e1.getText().toString();
        String name2 = e2.getText().toString();

        tv1.setText(name1);
        tv2.setText(name2);
        e1.setText(name1);
        e2.setText(name2);
    }

    public String getName1(){
        return name1;
    }
    public String getName2(){
        return name2;
    }
}
