package com.example.gomoku;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

public class GameboardFragment extends Fragment {

    private Board boardObject = null;
    private Activity containerActivity = null;
    private View view = null;


    public GameboardFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_gameboard, container, false);


        return view;
    }

    public void setContainerActivity(Activity containerActivity) {
        this.containerActivity = containerActivity;

    }
}
