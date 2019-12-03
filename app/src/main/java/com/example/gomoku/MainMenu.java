 package com.example.gomoku;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

 public class MainMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.out.println("commit");
    }

     public void run2pGame (View view) {
         Intent intent = new Intent(this, GameActivity.class);
         startActivity(intent);
     }

}
