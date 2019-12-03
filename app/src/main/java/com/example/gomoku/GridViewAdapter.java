package com.example.gomoku;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;

public class GridViewAdapter extends BaseAdapter {
    Piece[][] array;
    Context context;

    public GridViewAdapter(Piece[][] pieceArray, Context context) {
        this.array = pieceArray;
        this.context = context;
    }

    @Override
    public int getCount() {
        return 15 * 15;
    }

    @Override
    public Object getItem(int position) {
        if (position >= 225 || position < 0) {
            return null;
        }
        int[] twoD = convert1dTo2d(position);
        int x = twoD[0];
        int y = twoD[1];
        return array[x][y];
    }

    // Convert an index in a 1-dimensional array to desired position in the 15x15 2-d array
    private int[] convert1dTo2d (int i) {
        assert i < 225;
        assert i >= 0;
        int y = i / 10;
        int x = i - (10*y);
        return new int[]{x,y};
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Button b;

        if (convertView == null) {
            // Create new button
            b = new Button(context);
            b.setLayoutParams(new GridView.LayoutParams(25,25));

            // TODO: REMOVE
            b.setText("o");
            b.setBackgroundColor(0xFF666666);

        }
        else {
            // Return current button
            b = (Button) convertView;
        }
        return b;
    }
}



