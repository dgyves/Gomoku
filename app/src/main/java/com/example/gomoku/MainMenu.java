 package com.example.gomoku;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

 public class MainMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

     public void run2pGame (View view) {
         Intent intent = new Intent(this, GameActivity.class);
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
 }
