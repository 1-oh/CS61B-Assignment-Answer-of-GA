package byog.Core;

import byog.Phase1.MapGenerator;
import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;

import org.junit.Test;

import javax.imageio.IIOException;
import java.awt.Color;
import java.awt.Font;

import java.io.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Game {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 100;
    public static final int HEIGHT = 60;

    /**
     * Method used for playing a fresh game. The game should start from the main
     * menu.
     */

    public void playWithKeyboard() {
        ter.initialize(WIDTH, HEIGHT);
        GameUI ui = new GameUI(WIDTH, HEIGHT);
        GameUI.menu();
        while (!StdDraw.hasNextKeyTyped()) {
        }
        ;
        if (StdDraw.hasNextKeyTyped()) {
            char choice = StdDraw.nextKeyTyped();
            switch (choice) {
                case 'N':
                case 'n':
                    GameUI.SeedInput();
                    GameUI.StartNewGame(1, "");
                    break;
                case 'Q':
                case 'q':
                    break;
                case 'L':
                case 'l':
                    GameUI.LoadOldGame(1, "");
                    break;
                default:
                    playWithKeyboard();
            }
        }
    }

    /**
     * Method used for autograding and testing the game code. The input string will
     * be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The
     * game should
     * behave exactly as if the user typed these characters into the game after
     * playing
     * playWithKeyboard. If the string ends in ":q", the same world should be
     * returned as if the
     * string did not end with q. For example "n123sss" and "n123sss:q" should
     * return the same
     * world. However, the behavior is slightly different. After playing with
     * "n123sss:q", the game
     * should save, and thus if we then called playWithInputString with the string
     * "l", we'd expect
     * to get the exact same world back again, since this corresponds to loading the
     * saved game.
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] playWithInputString(String input) {
        // TODO: Fill out this method to run the game using the input passed in,
        // and return a 2D tile representation of the world that would have been
        // drawn if the same inputs had been given to playWithKeyboard().

        char mode = input.charAt(0);

        if (mode == 'N' || mode == 'n') {
            int endofseed;
            for (endofseed = 1; endofseed < input.length(); endofseed += 1) {
                if (input.charAt(endofseed) == 'S' || input.charAt(endofseed) == 's')
                    break;
            }
            long newseed = 0;
            for (int i = 1; i < endofseed; i+=1) {
                newseed *= 10;
                newseed += input.charAt(i) - '0';
            }
            GameUI ui = new GameUI(WIDTH, HEIGHT);
            newMapGenerator map = new newMapGenerator(WIDTH, HEIGHT, newseed);

            String operation = input.substring(endofseed + 1);
            GameUI.StartNewGame(0, operation);

        } else if (mode == 'L' || mode == 'l') {
            GameUI ui = new GameUI(WIDTH, HEIGHT);
            GameUI.LoadOldGame(0, input.substring(1));
        }
        return newMapGenerator.getTileArray();
    }

}
