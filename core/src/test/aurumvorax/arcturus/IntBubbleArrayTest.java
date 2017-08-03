package aurumvorax.arcturus;

import org.junit.Test;

public class IntBubbleArrayTest{

    @Test
    public void defaultTest() throws Exception{
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

    @Test
    public void comparatorTest() throws Exception{

        float values[] = {2.4f, 1.7f, 9.5f, 2.4f, 6.5f};

        IntBubbleArray array = new IntBubbleArray(new IDComparator(){
            @Override
            public int compare(int a, int b){ return Float.compare(values[a], values[b]); }
            @Override public boolean equals(int a, int b){ return (a == b); }
        });

        array.add(0);
        array.add(1);
        array.add(2);
        array.add(3);
        array.add(4);
        assert(array.size() == 5);
        assert(array.get(0) == 1);
        assert(array.get(4) == 2);
    }
}