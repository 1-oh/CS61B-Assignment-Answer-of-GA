public class ArrayDeque<T> {
    private T[] item;
    private int arrsize;
    private int size;
    private int nextlast;
    private int nextfirst;

    /** In this program ,in order to achieve the Deque from array,
     * I came up with an idea that when add first,just add elements
     * to the end of the array.Similarly,when add last,just add elements
     * to the head of the array.This method is so good that you just have the
     * pointer nextlast and nextfirst to the two edge of the array and don't need to
     * travel to another side*/
    public ArrayDeque(){
        item=(T[])new Object[8];
        /*to create an array for a uncertain type*/
        arrsize =8;
        size=0;
        nextlast=0;
        nextfirst= arrsize -1;
    }

    private void sizeUpgrade(){
        /*to enlarge the array when it is full*/
        T[] newone=(T[])new Object[arrsize *2];/*take care of this trick*/
        System.arraycopy(item,0,newone,0,nextlast);
        System.arraycopy(item,nextfirst+1,newone,nextfirst+ arrsize +1, arrsize -1-nextfirst);
        item=newone;
        arrsize =2* arrsize;
        nextfirst=nextfirst+ arrsize;
    }

    private boolean isFull(){
        if(nextfirst<nextlast) return true;
        else return false;
    }

    public boolean isEmpty(){
        if(size ==0) return true;
        return false;
    }

    public void addFirst(T element){
       if(isFull()){
           sizeUpgrade();
       }
       item[nextfirst]=element;
       nextfirst-=1;
       size +=1;
    }

    public void addLast(T element){
        if(isFull()){
            sizeUpgrade();
        }
        item[nextlast]=element;
        nextlast+=1;
        size +=1;
    }

    public int size(){
        return size;
    }

    public T removeFirst(){
        T ret=item[nextfirst+1];
        nextfirst+=1;
        size -=1;
        return ret;
    }

    public T removeLast(){
        T ret=item[nextlast-1];
        nextlast-=1;
        size -=1;
        return ret;
    }

    public T get(int index){
        int numintail= arrsize -1-nextfirst;
        if(index+1<=numintail){
            return item[nextfirst+index+1];
        }
        else{
            return item[index-numintail];
        }
    }

}
