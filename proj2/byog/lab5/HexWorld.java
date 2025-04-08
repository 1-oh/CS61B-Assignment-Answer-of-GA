package byog.lab5;
import org.junit.Test;
import static org.junit.Assert.*;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */

public class HexWorld {
       private static final long SEED=2873123;
       private static final Random RANDOM1=new Random(SEED);

       public static void DrawAline(TETile[][] world,int height,int start,int length,TETile t){
           for(int i=0;i<length;i++){
               world[start+i][height]=t;
           }
       }
       public static void addHexagon(TETile[][] world, Position p, int s, TETile t){
           /**world is an array of TETile where hexagins are put.p is the position where it is put
            * s is the size of the hexagon and t is the type of the TETile*/
            int maxwidth=3*s-2;
            DrawAline(world,p.py,p.px,maxwidth,t);
            DrawAline(world,p.py-1,p.px,maxwidth,t);
            for(int i=1;i<s;i++){
                DrawAline(world,p.py-1-i,p.px+i,maxwidth-2*i,t);
            }
            for(int i=1;i<s;i++){
               DrawAline(world,p.py+i,p.px+i,maxwidth-2*i,t);
            }
       }

       public static void main(String[] Args){
            int width=50;
            int height=50;
           Position[] pos=new Position[19];
           pos[0]=new Position(25,37);
           pos[1]=new Position(20,34) ;
           for(int i=2;i<19;i++){
               pos[i]=new Position();
           }
           pos[2].px=15;pos[2].py=31;
           pos[3].px=25;pos[3].py=31;
           pos[4].px=35;pos[4].py=31;
           pos[5].px=20;pos[5].py=28;
           pos[6].px=30;pos[6].py=28;
           pos[7].px=15;pos[7].py=25;
           pos[8].px=25;pos[8].py=25;
           pos[9].px=35;pos[9].py=25;
           pos[10].px=30;pos[10].py=34;
           pos[11].px=20;pos[11].py=22;
           pos[12].px=30;pos[12].py=22;
           pos[13].px=15;pos[13].py=19;
           pos[14].px=25;pos[14].py=19;
           pos[15].px=35;pos[15].py=19;
           pos[16].px=20;pos[16].py=16;
           pos[17].px=30;pos[17].py=16;
           pos[18].px=25;pos[18].py=13;


           // initialize the tile rendering engine with a window of size WIDTH x HEIGHT
            TERenderer trd=new TERenderer();
            trd.initialize(width,height);
            //Initialize the tile array newWorld
            TETile[][] newWorld=new TETile[width][height];

            for (int x = 0; x < 50; x += 1) {
                for (int y = 0; y < 50; y += 1) {
                   newWorld[x][y] = Tileset.NOTHING;
                }
            }

            TETile[] forest={Tileset.GRASS,Tileset.WATER,Tileset.TREE,
            Tileset.SAND,Tileset.MOUNTAIN};
            for(int i=0;i<19;i++){
                addHexagon(newWorld,pos[i],3,forest[i%5]);
            }

            trd.renderFrame(newWorld);

       }
}
