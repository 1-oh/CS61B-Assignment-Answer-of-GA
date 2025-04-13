package hw2;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private int size;
    private int [][] states;/*0 for block,1 for air,0 for water*/
    private int NumOfOpenSites;
    private boolean isPercolate;
    WeightedQuickUnionUF setarray;

    // create N-by-N grid, with all sites initially blocked
    public Percolation(int N) {
        size=N;
        states = new int[N][N];
        for (int i = 0; i < N; i += 1) {
            for (int j = 0; j < N; j += 1) {
                states[i][j] = 0;
            }
            NumOfOpenSites=0;
        }
        setarray=new WeightedQuickUnionUF(N*N);
        isPercolate=false;
    }

    /*Convert (row,col) to int*/
    private int  Convert(int row ,int col){
        return size*row+col;
    }

    /*To fill the element @(row,col) and all other elements connected to it*/
    public void fill(int row ,int col){
        states[row][col]=2;
        if(row==size-1) isPercolate=true;

        if(row>0 && setarray.connected(Convert(row, col),Convert(row-1,col)) && states[row-1][col]==1){
            fill(row-1,col);
        }
        if(row<size-1 && setarray.connected(Convert(row, col),Convert(row+1,col))&& states[row+1][col]==1){
            fill(row+1,col);
        }
        if(col>0 && setarray.connected(Convert(row, col),Convert(row,col-1))&& states[row][col-1]==1){
            fill(row,col-1);
        }
        if(col<size-1 && setarray.connected(Convert(row, col),Convert(row,col+1))&& states[row][col+1]==1){
            fill(row,col+1);
        }
    }

    // open the site (row, col) if it is not open already
    public void open ( int row, int col){
        NumOfOpenSites+=1;
        if(row == 0) {
            if (col>0 && isOpen(row,col-1))
                setarray.union(Convert(row,col-1),Convert(row, col));
            if (col<size-1 && isOpen(row,col+1))
                setarray.union(Convert(row,col+1),Convert(row, col));
            if (isOpen(row+1,col))
                setarray.union(Convert(row+1,col),Convert(row, col));
            fill(row,col);
            return;
        }

        states[row][col]=1;
        boolean NeedToFill=false;

        if (row>0 && isOpen(row - 1, col)) {
            setarray.union(Convert(row, col), Convert(row - 1, col));
            if(isFull(row-1,col)) NeedToFill=true;
        }

        if(row<size-1 && isOpen( row+1, col)){
            setarray.union(Convert(row, col),Convert(row+1,col));
            if(isFull(row+1,col)) NeedToFill=true;
        }
        if(col>0 && isOpen( row, col-1)){
            setarray.union(Convert(row, col),Convert(row,col-1));
            if(isFull(row,col-1)) NeedToFill=true;
        }
        if(col<size-1 && isOpen( row, col+1)){
            setarray.union(Convert(row, col),Convert(row,col+1));
            if(isFull(row,col+1)) NeedToFill=true;
        }

        if(NeedToFill) fill(row, col);
    }

    // is the site (row, col) open?
    public boolean isOpen ( int row, int col){
        return states[row][col]>=1;
    }

    // is the site (row, col) full?
    public boolean isFull ( int row, int col){
        return states[row][col]==2;
    }
    // number of open sites
    public int numberOfOpenSites () {
        return NumOfOpenSites;
    }

    // does the system percolate?
    public boolean percolates () {
        return isPercolate;
    }
    // use for unit testing (not required)
    public static void main (String[]args){
        Percolation p=new Percolation(6);
        for(int i =0;i< 6;i+=1){
            for(int j=0;j<6;j+=1){
                System.out.print(p.states[i][j]);
            }
            System.out.println();
        }
        System.out.println();
        p.open(3,5);
        for(int i =0;i< 6;i+=1){
            for(int j=0;j<6;j+=1){
                System.out.print(p.states[i][j]);
            }
            System.out.println();
        }
        System.out.println();
        p.open(1,3);
        p.open(1,4);
        p.open(0,3);
        p.open(2,2);
        p.open(2,3);
        p.open(2,4);
        p.open(3,4);
        p.open(4,4);
        p.open(5,4);
        for(int i =0;i< 6;i+=1){
            for(int j=0;j<6;j+=1){
                System.out.print(p.states[i][j]);
            }
            System.out.println();
        }
        System.out.println("Is Open?"+p.isOpen(0,3));
    }
}
