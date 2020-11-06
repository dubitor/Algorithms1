import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;
import java.util.LinkedList;
import java.util.Comparator;


// TO DO:
// --- apparently doesn't find quickest solution for some reason...
public class Solver {

    private SearchNode min;
    private final SearchNode solved;
    private LinkedList<Board> solution;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {

        if (initial == null) {
            throw new IllegalArgumentException();
        }

        min = new SearchNode(initial);

        if (isSolvable()) {
            this.solved = min;
        }
        else {
            this.solved = null;
        }
    }




    // is the initial board solvable? (see below)
    public boolean isSolvable() {

        if (solved != null) { // already solved it
            return true;
        }

        MinPQ<SearchNode> minPQ = new MinPQ<SearchNode>(min.getComparator());
        minPQ.insert(min);

        SearchNode twin = new SearchNode(min.board.twin(), "twin");
        MinPQ<SearchNode> twinPQ = new MinPQ<SearchNode>(min.getComparator());
        twinPQ.insert(twin);

        while (!min.board.isGoal()) {
            if (min.isTwin) {
                nextStep(minPQ);
            }
            else {
                nextStep(twinPQ);
            }
        }

        if (min.isTwin) {
            return false;
        }
        return true;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (solved == null) { // not solvable
            return -1;
        }
        return min.moves;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (solved == null) {
            return null;
        }

        if (solution == null) {

            solution = new LinkedList<Board>();
            SearchNode current = solved;
            while (current != null) {
                solution.addFirst(current.board);
                current = current.previous;
            }
        }

        return solution;
    }

    // need to change to make sure getting twin or not
    // loop involving removing min, checking and repeating then readding
    private void nextStep(MinPQ<SearchNode> thisPQ) {
        
        min = thisPQ.delMin();
        for (Board neighbour : min.board.neighbors()) {
            if (min.previous == null || neighbour != min.previous.board) { // critical optimisation
                thisPQ.insert(new SearchNode(neighbour, min));
            }
        }
    }

    private class SearchNode  { 

        private final Board board;
        private final int moves;
        private final int manhattan;
        private final int manhattanPriority;
        private final SearchNode previous;
        private final boolean isTwin;

        private SearchNode(Board board, SearchNode previous) {
            this.board = board;
            this.manhattan = board.manhattan();
            this.previous = previous;
            this.moves = previous.moves + 1;
            this.manhattanPriority = moves + manhattan;
            this.isTwin = this.previous.isTwin;
        }

        // overloaded constructor for first node
        // where moves = 0; previous = null
        private SearchNode(Board board) {
            this.board = board;
            this.moves = 0;
            this.manhattan = board.manhattan();
            this.manhattanPriority = moves + manhattan;
            this.previous = null;
            this.isTwin = false;
        }

        // overloaded constructor for first twin node
        private SearchNode(Board board, String twin) {
            this.board = board;
            this.moves = 0;
            this.manhattan = board.manhattan();
            this.manhattanPriority = moves + manhattan;
            this.previous = null;
            this.isTwin = true;
        }

        private Comparator<SearchNode> getComparator() {
            return new ManhattanComparator();
        }

        // comparator for SearchNodes
        private class ManhattanComparator implements Comparator<SearchNode> {

            public int compare(SearchNode s1, SearchNode s2) {

                if (s1.manhattanPriority > s2.manhattanPriority) {
                    return 1;
                }
                else if (s1.manhattanPriority > s2.manhattanPriority) {
                    return -1;
                }
                else { // breaking ties by manhattan distance
                    if (s1.manhattan > s2.manhattan) {
                        return 1;
                    }
                    else if (s1.manhattan < s2.manhattan) {
                        return -1;
                    }
                    else {
                        return 0;
                    }
                }
            }
        }
    }



    // test client (see below) 
    public static void main(String[] args) {

		// create initial board from file
		In in = new In(args[0]);
		int n = in.readInt();
		int[][] tiles = new int[n][n];
		for (int i = 0; i < n; i++)
			for (int j = 0; j < n; j++)
				tiles[i][j] = in.readInt();
		Board initial = new Board(tiles);

		// solve the puzzle
		Solver solver = new Solver(initial);

		// print solution to standard output
		if (!solver.isSolvable())
			StdOut.println("No solution possible");
		else {
			StdOut.println("Minimum number of moves = " + solver.moves());
			for (Board board : solver.solution())
				StdOut.println(board);
		}
	} 

}
