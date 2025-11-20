package deque;

import org.junit.Test;
import java.util.Comparator;
import static org.junit.Assert.*;

public class MaxArrayDequeTest {
    @Test
    public void Comparator1Test()
    {
        comparator1 cmp = new comparator1();
        MaxArrayDeque<Integer> IntArray = new MaxArrayDeque<>(cmp);
        for (int i = 0 ; i < 500; i++)
        {
            IntArray.addLast(i);
        }
        int MaxInt = IntArray.max();
        assertEquals(499, MaxInt);
    }

    @Test
    public void Comparator2Test()
    {
        comparator2 cmp = new comparator2();
        MaxArrayDeque<String> StringArray = new MaxArrayDeque<>(cmp);
        StringArray.addLast("a");
        StringArray.addLast("b");
        StringArray.addLast("c");
        StringArray.addLast("d");
        StringArray.addLast("e");
        StringArray.addLast("woshinibaba");
        String MaxString = StringArray.max();
        assertEquals("woshinibaba", MaxString);
    }

    @Test
    public void Comparator3Test()
    {
        comparator3 cmp = new comparator3();
        MaxArrayDeque<Float> FloatArray = new MaxArrayDeque<>(cmp);
        FloatArray.addLast(0.0f);
        FloatArray.addLast(0.1f);
        FloatArray.addLast(0.2f);
        FloatArray.addLast(0.3f);
        FloatArray.addLast(0.4f);
        float MaxFloat =  FloatArray.max();
        assertEquals(0.1f, MaxFloat);
    }


    private static class comparator1  implements Comparator<Integer>
    {
        public int compare(Integer a, Integer b)
        {
            return a.compareTo(b);
        }
    }

    private static class comparator2  implements Comparator<String>
    {
        public int compare(String a, String b)
        {
            return a.compareTo(b);
        }
    }

    private static class comparator3  implements Comparator<Float>
    {
        public int compare(Float a, Float b)
        {
            return a.compareTo(b);
        }
    }
}








