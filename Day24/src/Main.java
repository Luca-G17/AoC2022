import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class Main {
    // Blizzard and elves move at the same time
    // Compute blizzard movement first
    // BFS to exit, no movement is closed as map is dynamic
    public static void main(String[] args) {
        BufferedReader reader;
        List<String> gridRows = new ArrayList<>();
        List<Blizzard> blizzards = new ArrayList<>();
        try {
            reader = new BufferedReader(new FileReader("input.txt"));
            String line = reader.readLine();
            while (line != null) {
                gridRows.add(line);
                line = reader.readLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        int[][] map = generateMapAndBlizzards(blizzards, gridRows);
        int part1 = shortestPath(map, blizzards);
        int back = shortestPathBack(map, blizzards);
        int backAgain = shortestPath(map, blizzards);
        System.out.printf("Shortest time to cross valley: %d\n", part1);
        System.out.printf("Shortest time to cross valley and back: %d\n", part1 + back + backAgain);
    }

    public static int[][] generateMapAndBlizzards(List<Blizzard> blizzards, List<String> rows) {
        int[][] map = new int[rows.size()][rows.get(0).length()];
        for (int i = 0; i < rows.size(); i++) {
            populateRow(map, rows.get(i), blizzards, i);
        }
        return map;
    }

    public static void populateRow(int[][] grid, String row, List<Blizzard> blizzards, int r) {
        char[] rowChars = row.toCharArray();
        for (int i = 0; i < rowChars.length; i++) {
            if (rowChars[i] != '#' && rowChars[i] != '.')
                blizzards.add(new Blizzard(rowChars[i], new Point(i, r), new Point()));
            if (rowChars[i] != '.')
                grid[r][i]++;
        }
    }

    public static Point findEnd(int[][] grid) {
        int y = grid.length - 1;
        for (int x = 0; x < grid[y].length; x++) {
            if (grid[y][x] == 0)
                return new Point(x, y);
        }
        return null;
    }
    public static List<Blizzard> moveBlizzards(int[][] grid, List<Blizzard> blizzards) {
        int height = grid.length;
        int width = grid[0].length;
        List<Blizzard> newBlizzards = new ArrayList<>();
        for (Blizzard blizzard : blizzards) {
            newBlizzards.add(blizzard.move(width, height));
        }
        return newBlizzards;
    }
    public static int[][] updateBlizzardMap(int[][] grid, List<Blizzard> blizzards) {
        int[][] map = new int[grid.length][grid[0].length];
        for (int i = 0; i < grid.length; i++) {
            for (int f = 0; f < grid[i].length; f++) {
                map[i][f] = grid[i][f];
            }
        }
        for (Blizzard b : blizzards) {
            map[b.getLastPosition().y][b.getLastPosition().x]--;
            map[b.getPosition().y][b.getPosition().x]++;
        }
        return map;
    }
    public static List<Node> getNeighbors(int[][] grid, Node[][][] nodeMap, Node parent) {
        List<Node> ns = new ArrayList<>();
        int height = grid.length;
        int width = grid[0].length;
        Point p = parent.getPosition();
        if (grid[p.y][p.x] == 0)
            ns.add(nodeMap[p.y][p.x][parent.getDistance() + 1]);
        if (p.y - 1 >= 0) {
            if (grid[p.y - 1][p.x] == 0)
                ns.add(nodeMap[p.y - 1][p.x][parent.getDistance() + 1]);
        }
        if (p.y + 1 < height) {
            if (grid[p.y + 1][p.x] == 0)
                ns.add(nodeMap[p.y + 1][p.x][parent.getDistance() + 1]);

        }
        if (p.x - 1 >= 0) {
            if (grid[p.y][p.x - 1] == 0)
                ns.add(nodeMap[p.y][p.x - 1][parent.getDistance() + 1]);

        }
        if (p.x + 1 < width) {
            if (grid[p.y][p.x + 1] == 0)
                ns.add(nodeMap[p.y][p.x + 1][parent.getDistance() + 1]);
        }
        return ns;
    }

    public static int shortestPath(int[][] grid, List<Blizzard> bs) {
        Node start = new Node(0, new Point(1, 0), null);
        Point end = findEnd(grid);
        return shortestPath(grid, bs, start, end);
    }
    public static int shortestPathBack(int[][] grid, List<Blizzard> bs) {
        Node start = new Node(0, findEnd(grid), null);
        Point end = new Point(1, 0);
        return shortestPath(grid, bs, start, end);
    }
    public static Blizzard getBlizzardAt(List<Blizzard> bs, Point p) {
        for (Blizzard b : bs) {
            if (b.getPosition().equals(p))
                return b;
        }
        return null;
    }
    public static void printBlizzards(int[][] grid, List<Blizzard> bs) {
        int height = grid.length;
        int width = grid[0].length;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (y == 0 || y == height - 1 || x == 0 || x == width - 1) {
                    if (grid[y][x] == 1)
                        System.out.print("#");
                    else
                        System.out.print(".");
                }
                else {
                    if (grid[y][x] == 1)
                        System.out.print(getBlizzardAt(bs, new Point(x, y)).dir);
                    else if (grid[y][x] > 1)
                        System.out.print(grid[y][x]);
                    else
                        System.out.print(".");
                }
            }
            System.out.println();
        }
        System.out.println();
    }
    public static Node[][][] initNodeMap(int height, int width, int maxTime) {
        Node[][][] nodeMap = new Node[height][width][maxTime];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                for (int z = 0; z < maxTime; z++) {
                    nodeMap[y][x][z] = new Node(z, new Point(x, y), null);
                }
            }
        }
        return nodeMap;
    }
    public static int shortestPath(int[][] blizzardMap, List<Blizzard> bs, Node start, Point end) {
        Map<Integer, BlizzardState> blizzardCache = new HashMap<>();
        blizzardCache.put(0, new BlizzardState(blizzardMap, bs));
        int height = blizzardMap.length;
        int width = blizzardMap[0].length;
        Node[][][] nodeMap = initNodeMap(height, width, 1000);
        nodeMap[start.getPosition().y][start.getPosition().x][start.getDistance()] = start;
        Set<Node> open = new HashSet<>();
        Set<Node> closed = new HashSet<>();
        open.add(start);
        Node current = start;
        int ex = 0;

        while (!current.getPosition().equals(end)) {
            if (!blizzardCache.containsKey(current.getDistance() + 1)) {
                int[][] prevMap = blizzardCache.get(current.getDistance()).getMap();
                List<Blizzard> prevBlizzards = blizzardCache.get(current.getDistance()).getBlizzards();
                List<Blizzard> blizzards = moveBlizzards(prevMap, prevBlizzards);
                int[][] updatedBlizzardMap = updateBlizzardMap(prevMap, blizzards);
                blizzardCache.put(current.getDistance() + 1, new BlizzardState(updatedBlizzardMap, blizzards));
                // printBlizzards(updatedBlizzardMap, blizzards);
            }
            for (Node n : getNeighbors(blizzardCache.get(current.getDistance() + 1).getMap(), nodeMap, current)) {
                if (!open.contains(n) && !closed.contains(n)) {
                    open.add(n);
                    n.setParent(current);
                    n.setDistance(current.getDistance() + 1);
                }
                else if (open.contains(n)) {
                    if ( n.getDistance() > current.getDistance() + 1) {
                        n.setParent(current);
                        n.setDistance(current.getDistance() + 1);
                    }
                }
            }
            open.remove(current);
            closed.add(current);
            current = getClosestNode(open);
            System.out.printf("Nodes explored: %06d/%d\r", ex++, height * width * 300);
        }
        System.out.println();
        copyBlizzardState(blizzardMap, bs, blizzardCache.get(current.getDistance()));
        return current.getDistance();
    }
    public static void copyBlizzardState(int[][] map, List<Blizzard> blizzards, BlizzardState state) {
        for (int i = 0; i < map.length; i++) {
            for (int f = 0; f < map[i].length; f++) {
                map[i][f] = state.getMap()[i][f];
            }
        }
        for (int i = 0; i < blizzards.size(); i++)
            blizzards.set(i, state.getBlizzards().get(i));
    }
    public static Node getClosestNode(Set<Node> open) {
        Node min = open.stream().toList().get(0);
        for (Node n : open) {
            if (n.getDistance() < min.getDistance())
                min = n;
        }
        return min;
    }
}