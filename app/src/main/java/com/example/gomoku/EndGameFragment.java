package com.example.gomoku;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class EndGameFragment extends Fragment {

    public Activity containerActivity = null;
    Bitmap game_result;

    public void setContainerActivity(Activity containerActivity) {
        this.containerActivity = containerActivity;
    }

    public EndGameFragment() {
        // Required empty public constructor
    }

    public EndGameFragment(Bitmap bitmap) {
        game_result = bitmap;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_end_game, container, false);
        ImageView iv = view.findViewById(R.id.screenshot);
        iv.setImageBitmap(game_result);
        return view;
    }

    public void openContacts(View view){
    }

    public void playAgain(View view){
    }
}
