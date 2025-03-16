public class LinkedListDeque<Type> {
    private class StuffNode{
        Type value;
        StuffNode pre;
        StuffNode next;

        public StuffNode(Type val,StuffNode Pre,StuffNode Next){
            value=val;
            pre=Pre;
            next=Next;
        }
    }

    public StuffNode sentinel_head,sentinel_tail;
    public int size;

     public LinkedListDeque(){
           sentinel_head=new StuffNode(null,null,null);
           sentinel_tail=new StuffNode(null,null,null);
           size=0;
    }

    public void addFirst(Type item){
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

    public void addLast(Type item){
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

    public Type removeFirst(){
         Type ret=sentinel_head.next.value;
         StuffNode delptr=sentinel_head.next;
         sentinel_head.next=sentinel_head.next.next;
         sentinel_head.next.pre=sentinel_head;

         delptr.pre=null;
         delptr.next=null;
         delptr=null;
         size--;

         return ret;
    }

    public Type removeLast(){
        Type ret=sentinel_tail.pre.value;
        StuffNode delptr=sentinel_tail.pre;
        sentinel_tail.pre=sentinel_head.pre.pre;
        sentinel_tail.pre.next=sentinel_tail;

        delptr.pre=null;
        delptr.next=null;
        delptr=null;

        return ret;
    }

    public Type get(int index){
         if(index>size-1) return null;
         StuffNode ptr=sentinel_head;
         for(int i=0;i<=index;i++) {
             ptr = ptr.next;
         }
         return ptr.value;
    }

    public Type getRecursive(int index){
         if(index>size-1) return null;
         else return NodeRecursive(index,sentinel_head.next);
    }

    private Type NodeRecursive(int index,StuffNode start){
         if(index==0) return start.value;
         else return NodeRecursive(index-1,start.next);
    }

    public static void main(String Args[]){
        LinkedListDeque ad=new LinkedListDeque<Integer>();
        for(int i=0;i<20;i++){
            ad.addFirst(i);
        }
        Integer GET=(int)ad.get(5);
        System.out.println(GET);
        System.out.println(ad.isEmpty());
    }
}
