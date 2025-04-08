package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import byog.lab5.Position;
import edu.princeton.cs.introcs.StdDraw;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class newMapGenerator implements Serializable {
    private static int width;
    private static int height;
    private static int margin;

    private static Random RANDOM;
    private static Random RANDOM2;
    public static boolean gameOver;

    private static TERenderer ter = new TERenderer();
    private static TETile[][] Tiles;
    public static int[][] isFull;
    /** 0 for nothing,1 for floors and 2 for walls,3 for frog,4 for bear,5 for
     *communism*/
    /** Consider the transpose of isFull */
    public static Player Frog;
    public static Player Bear;

    private static List<Position> ListOfFloor = new ArrayList<>();
    private static List<Position> FloorToBeAdded = new ArrayList<>();

    public newMapGenerator(int Width, int Height, long seed) {
        /*
         * Sets up StdDraw so that it has a width by height grid of 16 by 16 squares as
         * its canvas
         * Also sets up the scale so the top left is (0,0) and the bottom right is
         * (width, height)
         */
        width = Width;
        height = Height;
        margin = 10;
        Tiles = new TETile[width][height];
        isFull = new int[width][height];

        RANDOM = new Random(seed);
        RANDOM2 = new Random(seed);
        gameOver = false;
        
    }

    /*-------------------------The part for Serializable(Saving the current situation of the class(Begin)-------------------*/
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeInt(width);
        out.writeInt(height);
        out.writeInt(margin);

        out.writeObject(RANDOM);
        out.writeObject(RANDOM2);
        out.writeObject(isFull);
        // out.writeObject(ter);
        // out.writeObject(Tiles);
        out.writeObject(gameOver);

        out.writeInt(Frog.x);
        out.writeInt(Frog.y);
        out.writeInt(Frog.PoliticLife);
        out.writeObject(Frog.experiences);
        out.writeObject(Frog.Job);
        out.writeInt(Frog.status);
        out.writeObject(Frog.random);

        out.writeInt(Bear.x);
        out.writeInt(Bear.y);
        out.writeInt(Bear.PoliticLife);
        out.writeObject(Bear.experiences);
        out.writeObject(Bear.Job);
        out.writeInt(Bear.status);
        out.writeObject(Bear.random);
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        width = in.readInt();
        height = in.readInt();
        margin = in.readInt();

        RANDOM = (Random) in.readObject();
        RANDOM2 = (Random) in.readObject();
        isFull = (int[][]) in.readObject();
        // ter=(TERenderer) in.readObject();
        ter.initialize(width, height);
        Tiles = new TETile[width][height];
        for (int i = 0; i < width; i += 1) {
            for (int j = 0; j < height; j += 1) {
                switch (isFull[i][j]) {
                    case 0:
                        Tiles[i][j] = Tileset.NOTHING;
                        break;
                    case 1:
                        Tiles[i][j] = Tileset.FLOOR;
                        break;
                    case 2:
                        Tiles[i][j] = Tileset.WALL;
                        break;
                    case 3:
                        Tiles[i][j] = Tileset.FROG;
                        break;
                    case 4:
                        Tiles[i][j] = Tileset.BEAR;
                        break;
                    case 5:
                        Tiles[i][j] = Tileset.COMMUNISM;
                }
            }
        }
        gameOver = (boolean) in.readObject();

        Frog = new Player(in.readInt(), in.readInt());
        Frog.pos = new Position(Frog.x, Frog.y);
        Frog.PoliticLife = in.readInt();
        Frog.experiences = (List<String>) in.readObject();
        Frog.Job = (String) in.readObject();
        Frog.status = in.readInt();
        Frog.random = (Random) in.readObject();

        Bear = new Player(in.readInt(), in.readInt());
        Bear.pos = new Position(Bear.x, Bear.y);
        Bear.PoliticLife = in.readInt();
        Bear.experiences = (List<String>) in.readObject();
        Bear.Job = (String) in.readObject();
        Bear.status = in.readInt();
        Bear.random = (Random) in.readObject();
    }
    /*-------------------------The part for Serializable(Saving the current situation of the class(End)-------------------*/

    public static TETile[][] getTileArray() {
        return Tiles;
    }

    public static void movePlayer(Player p, char direction) {
        /* Move the thing on the position given in some direction */
        p.PoliticLife -= 1;
        switch (direction) {
            case 'a':
            case 'A':
            case 'j':
            case 'J':
                if (isFull[p.x - 1][p.y] == 1 || isFull[p.x - 1][p.y] == 5) {
                    if (isFull[p.x - 1][p.y] == 5) {
                        p.upgrade();
                    }
                    Tiles[p.x - 1][p.y] = Tiles[p.x][p.y];
                    Tiles[p.x][p.y] = Tileset.FLOOR;
                    isFull[p.x - 1][p.y] = isFull[p.x][p.y];
                    isFull[p.x][p.y] = 1;

                    p.pos = new Position(p.x - 1, p.y);
                    p.x -= 1;
                }
                break;
            case 'w':
            case 'W':
            case 'i':
            case 'I':
                if (isFull[p.x][p.y + 1] == 1 || isFull[p.x][p.y + 1] == 5) {
                    if (isFull[p.x][p.y + 1] == 5) {
                        p.upgrade();
                    }
                    Tiles[p.x][p.y + 1] = Tiles[p.x][p.y];
                    Tiles[p.x][p.y] = Tileset.FLOOR;
                    isFull[p.x][p.y + 1] = isFull[p.x][p.y];
                    isFull[p.x][p.y] = 1;

                    p.pos = new Position(p.x, p.y + 1);
                    p.y += 1;

                }
                break;
            case 'd':
            case 'D':
            case 'l':
            case 'L':
                if (isFull[p.x + 1][p.y] == 1 || isFull[p.x + 1][p.y] == 5) {
                    if (isFull[p.x + 1][p.y] == 5) {
                        p.upgrade();
                    }
                    Tiles[p.x + 1][p.y] = Tiles[p.x][p.y];
                    Tiles[p.x][p.y] = Tileset.FLOOR;
                    isFull[p.x + 1][p.y] = isFull[p.x][p.y];
                    isFull[p.x][p.y] = 1;
                    p.pos = new Position(p.x + 1, p.y);

                    p.x += 1;
                }
                break;
            case 's':
            case 'S':
            case 'k':
            case 'K':
                if (isFull[p.x][p.y - 1] == 1 || isFull[p.x][p.y - 1] == 5) {
                    if (isFull[p.x][p.y - 1] == 5) {
                        p.upgrade();
                    }
                    Tiles[p.x][p.y - 1] = Tiles[p.x][p.y];
                    Tiles[p.x][p.y] = Tileset.FLOOR;
                    isFull[p.x][p.y - 1] = isFull[p.x][p.y];
                    isFull[p.x][p.y] = 1;

                    p.pos = new Position(p.x, p.y - 1);
                    p.y -= 1;
                }
                break;
        }
    }

    private static void TilesClear() {
        for (int i = 0; i < width; i += 1) {
            for (int j = 0; j < height; j += 1) {
                Tiles[i][j] = Tileset.NOTHING;
                isFull[i][j] = 0;
            }
        }
    }

    private static boolean isAnything(Position p) {
        return isFull[p.px][p.py] > 0;
    }

    private static boolean isOnBoundary(Position p) {
        return p.px == margin || p.py == margin || p.px == width - 1 - margin || p.py == height - 1 - margin;
    }

    private static boolean insert(Position p, TETile t) {
        /**
         * Return True if the insert is correct and insert the tile
         * otherwise return False
         */
        if (p.px < margin || p.px >= width - margin || p.py < margin || p.py >= height - margin)
            return false;
        if (isAnything(p)) {
            return false;
        }
        Tiles[p.px][p.py] = t;
        if (t == Tileset.FLOOR)
            isFull[p.px][p.py] = 1;
        if (t == Tileset.WALL)
            isFull[p.px][p.py] = 2;
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
        } else if (direction < 9) {
            ExpandPositon.px = p.px + 1;
            ExpandPositon.py = p.py;
        } else {
            ExpandPositon.px = p.px;
            ExpandPositon.py = p.py - 1;
        }

        if (!isOnBoundary(ExpandPositon)) {
            boolean jud = insert(ExpandPositon, Tileset.FLOOR);
            if (jud) {
                FloorToBeAdded.add(ExpandPositon);
                ExpandPositon.timesofvisit = 0;
            }
        }
        p.timesofvisit += 1;
    }

    private static void BuildWalls(Position p) {
        insert(new Position(p.px - 1, p.py), Tileset.WALL);
        insert(new Position(p.px - 1, p.py + 1), Tileset.WALL);
        insert(new Position(p.px, p.py + 1), Tileset.WALL);
        insert(new Position(p.px + 1, p.py + 1), Tileset.WALL);
        insert(new Position(p.px + 1, p.py), Tileset.WALL);
        insert(new Position(p.px + 1, p.py - 1), Tileset.WALL);
        insert(new Position(p.px, p.py - 1), Tileset.WALL);
        insert(new Position(p.px - 1, p.py - 1), Tileset.WALL);
    }

    public static void Skeleton() {
        for (int i = 0; i < width / 2 - margin; i++) {
            Position beginpoint1 = new Position(width / 4 + margin / 2 + i, height / 2);
            Tiles[width / 4 + margin / 2 + i][height / 2] = Tileset.FLOOR;
            isFull[width / 4 + margin / 2 + i][height / 2] = 1;
            ListOfFloor.add(beginpoint1);
        }
        for (int i = 0; i < height / 2 - margin; i++) {
            Position beginpoint2 = new Position(width / 2, height / 4 + margin / 2 + i);
            Tiles[width / 2][height / 4 + margin / 2 + i] = Tileset.FLOOR;
            isFull[width / 2][height / 4 + margin / 2 + i] = 1;
            ListOfFloor.add(beginpoint2);
        }
    }

    public static void EndCleaning() {
        for (int i = 0; i < width; i += 1) {
            for (int j = 0; j < height; j += 1) {
                isFull[i][j] = 0;
            }
        }
        ListOfFloor.clear();
    }

    public static void MapGenerate() {
        TilesClear();
        Skeleton();

        /** Build the floors and make sure they're connected */
        for (int i = 0; i < width * height / 7; i++) {
            for (Position p : ListOfFloor) {
                if (RANDOM2.nextInt(100) > 30 && p.timesofvisit < 2) {
                    RandomFloorExpand(p);
                }
            }
            ListOfFloor.addAll(FloorToBeAdded);
            FloorToBeAdded.clear();
        }

        /** Build the walls for the floor */
        for (Position p : ListOfFloor) {
            BuildWalls(p);
        }

        /** Build players */
        int FrogIndex = RANDOM.nextInt(ListOfFloor.size());
        Frog = new Player(ListOfFloor.get(FrogIndex));
        Tiles[Frog.x][Frog.y] = Tileset.FLOWER;
        isFull[Frog.x][Frog.y] = 3;
        ListOfFloor.remove(FrogIndex);

        

        int BearIndex = RANDOM.nextInt(ListOfFloor.size());
        Bear = new Player(ListOfFloor.get(BearIndex));
        Tiles[Bear.x][Bear.y] = Tileset.GRASS;
        isFull[Bear.x][Bear.y] = 4;
        ListOfFloor.remove(BearIndex);

        /** Build the award(Communism) */
        for (int i = 0; i < 10; i += 1) {
            int AwardIndex = RANDOM.nextInt(ListOfFloor.size());
            Position AwardPos = ListOfFloor.get(AwardIndex);
            Tiles[AwardPos.px][AwardPos.py] = Tileset.COMMUNISM;
            isFull[AwardPos.px][AwardPos.py] = 5;
            ListOfFloor.remove(AwardIndex);
        }
        ListOfFloor.clear();
    }
}
