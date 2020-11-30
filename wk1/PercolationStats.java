import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.StdOut;

public class PercolationStats {
    private final double mean;
    private final double stddev;
    private final double confidenceLo;
    private final double confidenceHi;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException();
        }

        double[] thresholds = new double[trials];

        for (int i = 0; i < trials; i++) {
            thresholds[i] = thresholdEstimate(n);
        }

        mean = StdStats.mean(thresholds);
        stddev = StdStats.stddev(thresholds);
        confidenceLo = mean - (1.96 * stddev) / (Math.sqrt(trials));
        confidenceHi = mean + (1.96 * stddev) / (Math.sqrt(trials));

    }

    // sample mean of percolation threshold
    public double mean() {
        return mean;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return stddev;
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return confidenceLo;
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return confidenceHi;
    }

    // simulates one trial on n-by-n grid 
    // returns percolation threshold result
    private double thresholdEstimate(int n) {
        Percolation perky = new Percolation(n);
        while (!perky.percolates()) {
            int row = StdRandom.uniform(1, n + 1);
            int col = StdRandom.uniform(1, n + 1);
            perky.open(row, col);
        }
        return (double) perky.numberOfOpenSites() / (double) (n * n);
    }
    
    // test client
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int t = Integer.parseInt(args[1]);
        PercolationStats stats = new PercolationStats(n, t);
        StdOut.printf("\nmean\t\t= %.6f", stats.mean());
        StdOut.printf("\nstddev\t\t= %.6f", stats.stddev());
        StdOut.printf("\n95%% confidence interval = [%.6f, %.6f]", stats.confidenceLo(), stats.confidenceHi());
    }
}
