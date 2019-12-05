package com.example.gomoku;

import android.content.Context;
import android.view.View;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

public class GridViewAdapter extends BaseAdapter {
    Piece[][] array;
    Board gameBoard;
    Context context;
    GameActivity containerActivity;

    // <value in dp> * dpConvertFactor = <equivalent pixel value>
    float dpConvertFactor;

    public GridViewAdapter(Context context, Board board) {
        gameBoard = board;
        this.array = board.getBoardState();
        this.context = context;
        containerActivity = (GameActivity) context;
        dpConvertFactor = context.getResources().getDisplayMetrics().density;
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
    // First row of the 2d array contains i = 0 through 14
    // Second row contains i = 15 through 29, etc...
    private int[] convert1dTo2d (int i) {
        assert i < 225;
        assert i >= 0;
        int y = i / 15;
        int x = i - (15*y);
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
            b = new Button(context,null,R.style.BoardButton);

            // TODO: BUTTON CREATION SPECS
            b.setId(position);
            b.setGravity(Gravity.CENTER);

            ///////// THESE ATTRIBUTES FOR TESTING ONLY ///////////////
            // TODO: REMOVE THESE
            b.setText(".");
            b.setBackgroundColor(0xFF666666);
            ///////////////////////////////////////////////////////////

            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Button b = (Button) v;

                    int pos = b.getId();
                    int[] posXY = convert1dTo2d(pos);
                    int x = posXY[0];
                    int y = posXY[1];
                    int currentPlayer = containerActivity.currentPlayer;

                    // Check if clicked space is blank, place a piece if so
                    if (canMoveAt(x,y,currentPlayer)) {
                        // TODO: TESTING CODE, REMOVE /////////////
                        b.setText(Integer.toString(currentPlayer));
                        System.out.println("x: "+x+", y:"+y+"\n\n");
                        ///////////////////////////////////////////
                        // Check game win condition.  If win end game, if not advance turn
                        if (gameBoard.gameWon(currentPlayer,x,y)) {
                            // Game is over woohoo
                            Toast toast = Toast.makeText(context, "GAME OVER, " + currentPlayer + " WINS!", Toast.LENGTH_LONG);
                            toast.show();
                        }
                        else {
                            // Game is not over, it's the opponent's turn now
                            containerActivity.currentPlayer = (1-currentPlayer);
                        }

                    }
                    else {
                        // If player clicks an occupied space, nothing should happen
                        return;
                    }
                }
            });


        }
        else {
            // Return current button
            b = (Button) convertView;
        }
        return b;
    }


    /**
     * Check whether a space on the board is blank and place the player's token if so
     * @param x x-coord on game grid
     * @param y y-coord on game grid
     * @return true if the space is blank, false if not
     */
    public boolean canMoveAt(int x, int y, int player) {
        if (array[x][y].color() == -1) {
            // Board location is empty
            array[x][y].setColor(player);
            return true;
        }
        return false;
    }

}



