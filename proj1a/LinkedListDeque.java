public class LinkedListDeque<T> {
    private class StuffNode{
        T value;
        StuffNode pre;
        StuffNode next;

        public StuffNode(T val, StuffNode Pre, StuffNode Next){
            value=val;
            pre=Pre;
            next=Next;
        }
    }

    private StuffNode sentinel_head,sentinel_tail;
    private int size;

     public LinkedListDeque(){
           sentinel_head=new StuffNode(null,null,null);
           sentinel_tail=new StuffNode(null,null,null);
           size=0;
    }

    public void addFirst(T item){
         sentinel_head.next=new StuffNode(item,sentinel_head,sentinel_head.next);
         if(size==0){
             sentinel_tail.pre=sentinel_head.next;
             sentinel_head.next.next=sentinel_tail;
         }
         else{
             sentinel_head.next.next.pre=sentinel_head.next;
         }
         size++;
    }

    public void addLast(T item){
        sentinel_tail.pre=new StuffNode(item,sentinel_tail.pre,sentinel_tail);
        if(size==0){
            sentinel_head.next=sentinel_tail.pre;
            sentinel_tail.pre.pre=sentinel_head;
        }
        else{
            sentinel_tail.pre.pre.next=sentinel_tail.pre;
        }
        size++;
    }

    public boolean isEmpty(){
         if (size==0) return true;
         else return false;
    }

    public int size(){
         return size;
    }

    public void printDeque(){
         StuffNode ptr=sentinel_head.next;
         while(ptr!=sentinel_tail){
             System.out.print(ptr.value);
             System.out.print(" ");
             ptr=ptr.next;
         }
    }

    public T removeFirst(){
         T ret=sentinel_head.next.value;
         StuffNode delptr=sentinel_head.next;
         sentinel_head.next=sentinel_head.next.next;
         sentinel_head.next.pre=sentinel_head;

         delptr.pre=null;
         delptr.next=null;
         delptr=null;
         size--;

         return ret;
    }

    public T removeLast(){
        T ret=sentinel_tail.pre.value;
        StuffNode delptr=sentinel_tail.pre;
        sentinel_tail.pre=sentinel_tail.pre.pre;
        sentinel_tail.pre.next=sentinel_tail;

        delptr.pre=null;
        delptr.next=null;
        delptr=null;

        return ret;
    }

    public T get(int index){
         if(index>size-1) return null;
         StuffNode ptr=sentinel_head;
         for(int i=0;i<=index;i++) {
             ptr = ptr.next;
         }
         return ptr.value;
    }

    public T getRecursive(int index){
         if(index>size-1) return null;
         else return NodeRecursive(index,sentinel_head.next);
    }

    private T NodeRecursive(int index, StuffNode start){
         if(index==0) return start.value;
         else return NodeRecursive(index-1,start.next);
    }

}
