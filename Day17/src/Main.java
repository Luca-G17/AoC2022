import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        BufferedReader reader;
        String jets = "";
        try {
            reader = new BufferedReader(new FileReader("input.txt"));
            String line = reader.readLine();
            while(line != null) {
                jets = line;
                line = reader.readLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        List<Point>[] shapes = generateShapes();
        boolean[][] world = new boolean[10000][7];
        long h = processNShapes(world, 1000000000000L, jets, shapes);
        System.out.printf("Highest point after 2022 rocks: %d\n", h);
    }
    public static void printWorld(boolean[][] world) {
        for (int i = 20; i >= 0; i--) {
            for (int f = 0; f < world[i].length; f++) {
                if (world[i][f])
                    System.out.print("#");
                else
                    System.out.print(".");
            }
            System.out.println();
        }
        System.out.println();
    }
    public static long processNShapes(boolean[][] world, long n, String jets, List<Point>[] shapes) {
        State c = findCycle(world, n, jets, shapes);
        int heightBeforeCycle = c.getRockIndex() - c.getCycleLength();
        long totalCycles = Math.floorDiv(1000000000000L - heightBeforeCycle, c.getCycleLength());
        long rem = 1000000000000L - (totalCycles * c.getCycleLength()) - heightBeforeCycle;
        int j = c.getJetIndex();
        world = new boolean[3000][7];
        Rock start = new Rock(shapes[c.getRockType()]);
        start.translate(new Point(c.getMinX(), 0));
        lockRock(world, start);
        int highestPoint = highestPoint(world, 0);
        int startHeight = highestPoint;
        for (int i = 0; i < rem; i++) {
            Rock r = new Rock(shapes[i % 5]);
            highestPoint = highestPoint(world, highestPoint);
            r.translate(new Point(2, highestPoint + 3));
            j = processShape(world, r, jets, j);
            printWorld(world);
        }
        int heightBeforeStart = c.getHighestPoint() - c.getCycleHeight();
        return highestPoint + heightBeforeStart + (c.getCycleHeight() * totalCycles) - startHeight - 1;
    }
    public static State findCycle(boolean[][] world, long n, String jets, List<Point>[] shapes) {
        int highestPoint = 0;
        int j = 0;
        List<State> states = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            Rock r = new Rock(shapes[i % 5]);
            r.translate(new Point(2, highestPoint + 3));
            j = processShape(world, r, jets, j);
            highestPoint = highestPoint(world, highestPoint);

            State newState = new State(i % 5, j, i, r.minX(), r, highestPoint);
            State s1 = containsState(states, newState);
            if (s1 != null) {
                newState.setCycleLength(i - s1.getRockIndex());
                newState.setCycleHeight(highestPoint - s1.getHighestPoint());
                return newState;
            } else {
                states.add(newState);
            }
            // printWorld(world);
        }
        return null;
    }
    public static State containsState(List<State> states, State s) {
        for (int i = 0; i < states.size(); i++) {
            if (states.get(i).equals(s))
                return states.get(i);
        }
        return null;
    }
    public static int highestPoint(boolean[][] world, int prevHighest) {
        int highest = prevHighest;
        for (int i = prevHighest; i < world.length; i++) {
            for (int f = 0; f < world[i].length; f++) {
                if (world[i][f])
                    highest = i + 1;
            }
        }
        return highest;
    }
    public static int processShape(boolean[][] world, Rock r, String jetStr, int j) {
        char[] jets = jetStr.toCharArray();
        while(!rocksOverlap(world, r)) {
            Point translation;
            if (jets[j] == '<') {
                translation = new Point(-1, 0);
            }
            else {
                translation = new Point(1, 0);
            }
            r.translate(translation);
            if (rocksOverlap(world, r)){
                r.translate(new Point(-translation.x, -translation.y));
            }
            j = (j + 1) % jetStr.length();
            r.translate(new Point(0, -1));
        }
        r.translate(new Point(0, 1));
        lockRock(world, r);
        return j;
    }
    public static void lockRock(boolean[][] world, Rock r) {
        for (Point p : r.getPoints()) {
            world[p.y][p.x] = true;
        }
    }
    public static boolean rocksOverlap(boolean[][] world, Rock r) {
        for (Point p : r.getPoints()) {
            if (p.y == -1)
                return true;
            if (world[p.y][p.x])
                return true;
        }
        return false;
    }
    public static List<Point>[] generateShapes() {
        return new List[]{
                new ArrayList<>(List.of(
                        new Point(0, 0),
                        new Point(1, 0),
                        new Point(2, 0),
                        new Point(3, 0)
                )),
                new ArrayList<>(List.of(
                        new Point(1, 0),
                        new Point(0, 1),
                        new Point(1, 1),
                        new Point(2, 1),
                        new Point(1, 2)
                )),
                new ArrayList<>(List.of(
                        new Point(0, 0),
                        new Point(1, 0),
                        new Point(2, 0),
                        new Point(2, 1),
                        new Point(2, 2)
                )),
                new ArrayList<>(List.of(
                        new Point(0, 0),
                        new Point(0, 1),
                        new Point(0, 2),
                        new Point(0, 3)
                )),
                new ArrayList<>(List.of(
                        new Point(0, 0),
                        new Point(0, 1),
                        new Point(1, 0),
                        new Point(1, 1)
                ))
        };
    }
}