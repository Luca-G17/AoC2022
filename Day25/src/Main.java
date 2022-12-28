import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        BufferedReader reader;
        List<String> SNAFUs = new ArrayList<>();
        try {
            reader = new BufferedReader(new FileReader("input.txt"));
            String line = reader.readLine();
            while (line != null) {
                SNAFUs.add(line);
                line = reader.readLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String part1 = decimalToSNAFU(sumSNAFUNums(SNAFUs));
        System.out.printf("Sum of fuel values in base SNAFU: %s\n", part1);
    }
    public static long sumSNAFUNums(List<String> SNAFU) {
        long total = 0;
        for (String s : SNAFU) {
            total += SNAFUToDecimal(s);
        }
        return total;
    }
    public static String decimalToSNAFU(long d) {
        StringBuilder SNAFU = new StringBuilder();
        while(SNAFUToDecimal(SNAFU.toString()) < d) SNAFU.append("2");
        for (int i = 0; i < SNAFU.length(); i++) {
            decrementDigit(SNAFU, i);
            while (SNAFUToDecimal(SNAFU.toString()) >= d && SNAFU.charAt(i) != '=') {
                decrementDigit(SNAFU, i);
            }
            if (SNAFUToDecimal(SNAFU.toString()) < d)
                incrementDigit(SNAFU, i);
        }
        return SNAFU.toString();
    }
    public static void incrementDigit(StringBuilder s, int i) {
        if (s.charAt(i) != '=' && s.charAt(i) != '-') {
            int prev = Integer.parseInt(String.valueOf(s.charAt(i)));
            s.setCharAt(i, String.valueOf(prev + 1).charAt(0));
        }
        else if (s.charAt(i) == '-')
            s.setCharAt(i, '0');
        else if (s.charAt(i) == '=')
            s.setCharAt(i, '-');
    }
    public static void decrementDigit(StringBuilder s, int i) {
        if (s.charAt(i) != '=' && s.charAt(i) != '-' && s.charAt(i) != '0') {
            int prev = Integer.parseInt(String.valueOf(s.charAt(i)));
            s.setCharAt(i, String.valueOf(prev - 1).charAt(0));
        }
        else if (s.charAt(i) == '0')
            s.setCharAt(i, '-');
        else if (s.charAt(i) == '-')
            s.setCharAt(i, '=');
    }
    public static long SNAFUToDecimal(String SNAFU) {
        char[] charSNAFU = SNAFU.toCharArray();
        long total = 0;
        for (int i = SNAFU.length() - 1; i >= 0; i--) {
            long scalar = (long) Math.pow(5, SNAFU.length() - 1 - i);
            if (charSNAFU[i] != '-' && charSNAFU[i] != '=') {
                total += Long.parseLong(String.valueOf(charSNAFU[i])) * scalar;
            }
            else if (charSNAFU[i] == '-')
                total += -1 * scalar;
            else
                total += -2 * scalar;
        }
        return total;
    }
}