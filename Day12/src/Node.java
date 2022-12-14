import java.awt.*;

public class Node implements Comparable<Node> {
    private double f = 0;
    private double g = 0;
    private final Point coords;
    private double h = 0;
    private int height;
    private Node parent = null;

    public Node(int height, int x, int y) {
        this.height = height;
        this.coords = new Point(x, y);
    }

    public double getF() { return f; }
    public double getG() { return g; }
    public double getH() { return h; }
    public int getHeight() { return height; }
    public Point getCoords() { return coords; }
    public Node getParent() { return parent; }

    public void setHeight(int height) { this.height = height; }
    public void setParent(Node parent) { this.parent = parent; }
    public void setF(Double f) { this.f = f; }
    public void setG(double g) { this.g = g; }
    public void setH(double h) { this.h = h; }

    @Override
    public int compareTo(Node o) {
        return Double.compare(this.f, o.getF());
    }
}
