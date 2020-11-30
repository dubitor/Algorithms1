import edu.princeton.cs.algs4.StdIn;
import java.util.Iterator;

public class Permutation {

	public static void main(String[] args) {

		int k = Integer.parseInt(args[0]);

		RandomizedQueue<String> randQ = new RandomizedQueue<String>();
		while (!StdIn.isEmpty()) {
			randQ.enqueue(StdIn.readString());	
		}

		Iterator<String> itr = randQ.iterator();

		for (int i = 0; i < k; i++) {
			System.out.println(itr.next());
		}
	}
}
