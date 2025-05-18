import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.lang.classfile.components.ClassPrinter;
import java.util.*;

import static java.lang.Math.*;

/**
 * Graph for storing all of the intersection (vertex) and road (edge) information.
 * Uses your GraphBuildingHandler to convert the XML files into a graph. Your
 * code must include the vertices, adjacent, distance, closest, lat, and lon
 * methods. You'll also need to include instance variables and methods for
 * modifying the graph (e.g. addNode and addEdge).
 *
 * @author Alan Yao, Josh Hug
 */
public class GraphDB {
    /** Your instance variables for storing the graph. You should consider
     * creating helper classes, e.g. Node, Edge, etc. */
    Map<Long, Node> NodeMap = new HashMap<>();
    Map<Long, Edge> EdgeMap = new HashMap<>();

    void AddNode(double lat, double lon, long ID){
        Node n = new Node(lat, lon, ID);
    }

    void InitialEdge(long ID){
        Edge e = new Edge(ID);
    }

    /**Helper class : Node*/
    class Node{
        /*Some properties of the node*/
        double lat;
        double lon;
        long id;
        Map<String, String> tags;

        /*The node's neighbors*/
        Set<Long> AdjacentNodes;
        /*The ways that a node is connected to*/
        Set<Long> ConnectedEdges;

        Node(double LAT, double LON, long ID){
            id = ID;
            lat = LAT;
            lon = LON;
            tags = new HashMap<>();

            NodeMap.put(ID, this);
            AdjacentNodes = new HashSet<>();
            ConnectedEdges = new HashSet<>();
        }

        void ConnectToAnotherNode(long AnotherRoadID){
            NodeMap.get(id).AdjacentNodes.add(AnotherRoadID);
            NodeMap.get(AnotherRoadID).AdjacentNodes.add(id);
        }
    }

    /**Helper class : Edge*/
    class Edge{
        long WayID;
        List<Long> NodesAlongTheRoads;
        Map<String, String> tags;

        Edge(long WayId){
            WayID= WayId;
            NodesAlongTheRoads = new ArrayList<>();
            tags = new HashMap<>();
            EdgeMap.put(WayID, this);
        }

        void addNode(long NodeIndex){
            NodeMap.get(NodeIndex).ConnectedEdges.add(WayID);
            if(!NodesAlongTheRoads.isEmpty()) {
                NodeMap.get(NodeIndex).ConnectToAnotherNode(NodesAlongTheRoads.getLast());
            }
            NodesAlongTheRoads.add(NodeIndex);
        }
    }

    long FindWay(long node1, long node2 , Long PreWay){
        List<Long> PossibleAnswers = new ArrayList<>();
        for(Long e : NodeMap.get(node1).ConnectedEdges){
           if(NodeMap.get(node2).ConnectedEdges.contains(e)){
               PossibleAnswers.add(e);
           }
        }
        if(PreWay != null && PossibleAnswers.contains(PreWay)){
            return PreWay;
        }
        return PossibleAnswers.getFirst();
    }
    /**
     * Example constructor shows how to create and start an XML parser.
     * You do not need to modify this constructor, but you're welcome to do so.
     * @param dbPath Path to the XML file to be parsed.
     */
    public GraphDB(String dbPath) {
        try {
            File inputFile = new File(dbPath);
            FileInputStream inputStream = new FileInputStream(inputFile);
            // GZIPInputStream stream = new GZIPInputStream(inputStream);

            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            GraphBuildingHandler gbh = new GraphBuildingHandler(this);
            saxParser.parse(inputStream, gbh);

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        clean();
    }

    /**
     * Helper to process strings into their "cleaned" form, ignoring punctuation and capitalization.
     * @param s Input string.
     * @return Cleaned string.
     */
    static String cleanString(String s) {
        return s.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }

    /**
     *  Remove nodes with no connections from the graph.
     *  While this does not guarantee that any two nodes in the remaining graph are connected,
     *  we can reasonably assume this since typically roads are connected.
     */
    private void clean() {
        // TODO: Your code here.
        /*There will be error if we delete in the for-each loop
        * thus we choose to collect the key and delete later*/
        List<Long> NodeToDelete = new ArrayList<>();
        for(Long i : NodeMap.keySet()){
            Node e = NodeMap.get(i);
            if(e.AdjacentNodes.isEmpty()){
                NodeToDelete.add(i);
            }
        }
        for(Long i : NodeToDelete){
            NodeMap.remove(i);
        }
    }

    /**
     * Returns an iterable of all vertex IDs in the graph.
     * @return An iterable of id's of all vertices in the graph.
     */
    Iterable<Long> vertices() {
        //YOUR CODE HERE, this currently returns only an empty list.
        return NodeMap.keySet();
    }

    /**
     * Returns ids of all vertices adjacent to v.
     * @param v The id of the vertex we are looking adjacent to.
     * @return An iterable of the ids of the neighbors of v.
     */
    Iterable<Long> adjacent(long v) {
       return NodeMap.get(v).AdjacentNodes;
    };

    /**
     * Returns the great-circle distance between vertices v and w in miles.
     * Assumes the lon/lat methods are implemented properly.
     * <a href="https://www.movable-type.co.uk/scripts/latlong.html">Source</a>.
     * @param v The id of the first vertex.
     * @param w The id of the second vertex.
     * @return The great-circle distance between the two locations from the graph.
     */
    double distance(long v, long w) {
        return distance(lon(v), lat(v), lon(w), lat(w));
    }

    static double distance(double lonV, double latV, double lonW, double latW) {
        double phi1 = Math.toRadians(latV);
        double phi2 = Math.toRadians(latW);
        double dphi = Math.toRadians(latW - latV);
        double dlambda = Math.toRadians(lonW - lonV);

        double a = Math.sin(dphi / 2.0) * Math.sin(dphi / 2.0);
        a += Math.cos(phi1) * Math.cos(phi2) * Math.sin(dlambda / 2.0) * Math.sin(dlambda / 2.0);
        double c = 2 * Math.atan2(sqrt(a), sqrt(1 - a));
        return 3963 * c;
    }

    /**
     * Returns the initial bearing (angle) between vertices v and w in degrees.
     * The initial bearing is the angle that, if followed in a straight line
     * along a great-circle arc from the starting point, would take you to the
     * end point.
     * Assumes the lon/lat methods are implemented properly.
     * <a href="https://www.movable-type.co.uk/scripts/latlong.html">Source</a>.
     * @param v The id of the first vertex.
     * @param w The id of the second vertex.
     * @return The initial bearing between the vertices.
     */
    double bearing(long v, long w) {
        return bearing(lon(v), lat(v), lon(w), lat(w));
    }

    static double bearing(double lonV, double latV, double lonW, double latW) {
        double phi1 = Math.toRadians(latV);
        double phi2 = Math.toRadians(latW);
        double lambda1 = Math.toRadians(lonV);
        double lambda2 = Math.toRadians(lonW);

        double y = Math.sin(lambda2 - lambda1) * Math.cos(phi2);
        double x = Math.cos(phi1) * Math.sin(phi2);
        x -= Math.sin(phi1) * Math.cos(phi2) * Math.cos(lambda2 - lambda1);
        return Math.toDegrees(Math.atan2(y, x));
    }

    /**
     * Returns the vertex closest to the given longitude and latitude.
     * @param lon The target longitude.
     * @param lat The target latitude.
     * @return The id of the node in the graph closest to the target.
     */
    long closest(double lon, double lat) {
        long ret = 0;
        double DisMin = 0;
        long cnt = 0;
        for(long i : NodeMap.keySet()){
            if(cnt == 0){
                ret = i;
                DisMin = ComputeSigma(lon, lat, i);
                cnt = 1;
                continue;
            }
            if(ComputeSigma(lon, lat, i) < DisMin){
                DisMin = ComputeSigma(lon, lat, i);
                ret = i;
            }
        }
        return ret;
    }

    private double ComputeSigma(double lon, double lat, long index){
        Node n = NodeMap.get(index);
        return distance(lon, lat, n.lon, n.lat);
    }
    /**
     * Gets the longitude of a vertex.
     * @param v The id of the vertex.
     * @return The longitude of the vertex.
     */
    double lon(long v) {
        return NodeMap.get(v).lon;
    }

    /**
     * Gets the latitude of a vertex.
     * @param v The id of the vertex.
     * @return The latitude of the vertex.
     */
    double lat(long v) {
        return NodeMap.get(v).lat;
    }
}
