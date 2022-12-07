import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        BufferedReader reader;
        String datastream;
        int charsBeforeStartOfPackets = 0;
        int charsBeforeStartOfMessages = 0;
        try {
            reader = new BufferedReader(new FileReader("input.txt"));
            datastream = reader.lines().collect(Collectors.joining());
            charsBeforeStartOfPackets = processBuffer(datastream, 4);
            charsBeforeStartOfMessages = processBuffer(datastream, 14);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        System.out.printf("Number of characters before start of packets in datastream: %d\n", charsBeforeStartOfPackets);
        System.out.printf("Number of characters before start of messages in datastream: %d\n", charsBeforeStartOfMessages);

    }
    public static boolean stringHasDuplicates(String substring) {
        int[] letters = new int[26];
        for (char letter : substring.toCharArray()) {
            letters[letter - 97]++;
            if (letters[letter - 97] > 1) {
                return true;
            }
        }
        return false;
    }
    public static int processBuffer(String datastream, int distinctLength) {
        for (int i = 0; i < datastream.length() - distinctLength; i++) {
            if (!stringHasDuplicates(datastream.substring(i, i + distinctLength))) {
                return i + distinctLength;
            }
        }
        return -1;
    }
}