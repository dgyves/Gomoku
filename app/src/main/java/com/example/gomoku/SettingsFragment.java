package com.example.gomoku;

import android.app.Activity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

public class SettingsFragment extends Fragment {

    public Activity containerActivity = null;

    public void setContainerActivity(Activity containerActivity) {
        this.containerActivity = containerActivity;
    }

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        EditText e1 = view.findViewById(R.id.player1edit);
        EditText e2 = view.findViewById(R.id.player2edit);

        if (containerActivity.getClass() == MainMenu.class){
            System.out.println("parent is MainMenu");
            e1.setText(MainMenu.name1);
            e2.setText(MainMenu.name2);

        } else {
            System.out.println("parent is GameActivity");
            TextView tv1 = containerActivity.findViewById(R.id.player1TV);
            TextView tv2 = containerActivity.findViewById(R.id.player2TV);
            e1.setText(tv1.getText().toString());
            e2.setText(tv2.getText().toString());
        }
        return view;
    }


}
