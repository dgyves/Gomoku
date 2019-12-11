package com.example.gomoku;

/*
This class contains much (probably too much) of the logic for the game
 */

import android.content.Context;
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
    int multiplayer;
    final int PIECE_DRAWTIME = 250;

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

    public void setPlayers(int multi) {
        // 0 if AI game, 1 if human v. human
        this.multiplayer = multi;
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
    public View getView(int position, final View convertView, ViewGroup parent) {
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
                            anim.setDuration(PIECE_DRAWTIME);
                            iv.startAnimation(anim);


                        ///////////////////////////////////////////
                        // Check if human player won.  If win end game, if not advance to next player
                        if (gameBoard.gameWon(currentPlayer,x,y)) {
                            // GAME OVER MAN

                            // Prevent further pieces from being played on board after game ends
                            int winner = currentPlayer;
                            currentPlayer = -1;
                            containerActivity.currentPlayer = -1;


                            /*
                            String color;
                            if (currentPlayer == 0) {color = "BLACK";}
                            else {color = "WHITE";}
                            Toast toast = Toast.makeText(context, "GAME OVER, " + color + " WINS!", Toast.LENGTH_LONG);
                            toast.show();

                            I might use this to get winner and loser for email intent
                             */


                            // Launch EndGameFragment (after short delay to allow final piece animation to complete)
                            Handler handler = new Handler();
                            Runnable r = new Runnable() {
                                public void run() {
                                    // To be executed after specified delay
                                    containerActivity.currentPlayer = -1;
                                    containerActivity.createImageFile();
                                    EndGameFragment endFrag = new EndGameFragment(containerActivity.game_result);
                                    endFrag.setContainerActivity(containerActivity);
                                    FragmentManager fragMgr = containerActivity.getSupportFragmentManager();
                                    FragmentTransaction transaction = fragMgr.beginTransaction();
                                    transaction.replace(R.id.gameActivityLayout, endFrag, "frag");
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
                            if (currentPlayer == 0 && multiplayer == 1) {
                                tv = containerActivity.findViewById(R.id.player1TV);
                                tv.setBackgroundColor(containerActivity.getResources().getColor(R.color.transparentWhite));
                                tv = containerActivity.findViewById(R.id.player2TV);
                                tv.setBackgroundColor(containerActivity.getResources().getColor(R.color.transparentGreen));
                            } else if (multiplayer == 1) {
                                tv = containerActivity.findViewById(R.id.player2TV);
                                tv.setBackgroundColor(containerActivity.getResources().getColor(R.color.transparentWhite));
                                tv = containerActivity.findViewById(R.id.player1TV);
                                tv.setBackgroundColor(containerActivity.getResources().getColor(R.color.transparentGreen));
                            }

                            // Advance current player tracker
                            containerActivity.currentPlayer = (1 - currentPlayer);

                            // If it's an AI game, now take the computer's turn
                            if (containerActivity.currentPlayer == 1 && multiplayer == 0) {
                                int[] aiMoveCoords = aiTakesATurn();

                                // Check if game is over
                                if (gameBoard.gameWon(containerActivity.currentPlayer, aiMoveCoords[0], aiMoveCoords[1])) {
                                    // GAME OVER, COMPUTER WON
                                    // Prevent further pieces from being played on board after game ends
                                    int winner = currentPlayer;
                                    currentPlayer = -1;
                                    containerActivity.currentPlayer = -1;

                                    // Launch EndGameFragment (after short delay to allow final piece animation to complete)
                                    Handler handler = new Handler();
                                    Runnable r = new Runnable() {
                                        public void run() {
                                            // To be executed after specified delay
                                            containerActivity.currentPlayer = -1;
                                            String file_path = containerActivity.createImageFile();
                                            EndGameFragment endFrag = new EndGameFragment(containerActivity.game_result);
                                            endFrag.setContainerActivity(containerActivity);
                                            FragmentManager fragMgr = containerActivity.getSupportFragmentManager();
                                            FragmentTransaction transaction = fragMgr.beginTransaction();
                                            transaction.replace(R.id.gameActivityLayout, endFrag, "frag");
                                            transaction.addToBackStack(null);
                                            transaction.commit();
                                        }
                                    };
                                    handler.postDelayed(r, 2000);

                                }
                                else {
                                    containerActivity.currentPlayer = 0;
                                }
                            }
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


    private int[] aiTakesATurn() {
        int blackInARow = 1;
        int bestBlackInARow = 1;
        int whiteInARow = 1;
        int bestWhiteInARow = 1;
        int[] defensivePlay = new int[]{0,0}; // dummy values
        int[] bestMove = defensivePlay;
        boolean blank = false;

        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                 int x = j;
                 int y = i;

                // BLACK PIECE FOUND
                if (array[x][y].color() == 0) {
                    //HORIZONTAL AXIS///////////////////////////////////////////////////////////////
                    // CHECK LEFT
                    blank = false;
                    while (x>0) {
                        x--;
                        if (array[x][y].color() == 0) {
                            blackInARow++;
                        } else {
                            if (array[x][y].color() == -1) {
                                blank = true;
                                defensivePlay = new int[]{x,y};
                            }
                            x = j;
                            break;
                        }
                    }
                    // CHECK RIGHT
                    while (x<14) {
                        x++;
                        if (array[x][y].color() == 0) {
                            blackInARow++;
                        } else {
                            if (array[x][y].color() == -1) {
                                blank = true;
                                defensivePlay = new int[]{x,y};
                            }
                            x = j;
                            break;
                        }
                    }
                    if (blackInARow >= bestBlackInARow && blank == true) {
                        bestBlackInARow = blackInARow;
                        bestMove = defensivePlay;
                    }

                    //VERTICAL AXIS/////////////////////////////////////////////////////////////////
                    // CHECK UP
                    x = j;
                    y = i;
                    blackInARow = 1;
                    blank = false;
                    while (y>0) {
                        y--;
                        if (array[x][y].color() == 0) {
                            blackInARow++;
                        } else {
                            if (array[x][y].color() == -1) {
                                blank = true;
                                defensivePlay = new int[]{x,y};
                            }
                            y = i;
                            break;
                        }
                    }
                    // CHECK DOWN
                    while (y<14) {
                        y++;
                        if (array[x][y].color() == 0) {
                            blackInARow++;
                        } else {
                            if (array[x][y].color() == -1) {
                                blank = true;
                                defensivePlay = new int[]{x,y};
                            }
                            y = i;
                            break;
                        }
                    }
                    if (blackInARow >= bestBlackInARow && blank == true) {
                        bestBlackInARow = blackInARow;
                        bestMove = defensivePlay;
                    }

                    //DIAGONAL AXIS 1 (NW TO SE)////////////////////////////////////////////////////
                    // CHECK UPLEFT
                    x = j;
                    y = i;
                    blackInARow = 1;
                    blank = false;
                    while (x>0 && y>0) {
                        x--;
                        y--;
                        if (array[x][y].color() == 0) {
                            blackInARow++;
                        } else {
                            if (array[x][y].color() == -1) {
                                blank = true;
                                defensivePlay = new int[]{x,y};
                            }
                            x = j;
                            y = i;
                            break;
                        }
                    }
                    // CHECK DOWNRIGHT
                    while (x<14 && y<14) {
                        x++;
                        y++;
                        if (array[x][y].color() == 0) {
                            blackInARow++;
                        } else {
                            if (array[x][y].color() == -1) {
                                blank = true;
                                defensivePlay = new int[]{x,y};
                            }
                            x = j;
                            y = i;
                            break;
                        }
                    }
                    if (blackInARow >= bestBlackInARow && blank == true) {
                        bestBlackInARow = blackInARow;
                        bestMove = defensivePlay;
                    }

                    //DIAGONAL AXIS 2 (SW TO NE)////////////////////////////////////////////////////
                    // CHECK DOWNLEFT
                    x = j;
                    y = i;
                    blackInARow = 1;
                    blank = false;
                    while (x>0 && y<14) {
                        x--;
                        y++;
                        if (array[x][y].color() == 0) {
                            blackInARow++;
                        } else {
                            if (array[x][y].color() == -1) {
                                blank = true;
                                defensivePlay = new int[]{x,y};
                            }
                            x = j;
                            y = i;
                            break;
                        }
                    }
                    // CHECK UPRIGHT
                    while (x<14 && y>0) {
                        x++;
                        y--;
                        if (array[x][y].color() == 0) {
                            blackInARow++;
                        } else {
                            if (array[x][y].color() == -1) {
                                blank = true;
                                defensivePlay = new int[]{x,y};
                            }
                            x = j;
                            y = i;
                            break;
                        }
                    }
                    if (blackInARow >= bestBlackInARow && blank == true) {
                        bestBlackInARow = blackInARow;
                        bestMove = defensivePlay;
                    }

                    // WHEW, all 4 axes have been checked and bestMove should be the [x,y] coords
                    // of an optimal defensive move
                }
            }
        }
        // Board has been completely iterated over
        int x = bestMove[0];
        int y = bestMove[1];


        // Place a white piece after a delay to allow the human's black piece to finish animating
        array[x][y].setColor(1);
        final ImageView iv = (ImageView) containerActivity.gv.getChildAt(y*15+x);

        Handler handler = new Handler();
        Runnable r = new Runnable() {
            public void run() {
                ScaleAnimation anim = new ScaleAnimation(0, 1f, 0, 1f, Animation.ABSOLUTE,iv.getPivotX(),Animation.ABSOLUTE,iv.getPivotY());
                anim.setDuration(PIECE_DRAWTIME);
                iv.setImageDrawable(context.getDrawable(R.drawable.white));
                iv.startAnimation(anim);
            }
        };
        handler.postDelayed(r, PIECE_DRAWTIME + 200);

        return bestMove;
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



