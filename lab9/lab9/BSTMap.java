package lab9;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Implementation of interface Map61B with BST as core data structure.
 *
 * @author Gary Agasa
 */
public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {

    private class Node {
        /* (K, V) pair stored in this Node. */
        private K key;
        private V value;

        /* Children of this Node. */
        private Node left;
        private Node right;

        private Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    private Node root;  /* Root node of the tree. */
    private int size; /* The number of key-value pairs in the tree */

    private class NodeStruct{
        public Node current;
        public Node parent;
        public int direction;/*0 for left,1 for right*/

        public NodeStruct(Node Parent ,Node Current,int Direction){
            current=Current;
            parent=Parent;
            direction=Direction;
        }

        public void Reconnect(Node newChild){
            if(parent == null){
                root=newChild;
            }
            else if(direction == 0)
                parent.left=newChild;
            else if(direction == 1)
                parent.right=newChild;
            else throw new UnsupportedOperationException();
        }
    }

    /* Creates an empty BSTMap. */
    public BSTMap() {
        this.clear();
    }

    /* Removes all of the mappings from this map. */
    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    /** Returns the value mapped to by KEY in the subtree rooted in P.
     *  or null if this map contains no mapping for the key.
     */
    private V getHelper(K key, Node p) {
        if(p==null) return null;
        if( key.compareTo(p.key)==0 ) return p.value;
        else if( key.compareTo(p.key)<0 ){
            return getHelper(key,p.left);
        }
        else{
            return getHelper(key,p.right);
        }
    }
    /** Returns the value to which the specified key is mapped, or null if this
     *  map contains no mapping for the key.
     */
    @Override
    public V get(K key) {
        return getHelper(key,root);
    }

    /**e Returns a BSTMap rooted in p with (KEY, VALUE) added as a key-value mapping.
      * Or if p is null, it returns a one node BSTMap containing (KEY, VALUE).
     */
    private Node putHelper(K key, V value, Node p) {
        if( p==null ) return new Node(key,value);
        else if(key.compareTo(p.key)<0){
            p.left=putHelper(key,value,p.left);
            return p;
        }
        else if(key.compareTo(p.key)>0){
            p.right=putHelper(key,value,p.right);
            return p;
        }
        else throw new UnsupportedOperationException();
    }

    /** Inserts the key KEY
     *  If it is already present, updates value to be VALUE.
     */
    @Override
    public void put(K key, V value) {
        root=putHelper(key,value,root);
        size+=1;
    }

    /* Returns the number of key-value mappings in this map. */
    @Override
    public int size() {
        return this.size;
    }

    //////////////// EVERYTHING BELOW THIS LINE IS OPTIONAL ////////////////

    /* Returns a Set view of the keys contained in this map. */
    private void keySetTraversal(Node p,Set<K> ret){
        if( p==null ) return;
        ret.add(p.key);
        keySetTraversal(p.left,ret);
        keySetTraversal(p.right,ret);
    }

    @Override
    public Set<K> keySet() {
        Set<K> ret=new HashSet<>();
        keySetTraversal(root,ret);
        return ret;
    }

    private NodeStruct FindParentHelper(K Key,Node p){
        /**If the root is the right answer*/
        if(p==null) return null;
        if(Key.compareTo(p.key)==0)
             return new NodeStruct(null,p,1);

        /**When the left child may be the right answer*/
        else if( Key.compareTo(p.key)<0 ){
            if(p.left ==null) return null;
            else if(Key.compareTo(p.left.key)==0)
                 return new NodeStruct(p,p.left,0);
            else return FindParentHelper(Key,p.left);
        }

        /**When the right child may be the right answer*/
        else{
            if(p.right==null) return null;
            else if(Key.compareTo(p.right.key)==0)
                return new NodeStruct(p,p.right,1);
            else return FindParentHelper(Key,p.right);
        }
    }

    private Node HeadToRight(Node Start){
        if(Start == null) throw new RuntimeException();
        if(Start.right == null) return Start;
        return HeadToRight(Start.right);
    }

    private Node FindMaxNodeOnLeftChild(Node p){
         if(p.left == null) throw new RuntimeException();
         return HeadToRight(p.left);
    }

    /** Removes KEY from the tree if present
     *  returns VALUE removed,
     *  null on failed removal.
     */
    @Override
    public V remove(K key) {
        NodeStruct ns=FindParentHelper(key,root);
        if(ns == null) return null;

        Node Parent=ns.parent;
        Node Ans=ns.current;
        V ret= Ans.value;

        /*When No Child*/
        if(Ans.left == null && Ans.right == null){
            ns.Reconnect(null);
        }
        /*When one child only*/
        else if(Ans.right == null){
            ns.Reconnect(Ans.left);
            Ans.left=null;
        }
        else if(Ans.left == null){
            ns.Reconnect(Ans.right);
            Ans.right=null;
        }
        /*When two child*/
        else{
            Node Inheritor=FindMaxNodeOnLeftChild(Ans);
            remove(Inheritor.key);

            Inheritor.left=Ans.left;
            Inheritor.right=Ans.right;
            ns.Reconnect(Inheritor);

            Ans.left=null;
            Ans.right=null;

            /*Compensate for overReduce*/
            size+=1;
        }

        size-=1;
        return ret;
    }

    /** Removes the key-value entry for the specified key only if it is
     *  currently mapped to the specified value.  Returns the VALUE removed,
     *  null on failed removal.
     **/
    @Override
    public V remove(K key, V value) {
        NodeStruct ns=FindParentHelper(key,root);
        if(ns == null) return null;
        if(ns.current.value != value) return null;
        return remove(key);
    }


    @Override
    public Iterator<K> iterator() {
        Set<K> SetOfKey=keySet();
        return SetOfKey.iterator();
    }
}
