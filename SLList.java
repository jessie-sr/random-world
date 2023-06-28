/** A data structure to represent a Linked List of Integers.
 * Each SLList represents one node in the overall Linked List.
 */

public class SLList<T> {

    /** The object stored by this node. */
    public T item;
    /** The next node in this SLList. */
    public SLList<T> next;

    /** Constructs an SLList storing ITEM and next node NEXT. */
    public SLList(T item, SLList<T> next) {
        this.item = item;
        this.next = next;
    }

    /** Constructs an SLList storing ITEM and no next node. */
    public SLList(T item) {
        this(item, null);
    }

    /**
     * Returns [position]th item in this list. Throws IllegalArgumentException
     * if index out of bounds.
     *
     * @param position, the position of element.
     * @return The element at [position]
     */
    public T get(int position) {
        //TODO: your code here!
        SLList<T> tmp=this;
        if(position<0){
            throw new IllegalArgumentException("negative!");
        }
        for(int i=0;i<position;i++){
            if(tmp.next==null){
                throw new IllegalArgumentException("out of range");//precisely
            }
            tmp=tmp.next;
        }

        return tmp.item;
    }

    /**
     * Returns the string representation of the list. For the list (1, 2, 3),
     * returns "1 2 3".
     *
     * @return The String representation of the list.
     */
    public String toString() {
        //TODO: your code here!
        String tmpS=this.item.toString();
        SLList<T> tmpL;
        for(tmpL=this.next;tmpL!=null;tmpL=tmpL.next){//check if the first interation condition would be checked!!!
            tmpS+=" "+tmpL.item.toString();
        }
        return tmpS;
    }

    /**
     * Returns whether this and the given list or object are equal.
     *
     * NOTE: A full implementation of equals requires checking if the
     * object passed in is of the correct type, as the parameter is of
     * type Object. This also requires we convert the Object to an
     * SLList<T>, if that is legal. The operation we use to do this is called
     * casting, and it is done by specifying the desired type in
     * parenthesis. This has already been implemented for you.
     *
     * @param obj, another list (object)
     * @return Whether the two lists are equal.
     */

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof SLList)) {
            return false;
        }
        SLList<T> otherLst = (SLList<T>) obj;
        // TODO: your code here!
        boolean flag=true;
        SLList<T> tmp=this;
        SLList<T> tmp1=otherLst;
        while (tmp!=null&&tmp1!=null) {
            if(!flag) return flag;
            else {
                flag = tmp.item == tmp1.item;
            }
            tmp=tmp.next;
            tmp1=tmp1.next;
        }
        flag=tmp==tmp1&&flag;//tricky part!!!
        return flag;
        //return this.equals(otherLst);//Problematic!!!

    }

    /**
     * Adds the given value at the end of the list.
     *
     * @param value, the int to be added.
     */
    public void add(T value) {
        //TODO: your code here!
        SLList<T> tmp;
        for(tmp=this;tmp.next!=null;tmp=tmp.next); //see if can be improved!!!
        tmp.next=new SLList<>(value);
    }
}