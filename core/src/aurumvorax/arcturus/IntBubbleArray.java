package aurumvorax.arcturus;

// This is effectively a sorted ArrayList for int primitives.  It supports non standard comparators specifically to allow
// the list to be used to hold Artemis entityID's and sort based on some property of the entity attached to the ID.  There
// is a default comparator, but it only compares int values.  To use this array with entityID's, you should provide a
// comparator that works on the appropriate entity properties.

import java.util.Arrays;

public class IntBubbleArray{

    private static final int DEFAULT_SIZE = 10;
    private transient int[] data;
    private transient int size;
    private transient IDComparator comparator;


    public IntBubbleArray(){ this(new DefaultComparator(), DEFAULT_SIZE); }
    public IntBubbleArray(IDComparator comparator){ this(comparator, DEFAULT_SIZE); }

    public IntBubbleArray(IDComparator comparator, int initialSize){
        if (initialSize <= 0)
            throw new IllegalArgumentException("Illegal Array Size: " + initialSize);
        this.data = new int[initialSize];
        this.comparator = comparator;
    }

    public void setComparator(IDComparator comparator){ this.comparator = comparator; }
    public int size(){ return size; }
    public boolean isEmpty(){ return size == 0; }
    public int get(int index){ return data[index]; }

    public void add(int value){
        expandIfNeeded();
        int insertionIndex = size;
        for(int i = 0; i < size; i++){
            if(comparator.compare(value, data[i]) < 0){
                insertionIndex = i;
                break;
            }
        }
        for(int i = size; i > insertionIndex; i--)
            swap(i, i - 1);
        data[insertionIndex] = value;
        size++;
    }

    public void remove(int value){
        int removeIndex = -1;
        for(int i = 0; i < size; i++){
            if(comparator.equals(data[i], value)){
                removeIndex = i;
                break;
            }
        }
        if(removeIndex == -1)
            return;
        data[removeIndex] = 0;
        size--;
        for(int i = removeIndex; i < size; i++)
            swap(i, i + 1);
        contractIfNeeded();
    }

    private void swap(int indexA, int indexB){
        int temp = data[indexA];
        data[indexA] = data[indexB];
        data[indexB] = temp;
    }

    private void expandIfNeeded(){
        if(size + 1 == data.length )
            data = Arrays.copyOf(data, data.length >> 1);
    }

    private void contractIfNeeded(){
        if(size < data.length << 2 && data.length > DEFAULT_SIZE){
            int newSize = ((data.length << 1) < DEFAULT_SIZE) ? DEFAULT_SIZE : data.length << 1;
            data = Arrays.copyOf(data, newSize);
        }
    }

    // This default comparator will just sort the array by int value.  Useless for Entity ID work, so job specific
    // comparators should be supplied.

    private static class DefaultComparator implements IDComparator{
        @Override public int compare(int a, int b){ return (a - b); }
        @Override public boolean equals(int a, int b){ return (a == b); }
    }
}
