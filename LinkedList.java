//written by Peter Olsen, olse0321

public class LinkedList<T extends Comparable<T>> implements List<T> {

    private boolean isSorted;
    private Node<T> head;
    private Node<T> tail;

    public LinkedList(){
        head = new Node<>(null, null);
        tail = head;
        isSorted = true;
    }

    public boolean add(T element){
        if (element == null) return false;
        //special case: head is null, came across this issue while testing
        else if (head == null){
            head = new Node<>(element, null);
        }
        //special case: there is no data at the beginning of the list, it is empty
        else if (head.getData() == null) {
            head.setData(element);
        }
        else {
            //otherwise add to the tail pointer then move it
            tail.setNext(new Node<>(element, null));
            tail = tail.getNext();
        }

        //check sorted
        Node<T> ptr = head;
        isSorted = true;
        while (ptr.getNext() != null){
            if (ptr.getData().compareTo(ptr.getNext().getData()) > 0) isSorted = false;
            ptr = ptr.getNext();
        }
        return true;
    }

    public boolean add(int index, T element){
        int n = size();
        //out of bounds
        if (element == null || index < 0 || index >= n) return false;

        //if adding at the end, just use the regular add method
        if (index == n) return add(element);

        else if (index == 0){
            //special case: adding to the start of the list
            head = new Node<>(element, head);
        }

        else if (index < n-1) {
            //special case: adding from 0 < index < n-1
            Node<T> ptr = head;
            int traverse = 0;
            while (ptr != null && traverse < index-1){
                ptr = ptr.getNext();
                traverse++;
            }

            ptr.setNext(new Node<>(element, ptr.getNext()));
        }
        else if (index == n-1){
            //adding to the end
            tail.setNext(new Node<>(element, null));
            tail = tail.getNext();
        }

        //check sorted
        Node<T> ptr = head;
        isSorted = true;
        while (ptr.getNext() != null){
            if (ptr.getData().compareTo(ptr.getNext().getData()) > 0) isSorted = false;
            ptr = ptr.getNext();
        }
        return true;
    }

    public void clear(){
        //clear the list, move tail back to head
        head = new Node<>(null, null);
        tail = head;
        isSorted = true;
    }

    public T get(int index) {
        //index out of range
        if (index < 0 || index >= size()) return null;
        Node<T> ptr = head;
        int traverse = 0;
        //traverse the list until you reach the index
        while (ptr != null && traverse < index){
            traverse++;
            ptr = ptr.getNext();
        }
        return ptr.getData();
    }

    public int indexOf(T element){
        //this can be improved while sorted by finishing list indexing when we either find element or some value n where n > element
        if (element == null) return -1;
        if (isSorted){
            int idx = 0;
            Node<T> ptr = head;
            while (ptr != null){
                if (ptr.getData() != null && ptr.getData().compareTo(element) == 0) return idx;
                else if (ptr.getData() != null && ptr.getData().compareTo(element) > 0) return -1;
                idx++;
                ptr = ptr.getNext();
            }
        }
        else {
            //not sorted, just index up list until you find element
            int idx = 0;
            Node<T> ptr = head;
            while (ptr != null) {
                if (ptr.getData() != null && ptr.getData().compareTo(element) == 0) return idx;
                idx++;
                ptr = ptr.getNext();
            }
        }
        return -1;
    }

    public boolean isEmpty(){
        return size() == 0;
    }

    public int size(){
        //iterate up and count
        int count = 0;
        Node<T> ptr = head;

        while (ptr != null){
            if (ptr.getData() != null) count++;
            ptr = ptr.getNext();
        }
        return count;
    }

    public void sort(){
        if (isSorted || size() == 1) return;

        Node<T> ptr = head;
        Node<T> ptr2 = head;

        int n = size();
        int index = 0;
        int index2 = 0;

        while (ptr != null){
            if (ptr.getData().compareTo(ptr2.getData()) < 0){
                //if you have found the index where ptr data is less than ptr2 data, remove ptr and add it before ptr2, then reset ptr2 and index2, so you can loop again
                T data = remove(index);
                add(index2, data);
                ptr = ptr.getNext();
                index++;
                ptr2 = head;
                index2 = 0;
            }
            else {
                //this is the case where pointer1 data >= pointer2 data
                if (ptr == ptr2) {
                    // if pointer1 and pointer2 point to the same place in memory, this means that pointer2 has made it to pointer1 in the list
                    // and has not found a value less than pointer1, therefore you must reset pointer2 then move pointer1 onto the next node
                    ptr = ptr.getNext();
                    index++;
                    ptr2 = head;
                    index2 = 0;
                }
                else{
                    //move pointer2 up if it is not equal to pointer1
                    ptr2 = ptr2.getNext();
                    index2++;
                }
            }
        }
        isSorted = true;
    }

    public T remove(int index){
        //index out of bounds
        if (index < 0 || index >= size()) return null;
        T data;

        if (index == 0){
            //special case: removing the beginning node
            data = head.getData();
            head = head.getNext();
        }
        else{
            int temp = 0;
            Node<T> ptr = head;
            //iterate until the node before the index, so you can setNext() of index-1
            while (ptr != null && temp < index-1){
                ptr = ptr.getNext();
                temp++;
            }
            //change the pointers
            data = ptr.getNext().getData();
            ptr.setNext(ptr.getNext().getNext());
        }

        //check sorted
        if (size() <= 1){
            isSorted = true;
            return data;
        }
        Node<T> ptr = head;
        isSorted = true;
        while (ptr.getNext() != null){
            if (ptr.getData().compareTo(ptr.getNext().getData()) > 0) isSorted = false;
            ptr = ptr.getNext();
        }
        return data;
    }

    public void equalTo(T element){
        if (element == null) return;
        if (isSorted){
            Node<T> ptr = head;
            //iterate up the list to find the first occurrence of element
            while (ptr != null && ptr.getData().compareTo(element) < 0){
                ptr = ptr.getNext();
            }
            head = ptr;
            //iterate through all occurrences of element
            while (ptr.getNext() != null && ptr.getNext().getData().compareTo(element) == 0){
                ptr = ptr.getNext();
            }
            ptr.setNext(null);
        }
        else{
            //not sorted
            int count = 0;
            Node<T> ptr = head;
            while (ptr != null){
                //count the number of occurrences of element
                if (ptr.getData().compareTo(element) == 0) count++;
                ptr = ptr.getNext();
            }
            ptr = head;
            for (int i = 0; i < count-1; i++){
                //i occurrences of element, set i nodes equal to element
                ptr.setData(element);
                ptr = ptr.getNext();
            }
            ptr.setData(element);
            ptr.setNext(null);
        }

        isSorted = true;
    }

    public void reverse(){
        if (head.getNext() == null) return;

        Node<T> ptr = head;
        int n = size();
        //iterate up the list
        for (int i = 0; i < n/2; i++) {
            Node<T> ptr2 = ptr;
            int temp = n - i - 1;
            int traverse = i;
            //set pointer equal to the n-1-i element
            while (traverse < temp) {
                ptr2 = ptr2.getNext();
                traverse++;
            }
            //swap the elements
            T swap = ptr.getData();
            ptr.setData(ptr2.getData());
            ptr2.setData(swap);
            ptr = ptr.getNext();
        }
        //check sorted
        ptr = head;
        isSorted = true;
        while (ptr.getNext() != null){
            if (ptr.getData().compareTo(ptr.getNext().getData()) > 0) isSorted = false;
            ptr = ptr.getNext();
        }
    }

    public void intersect(List<T> otherList){

        if (otherList == null) return;

        //typecast other list
        LinkedList<T> other = (LinkedList<T>) otherList;
        sort();
        //we will index over the main list, and see if each element is in otherList
        //if it is not, remove it from the main list
        Node<T> ptr = head;
        int idx = 0;
        while (ptr != null){
            if (otherList.indexOf(ptr.getData()) == -1){
                remove(idx);
            }
            else {
                idx++;
            }
            ptr = ptr.getNext();
        }
        isSorted = true;
    }

    public T getMin(){
        if (isSorted) return head.getData();
        //set min to first element
        T min = head.getData();
        Node<T> ptr = head;
        //iterate up the list, adjusting min accordingly
        while (ptr != null){
            if (ptr.getData() != null && ptr.getData().compareTo(min) < 0) min = ptr.getData();
            ptr = ptr.getNext();
        }
        return min;
    }

    public T getMax(){
        //this cannot be improved if sorted, since the max will always be at the end of the list, so you must traverse regardless
        //set max to first element
        T max = head.getData();
        Node<T> ptr = head;
        //iterate up the list, adjusting max accordingly
        while (ptr != null){
            if (ptr.getData() != null && ptr.getData().compareTo(max) > 0) max = ptr.getData();
            ptr = ptr.getNext();
        }
        return max;
    }

    public String toString(){
        String str = "";
        Node<T> ptr = head;
        while (ptr != null){
            str += ptr.getData() + "\n";
            ptr = ptr.getNext();
        }
        return str;
    }

    public boolean isSorted(){
        return isSorted;
    }
}
