 package com.example.gomoku;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

 public class MainMenu extends AppCompatActivity {

     public static String name1;
     public static String name2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        name1 = "Player 1";
        name2 = "Player 2";
    }

    public void run1pGame (View view) {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("Players",0);
        startActivity(intent);
    }

    public void run2pGame (View view) {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("Players",1);
        startActivity(intent);
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
         EditText e1 = findViewById(R.id.player1edit);
         EditText e2 = findViewById(R.id.player2edit);

         name1 = e1.getText().toString();
         name2 = e2.getText().toString();
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
