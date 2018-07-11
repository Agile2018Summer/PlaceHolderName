import edu.harvard.integration.Trello.Commons;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CommonTest {
    @Test
    public void testConcat(){
        String[] ss = {"a", "bc", "def"};
        String res = Commons.concatArr(ss);
        assertEquals("a bc def", res);
    }

    @Test
    public void testConcat2(){
        String[] ss = {};
        String res = Commons.concatArr(ss);
        assertEquals("", res);
    }
}
