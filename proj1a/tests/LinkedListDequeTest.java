import jh61b.utils.Reflection;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;

/** Performs some basic linked list tests. */
public class LinkedListDequeTest {

     @Test
     @DisplayName("LinkedListDeque has no fields besides nodes and primitives")
     void noNonTrivialFields() {
         Class<?> nodeClass = NodeChecker.getNodeClass(LinkedListDeque.class, true);
         List<Field> badFields = Reflection.getFields(LinkedListDeque.class)
                 .filter(f -> !(f.getType().isPrimitive() || f.getType().equals(nodeClass) || f.isSynthetic()))
                 .toList();

         assertWithMessage("Found fields that are not nodes or primitives").that(badFields).isEmpty();
     }

     @Test
     /** In this test, we have three different assert statements that verify that addFirst works correctly. */
     public void addFirstTestBasic() {
         Deque<String> lld1 = new LinkedListDeque<>();

         lld1.addFirst("back"); // after this call we expect: ["back"]
         assertThat(lld1.toList()).containsExactly("back").inOrder();

         lld1.addFirst("middle"); // after this call we expect: ["middle", "back"]
         assertThat(lld1.toList()).containsExactly("middle", "back").inOrder();

         lld1.addFirst("front"); // after this call we expect: ["front", "middle", "back"]
         assertThat(lld1.toList()).containsExactly("front", "middle", "back").inOrder();

         /* Note: The first two assertThat statements aren't really necessary. For example, it's hard
            to imagine a bug in your code that would lead to ["front"] and ["front", "middle"] failing,
            but not ["front", "middle", "back"].
          */
     }

     @Test
     /** In this test, we use only one assertThat statement. IMO this test is just as good as addFirstTestBasic.
      *  In other words, the tedious work of adding the extra assertThat statements isn't worth it. */
     public void addLastTestBasic() {
         Deque<String> lld1 = new LinkedListDeque<>();

         lld1.addLast("front"); // after this call we expect: ["front"]
         lld1.addLast("middle"); // after this call we expect: ["front", "middle"]
         lld1.addLast("back"); // after this call we expect: ["front", "middle", "back"]
         assertThat(lld1.toList()).containsExactly("front", "middle", "back").inOrder();
     }

     @Test
     /** This test performs interspersed addFirst and addLast calls. */
     public void addFirstAndAddLastTest() {
         Deque<Integer> lld1 = new LinkedListDeque<>();

         /* I've decided to add in comments the state after each call for the convenience of the
            person reading this test. Some programmers might consider this excessively verbose. */
         lld1.addLast(0);   // [0]
         lld1.addLast(1);   // [0, 1]
         lld1.addFirst(-1); // [-1, 0, 1]
         lld1.addLast(2);   // [-1, 0, 1, 2]
         lld1.addFirst(-2); // [-2, -1, 0, 1, 2]

         assertThat(lld1.toList()).containsExactly(-2, -1, 0, 1, 2).inOrder();
     }

    //Below, you'll write your own tests for LinkedListDeque.
    @Test
    /** This test performs get calls. */
    public void getTest() {
        Deque<Integer> lld1 = new LinkedListDeque<>();

        lld1.addLast(0);   // [0]
        lld1.addLast(1);   // [0, 1]
        lld1.addFirst(-1); // [-1, 0, 1]
        lld1.addLast(2);   // [-1, 0, 1, 2]
        lld1.addFirst(-2); // [-2, -1, 0, 1, 2]

        assertWithMessage("first item should be -2").that(lld1.get(0)).isEqualTo(-2);
        assertWithMessage("second item should be -1").that(lld1.get(1)).isEqualTo(-1);
        assertWithMessage("third item should be 0").that(lld1.get(2)).isEqualTo(0);
        assertWithMessage("fourth item should be 1").that(lld1.get(3)).isEqualTo(1);
        assertWithMessage("fifth item should be 2").that(lld1.get(4)).isEqualTo(2);
        assertWithMessage("fifth item should be 2").that(lld1.get(23456)).isEqualTo(null);
        assertWithMessage("fifth item should be 2").that(lld1.get(-999)).isEqualTo(null);
    }

    @Test
    /** This test performs isEmpty calls. */
    public void isEmptyTest() {
        Deque<String> lld1 = new LinkedListDeque<>();

        assertWithMessage("this lld should be empty").that(lld1.isEmpty()).isEqualTo(true);

        lld1.addLast("front"); // after this call we expect: ["front"]
        lld1.addLast("middle"); // after this call we expect: ["front", "middle"]
        lld1.addLast("back"); // after this call we expect: ["front", "middle", "back"]

        assertWithMessage("this lld should not be empty").that(lld1.isEmpty()).isEqualTo(false);
    }

    @Test
    /** This test performs size calls. */
    public void sizeTest() {
        Deque<String> lld1 = new LinkedListDeque<>();

        assertWithMessage("this lld should have size 0").that(lld1.size()).isEqualTo(0);

        lld1.addLast("front"); // after this call we expect: ["front"]
        assertWithMessage("this lld should have size 1").that(lld1.size()).isEqualTo(1);

        lld1.addLast("middle"); // after this call we expect: ["front", "middle"]
        assertWithMessage("this lld should have size 2").that(lld1.size()).isEqualTo(2);

        lld1.addLast("back"); // after this call we expect: ["front", "middle", "back"]
        assertWithMessage("this lld should have size 3").that(lld1.size()).isEqualTo(3);
    }

    @Test
    /** This test performs removeFirst calls. */
    public void removeFirstTest() {
        Deque<Integer> lld1 = new LinkedListDeque<>();

        assertWithMessage(" nothing should be removed and returned").that(lld1.removeFirst()).isEqualTo(null);

        lld1.addLast(0);   // [0]
        lld1.addLast(1);   // [0, 1]
        lld1.addFirst(-1); // [-1, 0, 1]
        lld1.addLast(2);   // [-1, 0, 1, 2]
        lld1.addFirst(-2); // [-2, -1, 0, 1, 2]

        assertWithMessage("-2 should be removed and returned").that(lld1.removeFirst()).isEqualTo(-2);
        assertThat(lld1.toList()).containsExactly(-1, 0, 1, 2).inOrder();
    }

    @Test
    /** This test performs removeLast calls. */
    public void removeLastTest() {
        Deque<Integer> lld1 = new LinkedListDeque<>();

        assertWithMessage(" nothing should be removed and returned").that(lld1.removeLast()).isEqualTo(null);

        lld1.addLast(0);   // [0]
        lld1.addLast(1);   // [0, 1]
        lld1.addFirst(-1); // [-1, 0, 1]
        lld1.addLast(2);   // [-1, 0, 1, 2]
        lld1.addFirst(-2); // [-2, -1, 0, 1, 2]

        assertWithMessage("-2 should be removed and returned").that(lld1.removeLast()).isEqualTo(2);
        assertThat(lld1.toList()).containsExactly(-2, -1, 0, 1).inOrder();
    }

    @Test
    /** This test performs getRecursive calls. */
    public void getRecursiveTest() {
        Deque<Integer> lld1 = new LinkedListDeque<>();

        lld1.addLast(0);   // [0]
        lld1.addLast(1);   // [0, 1]
        lld1.addFirst(-1); // [-1, 0, 1]
        lld1.addLast(2);   // [-1, 0, 1, 2]
        lld1.addFirst(-2); // [-2, -1, 0, 1, 2]

        assertWithMessage("first item should be -2").that(lld1.getRecursive(0)).isEqualTo(-2);
        assertWithMessage("second item should be -1").that(lld1.getRecursive(1)).isEqualTo(-1);
        assertWithMessage("third item should be 0").that(lld1.getRecursive(2)).isEqualTo(0);
        assertWithMessage("fourth item should be 1").that(lld1.getRecursive(3)).isEqualTo(1);
        assertWithMessage("fifth item should be 2").that(lld1.getRecursive(4)).isEqualTo(2);
        assertWithMessage("fifth item should be 2").that(lld1.getRecursive(23456)).isEqualTo(null);
        assertWithMessage("fifth item should be 2").that(lld1.getRecursive(-999)).isEqualTo(null);
    }
}