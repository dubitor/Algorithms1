import java.util.Stack;
import java.util.Arrays;
import edu.princeton.cs.algs4.In;

public class Board {

    private final int[][] tiles;
    private final int n;
    private int[] blank; // location of blank square -- could this be local to neighbors() method?

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        if (tiles == null) {
            throw new IllegalArgumentException("parameter 'int[][] tiles' cannot be null");
        }
        if (tiles.length < 2 || tiles.length >= 128) {
            throw new IllegalArgumentException("n must be in range [2, 128)");
        }
        this.n = tiles.length;
        this.tiles = getTilesCopy(tiles);
    }
                                           
    // string representation of this board
    public String toString() {
        StringBuilder s = new StringBuilder();
            s.append(n + "\n");
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    s.append(String.format("%2d ", tiles[i][j]));
                }
                s.append("\n");
            }
        return s.toString();
    }

    // board dimension n
    public int dimension() {
        return n;
    }

    // number of tiles out of place
    public int hamming() {

        int ham = 0;

        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                if (tiles[row][col] == 0) { ; }// if it's the blank space we don't care
                else if (!tileInPlace(row, col)) {
                   ham++;
                }
            }
        }
        return ham;
    }


    // sum of Manhattan distances between tiles and goal
    public int manhattan() {

        int man = 0;
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                if (tiles[row][col] != 0) { // we don't want to calculate for the blank tile
                    int vDist = verticalDistance(row, col); 
                    int hDist = horizontalDistance(row, col); 
                    man += vDist + hDist;
                }
            }
        }
        return man;
    }


    // is this board the goal board?
    public boolean isGoal() {
        return hamming() == 0;
    }

    // does this board equal y?
    // oddly, you can access private instance vars directly
    // from another instance of the same class
    public boolean equals(Object y) {

        if (y == null) {
            return false;
        }
        if (this == y) { // do they refer to the same object?
            return true;
        }
        if (this.getClass() != y.getClass()) {
            return false;
        }

        Board that = (Board) y; // guaranteed to succeed because of previous condition

        if (this.n != that.n) {
            return false;
        }
        return Arrays.deepEquals(this.tiles, that.tiles);
    }


    // all neighboring boards
    // a board can have 2, 3, or 4 neighbors, depending 
    // on location of black square. If blank in corner, 2 neighb.
    // If blank[x] == 0 or n-1 or blank[y] = 0 or n-1, 3 neigb
    // Else 4 neighb.
    // -- find blank square
    public Iterable<Board> neighbors() {
        Stack<Board> neighbors = new Stack<Board>();
        findBlank();
        if (blank[0] == 0) { // blank's in first column (x = 0)
            neighbors.push(makeNeighbor("right"));
            if (blank[1] == 0) { // blank's in top left corner
                neighbors.push(makeNeighbor("down"));
                return neighbors;
            }
            if (blank[1] == n - 1) { // blank's in bottom left corner
                neighbors.push(makeNeighbor("up"));
                return neighbors;
            }
            neighbors.push(makeNeighbor("up"));
            neighbors.push(makeNeighbor("down"));
            return neighbors;
        }
        else if (blank[0] == n - 1) { // blank's in last column
            neighbors.push(makeNeighbor("left"));
            if (blank[1] == 0) { // blank's in top right corner
                neighbors.push(makeNeighbor("down"));
                return neighbors;
            }
            if (blank[1] == n - 1) { // blank's in bottom right corner
                neighbors.push(makeNeighbor("up"));
                return neighbors;
            }
            neighbors.push(makeNeighbor("up"));
            neighbors.push(makeNeighbor("down"));
            return neighbors;
        }
        else if (blank[1] == 0) { // blank's somewhere in the middle of first row
            neighbors.push(makeNeighbor("left"));
            neighbors.push(makeNeighbor("right"));
            neighbors.push(makeNeighbor("down"));
            return neighbors;
        }
        else if (blank[1] == n - 1) { // blank's somewhere in the middle of last row
            neighbors.push(makeNeighbor("left"));
            neighbors.push(makeNeighbor("right"));
            neighbors.push(makeNeighbor("up"));
            return neighbors;
        }
        else { // blank's somewhere in the middle
            neighbors.push(makeNeighbor("left"));
            neighbors.push(makeNeighbor("right"));
            neighbors.push(makeNeighbor("up"));
            neighbors.push(makeNeighbor("down"));
            return neighbors;
        }
    }



    // a board that is obtained by exchanging any pair of tiles
    // (the blank is not a tile)
    public Board twin() {
        if (tiles[0][0] == 0) {
            return twin(0, 1, 1, 1);
        }
        if (tiles[0][1] == 0) {
            return twin(0, 0, 1, 0);
        }
        return twin(0, 0, 0, 1);
    }

    // makes a twin by swapping tile (a,b) with (c,d)
    private Board twin(int a, int b, int c, int d) {
        int[][] tilesCopy = getTilesCopy();
        int temp = tilesCopy[a][b];
        tilesCopy[a][b] = tilesCopy[c][d];
        tilesCopy[c][d] = temp;
        return new Board(tilesCopy);
    }

    // a neighbouring board where the blank has been moved in
    // the direction supplied. Acceptable inputs are "up", "down",
    // "left" and "right"
    private Board makeNeighbor(String direction) {
        int[][] tilesCopy = getTilesCopy();

        if (direction.equals("up")) {
            tilesCopy[blank[0]][blank[1]] = tilesCopy[blank[0]][blank[1] - 1];
            tilesCopy[blank[0]][blank[1] - 1] = 0;
        }
        else if (direction.equals("down")) {
            tilesCopy[blank[0]][blank[1]] = tilesCopy[blank[0]][blank[1] + 1];
            tilesCopy[blank[0]][blank[1] + 1] = 0;
        }
        else if (direction.equals("left")) {
            tilesCopy[blank[0]][blank[1]] = tilesCopy[blank[0] - 1][blank[1]];
            tilesCopy[blank[0] - 1][blank[1]] = 0;
        }
        else if (direction.equals("right")) {
            tilesCopy[blank[0]][blank[1]] = tilesCopy[blank[0] + 1][blank[1]];
            tilesCopy[blank[0] + 1][blank[1]] = 0;
        }
        else {
            throw new IllegalArgumentException("direction must be 'up', 'down', 'left' or 'right'");
        }
            
        return new Board(tilesCopy);
    }


    // the coordinates of the blank square
    // i.e. [row, col] where tiles[row][col] is blank
    private void findBlank() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (tiles[i][j] == 0) {
                    this.blank = new int[] { i, j };
                    return;
                }
            }
        }
        throw new NullPointerException("Couldn't find blank square");
    }

    // an independent copy of this board's tiles array
    private int[][] getTilesCopy() {
        int[][] tilesCopy = new int[n][n];
        for (int i = 0; i < n; i++) {
            tilesCopy[i] = Arrays.copyOf(tiles[i], n);
        }
        return tilesCopy;
    }

    // an independent copy of the input's tiles array
    private int[][] getTilesCopy(int[][] tiles) {
        int[][] tilesCopy = new int[n][n];
        for (int i = 0; i < n; i++) {
            tilesCopy[i] = Arrays.copyOf(tiles[i], n);
        }
        return tilesCopy;
    }

    // returns true if tiles[x][y] is in the right place
    // the blank space should NOT be passed to this method
    // 0-indexed array, so each square should equal x*N+y+1
    // ---> (0,0) = 1, (1,0) = N+1...
    // except the last square which should be 0
    private boolean tileInPlace(int row, int col) {
        return tiles[row][col] == row * n + col + 1 && !isLastSquare(row, col);
    }

    // returns true if tiles[x][y] is the last square     
    private boolean isLastSquare(int row, int col) {
        return row == n - 1 && col == n - 1;
    }

    // number of columns away from goal column (i.e. goal col)
    // the goal column index is tiles[row][col] % N - 1
    private int horizontalDistance(int row, int col) {
        int val = tiles[row][col];
        if (val % n == 0) { // tile should be in last column
            return (n - 1) - col;
        }
        return Math.abs(col - (val % n - 1));
    }


    // number of rows away from goal row (i.e. goal row)
    // the goal row index of a given value is calculated
    // by floor dividing that value by n
    // I've trid to do some sneakiness to overcome the fact
    // that if the tile's goal column is the last it is difficult
    // to calculate the goal row (hence the - 1)
    // n.b. assumes tiles[row][col] != 0
    private int verticalDistance(int row, int col) {
        return Math.abs(row - (tiles[row][col] - 1 )/ n);
    }

    // unit testing
    public static void main(String[] args) {

        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j ++) {
                tiles[i][j] = in.readInt();
            }
        }

        Board b = new Board(tiles);
        System.out.printf("\nn = %d", b.dimension());
        System.out.printf("\n%s", b.toString());
        System.out.printf("\nhamming: %d   manhattan: %d", b.hamming(), b.manhattan());
        System.out.printf("\nis goal? %b", b.isGoal());
        System.out.printf("\nboard == board? %b\n", b.equals(b));

        Board twin = b.twin();
        System.out.printf("\ntwin board: \n%s", twin.toString());
        System.out.printf("\nboard == twin? %b", b.equals(twin));
        System.out.printf("\ntwin == board? %b\n", twin.equals(b));

        for (Board neighbor : b.neighbors()) {
            System.out.printf("\nprinting a neighbour: %s", neighbor.toString());
            System.out.printf("\nhamming: %d   manhattan: %d", neighbor.hamming(), neighbor.manhattan());
            System.out.printf("\nis goal? %b\n", neighbor.isGoal());
            System.out.printf("\nneighbour == board? %b\n", neighbor.equals(b));
        }
    }
}
