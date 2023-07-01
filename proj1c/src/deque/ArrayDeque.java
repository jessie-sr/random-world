package deque;

import java.util.List;
import java.util.Iterator;
import java.util.NoSuchElementException;
public class ArrayDeque<Et> implements Deque<Et>{
    private Et[] backArray;
    private int size;
    private int capacity;
    private int nextFirst;
    private int nextLast;

    private static final int MIN_INITIAL_CAPACITY = 8;

    public ArrayDeque(){
        backArray= (Et[]) new Object[MIN_INITIAL_CAPACITY];
        size=0;
        nextFirst=3;
        nextLast=4;
    }

    @Override
    public void addFirst(Et x){
        if(size==capacity) resize();
        backArray[nextFirst]=x;
        size++;
        nextFirst--;
        nextFirst=nextFirst%capacity;//to notice


    }

    @Override
    public void addLast(Et x){
        if(size==capacity) resize();
        backArray[nextLast]=x;
        size++;
        nextLast++;
        nextLast=nextLast%capacity;//to notice

    }

    @Override
    public boolean isEmpty(){
        return size==0;
    }

    @Override
    public int size(){
        return size;
    }

    @Override
    public Et get(int index) {
        if(index<0 || index>=size) return null;
        return backArray[index];
    }

    @Override
    public Et removeFirst() {
        if(size< backArray.length/4 && size>=16) resizeDown();
        nextFirst++;
        nextFirst=nextFirst%capacity;
        size--;
        return backArray[nextFirst];
    }

    public Et removeFirstUnsensorred() {//helper function
        nextFirst++;
        nextFirst=nextFirst%capacity;
        size--;
        return backArray[nextFirst];
    }

    @Override
    public Et removeLast() {
        if(size< backArray.length/4 && size>=16) resizeDown();
        nextLast--;
        nextLast=nextLast%capacity;
        size--;
        return backArray[nextLast];
    }

    @Override
    public List<Et> toList() {
        return null;
    }



    private void resize(){
        Et[] newArray = (Et[]) new Object[backArray.length * 2];
        for(int i=0;i<backArray.length;i++){
            newArray[i]=removeFirstUnsensorred();
        }


        nextFirst= newArray.length-1;
        nextLast= size;//potential bug!!!


        capacity= newArray.length;
        backArray = newArray;
    }

    private void resizeDown(){
        Et[] newArray = (Et[]) new Object[backArray.length/2];//check the parameter inputted!!!
        for(int i=0;i< backArray.length;i++){
            newArray[i]=removeFirstUnsensorred();
        }

        nextFirst= newArray.length-1;
        nextLast= size;//potential bug!!!


        capacity= newArray.length;
        backArray = newArray;

    }

    @Override
    public Et getRecursive(int index) {
        return get(index);
    }

    public Iterator<Et> iterator() {
        return new ArrayDequeIterator();
    }

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
