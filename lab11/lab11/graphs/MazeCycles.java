package lab11.graphs;
import java.util.*;

/**
 *  @author Josh Hug & Gary Agasa
 */
public class MazeCycles extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */
    private class StartEnd{
        int start;
        int end;
        public StartEnd(int s, int e){
            start = s;
            end = e;
        }
    }

    private Map<Integer, Integer> father;

    public MazeCycles(Maze m) {
        super(m);
    }

    @Override
    public void solve() {
        // TODO: Your code here!
        /*Try to find two vertexes in the cycle*/
        StartEnd se = DFS(0);

        /*If there is no cycle*/
        if(se == null){
            return;
        }

        /*Find the vertexes' CoFather, which is also a vertex in the cycle*/
        List<Integer> l1 = DadsFinder(se.start);
        List<Integer> l2 = DadsFinder(se.end);

        int CoFather = 0;
        for(int i : l1){
            for(int j : l2){
                if(i == j){
                   CoFather = i;
                   break;
                }
            }
            if(CoFather != 0){
                break;
            }
        }

        RecursiveDraw1(se.end, CoFather);
        RecursiveDraw2(se.start, CoFather);
    }

    //ToDo: Helper methods go here
    private StartEnd DFS(int source){
        Stack<Integer> stack = new Stack<>();
        father= new HashMap<>();

        stack.push(source);
        distTo[source] = 0;

        father.put(source, source);
        announce();

        while (!stack.empty()){
            int index = stack.pop();
            /*Notice that the "marked" here means "visit",
             * So when we implement BFS:
             * we marked every neighbor in the for "neighbor" loop, because we want to show the
             * unmarked neighbors(a.k.a sons) once we find them.
             * But when we implement DFS:
             * we marked every vertex when they are popped out of the stack, not in the for "neighbor"
             * loop*/
            marked[index] = true;
            announce();

            for(int neighbor : maze.adj(index)){
                /*If the neighbor is the son of the index*/
                if(!marked[neighbor]){
                    father.put(neighbor, index);
                    distTo[neighbor] = distTo[index] + 1;
                    stack.push(neighbor);
                }
                /*If the neighbor is neither the son nor the father, then there must be a cycle*/
                else if(father.get(index) != neighbor){
                    edgeTo[index] = neighbor;
                    announce();
                    return new StartEnd(index, neighbor);
                }
            }
        }
        return null;
    }

    private List<Integer> DadsFinder(int index){
        List<Integer> ret= new ArrayList<>();
        DadsFinderHelper(ret, index);
        return ret;
    }

    private void DadsFinderHelper(List l, int index){
        l.add(index);

        if(father.get(index) == index){
            return;
        }

        DadsFinderHelper(l, father.get(index));

    }

    private void RecursiveDraw1(int index, int Destination){
        if(index == Destination){
            return;
        }
        edgeTo[index] = father.get(index);
        announce();
        RecursiveDraw1(father.get(index), Destination);
    }

    private void RecursiveDraw2(int index, int Destination){
        if(index == Destination){
            return;
        }
        edgeTo[father.get(index)] = index;
        announce();
        RecursiveDraw2(father.get(index), Destination);
    }
}