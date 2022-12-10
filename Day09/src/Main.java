import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        BufferedReader reader;
        Point[] rope = new Point[] { new Point(0, 0), new Point(0, 0) };
        Point[] rope10 = new Point[10];
        for (int i = 0; i < rope10.length; i++) {
            rope10[i] = new Point(0, 0);
        }
        Set<Point> uniqueTailLocations = new HashSet<>();
        List<Point> uniqueTailLocations10 = new ArrayList<>();

        try {
            reader = new BufferedReader(new FileReader("input.txt"));
            String line = reader.readLine();
            while (line != null) {
                String[] command = line.split(" ");
                rope = processCommand(rope[0], rope[1], Integer.parseInt(command[1]), command[0], uniqueTailLocations);
                rope10 = processCommand(rope10, Integer.parseInt(command[1]), command[0], uniqueTailLocations10);
                line = reader.readLine();
            }
            // printTailSet(uniqueTailLocations);
            // printTailSet(uniqueTailLocations10);

            System.out.printf("Total locations visited by the tail: %d\n", uniqueTailLocations.size() + 1);
            System.out.printf("Total locations visited by the tail of the 10 length rope: %d\n", uniqueTailLocations10.size());
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean headIsAdjacent(Point head, Point tail) {
        if (head.distance(tail) < 2) {
            return true;
        }
        return false;
    }
    public static Point moveHead(Point head, int delta, boolean vert) {
        if (vert)
            head.y += delta;
        else
            head.x += delta;
        return head;
    }
    public static Point movePair(Point tail, Point head) {
        int diffX = head.x - tail.x;
        int diffY = head.y - tail.y;
        if (Math.abs(diffX) == 2 || Math.abs(diffY) == 2) {
            if (diffX > 0)
                tail.x++;
            if (diffX < 0)
                tail.x--;
            if (diffY > 0)
                tail.y++;
            if (diffY < 0)
                tail.y--;
        }
        return tail;
    }
    public static void printRopeState(Point[] rope) {
        boolean found;
        for (int i = 30; i >= 0; i--) {
            for (int f = -30; f < 30; f++) {
                found = false;
                for (Point p : rope) {
                    if (p.equals(new Point(f, i))) {
                        System.out.print("#");
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    System.out.print(".");
                }
            }
            System.out.println();
        }
    }
    public static Point[] moveRope(Point[] rope, int delta, boolean vert, int dist, List<Point> uniqueTailLocations) {
        Point[] newRope = rope;
        Point head = rope[0];
        for (int i = 0; i < dist; i++) {
            newRope[0] = moveHead(head, delta, vert);
            for (int f = 1; f < 10; f++) {
                newRope[f] = movePair(newRope[f], newRope[f - 1]);
                if (f == 9) {
                    boolean found = false;
                    for (Point p : uniqueTailLocations) {
                        if (newRope[f].x == p.x && newRope[f].y == p.y) {
                            found = true;
                            break;
                        }
                    }
                    if (!found)
                        uniqueTailLocations.add(new Point(newRope[f].x, newRope[f].y));
                }
            }
        }
        return newRope;
    }
    public static Point[] processCommand(Point[] rope, int dist, String direction, List<Point> uniqueTailLocations) {
        switch (direction) {
            case "U" -> {
                return moveRope(rope, 1, true, dist, uniqueTailLocations);
            }
            case "D" -> {
                return moveRope(rope, -1, true, dist, uniqueTailLocations);
            }
            case "R" -> {
                return moveRope(rope, 1, false, dist, uniqueTailLocations);
            }
            case "L" -> {
                return moveRope(rope, -1, false, dist, uniqueTailLocations);
            }
        }
        return null;
    }
    public static Point[] move(Point head, Point tail, int dist, int delta, boolean vert, Set<Point> uniqueTailLocations) {
        for (int i = 0; i < dist; i++) {
            Point headCopy = head.getLocation();
            if (vert)
                head = new Point(head.x, head.y + delta);
            else
                head = new Point(head.x + delta, head.y);
            if (!headIsAdjacent(head, tail)) {
                tail = headCopy.getLocation();
                uniqueTailLocations.add(tail);
            }
        }
        return new Point[] {head, tail};
    }
    public static Point[] processCommand(Point head, Point tail, int dist, String direction, Set<Point> uniqueTailLocations) {
        switch (direction) {
            case "U" -> {
                return move(head, tail, dist, 1, true, uniqueTailLocations);
            }
            case "D" -> {
                return move(head, tail, dist, -1, true, uniqueTailLocations);
            }
            case "R" -> {
                return move(head, tail, dist, 1, false, uniqueTailLocations);
            }
            case "L" -> {
                return move(head, tail, dist, -1, false, uniqueTailLocations);
            }
        }
        return null;
    }
}