//written by Peter Olsen, 0321

public class ArrayList<T extends Comparable<T>> implements List<T>{

    private boolean isSorted;
    private T[] a;

    public ArrayList(){
        a = (T[]) new Comparable[2];
        isSorted = true;
    }

    public boolean add(T element){
        //store size so it does not need to be calculated every for loop
        int n = size();
        //check if the element is valid
        if (element == null) return false;
        //if the array is full, create a new one of length*2
        if (a.length == n){
            T[] a2 = (T[]) new Comparable[a.length * 2];
            for (int i = 0; i < a.length; i++){
                a2[i] = a[i];
            }
            a = a2;
        }
        //set the last element
        a[n] = element;

        //assume the array is sorted, and set isSorted to false if it is not
        isSorted = true;
        for (int i = 0; i < n; i++){
            if (a[i].compareTo(a[i+1]) > 0) isSorted = false;
        }

        return true;
    }

    public boolean add(int index, T element){
        //store size so it does not need to be calculated every for loop
        int n = size();
        //see if the inputs are valid
        if (element == null || index < 0 || index >= n) return false;

        //if the array is full, create a new one of length*2
        if (a.length == n){
            T[] a2 = (T[]) new Comparable[a.length * 2];
            for (int i = 0; i < a.length; i++){
                a2[i] = a[i];
            }
            a = a2;
        }
        //if you are adding to the end
        if (index == n-1){
            return add(element);
        }

        //push back elements of the array
        for (int i = n-1; i > index-1; i--){
            a[i+1] = a[i];
        }

        a[index] = element;

        //check if the array is still sorted
        //assume the array is sorted, and set isSorted to false if it is not
        isSorted = true;
        for (int i = 0; i < n; i++){
            if (a[i].compareTo(a[i+1]) > 0) isSorted = false;
        }

        return true;
    }

    public void clear(){
        //loop must iterate down, if we were to iterate until i <= size() it would update size every iteration
        for (int i = size()-1; i >=0; i--){
            a[i] = null;
        }
        isSorted = true;
    }

    public T get(int index) {
        if (index < 0 || index >= size()) return null;
        return a[index];
    }


    public int indexOf(T element){

        if (element == null) return -1;

        if (isSorted) {
            //iterate until we either find the element, reach an element > the parameter, or reach the end
            for (int i = 0; i < size(); i++){
                if (a[i].compareTo(element) > 0) return -1;
                if (a[i].compareTo(element) == 0) return i;
            }
        }
        //array is not sorted
        else {
            //iterate until we find it or run out of indices
            for (int i = 0; i < size(); i++){
                if (a[i].compareTo(element) == 0) return i;
            }
        }
        return -1;
    }

    public boolean isEmpty(){
        for (int i = 0; i < size(); i++){
            if (a[i] != null) return false;
        }
        return true;
    }

    public int size(){
        int temp = 0;
        for (T member:a) {
            if (member != null) temp++;
        }
        return temp;
    }


    public void sort(){

        //nothing needs to be done if it is already sorted
        if (isSorted) return;

        //declare j outside the for loop, so you can use it later in the sort
        int j;

        //insertion sort
        for (int i = 1; i < size(); i++){
            T temp = a[i];
            for (j = i-1; j >= 0 && temp.compareTo(a[j]) < 0; j--){
                a[j+1] = a[j];
            }
            a[j+1] = temp;
        }
        isSorted = true;
    }

    public T remove(int index){
        //store size so you don't need to calculate it in every for loop iteration
        int n = size();

        //out of bounds
        if (index < 0 || index >= n) {
            return null;
        }

        T temp = a[index];
        //shift elements after index to the left
        for (int i = index; i < n - 1; i++) {
            a[i] = a[i + 1];
        }
        a[n - 1] = null;

        //iterate up the array to check if it is sorted
        //assume the array is sorted, and set isSorted to false if it is not
        isSorted = true;
        for (int i = 0; i < n-2; i++){
            if (a[i].compareTo(a[i+1]) > 0) isSorted = false;
        }

        return temp;
    }

    public void equalTo(T element){
        if (element == null) return;

        else if (isSorted){
            int i = 0;

            //find the first occurrence of element
            while (i < size()-1 && a[i].compareTo(element) != 0){
                if (a[i+1] == null) break;
                else i++;
            }
            //shift all down
            int j;
            for (j = 0; j <= i; j++){
                a[j] = a[i+j];
            }
            //find how many elements there are equal to element
            int k = 0;
            while (k < size()-1 && a[k+1].compareTo(element) == 0){
                k++;
            }

            //set all spots after element to null
            for (int p = k+1; p < size(); p++){
                a[p] = null;
            }
        }
        else {
            //method will need to run in O(n^2) time
            for (int i = size()-1; i >= 0; i--) {
                if (a[i].compareTo(element) != 0) {
                    a[i] = null;
                    //shift values down after removal
                    for (int j = i+1; j <= size(); j++){
                        a[j-1] = a[j];
                    }
                }
            }
        }
        isSorted = true;
    }

    public void reverse(){

        //create variable so we don't have to call size() method every time we want it
        int n = size()-1;

        //swap beginning and end elements
        for (int i = 0; i <= n/2; i++){
            T temp = a[i];
            a[i] = a[n-i];
            a[n-i] = temp;
        }

        //assume the array is sorted, and set isSorted to false if it is not
        isSorted = true;
        for (int i = 0; i < n; i++){
            if (a[i].compareTo(a[i+1]) > 0) isSorted = false;
        }
    }

    public void intersect(List<T> otherList){

        if (otherList == null) return;

        ArrayList<T> other = (ArrayList<T>) otherList;
        sort();
        //for all values in this list, change to null if it does not appear in other list
        for (int i = 0; i < a.length; i++){
            if (otherList.indexOf(a[i]) == -1){
                a[i] = null;
            }
        }
        //iterate through the list and find null values
        for (int i = 0; i < a.length; i++) {
            if (a[i] != null) {
                int j = i;
                //when a[i] is null, shift it all down until you find another item
                while (j > 0 && a[j - 1] == null) {
                    a[j - 1] = a[j];
                    a[j] = null;
                    j--;
                }
            }
        }

        isSorted = true;
    }

    public T getMin(){
        if (isSorted) return a[0];
        T min = a[0];

        for (int i = 0; i < size(); i++){
            if (a[i].compareTo(min) < 0) min = a[i];
        }
        return min;
    }

    public T getMax(){
        if (isSorted && size() > 0) return a[size()-1];
        T max = a[0];

        for (int i = 0; i < size(); i++){
            if (a[i].compareTo(max) > 0) max = a[i];
        }
        return max;
    }

    public String toString(){
        String str = "";
        for (int i = 0; i < a.length; i++){
            if (a[i] != null) str += a[i] + "\n";
        }
        return str;
    }

    public boolean isSorted(){
        return isSorted;
    }
}
