package com.example.gomoku;

/*
This class contains much (probably too much) of the logic for the game
 */

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.transition.ChangeBounds;
import android.transition.TransitionManager;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

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
        final ImageView space;

        if (convertView == null) {
            // Create new ImageView representing a single space on the board
            space = new ImageView(context,null,R.style.BoardSpace);
            space.setId(position);
            space.setAdjustViewBounds(true);
            space.setTag(-1);
            space.setImageDrawable(context.getDrawable(R.drawable.emptysquare));

            // DECLARE ONCLICK FUNCTION
            space.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageView space = (ImageView) v;

                    int pos = space.getId();
                    int[] posXY = convert1dTo2d(pos);
                    int x = posXY[0];
                    int y = posXY[1];
                    int currentPlayer = containerActivity.currentPlayer;

                    // Check if clicked space is blank, place a piece if so; ELSE, do nothing.
                    if (canMoveAt(x,y,currentPlayer)) {
                        space.setTag(currentPlayer);
                        if (currentPlayer == 0) {
                            // set black token
                            space.setImageDrawable(context.getDrawable(R.drawable.black));

                            /////////////// TODO: TESTING ANIMATION CODE
                            ImageView iv = (ImageView) v;

                            ScaleAnimation anim = new ScaleAnimation(200f, 25f, 200f, 25f);
                            anim.setDuration(300);
                            anim.setFillAfter(true);


                            ////////////// // TODO: END ANIMATION TESTING
                        } else {
                            // set white token
                            space.setImageDrawable(context.getDrawable(R.drawable.white));
                        }

                        ///////////////////////////////////////////
                        // Check game win condition.  If win end game, if not advance turn
                        if (gameBoard.gameWon(currentPlayer,x,y)) {
                            // GAME OVER MAN
                            // TODO: ADDITIONAL CODE FOR GAME OVER CONDITION, NEED TO STOP ADDITIONAL PIECES FROM BEING PLAYED / GO INTO SCREENSHOT SHARING STUFF

                            // Toast winner (Optional, may want to remove this depending how the EndGameFragment pans out)
                            String color;
                            if (currentPlayer == 0) {color = "BLACK";}
                            else {color = "WHITE";}
                            Toast toast = Toast.makeText(context, "GAME OVER, " + color + " WINS!", Toast.LENGTH_LONG);
                            toast.show();
                            /////////////////


                        }
                        else {
                            // Game is not over, it's the opponent's turn now

                            // Highlight next player's (opponent) nametag green and current player's
                            // white
                            TextView tv;
                            if (currentPlayer==0) {
                                tv = containerActivity.findViewById(R.id.player1TV);
                                tv.setBackgroundColor(containerActivity.getResources().getColor(R.color.transparentWhite));
                                tv = containerActivity.findViewById(R.id.player2TV);
                                tv.setBackgroundColor(containerActivity.getResources().getColor(R.color.transparentGreen));
                            } else {
                                tv = containerActivity.findViewById(R.id.player2TV);
                                tv.setBackgroundColor(containerActivity.getResources().getColor(R.color.transparentWhite));
                                tv = containerActivity.findViewById(R.id.player1TV);
                                tv.setBackgroundColor(containerActivity.getResources().getColor(R.color.transparentGreen));
                            }

                            // Advance current player tracker
                            containerActivity.currentPlayer = (1-currentPlayer);
                        }

                    }
                    else {
                        return;
                    }
                }
            });


        }
        else {
            // Return current ImageView
            space = (ImageView) convertView;
        }
        return space;
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



