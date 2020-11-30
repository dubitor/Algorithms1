// must support each operation in constant worst case time
// therfore use linked list
import java.util.Iterator;

public class Deque<Item> implements Iterable<Item> {

	private Node first, last;
	private int N;

	private class Node {
		Item item;
		Node next;
		Node previous;
	}

	// construct an empty deque
	public Deque() {
	}

	// is the deque empty?
	public boolean isEmpty() {

		return first == null;

	}

	// returns number of items on the deque
	public int size() {

		return N;
	}

	// add item to the front
	public void addFirst(Item item) {

		if (item == null) {
			throw new IllegalArgumentException();
		}

		Node oldfirst = first;
		first = new Node();
		first.item = item;
		first.next = oldfirst;

		// assigning last and oldfirst.previous: check if there's only one node in the deque
		if  (oldfirst == null) {
			last = first;
		}
		else  {
			oldfirst.previous = first;
		}

		N++;

	}

	// add item to the back
	public void addLast(Item item) {

		if (item == null) {
			throw new IllegalArgumentException();
		}

		Node oldlast = last;
		last = new Node();
		last.item = item;
		last.previous = oldlast;


		if (oldlast == null) {
			first = last;
		}
		else {
			oldlast.next = last;
		}

		N++;

	}

	// remove and return item from the front
	public Item removeFirst() {

		if (this.isEmpty()) {
			throw new java.util.NoSuchElementException();
		}

		Item item = first.item;
		first = first.next;
		//if (first != null) {
		//	first.previous = null;
		//}
		N--;

		if (N == 0) {
			first = null;
			last = null;
		}
		else {
			first.previous = null;
		}

		return item;

	}

	// remove and return item from the back
	public Item removeLast() {

		if (this.isEmpty()) {
			throw new java.util.NoSuchElementException();
		}
	
		Item item = last.item;	
		last = last.previous;

		N--;

		if (N == 0) {
			first = null;
			last = null;
		}
		else {
			last.next = null;
		}
		return item;

	}

	// return an iterator over items in order from front to back
	// throw an UnsupportedOperationException if the client calls
	// remove() method in the iterator
	public Iterator<Item> iterator() {

		return new DequeIterator();
	}

	private class DequeIterator implements Iterator<Item> {

		private Node current = first;

		public boolean hasNext() {

			return current != null;
		}

		public Item next() {

			if (current == null) {
				throw new java.util.NoSuchElementException();
			}

			Item item = current.item;
			current = current.next;
			return item;
		}

		public void remove() {
			
			throw new UnsupportedOperationException();
		}
	}
	
	private void print() {

		Iterator<Item> itr = this.iterator();
		while (itr.hasNext()) {
			System.out.print(itr.next() + " ");
		}
		System.out.println();
	}

	// basic unit testing
	public static void main(String[] args) {

		Deque<String> d1 = new Deque<>();
		System.out.println(d1.isEmpty());
		d1.addFirst("b");
		System.out.println(d1.isEmpty());
		d1.print();
		System.out.println(" iterated once");
		d1.addLast("c");
		d1.print();
		System.out.println(" added c to end and iterated again");
		d1.addFirst("a");
		d1.print();
		System.out.println(" addedFirst a and iterated again");
		d1.removeLast();
		System.out.println(" removed last");
		d1.print();
		System.out.println("...and iterated again");
		d1.removeFirst();
		System.out.println("removed first");
		d1.print();
		System.out.println("and iterated a last time");
		System.out.println(d1.size());

	}

}
