package randomizedtest;

import org.junit.Test;
import static org.junit.Assert.*;
import edu.princeton.cs.algs4.StdRandom;


public class testThreeAddThreeRemove {
    @Test
    public void testThreeAddThreeRemove_() {
        AListNoResizing<Integer> correct = new AListNoResizing<>();
        BuggyAList<Integer> broken = new BuggyAList<>();

        correct.addLast(5);
        correct.addLast(10);
        correct.addLast(15);

        broken.addLast(5);
        broken.addLast(10);
        broken.addLast(15);

        assertEquals(correct.size(), broken.size());

        assertEquals(correct.removeLast(), broken.removeLast());
        assertEquals(correct.removeLast(), broken.removeLast());
        assertEquals(correct.removeLast(), broken.removeLast());
    }

    @Test
    public void randomizedTest()
    {
        AListNoResizing<Integer> L = new AListNoResizing<>();
        BuggyAList<Integer> broken = new BuggyAList<>();

        int N = 5000;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 4);
            if (operationNumber == 0) {
                // addLast
                int randVal = StdRandom.uniform(0, 100);
                L.addLast(randVal);
                broken.addLast(randVal);
                System.out.println("addLast(" + randVal + ")");
            }
            else if (operationNumber == 1) {
                // size
                int Lsize = L.size();
                int Bsize = broken.size();
                assertEquals(Lsize, Bsize);
                System.out.println("size: " + Lsize);
            }
            else if (operationNumber == 2)
            {
                //  getLast
                if (L.size() <= 1) continue;
                if (broken.size() <= 1) continue;
                int LastNum = L.getLast();
                int BLastNum = broken.getLast();
                assertEquals(LastNum, BLastNum);
                System.out.println("LastNum: " + LastNum);
            }
            else if (operationNumber == 3)
            {
                if (L.size() <= 1) continue;
                if (broken.size() <= 1) continue;
                int LastDeleted = L.removeLast();
                int BLastDeleted = broken.removeLast();
                assertEquals(LastDeleted, BLastDeleted);
                System.out.println("LastDeleted: " + LastDeleted);
            }


        }
    }
}
