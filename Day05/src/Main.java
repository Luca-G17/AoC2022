import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        BufferedReader reader;
        List<String[]> stockpileLines = new ArrayList<>();
        try {
            reader = new BufferedReader(new FileReader("input.txt"));
            String line = reader.readLine();
            while (!line.equals("")) {
                String[] crates = line.split(" ");
                stockpileLines.add(crates);
                line = reader.readLine();
            }
            line = reader.readLine();
            List<Stack<String>> stockpile = createStockpile(stockpileLines);
            List<Stack<String>> stockpile2 = createStockpile(stockpileLines);

            while (line != null){
                String[] command = line.split(" ");
                processStockpileCommand9000(command, stockpile);
                processStockpileCommand9001(command, stockpile2);
                line = reader.readLine();
            }
            outputTopRow(stockpile, "9000");
            outputTopRow(stockpile2, "9001");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void outputTopRow(List<Stack<String>> stockpile, String version) {
        String out = "";
        for (Stack<String> stack : stockpile) {
            if (!stack.isEmpty()) {
                out = out.concat(stack.peek());
            }
        }
        out = out.replaceAll("\\[", "");
        out = out.replaceAll("]", "");

        System.out.printf("Top row of stockpile (CrateMover %s): %s\n", version, out);
    }
    public static void processStockpileCommand9001(String[] command, List<Stack<String>> stockpile) {
        int count = Integer.parseInt(command[1]);
        int source = Integer.parseInt(command[3]) - 1;
        int dest = Integer.parseInt(command[5]) - 1;
        Stack<String> tmpStack = new Stack<>();
        for (int i = 0; i < count; i++) {
            String crate = stockpile.get(source).pop();
            tmpStack.push(crate);
        }
        while (!tmpStack.isEmpty()) {
            stockpile.get(dest).push(tmpStack.pop());
        }
    }
    public static void processStockpileCommand9000(String[] command, List<Stack<String>> stockpile) {
        int count = Integer.parseInt(command[1]);
        int source = Integer.parseInt(command[3]) - 1;
        int dest = Integer.parseInt(command[5]) - 1;
        for (int i = 0; i < count; i++) {
            String crate = stockpile.get(source).pop();
            stockpile.get(dest).push(crate);
        }
    }
    public static List<Stack<String>> createStockpile(List<String[]> stockpileLines) {
        List<Stack<String>> stockpile = new ArrayList<>();
        for (int i = 0; i < (stockpileLines.get(stockpileLines.size() - 1).length - 1) / 2; i++) {
            stockpile.add(new Stack<>());
        }
        for (int i = stockpileLines.size() - 2; i >= 0; i--) {
            addToStockpile(stockpile, stockpileLines.get(i));
        }
        return stockpile;
    }
    public static void addToStockpile(List<Stack<String>> stockpile, String[] crateRow) {
        int colPtr = 0;
        for (int i = 0; i < crateRow.length; i++) {
            String crate = crateRow[i];
            if (crate.equals("")) {
                i += 3;
            }
            else {
                stockpile.get(colPtr).push(crate);
            }
            colPtr++;
        }
    }
}