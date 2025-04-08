package byog.lab6;
import edu.princeton.cs.introcs.StdDraw;
import org.junit.Test;

import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.util.Random;

public class TestMemoryGame {

    @Test
    public void testDrawAFrame() throws IOException {
        MemoryGame test=new MemoryGame(30,30,38247455);
        test.drawFrame(test.generateRandomString(5));
        System.in.read();
    }

    @Test
    public void testflashSequence() throws IOException, InterruptedException {
        MemoryGame test=new MemoryGame(40,40,121324);
        test.flashSequence("President Jiang 1926");
        System.in.read();
    }

    @Test
    public void testsolicitNCharsInput(){
        MemoryGame test=new MemoryGame(40,40,121324);
        test.solicitNCharsInput(7);
    }

    public static void main(String[] args){
        MemoryGame test=new MemoryGame(40,40,121324);
        test.solicitNCharsInput(20);
    }
}
