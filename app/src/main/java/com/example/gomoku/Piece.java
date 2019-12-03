package com.example.gomoku;

public class Piece {
    // Enum this in -1 empty, 0 black, 1 white.. defaults to empty
    private int color = -1;
    // x,y grid value
    private int[] coords;

    // Public constructor used when initializing board with blanks
    public Piece (int x, int y) {
        this.coords = new int[]{x,y};
    }

    // Public constructor to initialize a black/white piece (when placed)
    public Piece (int x, int y, int color) {
        this.coords = new int[]{x,y};
        assert (color == 0 || color == 1);
        this.color = color;
    }

    public int color() {return this.color;}
}
