package aurumvorax.arcturus;

import org.junit.Test;

public class IntBubbleArrayTest{

    @Test
    public void test() throws Exception{
        IntBubbleArray array = new IntBubbleArray();
        array.add(37);
        array.add(45);
        array.add(22);
        array.add(17);
        array.add(67);
        assert(array.size() == 5);
        assert(array.get(1) == 22);
        assert(array.get(4) == 67);
        array.remove(37);
        assert(array.size() == 4);
        assert(array.get(1) == 22);
        assert(array.get(3) == 67);
    }
}