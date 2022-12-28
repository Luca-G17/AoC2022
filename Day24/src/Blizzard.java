import java.awt.*;

public class Blizzard {
    private final Point position = new Point();
    private final Point lastPosition = new Point();
    private Point delta;
    public Point getPosition() { return position; }
    char dir;
    public Blizzard(char dir, Point position, Point lastPosition) {
        this.dir = dir;
        this.position.setLocation(position);
        this.lastPosition.setLocation(lastPosition);
        switch (dir) {
            case '^' -> delta = new Point(0, -1);
            case 'v' -> delta = new Point(0, 1);
            case '<' -> delta = new Point(-1, 0);
            case '>' -> delta = new Point(1, 0);
        }
    }
    public Blizzard move(int width, int height) {
        int newX = position.x + delta.x;
        int newY = position.y + delta.y;
        if (newX == width - 1) newX = 1;
        else if (newX == 0) newX = width - 2;
        if (newY == height - 1) newY = 1;
        else if (newY == 0) newY = height - 2;
        Point newPos = new Point(newX, newY);
        return new Blizzard(dir, newPos, position.getLocation());
    }

    public Point getLastPosition() { return lastPosition; }
}
