import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class provides a shortestPath method for finding routes between two points
 * on the map. Start by using Dijkstra's, and if your code isn't fast enough for your
 * satisfaction (or the autograder), upgrade your implementation by switching it to A*.
 * Your code will probably not be fast enough to pass the autograder unless you use A*.
 * The difference between A* and Dijkstra's is only a couple of lines of code, and boils
 * down to the priority you use to order your vertices.
 */
public class Router {
    /**
     * Return a List of longs representing the shortest path from the node
     * closest to a start location and the node closest to the destination
     * location.
     * @param g The graph to use.
     * @param stlon The longitude of the start location.
     * @param stlat The latitude of the start location.
     * @param destlon The longitude of the destination location.
     * @param destlat The latitude of the destination location.
     * @return A list of node id's in the order visited on the shortest path.
     */
    private static Comparator<state> ComparatorOfState = new Comparator<state>() {
        @Override
        public int compare(state o1, state o2) {
            double f1 = o1.g_n + o1.g.distance(o1.node, o1.destination);
            double f2 = o2.g_n + o2.g.distance(o2.node, o2.destination);
            double com= f1 -f2;
            if(com < 1e-6 && com > -1e-6) return 0;
            if(com > 0) return 1;
            else return -1;
        }
    };

    /*The priority queue of states, whose contains G_n*/
    /*The Priority is the f(n)*/
    private static PriorityQueue<state> G_n = new PriorityQueue<>(ComparatorOfState);
    /*Already visited nodes with their best g_n value so far*/
    private static Map<Long, Double> AlreadyVisit = new HashMap<>();
    /*The shortest path found using A* algorithm*/
    private static List<Long> solution = new ArrayList<>();
    public static List<Long> shortestPath(GraphDB g, double stlon, double stlat,
                                   double destlon, double destlat) {
        solution.clear();
        G_n.clear();
        AlreadyVisit.clear();

        // ToDo:Implement the A* algorithm
        long StartNode = g.closest(stlon, stlat);/*Nearest node from the start*/
        long DesNode = g.closest(destlon, destlat);/*Nearest node from the end*/
        state StartState = new state(g, StartNode, null, DesNode, 0);
        G_n.add(StartState);
        AlreadyVisit.put(StartNode, .0);

        while(!G_n.isEmpty()){
            /*Find the state with the lowest f(n)*/
            state CurrentState = G_n.remove();

            if(CurrentState.node == DesNode){
                SolutionFinder(CurrentState);
                break;
            }
            for(long neighbor : g.NodeMap.get(CurrentState.node).AdjacentNodes){
                double NewG_n = CurrentState.g_n + g.distance(CurrentState.node, neighbor);
                if(CurrentState.father == null || neighbor != CurrentState.father.node){
                    //Push all kids into the G_n Map
                    if( !AlreadyVisit.containsKey(neighbor) || (AlreadyVisit.get(neighbor) > NewG_n)){
                        state neighborState = new state(g, neighbor, CurrentState, DesNode, NewG_n);
                        G_n.add(neighborState);
                        AlreadyVisit.put(neighbor, NewG_n);
                    }
                }
            }
        }

        return solution.reversed();
    }

    static class state{
        GraphDB g;
        long node;
        state father;
        long destination;
        double g_n;
        public state(GraphDB g,long node, state father, long destination, double g_n){
            this.g = g;
            this.node = node;
            this.father = father;
            this.destination = destination;
            this.g_n = g_n;
        }
        public state(){};

    }

    private static void SolutionFinder(state finalState){
        Stack<state> StatesToProcess = new Stack<>();
        StatesToProcess.add(finalState);
        while(!StatesToProcess.isEmpty()){
            state out = StatesToProcess.pop();
            if(out == null) return;
            solution.add(out.node);
            StatesToProcess.add(out.father);
        }
    }

    /**
     * Create the list of directions corresponding to a route on the graph.
     * @param g The graph to use.
     * @param route The route to translate into directions. Each element
     *              corresponds to a node from the graph in the route.
     * @return A list of NavigatiionDirection objects corresponding to the input
     * route.
     */
    public static List<NavigationDirection> routeDirections(GraphDB g, List<Long> route) {
        // ToDo:Generate the list for navigation command
        List<NavigationDirection> ret = new ArrayList<>();
        if (route.size() < 2) return ret;

        long PreWay = 0;
        Long NextWay = null;
        long CurNode = 0L;
        Long PreNode = null;
        long NextNode;
        double TotalMile = 0;
        for(int i = 0 ; i < route.size() - 1; i += 1){
            //Initialize the nodes
           CurNode = route.get(i);
           NextNode = route.get(i + 1);
           if(i > 0 )
               PreNode = route.get(i - 1);

           //Initialize the ways
            NextWay = g.FindWay(CurNode, NextNode, NextWay);
            if(i > 0)
                PreWay = g.FindWay(PreNode,  CurNode, PreWay);

            //Now let's move through the path!
            /*The start*/
            if(i == 0){
                NavigationDirection newNav = new NavigationDirection();
                newNav.direction = 0;
                ret.add(newNav);
            }
            else{
                /*Process the last NavigationDirection in the list*/
                ret.getLast().distance += g.distance(PreNode, CurNode);
                if(g.EdgeMap.get(PreWay).tags.containsKey("name")){
                    ret.getLast().way = g.EdgeMap.get(PreWay).tags.get("name");
                }

                if(PreWay != NextWay){
                    NavigationDirection newNav = new NavigationDirection();
                    newNav.direction = ComputeDirection(g, PreNode, CurNode, NextNode);
                    ret.add(newNav);
                }
                /*If we 're going to reach the end*/
                if(i == route.size() - 2) {
                    ret.getLast().distance += g.distance(CurNode, NextNode);
                }
            }

        }
        return ret;
    }

    private static int ComputeDirection(GraphDB g, long prevNode, long currNode, long nextNode) {
        double prevBearing = g.bearing(prevNode, currNode);
        double currBearing = g.bearing(currNode, nextNode);
        double angle = currBearing - prevBearing;
        angle = (angle > 180) ? angle - 360 : (angle < -180) ? angle + 360 : angle;

        if (angle >= -15 && angle <= 15) {
            return NavigationDirection.STRAIGHT;
        } else if (angle > 15 && angle <= 30) {
            return NavigationDirection.SLIGHT_RIGHT;
        } else if (angle > 30 && angle <= 100) {
            return NavigationDirection.RIGHT;
        } else if (angle > 100) {
            return NavigationDirection.SHARP_RIGHT;
        } else if (angle < -15 && angle >= -30) {
            return NavigationDirection.SLIGHT_LEFT;
        } else if (angle < -30 && angle >= -100) {
            return NavigationDirection.LEFT;
        } else {
            return NavigationDirection.SHARP_LEFT;
        }
    }
    /**
     * Class to represent a navigation direction, which consists of 3 attributes:
     * a direction to go, a way, and the distance to travel for.
     */
    public static class NavigationDirection {

        /** Integer constants representing directions. */
        public static final int START = 0;
        public static final int STRAIGHT = 1;
        public static final int SLIGHT_LEFT = 2;
        public static final int SLIGHT_RIGHT = 3;
        public static final int RIGHT = 4;
        public static final int LEFT = 5;
        public static final int SHARP_LEFT = 6;
        public static final int SHARP_RIGHT = 7;

        /** Number of directions supported. */
        public static final int NUM_DIRECTIONS = 8;

        /** A mapping of integer values to directions.*/
        public static final String[] DIRECTIONS = new String[NUM_DIRECTIONS];

        /** Default name for an unknown way. */
        public static final String UNKNOWN_ROAD = "unknown road";
        
        /** Static initializer. */
        static {
            DIRECTIONS[START] = "Start";
            DIRECTIONS[STRAIGHT] = "Go straight";
            DIRECTIONS[SLIGHT_LEFT] = "Slight left";
            DIRECTIONS[SLIGHT_RIGHT] = "Slight right";
            DIRECTIONS[LEFT] = "Turn left";
            DIRECTIONS[RIGHT] = "Turn right";
            DIRECTIONS[SHARP_LEFT] = "Sharp left";
            DIRECTIONS[SHARP_RIGHT] = "Sharp right";
        }

        /** The direction a given NavigationDirection represents.*/
        int direction;
        /** The name of the way I represent. */
        String way;
        /** The distance along this way I represent. */
        double distance;

        /**
         * Create a default, anonymous NavigationDirection.
         */
        public NavigationDirection() {
            this.direction = STRAIGHT;
            this.way = UNKNOWN_ROAD;
            this.distance = 0.0;
        }

        public String toString() {
            return String.format("%s on %s and continue for %.3f miles.",
                    DIRECTIONS[direction], way, distance);
        }

        /**
         * Takes the string representation of a navigation direction and converts it into
         * a Navigation Direction object.
         * @param dirAsString The string representation of the NavigationDirection.
         * @return A NavigationDirection object representing the input string.
         */
        public static NavigationDirection fromString(String dirAsString) {
            String regex = "([a-zA-Z\\s]+) on ([\\w\\s]*) and continue for ([0-9\\.]+) miles\\.";
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(dirAsString);
            NavigationDirection nd = new NavigationDirection();
            if (m.matches()) {
                String direction = m.group(1);
                if (direction.equals("Start")) {
                    nd.direction = NavigationDirection.START;
                } else if (direction.equals("Go straight")) {
                    nd.direction = NavigationDirection.STRAIGHT;
                } else if (direction.equals("Slight left")) {
                    nd.direction = NavigationDirection.SLIGHT_LEFT;
                } else if (direction.equals("Slight right")) {
                    nd.direction = NavigationDirection.SLIGHT_RIGHT;
                } else if (direction.equals("Turn right")) {
                    nd.direction = NavigationDirection.RIGHT;
                } else if (direction.equals("Turn left")) {
                    nd.direction = NavigationDirection.LEFT;
                } else if (direction.equals("Sharp left")) {
                    nd.direction = NavigationDirection.SHARP_LEFT;
                } else if (direction.equals("Sharp right")) {
                    nd.direction = NavigationDirection.SHARP_RIGHT;
                } else {
                    return null;
                }

                nd.way = m.group(2);
                try {
                    nd.distance = Double.parseDouble(m.group(3));
                } catch (NumberFormatException e) {
                    return null;
                }
                return nd;
            } else {
                // not a valid nd
                return null;
            }
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof NavigationDirection) {
                return direction == ((NavigationDirection) o).direction
                    && way.equals(((NavigationDirection) o).way)
                    && distance == ((NavigationDirection) o).distance;
            }
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(direction, way, distance);
        }
    }
}
