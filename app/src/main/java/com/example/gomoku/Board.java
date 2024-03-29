package com.example.gomoku;

public class Board {
    private Piece[][] grid;
    // Track AI (0) or human (1)
    private final int p1 = 1;
    private int p2;

    /**
     * Constructor - initialize the board with blank Pieces
     * @param p2 sets player 2 as AI or Human
     */
    public Board (int p2) {
        // Generate game board
        this.grid = new Piece[15][15];
        for (int y = 0; y < 15; y++) {
            for (int x = 0; x < 15; x++) {
                this.grid[x][y] = new Piece(x, y);
            }
        }

        // Set whether player 2 is AI (0) or human (1)
        this.p2 = p2;
    }

    // Return current grid representing board
    public Piece[][] getBoardState () {
        return this.grid;
    }


    /**
     * Called each time a Piece has just been placed.
     * Check neighboring pieces on all 4 axes to determine if game has been won
     * @param thisPlayer represents player that made this move
     * @param x x-coord of piece that was just played
     * @param y y-coord of piece that was just played
     **/
    public boolean gameWon(int thisPlayer, int x, int y) {
        int piecesInARow;

        // Horizontal
        piecesInARow = checkWtoE(thisPlayer,1-thisPlayer,true,true,x,y);
        if (piecesInARow >= 5) {return true;}

        // Vertical
        piecesInARow = checkNtoS(thisPlayer,1-thisPlayer,true,true, x,y);
        if (piecesInARow >= 5) {return true;}

        // Diagonals
        piecesInARow = checkNWtoSE(thisPlayer,1-thisPlayer,true,true,x,y);
        if (piecesInARow >= 5) {return true;}
        piecesInARow = checkSWtoNE(thisPlayer,1-thisPlayer,true,true,x,y);
        if (piecesInARow >= 5) {return true;}

    return false;
    }

    /**
     * Check the West-to-East (horizontal) axis to see how many of <thisColor> there are in a row
     @param thisColor represents player that just made the move
     @param otherColor represents opponent
     @param checkingWest are we currently checking west?  or east?  West is checked first
     @param firstCall this is a recursive function.  is this the first (original) call?
     @param x x-coord of the piece that was just played (on first call)
     @param y y-coord of the piece that was just played (on first call)
    **/
    private int checkWtoE (int thisColor, int otherColor, boolean checkingWest, boolean firstCall, int x, int y) {
        // Out of grid bounds (edge of game board passed)
        if (x < 0 || y < 0 || x > 14 || y > 14) {
            return 0;
        }

        // Blank space or opponent piece encountered
        if (grid[x][y].color() != thisColor) {
            return 0;
        }

        // Recurse
        if (firstCall) {
            return 1 + checkWtoE(thisColor, otherColor, true, false, x - 1, y) +
                    checkWtoE(thisColor, otherColor, false,false, x + 1, y);
        }
        else if (checkingWest) {
            return 1 + checkWtoE(thisColor, otherColor, true, false, x-1,y);
        }
        else { //checking East
            return 1 + checkWtoE(thisColor, otherColor, false, false, x + 1, y);
        }
    }

    /**
     * Check the North-to-South (vertical) axis to see how many of <thisColor> there are in a row
     @param thisColor represents player that just made the move
     @param otherColor represents opponent
     @param checkingN are we currently checking north? or south?  North is checked first
     @param firstCall this is a recursive function.  is this the first (original) call?
     @param x x-coord of the piece that was just played (on first call)
     @param y y-coord of the piece that was just played (on first call)
     **/
    private int checkNtoS (int thisColor, int otherColor, boolean checkingN, boolean firstCall, int x, int y) {
        // Out of grid bounds (edge of game board passed)
        if (x < 0 || y < 0 || x > 14 || y > 14) {
            return 0;
        }

        // Blank space or opponent piece encountered
        if (grid[x][y].color() != thisColor) {
            return 0;
        }

        // Recurse
        if (firstCall) {
            return 1 + checkNtoS(thisColor, otherColor, true, false, x, y-1) +
                    checkNtoS(thisColor, otherColor, false,false, x, y+1);
        }
        else if (checkingN) {
            return 1 + checkNtoS(thisColor, otherColor, true, false, x,y-1);
        }
        else { //checking South
            return 1 + checkNtoS(thisColor, otherColor, false, false, x, y+1);
        }
    }

    /**
     * Check the NW to SE (diagonal) axis to see how many of <thisColor> there are in a row
     @param thisColor represents player that just made the move
     @param otherColor represents opponent
     @param checkingNW are we currently checking northwest?  or southeast?  Northwest is checked first
     @param firstCall this is a recursive function.  is this the first (original) call?
     @param x x-coord of the piece that was just played (on first call)
     @param y y-coord of the piece that was just played (on first call)
     **/
    private int checkNWtoSE (int thisColor, int otherColor, boolean checkingNW, boolean firstCall, int x, int y) {
        // Out of grid bounds (edge of game board passed)
        if (x < 0 || y < 0 || x > 14 || y > 14) {
            return 0;
        }

        // Blank space or opponent piece encountered
        if (grid[x][y].color() != thisColor) {
            return 0;
        }

        // Recurse
        if (firstCall) {
            return 1 + checkNWtoSE(thisColor, otherColor, true, false, x-1, y-1) +
                    checkNWtoSE(thisColor, otherColor, false,false, x+1, y+1);
        }
        else if (checkingNW) {
            return 1 + checkNWtoSE(thisColor, otherColor, true, false, x-1,y-1);
        }
        else { //checking Southeast
            return 1 + checkNWtoSE(thisColor, otherColor, false, false, x+1, y+1);
        }
    }

    /**
     * Check the SW to NE (diagonal) axis to see how many of <thisColor> there are in a row
     @param thisColor represents player that just made the move
     @param otherColor represents opponent
     @param checkingSW are we currently checking southwest?  or northeast?  Southwest is checked first
     @param firstCall this is a recursive function.  is this the first (original) call?
     @param x x-coord of the piece that was just played (on first call)
     @param y y-coord of the piece that was just played (on first call)
     **/
    private int checkSWtoNE (int thisColor, int otherColor, boolean checkingSW, boolean firstCall, int x, int y) {
        // Out of grid bounds (edge of game board passed)
        if (x < 0 || y < 0 || x > 14 || y > 14) {
            return 0;
        }

        // Blank space or opponent piece encountered
        if (grid[x][y].color() != thisColor) {
            return 0;
        }

        // Recurse
        if (firstCall) {
            return 1 + checkSWtoNE(thisColor, otherColor, true, false, x-1, y+1) +
                    checkSWtoNE(thisColor, otherColor, false,false, x+1, y-1);
        }
        else if (checkingSW) {
            return 1 + checkSWtoNE(thisColor, otherColor, true, false, x-1,y+1);
        }
        else { //checking Northeast
            return 1 + checkSWtoNE(thisColor, otherColor, false, false, x+1, y-1);
        }
    }
}
