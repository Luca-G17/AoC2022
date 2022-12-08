import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        BufferedReader reader;
        Integer[][] forest;
        try {
            reader = new BufferedReader(new FileReader("input.txt"));
            String line = reader.readLine();
            forest = new Integer[line.length()][line.length()];
            int row = 0;
            while (line != null) {
                populateForestRow(line, forest, row++);
                line = reader.readLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        int count = countVisibleTrees(forest);
        int max = maxVisibleScore(forest);
        System.out.printf("Number of visible trees: %d\n", count);
        System.out.printf("The highest possible scenic score for any tree is: %d\n", max);
    }
    public static int countVisibleTrees(Integer[][] forest) {
        int count = 0;
        for (int i = 1; i < forest.length - 1; i++) {
            for (int f = 1; f < forest[i].length - 1; f++) {
                if (treeIsVisible(forest, i, f)) count++;
            }
        }
        return count + (forest.length * 4 - 4);
    }
    public static int maxVisibleScore(Integer[][] forest) {
        int max = 0;
        for (int i = 1; i < forest.length - 1; i++) {
            for (int f = 1; f < forest.length - 1; f++) {
                int score = scenicScore(forest, i, f);
                if (score > max)
                    max = score;
            }
        }
        return max;
    }

    public static int arrScorePart1(Integer[] arr, int x, int h) {
        int count = 0;
        for (int i = x - 1; i >= 0; i--) {
            if (arr[i] >= h) {
                i = -1; // break
            }
            count++;
        }
        return count;
    }
    public static int arrScorePart2(Integer[] arr, int x, int h) {
        int count = 0;
        for (int i = x + 1; i < arr.length; i++) {
            if (arr[i] >= h) {
                i = arr.length;
            }
            count++;
        }
        return count;
    }
    public static int scenicScore(Integer[][] forest, int row, int col) {
        int h = forest[row][col];
        int score = 1;
        // column
        Integer[] arr = Arrays.stream(forest).map(r -> r[col]).toList().toArray(new Integer[forest.length]);
        score *= arrScorePart1(arr, row, h);
        score *= arrScorePart2(arr, row, h);
        score *= arrScorePart1(forest[row], col, h);
        score *= arrScorePart2(forest[row], col, h);
        return score;
    }
    public static boolean treeIsVisible(Integer[][] forest, int row, int col) {
        int h = forest[row][col];
        boolean part1 = true, part2 = true;
        for (int i = 0; i < forest.length; i++) {
            if (forest[i][col] >= h && i != row) {
                if (i < row)
                    part1 = false;
                else
                    part2 = false;
            }
        }
        if (part1 || part2) {
            return true;
        }
        part1 = part2 = true;
        for (int i = 0; i < forest.length; i++) {
            if (forest[row][i] >= h && i != col) {
                if (i < col)
                    part1 = false;
                else
                    part2 = false;
            }
        }
        return part1 || part2;
    }
    public static void populateForestRow(String line, Integer[][] forest, int row) {
        for (int i = 0; i < line.length(); i++) {
            forest[row][i] = Character.getNumericValue(line.toCharArray()[i]);
        }
    }
}