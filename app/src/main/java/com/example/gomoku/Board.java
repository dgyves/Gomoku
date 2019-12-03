package com.example.gomoku;

public class Board {
    private Piece[][] grid;
    // Track AI (0) or human (1)
    private int p1;
    private int p2;
    private int turn;

    // Constructor - initialize the board with blank Pieces
    public Board (int p1, int p2) {
        // Generate game board
        this.grid = new Piece[15][15];
        for (int y = 0; y < 15; y++) {
            for (int x = 0; x < 15; x++) {
                this.grid[x][y] = new Piece(x, y);
            }
        }

        // Set players
        this.p1 = p1;
        this.p2 = p2;
        turn = 1;
    }

    // Return 1 for p1, 2 for p2
    public int whoseTurn() {
        return this.turn;
    }

    // After a move is made, change which player's turn it is
    public void advanceTurn() {
        if (this.turn == 1) {this.turn++;}
        else {this.turn = 1;}
    }

    // Called each time a Piece has just been placed.
    // Check neighboring pieces on all 3 axes to determine if game has been won
    public boolean gameWon(int player, int x, int y) {
        // Horizontal

        // Vertical

        // Diagonal
        // TODO: Real return value
    return false;
    }

    private int checkLR (int player, int otherPlayer, int x, int y) {
        // Out of grid bounds
        if (x < 0 || y < 0 || x > 14 || y > 14) {
            return 0;
        }

        // Blank space or opponent piece encountered
        if (grid[x][y].color() == -1 || grid[x][y].color() == otherPlayer) {
            return 0;
        }

        // Recurse
        // TODO: Verify if this results in too many checks on each piece, I think it may check
        // TODO: In both directions for EACH piece encountered
        return 1 + checkLR(player,otherPlayer,x-1,y) + checkLR(player,otherPlayer,x+1,y);
    }
}
