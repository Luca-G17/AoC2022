import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        BufferedReader reader;
        int total = 0;
        int totalGroupScore = 0;
        int groupCounter = 0;
        String[] group = new String[3];
        try {
            reader = new BufferedReader(new FileReader("input.txt"));
            String line = reader.readLine();
            while (line != null) {
                total += rucksackErrorScore(line);
                group[groupCounter] = line;
                groupCounter++;
                if (groupCounter == 3) {
                    totalGroupScore += groupScore(group);
                    groupCounter = 0;
                }
                line = reader.readLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.printf("Sum of priorities of erroneous items %d\n", total);
        System.out.printf("Sum of the group scores %d\n", totalGroupScore);

    }
    public static int groupScore(String[] group) {
        int[] items1 = rucksackItems(group[0].toCharArray());
        int[] items2 = rucksackItems(group[1].toCharArray());
        int[] items3 = rucksackItems(group[2].toCharArray());
        for (int i = 0; i < items1.length; i++) {
            if (items1[i] == 1 && items2[i] == 1 && items3[i] == 1) {
                return priority((char) (i + 65));
            }
        }
        return 0;
    }
    public static int[] rucksackItems(char[] rucksack) {
        int[] items = new int[58];
        for (char c : rucksack) {
            items[c - 65] = 1;
        }
        return items;
    }
    public static int rucksackErrorScore(String rucksack) {
        int[] items = new int[58];
        int score = 0;
        char[] charRucksack = rucksack.toCharArray();
        for (int i = 0; i < rucksack.length() / 2; i++ ) {
            items[charRucksack[i] - 65] = 1;
        }
        for (int i = rucksack.length() / 2; i < rucksack.length(); i++) {
            if (items[charRucksack[i] - 65] == 1) {
                items[charRucksack[i] - 65]++;
                score += priority(charRucksack[i]);
            }
        }
        return score;
    }
    public static int priority(char c) {
        if (c >= 97) {
            // lower case
            return c - 96;
        } else {
            return (c - 64) + 26;
        }
    }
}