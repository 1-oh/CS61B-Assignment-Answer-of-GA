package hw2;

import edu.princeton.cs.algs4.Stopwatch;
import org.junit.Test;

public class TestRunTime {
    @Test
    public void TestSpeed(){
        for(int N=50;N<200;N+=5){
            Stopwatch clock=new Stopwatch();
            PercolationStats ps=new PercolationStats(N,1000,new PercolationFactory());
            double time=clock.elapsedTime();
            System.out.println(time);
        }
    }
}
