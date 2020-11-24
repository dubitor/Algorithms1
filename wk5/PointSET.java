import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.In;
import java.util.TreeSet;
import java.util.ArrayDeque;

public class PointSET {

    private final TreeSet<Point2D> bst;

    // construct an empty set of points
    public PointSET() {
       bst = new TreeSet<Point2D>(); 
    }

    // is the set empty?
    public boolean isEmpty() {
        return bst.isEmpty();
    }

    // number of points in the set
    public int size() {
        return bst.size();
    }

    // add the point to the set (if it's not already in the set)
    public void insert(Point2D p) {

        if (p == null) {
            throw new IllegalArgumentException("Argument cannot be null"); 
        }

        bst.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {

        if (p == null) {
            throw new IllegalArgumentException("Argument cannot be null"); 
        }
        
        return bst.contains((Object) (p));
    }

    // draw all points to standard draw
    public void draw() {
        for (Point2D p : bst) { p.draw(); }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
    
        if (rect == null) {
            throw new IllegalArgumentException("Argument cannot be null"); 
        }

        ArrayDeque<Point2D> deque = new ArrayDeque<Point2D>();

        for (Point2D p : bst) {
            if (inRect(p, rect)) {
                deque.add(p);
            }
        }

        return deque;
    }

    // a nearest neighbour in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {

        if (p == null) {
            throw new IllegalArgumentException("Argument cannot be null"); 
        }

        if (isEmpty()) { return null; } 

        Point2D champion = bst.first();

        for (Point2D challenger : bst) {
            if (challenger.distanceSquaredTo(p) < champion.distanceSquaredTo(p)) {
                champion = challenger;
            }
        }

        return champion;
    }


    // is point in the rectangle?
    private boolean inRect(Point2D p, RectHV rect) {

        return (p.x() >= rect.xmin() && p.x() <= rect.xmax()
                && p.y() >= rect.ymin() && p.y() <= rect.ymax());
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {

        // unit test constructor and ops on empty set
        PointSET set = new PointSET();
        System.out.println("constructed pointset");
        System.out.printf("\npointset empty? %b", set.isEmpty());
        System.out.printf("\npointset size? %d\n", set.size());

        // taking file from stdin, create points and add to set
        In in = new In(args[0]);
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            set.insert(new Point2D(x, y));
        }

        System.out.println("added points to pointset");
        System.out.printf("\npointset empty? %b", set.isEmpty());
        System.out.printf("\npointset size? %d", set.size());

        // test draw and range methods
        RectHV rect = new RectHV(0.3, 0.5, 0.5, 0.8);
        rect.draw();
        set.draw();
        System.out.println("list of points in rectangle:\n");
        for (Point2D p : set.range(rect)) {
            System.out.printf("\t%s", p.toString());
        }

        // test nearest method
        Point2D botLeft = new Point2D(rect.xmin(), rect.ymin());
        Point2D nearest = set.nearest(botLeft);
        System.out.printf("\nbottom left corner of rectangle: %s", botLeft.toString());
        System.out.printf("\nnearest point to bottom left corner of rectangle: %s", 
                nearest.toString());
    }

}
