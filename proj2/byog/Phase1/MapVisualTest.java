package byog.Phase1;
import java.io.IOException;

public class MapVisualTest {
    public static long seed=System.currentTimeMillis();
    public static void main(String[] Args) throws IOException{
        MapGenerator map=new MapGenerator(50,70,seed);
        map.GenerateMap();
    };
};
