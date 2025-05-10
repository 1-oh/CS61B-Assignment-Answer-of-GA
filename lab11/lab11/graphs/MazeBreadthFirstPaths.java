package lab11.graphs;
import java.util.ArrayDeque;
import java.util.Queue;

/**
 *  @author Josh Hug
 */
public class MazeBreadthFirstPaths extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */
    private int s;
    private int t;
    private boolean targetFound;
    private Maze maze;
    private Queue<Integer> q;

    public MazeBreadthFirstPaths(Maze m, int sourceX, int sourceY, int targetX, int targetY) {
        super(m);
        // Add more variables here!
        maze = m;
        s = maze.xyTo1D(sourceX, sourceY);
        t = maze.xyTo1D(targetX, targetY);
        targetFound = false;
        q = new ArrayDeque();

        distTo[s] = 0;
        edgeTo[s] = s;
    }

    /** Conducts a breadth first search of the maze starting at the source. */
    private void bfs() {
        // TODO: Your code here. Don't forget to update distTo, edgeTo, and marked, as well as call announce()
        q.add(s);
        announce();

        while (!q.isEmpty()){
            int index= q.remove();

            if(index == t){
                targetFound= true;
                return;
            }

            for(int neighbor: maze.adj(index)){
                if(!marked[neighbor]){
                    edgeTo[neighbor] = index;
                    distTo[neighbor] = distTo[index] + 1;
                    marked[neighbor] = true;
                    q.add(neighbor);
                    announce();
                }
            }
        }
        throw new RuntimeException();
    }


    @Override
    public void solve() {
        bfs();
    }
}

