import org.junit.jupiter.api.*;
import deque.Deque;
import deque.ArrayDeque;
import deque.LinkedListDeque;

import java.util.Comparator;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;

public class MaxArrayDequeTest {
    public static class Comparator0<Et> implements Comparator<Et> {

        @Override
        public int compare(Et o1, Et o2) {

            return 0;
        }
    }

}
