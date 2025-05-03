package hw4.puzzle;
import edu.princeton.cs.algs4.MinPQ;

import java.util.*;

public class Solver {
    private WorldState Initial;
    private List<WorldState> Solution;
    private int TimesOfMove;

    /**States that can be compared*/
    private class StateToCompare{
        WorldState state;
        int InitialToHere;
        StateToCompare Father;
        /*the num of steps walking from the start to here*/

        public StateToCompare(WorldState ws, int experience, StateToCompare father){
            state = ws;
            InitialToHere = experience;
            Father = father;
        }

        public int PredictWholeStep(){
            return state.estimatedDistanceToGoal() + InitialToHere;
        }

    }

    /**The comparator of the StateToCompare*/
    private Comparator<StateToCompare> Com = new Comparator<StateToCompare>() {
        @Override
        public int compare(StateToCompare o1, StateToCompare o2) {
            return o1.PredictWholeStep() - o2.PredictWholeStep();
        }
    };

    private MinPQ<StateToCompare> PQ;

    /**Generate the Solution using a recursive method from the result
     * to the initial*/
    private void SolutionGenerate(StateToCompare s){
        if(s == null) return;
        else{
            Solution.add(0,s.state);
            SolutionGenerate(s.Father);
        }
    }

    public Solver(WorldState initial){
        Initial = initial;
        Solution = new LinkedList<>();

        PQ = new MinPQ<>(Com);
        TimesOfMove = 0;

        PQ.insert(new StateToCompare(Initial, 0, null));

        while(!PQ.isEmpty()){
            StateToCompare MinStc =PQ.delMin();

            if(MinStc.state.isGoal()){
                TimesOfMove = MinStc.InitialToHere;
                SolutionGenerate(MinStc);
                break;
            }

            for (WorldState ws : MinStc.state.neighbors()) {
                boolean NeedToInsert = true;

                /*Avoid insert the grandfather into the PQ*/
                if(MinStc.Father != null && ws.equals(MinStc.Father.state))
                    NeedToInsert = false;

                if (NeedToInsert) {
                    PQ.insert(new StateToCompare(ws, MinStc.InitialToHere + 1, MinStc));
                }
            }
        }
    }

    public int moves(){
        return TimesOfMove;
    }

    public Iterable<WorldState> solution(){
        return Solution;
    }
}
