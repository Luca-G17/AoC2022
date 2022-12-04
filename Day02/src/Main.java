import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Main {
    // 22 dif
    public static void main(String[] args) {
        BufferedReader reader;
        int total1 = 0;
        int total2 = 0;
        try {
            reader = new BufferedReader(new FileReader("input.txt"));
            String line = reader.readLine();
            while (line != null) {
                String[] play = line.split(" ");
                total1 += gameScore(play[0].charAt(0), play[1].charAt(0));
                total2 += gameScore2(play[0].charAt(0), play[1].charAt(0));
                line = reader.readLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.printf("Total game score %d\n", total1);
        System.out.printf("Total game score with new rules %d\n", total2);
    }
    public static int gameScore(char p1, char p2) {
        char p2Converted = (char) ((int)p2 - 23);
        int score = rockPaperScissors(p1, p2Converted);
        return score + (p2Converted - 64);
    }
    public static int gameScore2(char p1, char p2) {
        char newP2 = 0;
        int score = 0;
        switch (p2) {
            case 'X':
                // Need to lose
                newP2 = (char) (Math.floorMod(((p1 - 1) - 65), 3) + 65);
                score += 0;
                break;
            case 'Y':
                newP2 = p1;
                score += 3;
                break;
            case 'Z':
                // Need to win
                newP2 = (char) ((((p1 + 1) - 65) % 3) + 65);
                score += 6;
                break;
        }
        return score + (newP2 - 64);
    }
    public static int rockPaperScissors(char p1, char p2) {
        if (p1 == p2) return 3;
        if (p2 == (((p1 + 1) - 65) % 3) + 65) return 6;
        else return 0;
    }
}