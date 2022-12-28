import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class MapSegment {
    private final char[][] segment;
    private MapSegment up;
    private MapSegment down;
    private MapSegment left;
    private MapSegment right;
    private Map<MapSegment, Integer> turnings = new HashMap<>();
    public MapSegment(char[][] segment) {
        this.segment = segment;
    }

    public char[][] getSegment() { return segment; }
    public MapSegment getDown() { return down; }
    public MapSegment getUp() { return up; }
    public MapSegment getLeft() { return left; }
    public MapSegment getRight() { return right; }

    public void setDirections(MapSegment up, MapSegment down, MapSegment left, MapSegment right) {
        this.up = up;
        this.down = down;
        this.left = left;
        this.right = right;
    }
    public void setTurnDirections(int up, int down, int left, int right) {
        turnings.put(this.up, up);
        turnings.put(this.down, down);
        turnings.put(this.left, left);
        turnings.put(this.right, right);
    }
    // 0 Down to Left y = x, x = max
    // 1 Down to Right y = x, x = 0
    // 2 Up to Left y = x, x = max
    // 3 Up to Right y = x, x = 0
    // 4 Left to Up x = y, y = 0
    // 5 Left to Down x = y, y = max
    // 6 Right to Up x = y, y = max
    // 7 Right to Down x = y, y = 0
    // 8 Down to Top x = x, y = 0
    // 9 Top to Bottom x = x, y = max
    // 10 Right to Left x = 0, y = y
    // 11 Left to Right x = width, y = y
    // 12 Left to Left x = 0, y = y
    // 13 Right to Right x = max, y = y
    public int changeTurning(MapSegment dir) {
        switch (turnings.get(dir)) {
            case 1, 3, 11, 13 -> { return 2; } // Right
            case 5, 7, 9 -> { return 3; } // Down
            case 0, 2, 10, 12 -> { return 0; } // Left
            case 4, 6, 8 -> { return 1; } // Up
        }
        return -1;
    }
    public Point turnCoords(Point p, MapSegment dir, int width){
        switch (turnings.get(dir)) {
            case 0, 2 -> { return new Point(width - 1, p.x); }
            case 1, 3 -> { return new Point(0, p.x); }
            case 4, 7 -> { return new Point(p.y, 0); }
            case 5, 6 -> { return new Point(p.y, width - 1); }
            case 8 -> { return new Point(p.x, 0); }
            case 9 -> { return new Point(p.x, width - 1); }
            case 10, 12 -> { return new Point(0, p.y); }
            case 11, 13 -> { return new Point(width - 1, p.y); }
        }
        return null;
    }
    public void reassignNullNeighbors() {
        if (this.up == null)
            this.up = this;
        if (this.down == null)
            this.down = this;
        if (this.left == null)
            this.left = this;
        if (this.right == null)
            this.right = this;
    }
}

