package hw4.puzzle;
import edu.princeton.cs.algs4.Queue;


public class Board implements WorldState{
    private int[][] tiles;
    private int[][] goal;

    public Board(int[][] tiles){
        this.tiles = new int[tiles.length][tiles.length];
        for(int i = 0; i < size(); i += 1){
            for(int j = 0; j < size(); j += 1){
                this.tiles[i][j] = tiles[i][j];
            }
        }

        goal = new int[size()][size()];
        for(int i = 0; i < size(); i += 1){
            for(int j = 0; j < size(); j += 1){
                goal[i][j] = i * size() + j + 1;
            }
        }
        goal[size()-1][size()-1] = 0;
    }

    /**Returns value of tile at row i, column j (or 0 if blank)*/
    public int tileAt(int i, int j){
        if(!(i >= 0 && i < size() && j >= 0 && j < size()))
            throw new java.lang.IndexOutOfBoundsException();
        return tiles[i][j];
    }

    public int size(){
        //length here means the number of rows
        return tiles.length;
    }

    @Override
    public Iterable<WorldState> neighbors(){
        /**Thanks for Josh Hug 's effort, I can skip this function.
         * It is provided by CS61B*/
        Queue<WorldState> neighbors = new Queue<>();
        int BLANK = 0;
        int hug = size();
        int bug = -1;
        int zug = -1;
        for (int rug = 0; rug < hug; rug++) {
            for (int tug = 0; tug < hug; tug++) {
                if (tileAt(rug, tug) == BLANK) {
                    bug = rug;
                    zug = tug;
                }
            }
        }
        int[][] ili1li1 = new int[hug][hug];
        for (int pug = 0; pug < hug; pug++) {
            for (int yug = 0; yug < hug; yug++) {
                ili1li1[pug][yug] = tileAt(pug, yug);
            }
        }
        for (int l11il = 0; l11il < hug; l11il++) {
            for (int lil1il1 = 0; lil1il1 < hug; lil1il1++) {
                if (Math.abs(-bug + l11il) + Math.abs(lil1il1 - zug) - 1 == 0) {
                    ili1li1[bug][zug] = ili1li1[l11il][lil1il1];
                    ili1li1[l11il][lil1il1] = BLANK;
                    Board neighbor = new Board(ili1li1);
                    neighbors.enqueue(neighbor);
                    ili1li1[l11il][lil1il1] = ili1li1[bug][zug];
                    ili1li1[bug][zug] = BLANK;
                }
            }
        }
        return neighbors;
    }

    public int hamming(){
        int ret = 0;
        for(int i = 0;i < size(); i += 1){
            for(int j = 0;j < size(); j += 1){
                if(tiles[i][j] != goal[i][j])
                    ret += 1;
            }
        }
        return ret;
    }

    /**Assistant functions for manhattan()*/
    private int ColCal(int num){
        if(num % size() == 0) return size() - 1;
        return num % size() - 1;
    }

    private int RowCal(int num){
        return (num - 1)/size();
    }

    private int abs(int num){
        return num > 0 ? num : -num;
    }

    public int manhattan(){
        int ret = 0;
        for(int i = 0;i < size();i += 1){
            for(int j = 0;j < size();j += 1){
                if(tiles[i][j] == 0) continue;
                ret += abs(i - RowCal(tiles[i][j]));
                ret += abs(j - ColCal(tiles[i][j]));
            }
        }
        return ret;
    }

    public int estimatedDistanceToGoal(){
        return manhattan();
    }

    @Override
    public boolean equals(Object y){
        /**If the address are the same*/
        if (this == y) {
            return true;
        }
        /**If they belong to different types*/
        if (y == null || getClass() != y.getClass()) {
            return false;
        }

        Board Y = (Board) y;
        for(int i = 0; i < size(); i += 1){
            for(int j = 0; j < size(); j += 1){
                if(tiles[i][j] != Y.tiles[i][j])
                    return false;
            }
        }

        return true;
    }

    /** Returns the string representation of the board. 
      * Uncomment this method. */
    public String toString() {
        StringBuilder s = new StringBuilder();
        int N = size();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", tileAt(i,j)));
            }
            s.append("\n");
        }
        s.append("\n");
        return s.toString();
    }

}
