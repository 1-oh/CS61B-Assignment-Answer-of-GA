package byog.Core;

import byog.TileEngine.TERenderer;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.Color;
import java.awt.Font;
import java.io.*;
import java.util.Arrays;
import java.util.Random;

public class GameUI {
    private static int width;
    private static int height;
    private static long seed;
    public static  boolean gameOver;
    private static boolean playerTurn;
    private static final Font mini=new Font("Arial",Font.PLAIN,15);
    private static final Font sign=new Font("Arial",Font.PLAIN,20);
    private static final Font ordinary=new Font("Times new roman", Font.BOLD, 30);
    private static final Font mark=new Font("Times new roman",Font.BOLD,40);

    public GameUI(int Width, int Height) {
        /* Sets up StdDraw so that it has a width by height grid of 16 by 16 squares as its canvas
         * Also sets up the scale so the top left is (0,0) and the bottom right is (width, height)
         */
        width = Width;
        height = Height;
        StdDraw.setCanvasSize(this.width * 16, this.height * 16);
        StdDraw.setFont(ordinary);
        StdDraw.setXscale(0, width);
        StdDraw.setYscale(0, height);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();

        gameOver=false;
    }


    public static void menu(){
        StdDraw.setPenColor(Color.WHITE);
        drawFrame("The Game:Bear VS Frog",width/2,3*height/4,mark);

        drawFrame("New Game  (N)",width/2,height/2,sign);
        drawFrame("Load Game (L)",width/2,height/2-2,sign);
        drawFrame("Quit      (Q)",width/2,height/2-4,sign);
        StdDraw.show();
        StdDraw.pause(1000);
    }

    //ToDo:initialize the random number
    public static void SeedInput(){
        long ret=0;
        NormalDrawFrame("Please input the seed!");
        StdDraw.show();
        while(!StdDraw.hasNextKeyTyped()){};
        while(true){
            while(!StdDraw.hasNextKeyTyped()){};
            char CurrentChar=StdDraw.nextKeyTyped();
            if(CurrentChar<='9' && CurrentChar>='0'){
                ret*=10;
                ret+=CurrentChar-'0';
                NormalDrawFrame(((Long)ret).toString());
                drawFrame("Inputing the seed...Input 's' to finish.",width/2,height-3,mini);
                StdDraw.show();
            }
            else if(CurrentChar=='s'||CurrentChar=='S') break;
        }
        seed=ret;
    }

    public static void drawFrame(String s,int px,int py,Font FontToUse) {
        //ToDo:Show the string on the (px,py)
        /**The function would not clear previous graphs*/
        decoration();
        StdDraw.setFont(FontToUse);
        StdDraw.text(px,py,s);
    }

    public static void NormalDrawFrame(String s){
        /**The function will clear previous graphs and put the string on the middle*/
        StdDraw.clear(StdDraw.BLACK);
        drawFrame(s,width/2,height/2,ordinary);
    }

    public static void decoration(){
        //The decorations
        StdDraw.setFont(mini);
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.text((int)(width/2),4,"CS61B Project2");
        StdDraw.picture(width-3,height-3,"E:\\CSdiy\\CS61B\\skeleton-sp18\\proj2\\byog\\Jiang.jpg",4, 4);
        StdDraw.picture(3,height-3,"E:\\CSdiy\\CS61B\\skeleton-sp18\\proj2\\byog\\xi.jpg",4, 4);
        StdDraw.text((int)(width/2),2,"2025 Gary Agasa all rights reserved");
    }

    public static void DrawPlayerInformation(){
        if(newMapGenerator.Bear.PoliticLife>0){
            drawFrame("Politic Life remains:"+newMapGenerator.Bear.PoliticLife.toString(),12,height-10,sign);
            drawFrame("Status:"+newMapGenerator.Bear.status.toString(),12,height-12,sign);
            for(int i=0;i<newMapGenerator.Bear.status;i+=1){
                drawFrame(newMapGenerator.Bear.experiences.get(i),12,height-14-2*i,sign);
            }
        }

        if(newMapGenerator.Frog.PoliticLife>0){
            drawFrame("Politic Life remains:"+newMapGenerator.Frog.PoliticLife.toString(),width-12,height-10,sign);
            drawFrame("Status:"+newMapGenerator.Frog.status.toString(),width-12,height-12,sign);
            for(int i=0;i<newMapGenerator.Frog.status;i+=1){
                drawFrame(newMapGenerator.Frog.experiences.get(i),width-12,height-14-2*i,sign);
            }
        }

    }

    public static void MousePause(){
        if(StdDraw.isMousePressed()){
            int sign=newMapGenerator.isFull[(int)StdDraw.mouseX()][(int)StdDraw.mouseY()];
            String what="";
            switch (sign) {
                case 1: what="Floor";break;
                case 2: what="Wall";break;
                case 3: what="Frog";break;
                case 4: what="Bear";break;
                case 5: what="Place to progress";break;
                default: what="";
            };
            drawFrame(what,width/2,height-3,mini);
            StdDraw.show();
        }
    }

    public static void GamePlaying(int pattern,String inputString,TERenderer ter,newMapGenerator map) throws IOException {
        int IndexOfOperation=0;

        while(true){
            char mov=' ';

            if(pattern==1){
                while(!StdDraw.hasNextKeyTyped()){
                    MousePause();
                };
                mov=StdDraw.nextKeyTyped();
            }
            else if(pattern==0){
                if(IndexOfOperation>=inputString.length()) return;
                mov=inputString.charAt(IndexOfOperation);
                IndexOfOperation+=1;
            }

            if(mov==':'){
                char isquit=' ';
                if(pattern==1){
                    while (!StdDraw.hasNextKeyTyped()){
                        MousePause();
                    };
                    isquit=StdDraw.nextKeyTyped();
                }
               else if(pattern==0){
                   isquit=inputString.charAt(IndexOfOperation);
                   IndexOfOperation+=1;
                }
                if(isquit=='q'||isquit=='Q'){
                    ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("savefile.txt"));
                    oos.writeObject(map);
                    oos.close();
                    StdDraw.clear(StdDraw.BLACK);
                    NormalDrawFrame("Successfully saved!Please exit.See you again!");
                    StdDraw.show();
                    return;
                }
            }

            if(mov=='a'||mov=='w'||mov=='s'||mov=='d'||
                    mov=='A'||mov=='W'||mov=='S'||mov=='D'){
                newMapGenerator.movePlayer(newMapGenerator.Bear,mov);
                ter.renderFrame(newMapGenerator.getTileArray());

                DrawPlayerInformation();
                StdDraw.show();
            }
            else if(mov=='j'||mov=='k'||mov=='l'||mov=='i'||
                    mov=='J'||mov=='K'||mov=='L'||mov=='I'){
                newMapGenerator.movePlayer(newMapGenerator.Frog,mov);
                ter.renderFrame(newMapGenerator.getTileArray());

                DrawPlayerInformation();
                StdDraw.show();
            }

            if(pattern==0) StdDraw.pause(500);
        }
    }


    public static void StartNewGame(int pattern,String inputString) throws IOException {
        /**0 for playWithString,1 for play with KeyBoard*/
        TERenderer ter = new TERenderer();
        ter.initialize(width, height);
        newMapGenerator map = new newMapGenerator(width, height, seed);
        newMapGenerator.MapGenerate();

        ter.renderFrame(newMapGenerator.getTileArray());
        StdDraw.pause(1000);

        GamePlaying(pattern, inputString, ter, map);
    }

    public static void LoadOldGame(int pattern,String Operation) throws IOException, ClassNotFoundException {
        TERenderer ter = new TERenderer();
        ter.initialize(width, height);

        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("savefile.txt"));
        newMapGenerator map= (newMapGenerator) ois.readObject();

        ter.renderFrame(newMapGenerator.getTileArray());
        StdDraw.pause(1000);

        GamePlaying(pattern,Operation, ter, map);
    }

}
