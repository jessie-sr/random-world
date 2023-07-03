package deque;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class ArrayDeque<Et> implements Deque<Et> {
    private static final int MIN_INITIAL_CAPACITY = 8;
    private static final int THRESHOLD = 16;
    private Et[] backArray;
    private int size;
    private int capacity;
    private int nextFirst;
    private int nextLast;

    public ArrayDeque() {
        //can't new generic array directly
        backArray = (Et[]) new Object[MIN_INITIAL_CAPACITY];
        size = 0;
        nextFirst = 3;
        nextLast = 4;
        capacity = MIN_INITIAL_CAPACITY;
    }

    @Override
    public void addFirst(Et x) {
        if (size == capacity) {
            resize();
        }
        backArray[nextFirst] = x;
        size++;
        nextFirst--;
        //to notice
        nextFirst = (nextFirst + capacity) % capacity;
    }

    @Override
    public void addLast(Et x) {
        if (size == capacity) {
            resize();
        }
        backArray[nextLast] = x;
        size++;
        nextLast++;
        //to notice
        nextLast = nextLast % capacity;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Et get(int index) {
        if (index < 0 || index >= size) {
            return null;
        }
        int tmp = nextFirst;
        tmp++;
        tmp = tmp % capacity;
        for (int i = 0; i < index; i++) {
            tmp++;
            tmp = tmp % capacity;
        }

        return backArray[tmp];
    }

    @Override
    public Et removeFirst() {
        if (size == 0) {
            return null;
        }
        if (size < backArray.length / 2 && size >= THRESHOLD) {
            resizeDown();
        }
        nextFirst++;
        nextFirst = nextFirst % capacity;
        size--;
        return backArray[nextFirst];
    }

    public Et removeFirstUnsensorred() {
        //helper function
        if (size == 0) {
            return null;
        }
        nextFirst++;
        nextFirst = nextFirst % capacity;
        //size--;
        return backArray[nextFirst];
    }


    @Override
    public Et removeLast() {
        if (size == 0) {
            return null;
        }
        if (size < backArray.length / 4 && capacity >= THRESHOLD) {
            resizeDown();
        }
        nextLast--;
        nextLast = (nextLast + capacity) % capacity;
        size--;
        return backArray[nextLast];
    }

    @Override
    public List<Et> toList() {
        List<Et> returnList = new ArrayList<>();
        //why use polymorphism
        int tmp = nextFirst;
        tmp++;
        tmp = tmp % capacity;
        for (int i = 0; i < size; i++) {
            returnList.add(backArray[tmp]);
            tmp++;
            tmp = tmp % capacity;
        }
        return returnList;
    }


    private void resize() {
        Et[] newArray = (Et[]) new Object[backArray.length * 2];
        int tmpSize = size;
        for (int i = 0; i < backArray.length; i++) {
            newArray[i] = removeFirstUnsensorred();
        }


        nextFirst = newArray.length - 1;
        //potential bug!!!
        nextLast = tmpSize;


        capacity = newArray.length;
        backArray = newArray;
    }

    private void resizeDown() {
        //check the parameter inputted!!!
        Et[] newArray = (Et[]) new Object[backArray.length / 2];
        int tmpSize = size;
        for (int i = 0; i < tmpSize; i++) {
            newArray[i] = removeFirstUnsensorred();
        }

        nextFirst = newArray.length - 1;
        //potential bug!!!
        nextLast = tmpSize;


        capacity = newArray.length;
        backArray = newArray;

    }

    @Override
    public Et getRecursive(int index) {
        return get(index);
    }

    @Override
    public Iterator<Et> iterator() {
        return new ArrayDequeIterator();
    }

    @Override
    public boolean equals(Object otherObject) {
        if (this == otherObject) {
            return true;
        }
        if (otherObject instanceof Deque deque) {
            return this.toList().equals(deque.toList());

            /*
            if (this.size != ((ArrayDeque)deque).size) {
                return false;
            }
            for (int i = 0; i < size; i++) {
                if (!this.get(i).equals(((ArrayDeque<?>) otherObject).get(i))) {
                    return false;
                }
            }
            return true;
            */
        }
        return false;
    }

    @Override
    public String toString() {
        //ArrayList<Et> tmp=new ArrayList<>(this.toList());
        return this.toList().toString();

    }

    //Iterator implementation To be reviewed!!!
    private class ArrayDequeIterator implements Iterator<Et> {
        private int currentIndex = nextFirst + 1;
        private int elementsRemaining = size;

        @Override
        public boolean hasNext() {
            return elementsRemaining > 0;
        }

        @Override
        public Et next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            Et element = backArray[currentIndex % capacity];
            currentIndex++;
            elementsRemaining--;
            return element;
        }
    }


}
