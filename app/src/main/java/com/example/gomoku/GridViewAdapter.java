package com.example.gomoku;

/*
This class contains much (probably too much) of the logic for the game
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;;
import android.view.animation.ScaleAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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
                        } else if (currentPlayer == 1) {
                            // set white token
                            space.setImageDrawable(context.getDrawable(R.drawable.white));
                        }
                            // ANIMATE PIECE PLACEMENT
                            ImageView iv = (ImageView) v;
                            ScaleAnimation anim = new ScaleAnimation(0, 1f, 0, 1f, Animation.ABSOLUTE,iv.getPivotX(),Animation.ABSOLUTE,iv.getPivotY());
                            anim.setDuration(250);
                            iv.startAnimation(anim);


                        ///////////////////////////////////////////
                        // Check game win condition.  If win end game, if not advance turn
                        if (gameBoard.gameWon(currentPlayer,x,y)) {
                            // GAME OVER MAN
                            // TODO: ANIMATE WINNING PIECES(?) AND LAUNCH ENDGAME FRAGMENT
                            // TODO: ADD ANIMATION AND/OR DISTINCTION OF WINNING PIECES
                            // TODO: ADD MESSAGE TO ENDGAMEFRAGMENT STATING WINNING COLOR?

                            // Prevent further pieces from being played on board after game ends
                            int winner = currentPlayer;
                            currentPlayer = -1;
                            containerActivity.currentPlayer = -1;

                            // Launch EndGameFragment (after short delay to allow final piece animation to complete)
                            Handler handler = new Handler();
                            Runnable r = new Runnable() {
                                public void run() {
                                    // To be executed after specified delay (1000ms)
                                    containerActivity.currentPlayer = -1;
                                    String file_path = containerActivity.createImageFile();
                                    EndGameFragment endFrag = new EndGameFragment(containerActivity.game_result);
                                    endFrag.setContainerActivity(containerActivity);
                                    FragmentManager fragMgr = containerActivity.getSupportFragmentManager();
                                    FragmentTransaction transaction = fragMgr.beginTransaction();
                                    transaction.replace(R.id.gameActivityLayout, endFrag);
                                    transaction.addToBackStack(null);
                                    transaction.commit();
                                }
                            };
                            handler.postDelayed(r, 2000);

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
                        // Space clicked was not blank
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
        if (array[x][y].color() == -1 && player != -1) {
            // Board location is empty
            array[x][y].setColor(player);
            return true;
        }
        return false;
    }


}



