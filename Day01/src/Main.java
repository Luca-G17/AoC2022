import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;


public class Main {
    public static void main(String[] args) {
        BufferedReader reader;
        int max = 0;
        int total = 0;
        List<Integer> sums = new ArrayList<>();
        try {
            reader = new BufferedReader(new FileReader("input.txt"));
            String line = reader.readLine();
            while (line != null) {
                if (line.equals("")) {
                    sums.add(total);
                    if (total > max)
                        max = total;
                    total = 0;
                } else {
                    line = line.replace("\n", "");
                    total += Integer.parseInt(line);
                }
                line = reader.readLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        int max3 = sums.stream().sorted().toList().subList(sums.size() - 3, sums.size()).stream().reduce(0, Integer::sum);
        System.out.printf("Maximum calories carried by an elf: %d\n", max);
        System.out.printf("Total calories carried by the top three elves: %d\n", max3);

    }
}