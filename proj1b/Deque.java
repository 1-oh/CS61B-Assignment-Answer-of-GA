public interface Deque<Type>{
    public void addFirst(Type item);
    public void addLast(Type item);
    public boolean isEmpty();
    public int size();
    public Type removeFirst();
    public Type removeLast();
    public Type get(int index);
}
