import deque.Deque;
import deque.ArrayDeque;
import deque.LinkedListDeque;
import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;

public class ArrayDequeTest {

    @Test
    /** In this test, we have three different assert statements that verify that addFirst works correctly. */
    public void TestIterable() {

        //To test if Iterable in ArrayDeque is correctly implemented!!!

        Deque<String> lld1 = new ArrayDeque<>();

        lld1.addLast("front");
        lld1.addLast("middle");
        lld1.addLast("back");
        for (Object s : lld1) {
            System.out.println(s);
        }
    }

    @Test
    public void testEqualDeques() {

        //To test if equals() method in ArrayDeque is correctly implemented

        Deque<String> lld1 = new ArrayDeque<>();
        Deque<String> lld2 = new LinkedListDeque<>();

        lld1.addLast("front");
        lld1.addLast("middle");
        lld1.addLast("back");

        lld2.addLast("front");
        lld2.addLast("middle");
        lld2.addLast("back");

        assertThat(lld1.equals(lld2)).isEqualTo(true);
    }

    @Test
    public void testToString() {

        //To test if toString method is correctly implemented

        Deque<String> lld1 = new ArrayDeque<>();

        lld1.addLast("front");
        lld1.addLast("middle");
        lld1.addLast("back");

        System.out.println(lld1);
    }
}
