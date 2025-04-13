package hw2;

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private int Times;
    private Percolation p;
    private double [] FractionArray;

    // perform T independent experiments on an N-by-N grid
    public PercolationStats(int N, int T, PercolationFactory pf){
           if(N<=0 || T<=0) throw new java.lang.IllegalArgumentException();

           Times=T;
           FractionArray=new double[T];

           for(int i=0 ; i<T ;i+=1){
               int cnt=0;
               p=pf.make(N);
               while(!p.percolates()){
                   int Row= StdRandom.uniform(N);
                   int Col=StdRandom.uniform(N);
                   if(!p.isOpen(Row,Col)){
                       p.open(Row,Col);
                       cnt+=1;
                   }
               }
               FractionArray[i]=1.0*cnt/(N*N);
           }

    }
    
    // sample mean of percolation threshold
    public double mean(){
        return StdStats.mean(FractionArray);
    }
    // sample standard deviation of percolation threshold
    public double stddev(){
        return StdStats.stddev(FractionArray);
    }
    // low endpoint of 95% confidence interval
    public double confidenceLow(){
        return this.mean()-1.96*this.stddev()/(Math.sqrt(Times));
    }
    // high endpoint of 95% confidence interval
    public double confidenceHigh(){
        return this.mean()+1.96*this.stddev()/(Math.sqrt(Times));
    }
}
