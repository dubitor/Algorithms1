import edu.princeton.cs.algs4.StdRandom;
import java.util.Iterator;


public class RandomizedQueue<Item> implements Iterable<Item> {

	private int N;
	private Item[] s;

	// construct an empty randomized queue
	public RandomizedQueue() {

		s = (Item[]) new Object[1];
	}

	// is the randomised queue empty?
	public boolean isEmpty() {

		return N == 0;
	}

	// return the number of items in the queue
	public int size() {

		return N;
	}

	// add an item
	public void enqueue(Item item) {

		if (item == null) {
			throw new IllegalArgumentException();
		}

		if (N == s.length) {
			resize(2 * s.length);
		}
		s[N++] = item;
	}

	// copy all items to a new array of a specified size
	// how to make sure that all of the items are copied, when they're interspersed with nulls?
	// answer: with two variables
	private void resize(int capacity) {

		Item[] copy = (Item[]) new Object[capacity];
		for (int i = 0; i < N; i++) {
			copy[i] = s[i];
		}
		s = copy;
	}

	// remove and return a random item
	public Item dequeue() {

		// pick random item, set its entry to null and decrement N
		
		if (this.isEmpty()) { throw new java.util.NoSuchElementException(); }

		int i = StdRandom.uniform(N);
		Item item = s[i];
		s[i] = s[N - 1];
		s[N - 1] = null;
		N--;

		// resize if neccessary
		if (N > 0 && N == s.length / 4) {
			resize(s.length / 2);
		}

		return item;
	}

	// return a random item but do not remove it
	public Item sample() {

		if (this.isEmpty()) { throw new java.util.NoSuchElementException(); }

		return s[StdRandom.uniform(N)];
	}

	// return and independent iterator over items in random order
	public Iterator<Item> iterator() {

		return new RandomizedQueueIterator();
	}


	// constructor creates randomly sorted copy of the items in s using inside out Fisher Yates shuffle (linear time)
	// hasNext just loops through (constant time for each call)
	private class RandomizedQueueIterator  implements Iterator<Item> {

		private final Item[] arr;
		private int count;


		public RandomizedQueueIterator() {

			super();
			arr = (Item[]) new Object[N];
			for (int i = 0; i < N; i++) {
				int j = StdRandom.uniform(i + 1);
				arr[i] = arr[j];
				arr[j] = s[i];
			}

		}


		public boolean hasNext() {

			return count < N;
		}

		public Item next() {

			if (!this.hasNext()) {
				throw new java.util.NoSuchElementException();
			}
			
			return arr[count++];
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}
	}



	// unit testing
	public static void main(String[] args) {

		RandomizedQueue<String> randy = new RandomizedQueue<String>();
		randy.enqueue("a");
		randy.enqueue("b");
		randy.enqueue("c");
		System.out.print(randy.size());
		System.out.print(randy.sample());
		System.out.print(randy.dequeue());
		System.out.print(randy.size());

		System.out.println();

		Iterator<String> itr = randy.iterator();
		System.out.println("iterator has next? ");
		System.out.print(itr.hasNext());
		while (itr.hasNext()) {
			System.out.print(itr.next() + " ");
		}
	}


}
