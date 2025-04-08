package byog.Core;

import byog.Phase1.MapGenerator;
import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import edu.princeton.cs.introcs.StdDraw;
import org.junit.Test;

import java.awt.Color;
import java.awt.Font;

import java.util.concurrent.atomic.AtomicInteger;

public class TestnewMapGenerator {
    @Test
    public void TestMapGenerator(){
        int width=150;
        int height=60;
        newMapGenerator map=new newMapGenerator(width,height,System.currentTimeMillis());
        TERenderer ter = new TERenderer();
        ter.initialize(width,height);
        newMapGenerator.MapGenerate();

        ter.renderFrame(newMapGenerator.getTileArray());
        StdDraw.pause(1000);

        for(int i=0;i<10;i++){
            newMapGenerator.movePlayer(newMapGenerator.Bear,'s');
            newMapGenerator.movePlayer(newMapGenerator.Frog,'w');
            ter.renderFrame(newMapGenerator.getTileArray());
            StdDraw.pause(1000);
        }
        for(int i=0;i<10;i++){
            newMapGenerator.movePlayer(newMapGenerator.Frog,'a');
            newMapGenerator.movePlayer(newMapGenerator.Bear,'d');
            ter.renderFrame(newMapGenerator.getTileArray());
            StdDraw.pause(1000);
        }

        newMapGenerator.EndCleaning();
    }
}
