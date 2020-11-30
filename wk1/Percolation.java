import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private final WeightedQuickUnionUF UF;
    private final int n;
    private final int nSqd;
    private final int[] openSites;
    private int numberOpen;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) { throw new IllegalArgumentException("n must be positive"); }
        this.n = n;
        nSqd = n * n;
        UF = new WeightedQuickUnionUF(nSqd + 2);
        openSites = new int[nSqd + 2];
        openSites[0] = 1; // open virtual top site
        openSites[nSqd + 1] = 1; // open virtual bottom site
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (outOfRange(row, col)) {
            throw new IllegalArgumentException("index out of range");
        }
        if (isOpen(row, col)) {
            return;
        }
        openSites[gridTo1D(row, col)] = 1;
        connectToNeighbours(row, col);
        numberOpen++;
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (outOfRange(row, col)) {
            throw new IllegalArgumentException("index out of range");
        }
        return openSites[gridTo1D(row, col)] == 1;
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (outOfRange(row, col)) {
            throw new IllegalArgumentException("index out of range");
        }
        return connected(0, gridTo1D(row, col));
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        int x = numberOpen;
        return x;
    }

    // does the system percolate?
    public boolean percolates() {
        return connected(0, nSqd + 1);
    }

    // converts row-col index to 1D coord from 1 to n * n
    // for use in UF data type
    private int gridTo1D(int row, int col) {
        return (row - 1) * n + col;
    }

    // returns false iff no such site in grid
    private boolean outOfRange(int row, int col) {
        return row < 1 || row > n || col < 1 || col > n;
    }

    // is p connected to q? (1D)
    private boolean connected(int p, int q) {
        return UF.find(p) == UF.find(q);
    }

    // connect site at (row, col) to whichever of its neighbours are open and in range
    private void connectToNeighbours(int row, int col) {
        connect(row, col, row, col + 1);
        connect(row, col, row, col - 1);
        connect(row, col, row - 1, col);
        connect(row, col, row + 1, col);
    }

    // connects (r1, c1) to (r2, c2) iff the latter is open and in range
    // or it is can be interpreted as referring to a virtual site
    private void connect(int r1, int c1, int r2, int c2) {
        if (r2 == 0) { // top row to virtual top
            UF.union(0, gridTo1D(r1, c1));
        }

        if (r2 == n + 1) { // bottom row to virtual bottom site
            UF.union(gridTo1D(r1, c1), nSqd + 1);
        }

        if (outOfRange(r2, c2) || !isOpen(r2, c2)) {
            return;
        }

        int p = gridTo1D(r1, c1);
        int q = gridTo1D(r2, c2);
        UF.union(p, q);

    }
}
