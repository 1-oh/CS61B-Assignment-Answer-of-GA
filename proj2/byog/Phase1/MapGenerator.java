package byog.Phase1;

import byog.Core.Game;
import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import byog.lab5.Position;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class MapGenerator {
       public static long SEED= Game.seed;
       public static int width;
       public static int height;
       private static Random RANDOM=new Random(SEED);
       private static Random RANDOM2=new Random(SEED);
       private static final TERenderer ter = new TERenderer();
       public static TETile[][] Tiles=new TETile[60][40];
       private static int[][] isFull=new int[100][100]; /*0 for nothing,1 for floors and 2 for walls*/
       private static List<Position> ListOfFloor=new ArrayList<>();
       private static List<Position> FloorToBeAdded=new ArrayList<>();

       public MapGenerator(int WIDTH, int HEIGHT){
          width=WIDTH;
          height=HEIGHT;
       }

       private static void RenderInitial(){
           ter.initialize(width,height);
       }

       private static void RenderClear(){
           for(int i=0;i<width;i+=1){
               for(int j=0;j<height;j+=1){
                   Tiles[i][j]=Tileset.NOTHING;
                   isFull[i][j]=0;
               }
           }
       }

       private static boolean isAnything(Position p){
           return isFull[p.px][p.py]>0;
       }

       private static boolean isOnBoundary(Position p){
           return p.px * p.py == 0 || p.px == width - 1 || p.py == height - 1;
       }

       private static boolean insert(Position p,TETile t){
           /**Return True if the insert is correct and insert the tile
            * otherwise return False*/
           if(p.px<0||p.px>=width||p.py<0||p.py>=width) return false;
           if(isAnything(p)){
               return false;
           }
           Tiles[p.px][p.py]=t;
           if(t==Tileset.FLOOR)isFull[p.px][p.py]=1;
           if(t==Tileset.WALL)isFull[p.px][p.py]=2;
           return true;
       }

       private static void RandomFloorExpand(Position p) {
           int direction = RANDOM.nextInt(10);
           Position ExpandPositon = new Position(0, 0);

           if (direction < 4) {
               ExpandPositon.px = p.px - 1;
               ExpandPositon.py = p.py;
           } else if (direction < 5) {
               ExpandPositon.px = p.px;
               ExpandPositon.py = p.py + 1;
           } else if (direction < 9){
               ExpandPositon.px = p.px + 1;
               ExpandPositon.py = p.py;
           }else {
               ExpandPositon.px = p.px;
               ExpandPositon.py = p.py - 1;
           }

           if(!isOnBoundary(ExpandPositon)){
               boolean jud=insert(ExpandPositon,Tileset.FLOOR);
               if(jud){
                   FloorToBeAdded.add(ExpandPositon);
                   ExpandPositon.timesofvisit=0;
               }
           }
           p.timesofvisit+=1;
       }

       private static void BuildWalls(Position p){
           insert(new Position(p.px-1,p.py),Tileset.WALL);
           insert(new Position(p.px-1,p.py+1),Tileset.WALL);
           insert(new Position(p.px,p.py+1),Tileset.WALL);
           insert(new Position(p.px+1,p.py+1),Tileset.WALL);
           insert(new Position(p.px+1,p.py),Tileset.WALL);
           insert(new Position(p.px+1,p.py-1),Tileset.WALL);
           insert(new Position(p.px,p.py-1),Tileset.WALL);
           insert(new Position(p.px-1,p.py-1),Tileset.WALL);
       }

       public void GenerateMap(){
           RenderInitial();
           RenderClear();

           for(int i=0;i<30;i++){
               Position beginpoint1=new Position(15+i,20);
               Tiles[15+i][20]=Tileset.FLOOR;
               isFull[15+i][20]=1;
               ListOfFloor.add(beginpoint1);
           }

           for(int i=0;i<20;i++){
               Position beginpoint1=new Position(30,10+i);
               Tiles[30][10+i]=Tileset.FLOOR;
               isFull[30][10+i]=1;
               ListOfFloor.add(beginpoint1);
           }

           for(int i=0;i<500;i++){
              for(Position p:ListOfFloor){
                  if(RANDOM2.nextInt(100)>30&&p.timesofvisit<2){
                      RandomFloorExpand(p);
                  }
              }
              for(Position ToAdd: FloorToBeAdded){
                  ListOfFloor.add(ToAdd);
              }
              FloorToBeAdded.clear();
           }

           for(Position p:ListOfFloor){
               BuildWalls(p);
           }

           ter.renderFrame(Tiles);

           for(int i=0;i<width;i+=1){
               for(int j=0;j<height;j+=1){
                   isFull[i][j]=0;
               }
           }
           ListOfFloor.clear();
       }
}
