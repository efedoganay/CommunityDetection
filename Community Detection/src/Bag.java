import java.util.Iterator;
import java.util.NoSuchElementException;

public class Bag<Item> implements Iterable<Item> {
    private Node first;    // beginning of bag
    private int n;               // number of elements in bag

    // helper linked list class
    private class Node {
        private  Item item;
        private Node next;
    }

    /**
     * Initializes an empty bag.
     */
    public Bag() {
        first = null;
        n = 0;
    }

    /**
     * Returns true if this bag is empty.
     *
     * @return {@code true} if this bag is empty;
     *         {@code false} otherwise
     */
    public boolean isEmpty() {
        return first == null;
    }

    /**
     * Returns the number of items in this bag.
     *
     * @return the number of items in this bag
     */
    public int size() {
        return n;
    }

    /**
     * Adds the item to this bag.
     *
     * @param  item the item to add to this bag
     */
    public void add(Item item) {
        Node oldfirst = first;
        first = new Node();
        first.item = item;
        first.next = oldfirst;
        n++;
    }
    
    public void remove(Item item)      { 
    	Node currentNode = (Node) first;
        Node previousNode = null;
        while(currentNode != null){
            if(item.equals(currentNode.item)){
                if(previousNode  == null) {
                  first = (Node) currentNode.next;
                }
                else {
                  previousNode.next = currentNode.next;
                }
                n--;
            }
            else {
              previousNode = currentNode;
            }
            currentNode = currentNode.next;
        }
    
    }


    /**
     * Returns an iterator that iterates over the items in this bag in arbitrary order.
     *
     * @return an iterator that iterates over the items in this bag in arbitrary order
     */
    public Iterator<Item> iterator()  {
        return new ListIterator<Item>(first);  
    }

    // an iterator, doesn't implement remove() since it's optional
    private class ListIterator<Item> implements Iterator<Item> {
        private Node current;

        public ListIterator(Node first) {
            current = first;
        }

        public boolean hasNext()  { return current != null;                     }
        

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = (Item) current.item;
            current = current.next; 
            return item;
        }
    }

    /**
     * Unit tests the {@code Bag} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        Bag<String> bag = new Bag<String>();
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            bag.add(item);
        }

        StdOut.println("size of bag = " + bag.size());
        for (String s : bag) {
            StdOut.println(s);
        }
        
    }

}
