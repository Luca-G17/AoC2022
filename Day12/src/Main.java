import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class Main {
    public static void main(String[] args) {
        BufferedReader reader;
        List<List<Node>> grid = new ArrayList<>();
        List<List<List<Node>>> grids = new ArrayList<>();
        for (int i = 0; i < 2000; i++)
            grids.add(new ArrayList<>());

        int rowNum = 0;
        try {
            reader = new BufferedReader(new FileReader("input.txt"));
            String line = reader.readLine();
            while (line != null) {
                for (int i = 0; i < 2000; i++) {
                    populateGridRow(grids.get(i), line, rowNum);
                }
                populateGridRow(grid, line, rowNum++);
                line = reader.readLine();
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

        Node start = findPoint(grid,'S');
        int pathLength = pathLength(grid, start.getCoords().x, start.getCoords().y);
        int minLength = minPathLengthFromA(grids, pathLength);
        System.out.printf("Path length from start to end points: %d\n", pathLength);
        System.out.printf("Minimum path length from a to S: %d\n", minLength);
    }
    public static void resetGrid(List<List<Node>> grid) {
        for (int i = 0; i < grid.size(); i++) {
            for (int f = 0; f < grid.get(i).size(); f++) {
                grid.get(i).set(f, new Node(grid.get(i).get(f).getHeight(), i, f));
            }
        }
    }
    public static int minPathLengthFromA(List<List<List<Node>>> grids, int len) {
        List<Node> starts = findAllStarts(grids.get(0));
        for (int i = 0; i < starts.size(); i++) {
            Node s = starts.get(i);
            int newLen = pathLength(grids.get(i), s.getCoords().x, s.getCoords().y);
            if (newLen != -1) {
                len = Math.min(len, newLen);
            }
        }
        return len;
    }
    public static List<Node> findAllStarts(List<List<Node>> grid) {
        List<Node> starts = new ArrayList<>();
        for (int i = 0; i < grid.size(); i++) {
            for (int f = 0; f < grid.get(i).size(); f++) {
                if (grid.get(i).get(f).getHeight() == 'a')
                    starts.add(grid.get(i).get(f));
            }
        }
        return starts;
    }
    public static int pathLength(List<List<Node>> grid, int startX, int startY) {
        Node end = pathfinding(grid, startX, startY);
        return pathLength(end) - 1;
    }
    public static int pathLength(Node end) {
        Node curr = end;
        int length = 0;
        while (curr != null) {
            length++;
            curr = curr.getParent();
        }
        return length;
    }
    public static void populateGridRow(List<List<Node>> grid, String row, int rowNum) {
        grid.add(new ArrayList<>());
        for (int i = 0; i < row.toCharArray().length; i++) {
            grid.get(rowNum).add(new Node(row.toCharArray()[i], i, rowNum));
        }
    }
    public static Node findPoint(List<List<Node>> grid, char point) {
        for (int i = 0; i < grid.size(); i++) {
            for (int f = 0; f < grid.get(i).size(); f++) {
                if (grid.get(i).get(f).getHeight() == (int) point) {
                    if (point == 'S') {
                        grid.get(i).get(f).setHeight('a');
                    } else if (point == 'E') {
                        grid.get(i).get(f).setHeight('z');
                    }
                    return grid.get(i).get(f);
                }
            }
        }
        return null;
    }
    public static double calculateHeuristic(Node curr, Node end) {
        return 1;//curr.getCoords().distanceSq(end.getCoords());
    }
    public static Node pathfinding(List<List<Node>> grid, int startX, int startY) {
        Node start = grid.get(startY).get(startX);
        Node end = findPoint(grid,'E');
        PriorityQueue<Node> open = new PriorityQueue<>();
        PriorityQueue<Node> closed = new PriorityQueue<>();
        int nodes = 0;
        start.setF(start.getG() + calculateHeuristic(start, end));
        open.add(start);
        while(!open.isEmpty()) {
            nodes++;

            Node curr = open.peek();
            if (curr.getCoords().equals(end.getCoords())) {
                System.out.println();
            }
            if (curr == end) {
                end.setHeight('E');
                return curr;
            }
            for (Node n : neighbors(curr, grid)) {
                double totalWeight = curr.getG() + 1;
                if (!open.contains(n) && !closed.contains(n)) {
                    n.setParent(curr);
                    n.setG(totalWeight);
                    n.setF(n.getG() + calculateHeuristic(n, end));
                    open.add(n);
                } else {
                    if (totalWeight < n.getG()) {
                        n.setParent(curr);
                        n.setG(totalWeight);
                        n.setF(n.getG() + calculateHeuristic(n, end));
                        if (closed.contains(n)) {
                            closed.remove(n);
                            open.add(n);
                        }
                    }
                }
            }
            open.remove(curr);
            closed.add(curr);
        }
        return null;
    }
    public static void printGrid(int height, int width, PriorityQueue<Node> closed) {
        List<Node> allClosed = closed.stream().toList();
        for (int i = 0; i < height; i++) {
            for (int f = 0; f < width; f++) {
                boolean found = false;
                for (Node n : allClosed) {
                    if (n.getCoords().equals(new Point(f, i))) {
                        System.out.print("#");
                        found = true;
                    }
                }
                if (!found)
                    System.out.print(".");
            }
            System.out.println();
        }
        System.out.println();
    }
    public static boolean nodesAreNeighbours(Node n1, Node n2) {
        return (n1.getHeight() == n2.getHeight() + 1 || n1.getHeight() <= n2.getHeight());
    }
    public static List<Node> neighbors(Node curr, List<List<Node>> grid) {
        List<Node> neighbors = new ArrayList<>();
        Point currP = curr.getCoords();
        if (currP.x > 0) {
            if (nodesAreNeighbours(grid.get(currP.y).get(currP.x - 1), curr)) {
                // add to list
                neighbors.add(grid.get(currP.y).get(currP.x - 1));
            }
        }
        if (currP.x < grid.get(currP.y).size() - 1) {
            if (nodesAreNeighbours(grid.get(currP.y).get(currP.x + 1), curr)) {
                // add to list
                neighbors.add(grid.get(currP.y).get(currP.x + 1));
            }
        }
        if (currP.y > 0) {
            if (nodesAreNeighbours(grid.get(currP.y - 1).get(currP.x), curr)) {
                neighbors.add(grid.get(currP.y - 1).get(currP.x));
            }
        }
        if (currP.y < grid.size() - 1) {
            if (nodesAreNeighbours(grid.get(currP.y + 1).get(currP.x), curr)) {
                neighbors.add(grid.get(currP.y + 1).get(currP.x));
            }
        }
        return neighbors;
    }
}