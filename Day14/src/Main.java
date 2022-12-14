import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        BufferedReader reader;
        boolean[][] cave = new boolean[1000][1000];
        int maxY = 0;
        try {
            reader = new BufferedReader(new FileReader("input.txt"));
            String line = reader.readLine();
            while (line != null) {
                String[] rock = line.split(" -> ");
                maxY = createRock(cave, rock, maxY);
                line = reader.readLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        int sand = totalSandGrains(cave, maxY);
        System.out.printf("Total units of sand come to rest: %d\n", sand);
        System.out.printf("Total units of sand before it reaches the source: %d\n", totalSandGrainsUntilSourceBlocked(cave, maxY, sand));
    }
    public static int totalSandGrains(boolean[][] cave, int maxY) {
        int count = 0;
        while (!processSandGrain(cave, maxY)) count++;
        return count;
    }
    public static int totalSandGrainsUntilSourceBlocked(boolean[][] cave, int maxY, int offset) {
        int count = offset;
        while (!processSandGrain2(cave, maxY)) count++;
        return count + 2;
    }
    public static boolean processSandGrain2(boolean[][] cave, int maxY) {
        Point grain = new Point(500, 0);
        while(processSandGrainTurn(cave, grain)) {
            if (grain.y > maxY)
                break;
        }
        cave[grain.y][grain.x] = true;
        return (grain.equals(new Point(500, 0)));
    }
    public static boolean processSandGrain(boolean[][] cave, int maxY) {
        Point grain = new Point(500, 0);
        while(processSandGrainTurn(cave, grain)) {
            if (grain.y > maxY) {
                cave[grain.y][grain.x] = true;
                return true;
            }
        }
        cave[grain.y][grain.x] = true;
        return false;
    }
    public static boolean processSandGrainTurn(boolean[][] cave, Point curr) {
        if (!cave[curr.y + 1][curr.x])
            curr.y++;
        else if (!cave[curr.y + 1][curr.x - 1]) {
            curr.y++;
            curr.x--;
        }
        else if (!cave[curr.y + 1][curr.x + 1]) {
            curr.y++;
            curr.x++;
        }
        else {
            return false;
        }
        return true;
    }
    public static void printCaveArea(boolean[][] cave, int x, int y, int diff) {
        System.out.println();
        for (int i = 0; i < diff; i++) {
            for (int f = 0; f < diff + 20; f++) {
                if (cave[i + y][f + x])
                    System.out.print('#');
                else
                    System.out.print(".");
            }
            System.out.println();
        }
    }
    public static int createRock(boolean[][] cave, String[] rock, int maxY) {
        String[] coordStr = rock[0].split(",");
        Point prevPoint = new Point(Integer.parseInt(coordStr[0]), Integer.parseInt(coordStr[1]));
        maxY = Math.max(maxY, prevPoint.y);
        for (int i = 1; i < rock.length; i++) {
            maxY = createRockLine(cave, rock[i], prevPoint, maxY);
        }
        return maxY;
    }
    public static int createRockLine(boolean[][] cave, String rockLine, Point previousPoint, int maxY) {
        String[] coordStr = rockLine.split(",");
        Point coords = new Point(Integer.parseInt(coordStr[0]), Integer.parseInt(coordStr[1]));
        int diffx = Integer.signum(coords.x - previousPoint.x);
        int diffy = Integer.signum(coords.y - previousPoint.y);
        while (!coords.equals(previousPoint)) {
            cave[previousPoint.y][previousPoint.x] = true;
            previousPoint.x += diffx;
            previousPoint.y += diffy;
        }
        cave[previousPoint.y][previousPoint.x] = true;
        return Math.max(maxY, coords.y);
    }
}