import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    public static void main(String[] args) {
        BufferedReader reader;
        Elf[][] elves = new Elf[1000][1000];
        List<Elf> elfList = new ArrayList<>();
        String[] directions = new String[] {
                "North",
                "South",
                "West",
                "East"
        };
        try {
            reader = new BufferedReader(new FileReader("input.txt"));
            String line = reader.readLine();
            int r = 0;
            while (line != null) {
                populateElfList(elves, elfList, line, r++);
                line = reader.readLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        AtomicInteger notMoved = new AtomicInteger(0);
        int rounds = 10;
        directions = computeNRounds(elves, elfList, directions, rounds, notMoved);
        int area = areaContainingElves(elves, elfList);
        int totalRounds = roundsUntilFinishedMoving(elves, elfList, directions, notMoved, rounds);
        System.out.printf("Empty spaces inside rectangle containing elves: %d\n", area);
        System.out.printf("Total rounds until all elves have stopped moving: %d\n", totalRounds);
    }
    public static int roundsUntilFinishedMoving(Elf[][] elves, List<Elf> elfList, String[] directions, AtomicInteger notMoved, int rounds) {
        int x = rounds;
        while (notMoved.get() != elfList.size()) {
            directions = computeElfRound(elves, elfList, directions, notMoved);
            System.out.printf("Elves that are stopped: %05d\r", notMoved.get());
            x++;
        }
        System.out.println();
        return x;
    }
    public static void printAreaContainingElves(Elf[][] elves, int minX, int maxX, int minY, int maxY) {
        for (int y = minY; y <= maxY; y++) {
            for (int x = minX; x <= maxX; x++) {
                if (elves[y][x] != null)
                    System.out.print("#");
                else
                    System.out.print(".");
            }
            System.out.println();
        }
        System.out.println();
    }
    public static int areaContainingElves(Elf[][] elves, List<Elf> elfList) {
        int maxY = 0;
        int maxX = 0;
        int minY = Integer.MAX_VALUE;
        int minX = Integer.MAX_VALUE;
        for (Elf elf : elfList) {
            maxY = Math.max(maxY, elf.getCurrentPos().y);
            maxX = Math.max(maxX, elf.getCurrentPos().x);
            minY = Math.min(minY, elf.getCurrentPos().y);
            minX = Math.min(minX, elf.getCurrentPos().x);
        }
        int emptySpaces = 0;
        for (int y = minY; y <= maxY; y++) {
            for (int x = minX; x <= maxX; x++) {
                if (elves[y][x] == null) emptySpaces++;
            }
        }
        // printAreaContainingElves(elves, minX, maxX, minY, maxY);
        return emptySpaces;
    }
    public static String[] computeNRounds(Elf[][] elves, List<Elf> elfList, String[] directions, int n, AtomicInteger notMoved) {
        for (int i = 0; i < n; i++) {
            areaContainingElves(elves, elfList);
            directions = computeElfRound(elves, elfList, directions, notMoved);
        }
        return directions;
    }
    public static String[] computeElfRound(Elf[][] elves, List<Elf> elfList, String[] directions, AtomicInteger notMoved) {
        notMoved.set(proposeMoves(elves, elfList, directions));
        for (Elf elf : elfList)
            elf.move(elves);
        return updateDirections(directions);
    }
    public static String[] updateDirections(String[] directions) {
        String[] newDirections = directions.clone();
        for (int i = 0; i < directions.length; i++) {
            newDirections[Math.floorMod(i - 1, directions.length)] = directions[i];
        }
        return newDirections;
    }
    public static int proposeMoves(Elf[][] elves, List<Elf> elfList, String[] directions) {
        Map<Point, Elf> newElfPositions = new HashMap<>();
        int notMoved = 0;
        for (Elf elf : elfList) {
            if (!elf.setProposedPos(elves, directions))
                notMoved++;
            newElfPositions.put(elf.getProposedPos(), elf);
        }
        for (Elf elf : elfList) {
            if (newElfPositions.containsKey(elf.getProposedPos())) {
                if (!newElfPositions.containsValue(elf)) {
                    // If proposed position exists in map but elf does not set proposed position to current
                    // Set overlapping to true
                    newElfPositions.get(elf.getProposedPos()).setOverlap(true);
                    elf.setProposedPos(elf.getCurrentPos());
                    notMoved++;
                }
            }
        }
        for (Elf elf : newElfPositions.values()) {
            if (elf.isOverlap()) {
                elf.setProposedPos(elf.getCurrentPos());
                notMoved++;
            }
        }
        return notMoved;
    }
    public static void populateElfList(Elf[][] elves, List<Elf> elfList, String line, int r) {
        char[] row = line.toCharArray();
        for (int i = 0; i < row.length; i++) {
            if (row[i] == '#') {
                Elf newElf = new Elf(new Point(i + 500, r + 500));
                elves[r + 500][i + 500] = newElf;
                elfList.add(newElf);
            }
        }
    }
}