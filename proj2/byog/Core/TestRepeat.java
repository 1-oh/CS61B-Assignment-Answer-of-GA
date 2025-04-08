package byog.Core;

import byog.TileEngine.TETile;
import org.junit.Test;

import java.io.IOException;

public class TestRepeat {
    @Test
    public void testrepeat() throws IOException, ClassNotFoundException {
        Game game = new Game();
        TETile[][] worldstate1= game.playWithInputString("n125348145s");
        System.out.println(TETile.toString(worldstate1));
//        for(int i=0;i<3;i++) {
//            TETile[][] worldState = game.playWithInputString("n12545s");
//            System.out.println(TETile.toString(worldState));
//        }
//        for(int i=0;i<3;i++) {
//            TETile[][] worldState = game.playWithInputString("n125235s");
//            System.out.println(TETile.toString(worldState));
//        }
    }
}
