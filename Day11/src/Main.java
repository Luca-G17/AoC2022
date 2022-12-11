import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        BufferedReader reader;
        List<Monkey> monkeys = new ArrayList<>();
        List<Monkey> monkeys2 = new ArrayList<>();
        try {
            reader = new BufferedReader(new FileReader("input.txt"));
            String line = reader.readLine();
            while (line != null) {
                String startItems = reader.readLine();
                String operation  = reader.readLine();
                String test       = reader.readLine();
                String testTrue   = reader.readLine();
                String testFalse  = reader.readLine();
                monkeys.add(new Monkey(operation, test, testTrue, testFalse, startItems));
                monkeys2.add(new Monkey(operation, test, testTrue, testFalse, startItems));
                line = reader.readLine();
                line = reader.readLine();
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        long mb = processNRounds(monkeys, 20, true);
        calculateMonkeyDivisorProduct(monkeys2);
        long mb10000 = processNRounds(monkeys2, 10000, false);
        System.out.printf("Monkey business level after 20 rounds: %d\n", mb);
        System.out.printf("Monkey business level after 10000 rounds: %d\n", mb10000);

    }
    public static void calculateMonkeyDivisorProduct(List<Monkey> monkeys) {
        int modNum = 1;
        for (Monkey m : monkeys) {
            modNum *= m.getTestDivisor();
        }
        for (Monkey m : monkeys) {
            m.setModNum(modNum);
        }
    }
    public static long calculateMonkeyBusiness(List<Monkey> monkeys) {
        monkeys.sort(Comparator.comparingLong(Monkey::getInspected));
        return monkeys.get(monkeys.size() - 1).getInspected() * monkeys.get(monkeys.size() - 2).getInspected();
    }
    public static long processNRounds(List<Monkey> monkeys, int n, boolean div3) {
        for (int i = 0; i < n; i++) {
            processRound(monkeys, div3);
        }
        return calculateMonkeyBusiness(monkeys);
    }
    public static void processRound(List<Monkey> monkeys, boolean div3) {
        for (Monkey m : monkeys) {
            processMonkeyRound(m, monkeys, div3);
        }
    }
    public static void processMonkeyRound(Monkey monkey, List<Monkey> monkeys, boolean div3) {
        int itemCount = monkey.getItemCount();
        for (int i = 0; i < itemCount; i++) {
            BigInteger[] throwItem = monkey.processNextItem(div3);
            monkeys.get(throwItem[0].intValue()).catchItem(throwItem[1]);
        }
    }
}