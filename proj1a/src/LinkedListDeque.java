import java.util.ArrayList;
import java.util.List;

public class LinkedListDeque<T> implements Deque<T> {

    private class Node {

        public T item;
        public Node next;
        public Node prev;

        public Node(T item, Node next) {
            this.item = item;
            this.next = next;
            this.prev = null;
        }


        public Node(T item, Node next, Node prev) {
            this.item = item;
            this.next = next;
            this.prev = prev;
        }
    }

    private Node sentinel;
    private int size;

    public LinkedListDeque() {
        sentinel = new Node(null, null, null);
        sentinel.next = sentinel;
        sentinel.prev = sentinel.next;
        size = 0;
    }
    @Override
    public void addFirst(T x) {
        Node newNode = new Node(x, sentinel.next, sentinel); // New node points to the original first node and the sentinel
        sentinel.next.prev = newNode; // Update the previous reference of the original first node
        sentinel.next = newNode; // Update the next reference of the sentinel to the new node
        size++;
    }

    @Override
    public void addLast(T x) {
        Node newNode = new Node(x, sentinel, sentinel.prev); // New node points to the original last node and the sentinel
        sentinel.prev.next = newNode; // Update the next reference of the original last node
        sentinel.prev = newNode; // Update the previous reference of the sentinel to the new node
        size++;
    }

    @Override
    public List<T> toList() {
        List<T> returnList = new ArrayList<T>();
        Node pointer = sentinel.next;
        while (pointer != sentinel) { // Iterate through the entire LLD and add the item of each node to the list
            returnList.add(pointer.item);
            pointer = pointer.next;
        }
        return returnList;
    }

    @Override
    public boolean isEmpty() {
        return (size == 0);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public T removeFirst() {
        if (size == 0) {
            return null;
        } else {
            T result = sentinel.next.item; // Store the item value of the original first node
            sentinel.next.next.prev = sentinel; // Update the previous reference of the original second node
            sentinel.next = sentinel.next.next; // Update the next reference of the sentinel
            size--;
            return result;
        }
    }

    @Override
    public T removeLast() {
        if (size == 0) {
            return null;
        } else {
            T result = sentinel.prev.item; // Store the item value of the original last node
            sentinel.prev.prev.next = sentinel; // Update the next reference of the original second last node
            sentinel.prev = sentinel.prev.prev; // Update the prev reference of the sentinel
            size--;
            return result;
        }
    }

    @Override
    public T get(int index) {
        Node pointer = sentinel.next;
        if ((index < 0) || (index > size - 1)) {
            return null;
        } else {
            while (index > 0) {
                pointer = pointer.next;
                index--;
            } return pointer.item;
        }
    }

    @Override
    public T getRecursive(int index) {
        Node pointer = sentinel.next;
        return RecursiveHelper(index, pointer);
    }

    private T RecursiveHelper(int index, Node pointer) {
        if ((index < 0) || (index > size - 1)) {
            return null;
        } else if (index == 0) {
            return pointer.item;
        } else {
            pointer = pointer.next;
            return this.RecursiveHelper(index - 1, pointer);
        }
    }
}
