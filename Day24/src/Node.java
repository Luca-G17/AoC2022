import java.awt.*;
import java.util.Objects;

public class Node {
    private int distance;
    private Point position = new Point();
    private Node parent;

    public Node(int distance, Point position, Node parent) {
        this.distance = distance;
        this.position.setLocation(position);
        this.parent = parent;
    }
    public Node getParent() { return parent; }
    public void setParent(Node parent) { this.parent = parent; }
    public int getDistance() { return distance; }
    public void setDistance(int distance) { this.distance = distance; }
    public Point getPosition() { return position; }
    public void setPosition(Point position) { this.position = position; }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() == this.getClass()) {
            Node n = (Node) obj;
            return n.getPosition().equals(this.getPosition()) && n.getDistance() == this.getDistance();
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPosition().x, getPosition().y, getDistance());
    }
}
