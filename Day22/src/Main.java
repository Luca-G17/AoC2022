import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        BufferedReader reader;
        char[][] map = new char[1000][1000];
        MapSegment[][] segments1;
        MapSegment[][] segments2;
        String route = "";
        int r = 0;
        try {
            reader = new BufferedReader(new FileReader("input.txt"));
            String line = reader.readLine();
            while (!line.equals("")) {
                populateRow(map, line.toCharArray(), r++);
                line = reader.readLine();
            }
            route = reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        segments1 = buildMap(map);
        segments2 = buildMap(map);
        assignCubeNeighbors(segments2);
        int part1 = traverseMap(segments1, route, false);
        int part2 = traverseMap(segments2, route, true);
        System.out.printf("Encoded ending position for wrapping map: %d\n", part1);
        System.out.printf("Encoded ending position from cube map: %d\n", part2);
    }
    public static int minWidth(char[][] map) {
        int minWidth = Integer.MAX_VALUE;
        for (char[] chars : map) {
            int runStart = -1;
            for (int f = 0; f < chars.length; f++) {
                if (chars[f] != ' ' && chars[f] != 0 && runStart == -1)
                    runStart = f;
                else if ((chars[f] == ' ' || chars[f] == 0) && runStart != -1)
                    minWidth = Math.min(minWidth, f - runStart);
            }
        }
        return minWidth;
    }
    public static int[] maxCoords(char[][] map) {
        int maxX = 0;
        for (int i = 0; i < map.length; i++) {
            boolean hasChars = false;
            for (int f = 0; f < map[i].length; f++) {
                if (map[i][f] != ' ' && map[i][f] != 0) {
                    hasChars = true;
                    maxX = Math.max(maxX, f);
                }
            }
            if (!hasChars)
                return new int[] { maxX + 1, i };
        }
        return null;
    }
    // Cube Mappings
    // 0: Up = 5 - In from Left, Down = 2 - In from Top, Left = 3 - In from Left, Right = 1 - In from Left
    // 1: Up = 5 - In from Bottom, Down = 2 - In from Right, Right = 4 - In from Right, Left = 0 - In from Right
    // 2: Up = 0 - In from Bottom, Down = 4 - In from Top, Right = 1 - In from Bottom, Left = 3 - In from Top
    // 3: Up = 2 - In from Left, Down = 5 - In from Top, Right = 4 - In from Left, Left = 0 - In from Left
    // 4: Up = 2 - In from Bottom, Down = 5 - In from Right, Right = 1 - In from Right, Left = 3 - In from Right
    // 5: Up = 3 - In from Bottom, Down = 1 - In from Top, Right = 4 - In from Bottom, Left = 0 - In from Top

    // 0 Down to Left y = x, x = max
    // 1 Down to Right y = x, x = 0
    // 2 Up to Left y = x, x = max
    // 3 Up to Right y = x, x = 0
    // 4 Left to Down x = y, y = max
    // 5 Left to Up x = y, y = 0
    // 6 Right to Down x = y, y = max
    // 7 Right to Up x = y, y = 0
    // 8 Down to Top x = x, y = 0
    // 9 Up to Bottom x = x, y = max
    // 10 Right to Left x = 0, y = y
    // 11 Left to Right x = width, y = y
    // 12 Left to Left x = 0, y = y
    // 13 Right to Right x = max, y = y
    public static void assignCubeNeighbors(MapSegment[][] map) {
        int[][] turnings = new int[][] {
                new int[] { 2, 8, 12, 10 },
                new int[] { 9, 1, 11, 13 },
                new int[] { 9, 8, 5, 6 },
                new int[] { 2, 8, 12, 10 },
                new int[] { 9, 1, 11, 13 },
                new int[] { 9, 8, 5, 6 }
        };
        int[][] neighbors = new int[][] { // [Up, Down, Left, Right]
                new int[] { 5, 2, 3, 1 },
                new int[] { 5, 2, 0, 4 },
                new int[] { 0, 4, 3, 1 },
                new int[] { 2, 5, 0, 4 },
                new int[] { 2, 5, 3, 1 },
                new int[] { 3, 1, 0, 4 }
        };
        List<MapSegment> segments = new ArrayList<>();
        for (int i = 0; i < map.length; i++) {
            for (int f = 0; f < map[i].length; f++) {
                if (map[i][f] != null)
                    segments.add(map[i][f]);
            }
        }
        for (int i = 0; i < segments.size(); i++) {
            segments.get(i).setDirections(segments.get(neighbors[i][0]), segments.get(neighbors[i][1]), segments.get(neighbors[i][2]), segments.get(neighbors[i][3]));
        }
        for (int i = 0; i < segments.size(); i++) {
            segments.get(i).setTurnDirections(turnings[i][0], turnings[i][1], turnings[i][2], turnings[i][3]);
        }
    }
    public static MapSegment[][] buildMap(char[][] map) {
        int segWidth = minWidth(map);
        int[] dimensions = maxCoords(map);
        int rows = dimensions[1] / segWidth;
        int cols = dimensions[0] / segWidth;
        MapSegment[][] mapSegments = new MapSegment[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int f = 0; f < cols; f++) {
                // Populate segments
                mapSegments[i][f] = buildSegment(map, i, f, segWidth);
            }
        }
        assignMapNeighbors(mapSegments);
        return mapSegments;
    }
    public static void assignMapNeighbors(MapSegment[][] map) {
        int height = map.length;
        for (int i = 0; i < height; i++) {
            int width = map[i].length;
            for (int f = 0; f < map[i].length; f++) {
                if (map[i][f] != null) {
                    MapSegment up = nextInCol(map, i, f, -1);
                    MapSegment down = nextInCol(map, i, f, 1);
                    MapSegment left = nextInRow(map, i, f, -1);
                    MapSegment right = nextInRow(map, i, f, 1);
                    map[i][f].setDirections(up, down, left, right);
                    map[i][f].reassignNullNeighbors();
                }
            }
        }
    }
    public static MapSegment nextInCol(MapSegment[][] map, int row, int col, int right) {
        int height = map.length;
        for (int i = 1; i <= height; i++) {
            MapSegment seg = map[Math.floorMod(row + (i * right), height)][col];
            if (seg != null)
                return seg;
        }
        return null;
    }
    public static MapSegment nextInRow(MapSegment[][] map, int row, int col, int down) {
        int width = map[row].length;
        for (int i = 1; i <= width; i++) {
            MapSegment seg = map[row][Math.floorMod(col + (i * down), width)];
            if (seg != null)
                return seg;
        }
        return null;
    }
    public static MapSegment buildSegment(char[][] map, int row, int col, int width) {
        if (map[row * width][col * width] == ' ' || map[row * width][col * width] == 0) // No segment for this part
            return null;
        char[][] segment = new char[width][width];
        for (int i = 0; i < width; i++) {
            System.arraycopy(map[row * width + i], col * width, segment[i], 0, width);
        }
        return new MapSegment(segment);
    }
    public static String nextCommand(String route, int start) {
        char[] routeChars = route.toCharArray();
        if (routeChars[start] == 'L' || routeChars[start] == 'R') {
            // Left/Right command
            return String.valueOf(routeChars[start]);
        } else {
            // Iterate until left/right next command
            int x = start;
            StringBuilder command = new StringBuilder();
            while (routeChars[x] != 'L'&& routeChars[x] != 'R') {
                command.append(routeChars[x++]);
                if (x >= routeChars.length)
                    break;
            }
            return command.toString();
        }
    }
    public static MapSegment findStart(MapSegment[][] map) {
        for (int i = 0; i < map[0].length; i++) {
            if (map[0][i] != null)
                return map[0][i];
        }
        return null;
    }
    public static boolean isTraversable(MapSegment seg, Point p) {
        return (seg.getSegment()[p.y][p.x] == '.');
    }
    public static class Facing {
        public int value = 0;
        public Facing(int val) { value = val; }
        public Facing(Facing f) { value = f.value; }
    }
    public static MapSegment computeNextPosition2(MapSegment currentSeg, MapSegment[][] map, Point p, Point delta, int distance, Facing facing) {
        int height = currentSeg.getSegment().length;
        int width = currentSeg.getSegment()[0].length;
        MapSegment newSeg = currentSeg;
        Facing newFacing = new Facing(facing);
        for (int i = 0; i < distance; i++) {
            Point nextPos = new Point(p.x + delta.x, p.y + delta.y);
            if (nextPos.x >= width) {
                newSeg = currentSeg.getRight();
                newFacing.value = currentSeg.changeTurning(newSeg);
                nextPos = currentSeg.turnCoords(p, newSeg, width);
            }
            else if (nextPos.y >= height) {
                newSeg = currentSeg.getDown();
                newFacing.value = currentSeg.changeTurning(newSeg);
                nextPos = currentSeg.turnCoords(p, newSeg, width);
            }
            else if (nextPos.x < 0) {
                newSeg = currentSeg.getLeft();
                newFacing.value = currentSeg.changeTurning(newSeg);
                nextPos = currentSeg.turnCoords(p, newSeg, width);
            }
            else if (nextPos.y < 0) {
                newSeg = currentSeg.getUp();
                newFacing.value = currentSeg.changeTurning(newSeg);
                nextPos = currentSeg.turnCoords(p, newSeg, width);
            }
            if (nextPos.y == 50 || nextPos.x == 50)
                System.out.println();
            if (isTraversable(newSeg, nextPos)) {
                currentSeg = newSeg;
                p.setLocation(nextPos);
                facing.value = newFacing.value;
            } else
                return currentSeg;
        }
        return currentSeg;
    }
    public static MapSegment computeNextPosition(MapSegment currentSeg, MapSegment[][] map, Point p, Point delta, int distance, Facing facing) {
        int height = currentSeg.getSegment().length;
        int width = currentSeg.getSegment()[0].length;
        MapSegment newSeg = currentSeg;
        for (int i = 0; i < distance; i++) {
            Point nextPos = new Point(p.x + delta.x, p.y + delta.y);
            if (nextPos.x >= width) {
                nextPos = new Point(0, nextPos.y);
                newSeg = currentSeg.getRight();
            }
            else if (nextPos.y >= height) {
                nextPos = new Point(nextPos.x, 0);
                newSeg = currentSeg.getDown();
            }
            else if (nextPos.x < 0) {
                nextPos = new Point(width - 1, nextPos.y);
                newSeg = currentSeg.getLeft();
            }
            else if (nextPos.y < 0) {
                nextPos = new Point(nextPos.x, height - 1);
                newSeg = currentSeg.getUp();
            }
            if (isTraversable(newSeg, nextPos)) {
                currentSeg = newSeg;
                p.setLocation(nextPos);
            } else
                return currentSeg;
        }
        return currentSeg;
    }
    public static int traverseMap(MapSegment[][] map, String route, boolean cube) {
        int routeIndex = 0;
        int tmp;
        Facing facing = new Facing(0);
        Point delta = new Point(1, 0); // Facing right
        MapSegment segment = findStart(map);
        Point pos = new Point(0, 0);
        while (routeIndex < route.length()) {
            String command = nextCommand(route, routeIndex);
            routeIndex += command.length();
            switch (command) {
                case "L":
                    tmp = delta.x * -1;
                    delta.x = delta.y;
                    delta.y = tmp;
                    facing = new Facing(Math.floorMod(facing.value - 1, 4));
                    break;
                case "R":
                    tmp = delta.y * -1;
                    delta.y = delta.x;
                    delta.x = tmp;
                    facing = new Facing(Math.floorMod(facing.value + 1, 4));
                    break;
                default:
                    if (!cube)
                        segment = computeNextPosition(segment, map, pos, delta, Integer.parseInt(command), facing);
                    else
                        segment = computeNextPosition2(segment, map, pos, delta, Integer.parseInt(command), facing);
                    break;
            }
        }
        Point segCoords = findSegmentCoords(map, segment);
        int x = segCoords.x * segment.getSegment()[0].length + pos.x + 1;
        int y = segCoords.y * segment.getSegment().length + pos.y + 1;
        return 1000 * y + 4 * x + facing.value;
    }
    public static Point findSegmentCoords(MapSegment[][] map, MapSegment seg) {
        for (int i = 0; i < map.length; i++) {
            for (int f = 0; f < map[i].length; f++) {
                if (map[i][f] != null) {
                    if (map[i][f].equals(seg))
                        return new Point(f, i);
                }
            }
        }
        return null;
    }
    public static void populateRow(char[][] map, char[] row, int r) {
        System.arraycopy(row, 0, map[r], 0, row.length);
    }
}