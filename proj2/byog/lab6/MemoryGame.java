package byog.lab6;

import edu.princeton.cs.introcs.StdDraw;

import java.awt.Color;
import java.awt.Font;
import java.util.Arrays;
import java.util.Random;

public class MemoryGame {
    private int width;
    private int height;
    private int round;
    private Random rand;
    private boolean gameOver;
    private boolean playerTurn;
    private static final char[] CHARACTERS = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    private static final String[] ENCOURAGEMENT = {"You can do this!", "I believe in you!",
                                                   "You got this!", "You're a star!", "Go Bears!",
                                                   "Too easy for you!", "Wow, so impressive!"};

    public static void main(String[] args){
        if (args.length < 1) {
            System.out.println("Please enter a seed");
            return;
        }

        int seed = Integer.parseInt(args[0]);
        MemoryGame game = new MemoryGame(40, 40,seed);
        game.startGame();
    }

    public MemoryGame(int width, int height,int seed) {
        /* Sets up StdDraw so that it has a width by height grid of 16 by 16 squares as its canvas
         * Also sets up the scale so the top left is (0,0) and the bottom right is (width, height)
         */
        this.width = width;
        this.height = height;
        StdDraw.setCanvasSize(this.width * 16, this.height * 16);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, this.width);
        StdDraw.setYscale(0, this.height);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();

        //TODO: Initialize random number generator
        rand=new Random(seed);
        gameOver=false;
    }

    public String generateRandomString(int n) {
        //TODO: Generate random string of letters of length n
        char[] chararray=new char[n];
        for(int i=0;i<n;i++){
           int index=rand.nextInt(26);
            chararray[i]=CHARACTERS[index];
        }
        String str=new String(chararray);
        return str;
    }

    public void drawFrame(String s) {
        StdDraw.clear(Color.BLACK);
        //TODO: If game is not over, display relevant game information at the top of the screen
        if(!gameOver){
            StdDraw.setPenColor(StdDraw.WHITE);
            StdDraw.setFont(new Font("Arial",Font.PLAIN,15));
            StdDraw.text((int)(width/2),height-3,"CS61B Lab6 MemoryGame by Gary Agasa");
            StdDraw.picture(width-3,height-3,"E:\\CSdiy\\CS61B\\skeleton-sp18\\proj2\\byog\\president.jpg",4, 4);
            StdDraw.text((int)(width/2),3,"Round "+round);
        }
        //TODO: Take the string and display it in the center of the screen
        StdDraw.setPenColor(StdDraw.YELLOW);
        StdDraw.setFont(new Font("Times new Roman",Font.BOLD,35));
        StdDraw.text((int)(width/2),(int)(height/2),s);

        StdDraw.show();
        StdDraw.pause(500);
    }

    public void flashSequence(String letters){
        //TODO: Display each character in letters, making sure to blank the screen between letters
        for(int i=0;i<letters.length();i+=1){
            String DisplayString=letters.substring(i,i+1);
            drawFrame(DisplayString);
            StdDraw.pause(1000);

            StdDraw.show();
        }
    }

    public String solicitNCharsInput(int n) {
        //TODO: Read n letters of player input
        int i=0;
        char[] chararray=new char[n];

        while(i<n){
            if(StdDraw.hasNextKeyTyped()){
                chararray[i]=StdDraw.nextKeyTyped();
                i+=1;
            }
            drawFrame(new String(chararray,0,i));
        }
        StdDraw.pause(500);
        return new String(chararray);
    }

    public void startGame(){
        //TODO: Set any relevant variables before the game starts
         round=1;
        //TODO: Establish Game loop
        for(;round<10;round+=1) {
            drawFrame("Round:"+round);
            String randomstring = generateRandomString(round);

            flashSequence(randomstring);
            String answer = solicitNCharsInput(round);
            if (!answer.equals(randomstring)){
                gameOver=true;
                drawFrame("Game Over! You made it to Round"+round);
                return;
            }
        }
        drawFrame("Congratulations!All rounds passed!");
    }

}
