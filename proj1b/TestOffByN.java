import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.*;

public class TestOffByN{
    @Test
    public void equalChars(){
        OffByN test1=new OffByN(3);
        assertTrue(test1.equalChars('a','d'));
        assertTrue(test1.equalChars('e','b'));
        OffByN test2=new OffByN(2);
        assertFalse(test2.equalChars('a','a'));
    }
}
