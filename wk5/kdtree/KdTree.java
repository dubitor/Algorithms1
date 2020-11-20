import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {

    private Node root;
    private int n;

    // construct an empty set of points
    public KdTree() {
    }

    // is the set empty?
    public boolean isEmpty() {
        return root == null;
    }

    // number of points in the set
    public int size() {
        int copy = n;
        return copy;
    }

    // add the point to the set (if it's not already in the set)
    public void insert(Point2D p) {
        if (p == null) { throw new IllegalArgumentException("Argument cannot be null"); }

        if (!contains(p)) {
            double[] rectCorners = {0, 0, 1, 1}; // represents rectangle's xmin, ymin, xmax, & ymax
            root = insertVert(root, p, rectCorners);
            n++;
        }
    }
    
    // recursive helper method - is p left or right of node? go right if tie
    // (odd levels: divides rectangle by drawing vertical line; compare x coords)
    private Node insertVert(Node node, Point2D p, double[] corners) {

        if (node == null) {
            return new Node(p, corners);
        }
        if (p.x() < node.x()) {
            corners[2] = node.x(); // change xmax of rectangle if upper x found
            node.lb = insertHori(node.lb, p, corners);
        }
        else {
            corners[0] = node.x(); // change xmin
            node.rt = insertHori(node.rt, p, corners);
        }
        return node;
    }

    // recursive helper method - is p above or below node? go up if tie (i.e. right on tree)
    // (even levels: divides rectangle by drawing horizontal line; compare y coords)
    private Node insertHori(Node node, Point2D p, double[] corners) {

        if (node == null) {
            return new Node(p, corners);
        }
        if (p.y() < node.y()) {
            corners[3] = node.y(); // change ymax of rect if node marks top of rect
            node.lb = insertVert(node.lb, p, corners);
        }
        else {
            corners[1] = node.y(); // change ymin
            node.rt = insertVert(node.rt, p, corners);
        }
        return node;
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) { throw new IllegalArgumentException("Argument cannot be null"); }

        return containsVert(root, p);
    }

    // searches the tree for p, using x coords as key
    // (vertical partition: odd levels)
    private boolean containsVert(Node node, Point2D p) {
        if (node == null) {
            return false;
        }
        if (p.equals(node.p)) {
            return true;
        }
        if (p.x() < node.x()) {
            return containsHori(node.lb, p);
        }
        else {
            return containsHori(node.rt, p);
        }
    }

    // searches the tree for p, using y coords as key
    // (horizontal partition: even levels)
    private boolean containsHori(Node node, Point2D p) {
        if (node == null) {
            return false;
        }
        if (p.equals(node.p)) {
            return true;
        }
        if (p.y() < node.y()) {
            return containsVert(node.lb, p);
        }
        else {
            return containsVert(node.rt, p);
        }
    }

    // draw all points to standard draw
    // vertical partitions drawn in red; horizonatal ones in blue
    public void draw() {
        drawRed(root);
    }

    private void drawRed(Node node) {
        if (node == null) {
            return;
        }
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        node.p.draw();

        StdDraw.setPenColor(StdDraw.RED);
        StdDraw.setPenRadius();
        StdDraw.line(node.x(), node.rect.ymin(), node.x(), node.rect.ymax());

        drawBlue(node.lb);
        drawBlue(node.rt);
    }
        
    private void drawBlue(Node node) {
        if (node == null) {
            return;
        }
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        node.p.draw();

        StdDraw.setPenColor(StdDraw.BLUE);
        StdDraw.setPenRadius();
        StdDraw.line(node.rect.xmin(), node.y(), node.rect.xmax(), node.y());

        drawRed(node.lb);
        drawRed(node.rt);
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) { throw new IllegalArgumentException("Argument cannot be null"); }

        Queue<Point2D> queue = new Queue<Point2D>();
        range(root, rect, queue);
        return queue;
    }

    private void range(Node node, RectHV rect, Queue<Point2D> queue) {
        if (node == null) { // return if reach end of tree
            return;
        }
        if (!rect.intersects(node.rect)) { // go back up tree unless rect intersects query rect
            return;
        }
        if (node.isIn(rect)) {
            queue.enqueue(node.p);
        }
        range(node.lb, rect, queue);
        range(node.rt, rect, queue);
    }

    // a nearest neighbour in the set to point p; null if the set is empty
    //n.b. consider equidistant case
    // hmmm....
    public Point2D nearest(Point2D p) {
        if (p == null) { throw new IllegalArgumentException("Argument cannot be null"); }
        if (isEmpty()) { return null; }

        return nearestVert(p, root, root.p, root.rect.distanceSquaredTo(p));
    }

    // vertical partition; odd levels; x coords
    private Point2D nearestVert(Point2D p, Node current, Point2D champion, double champDist) {
        if (current == null) { // reached end of branch
            return champion;
        }
        if (current.rect.distanceSquaredTo(p) >= champDist) { // nothing in this sub.tree is closer than current champion
            return champion;
        }

        double currentDist = current.p.distanceSquaredTo(p);
        if (currentDist < champDist) { // found new champion
            champion = current.p;
            champDist = currentDist;
        }

        if (current.x() > champion.x()) { // go left first if champion to the left of current point
            champion = nearestHori(p, current.lb, champion, champDist);
            champion = nearestHori(p, current.rt, champion, champDist);
        }
        else  { // else go right
            champion = nearestHori(p, current.rt, champion, champDist);
            champion = nearestHori(p, current.lb, champion, champDist);
        }
        return champion;
    }
            
    // even nodes, horizontal partiion, y coords
    private Point2D nearestHori(Point2D p, Node current, Point2D champion, double champDist) {
        if (current == null) { // reached end of branch
            return champion;
        }
        if (current.rect.distanceSquaredTo(p) >= champDist) { // nothing in this sub.tree is closer than current champion
            return champion;
        }

        double currentDist = current.p.distanceSquaredTo(p);
        if (currentDist < champDist) { // found new champion
            champion = current.p;
            champDist = currentDist;
        }

        if (current.y() > champion.y()) { // go left first if champion below current point
            champion = nearestHori(p, current.lb, champion, champDist);
            champion = nearestHori(p, current.rt, champion, champDist);
        }
        else  { // else go right
            champion = nearestHori(p, current.rt, champion, champDist);
            champion = nearestHori(p, current.lb, champion, champDist);
        }
        return champion;
    }


    private static class Node {
        private final Point2D p; // the point
        private final RectHV rect; // the axis-aligned rectangle corresponding to this node
        private Node lb; // the left/bottom subtree
        private Node rt; // the right/top subtree

        public Node(Point2D p, double[] corners) {
            this.p = p;
            this.rect = new RectHV(corners[0], corners[1], corners[2], corners[3]);
        }

        public double x() {
            return p.x();
        }

        public double y() {
            return p.y();
        }

        public boolean isIn(RectHV rect) {
            return rect.contains(p);
        }

    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        Point2D p = new Point2D(0.5, 0.5);
        Point2D q = new Point2D(0.5, 0.7);
        Point2D r = new Point2D(0.3, 0.3);
        
        KdTree kd = new KdTree();
        System.out.printf("\nempty tree is empty? %b", kd.isEmpty());
        kd.insert(p);
        kd.insert(q);
        kd.insert(r);
        kd.insert(new Point2D(0.1, 0.23));
        System.out.printf("\ntree with 4 pts empty? %b size? %d", kd.isEmpty(), kd.size());
        System.out.printf("\ncontains point not in it? %b\n\n", kd.contains(new Point2D(0.1, 0.1)));

        RectHV unitSq = new RectHV(0, 0, 1, 1);
        System.out.printf("\n\npoints within the unit square: \n");
        for (Point2D point : kd.range(unitSq)) {
            System.out.print(point.toString() + "\n");
        }

        RectHV botHalf = new RectHV(0, 0, 0.5, 0.5);
        System.out.printf("\n\npoints within the bottom half: \n");
        for (Point2D point : kd.range(botHalf)) {
            System.out.print(point.toString() + "\n");
        }

        kd.draw();
        Point2D origin = new Point2D(0, 0);
        origin.draw();
        System.out.printf("\nnearest point to the origin is: %s\n\n", kd.nearest(origin).toString());

    }

}
