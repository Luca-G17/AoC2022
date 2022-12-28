import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        BufferedReader reader;
        Map<String, Monkey> monkeys = new HashMap<>();
        try {
            reader = new BufferedReader(new FileReader("input.txt"));
            String line = reader.readLine();
            while (line != null) {
                String[] monkeyStr = line.split(" ");
                String name = monkeyStr[0].replace(":", "");
                Monkey m;
                if (monkeyStr.length > 2) {
                    m = new Monkey(name, 1);
                    m.setOperation(
                            monkeyStr[2],
                            new String[] { monkeyStr[1], monkeyStr[3] }
                    );
                } else {
                    m = new Monkey(name, 0);
                    m.setValue(Integer.parseInt(monkeyStr[1]));
                }
                monkeys.put(name, m);
                line = reader.readLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        long rootMonkey = processMonkeys(monkeys, monkeys.get("root"));
        String equation = produceEquation(monkeys, monkeys.get("root"));
        long humanNumber = parseEquation(equation);
        System.out.printf("Number yelled by root monkey: %d\n", rootMonkey);
        System.out.printf("Human should yell number %d\n", humanNumber);
    }
    public static long processMonkeys(Map<String, Monkey> monkeys, Monkey root) {
        if (root.getType() == 1) { // Operation Monkey
            long p1 = processMonkeys(monkeys, monkeys.get(root.getParams()[0]));
            long p2 = processMonkeys(monkeys, monkeys.get(root.getParams()[1]));
            return root.performOperation(p1, p2);
        } else { // value monkey
            return root.getValue();
        }
    }
    public static int findOuterOperation(String eq) {
        char[] eqArr = eq.toCharArray();
        int b = 0;
        for (int i = 0; i < eq.length(); i++) {
            char c = eqArr[i];
            switch (c) {
                case '(' -> b++;
                case ')' -> b--;
                case '+', '-', '*', '/' -> {
                    if (b == 1)
                        return i;
                }
            }
        }
        return 0;
    }
    public static long parseEquation(String eq) {
        eq = eq.substring(1);
        String[] split = eq.split(" = ");
        long rhs = Long.parseLong(split[1].replace(")", ""));
        String fullEq = split[0];
        while (fullEq.split(" ").length > 1) {
            int i = findOuterOperation(fullEq);
            char[] eqArr = fullEq.toCharArray();
            String left = fullEq.substring(1, i - 1);
            String right = fullEq.substring(i + 2, fullEq.length() - 1);
            long num;
            boolean leftX = false;
            if (left.contains("x")){
                num = Long.parseLong(right.replace(")", ""));
                leftX = true;
            }
            else
                num = Long.parseLong(left.replace("(", ""));
            switch (eqArr[i]) {
                case '+':
                    rhs -= num;
                    break;
                case '-':
                    if (leftX)
                        rhs += num;
                    else
                        rhs = num - rhs;
                    break;
                case '*':
                    rhs /= num;
                    break;
                case '/':
                    if (leftX)
                        rhs *= num;
                    else
                        rhs = num / rhs;
                    break;
            }
            if (leftX)
                fullEq = left;
            else
                fullEq = right;
        }
        return rhs;
    }
    public static String produceEquation(Map<String, Monkey> monkeys, Monkey root) {
        if (root.getType() == 1) {
            String sp1 = produceEquation(monkeys, monkeys.get(root.getParams()[0]));
            String sp2 = produceEquation(monkeys, monkeys.get(root.getParams()[1]));
            if (!sp1.contains("x") && !sp2.contains("x"))
                return String.valueOf(root.performOperation(Long.parseLong(sp1), Long.parseLong(sp2)));
            String op = root.getOperation();
            if (root.getName().equals("root"))
                op = "=";
            return String.format("(%s %s %s)", sp1, op, sp2);
        } else {
            if (root.getName().equals("humn"))
                return "x";
            return String.valueOf(root.getValue());
        }
    }
}