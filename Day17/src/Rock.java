import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Rock {
    private final List<Point> points = new ArrayList<>();
    public Rock(List<Point> points) {
        for (Point p : points) {
            this.points.add(new Point(p.x, p.y));
        }
    }
    public int minX() {
        int min = Integer.MAX_VALUE;
        for (Point p : points) {
            min = Math.min(min, p.x);
        }
        return min;
    }
    public void translate(Point t) {
        boolean overlapping = false;
        for (Point p : points) {
            p.translate(t.x, t.y);
            if (p.x > 6 || p.x < 0)
                overlapping = true;
        }
        if (overlapping) {
            for (Point p : points) {
                p.translate(-t.x, -t.y);
            }
        }
    }
    public List<Point> getPoints() { return points; }
}
