import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        BufferedReader reader;
        int x = 1;
        int cycle = 1;
        int signalStrength = 0;
        String[] command = new String[]{"", ""};
        try {
            reader = new BufferedReader(new FileReader("input.txt"));
            String line = reader.readLine();
            while (line != null) {
                command = line.split(" ");
                drawCRT(x, cycle, command[0]);
                int[] signals = calculateSignalStrength(x, cycle, signalStrength, command[0]);
                signalStrength = signals[0]; cycle = signals[1];
                x = processCommand(command, x);
                line = reader.readLine();

            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.printf("Sum of the signal strengths: %d\n", signalStrength);
    }
    public static void spriteOverlapsCursor(int cursor, int sprite) {
        if (sprite - 1 == cursor || sprite == cursor || sprite + 1 == cursor) {
            System.out.print("#");
        }
        else {
            System.out.print(".");
        }
    }
    public static void drawCRT(int x, int cycle, String command) {
        int cursor = cycle % 40;
        int sprite = x + 1;
        if (command.equals("addx")) {
            if (cursor == 0 && cycle > 1) {
                spriteOverlapsCursor(cursor, sprite);
                System.out.println();
                spriteOverlapsCursor(cursor + 1, sprite);
            } else if (cursor == 1 && cycle > 5){
                System.out.println();
                spriteOverlapsCursor(cursor, sprite);
                spriteOverlapsCursor(cursor + 1, sprite);
            } else {
                spriteOverlapsCursor(cursor, sprite);
                spriteOverlapsCursor(cursor + 1, sprite);
            }
        }
        else {
            if (cursor == 0) {
                spriteOverlapsCursor(cursor, sprite);
                System.out.println();
            }
            else if (cursor == 1) {
                System.out.println();
                spriteOverlapsCursor(cursor, sprite);
            } else {
                spriteOverlapsCursor(cursor, sprite);
            }
        }
    }
    public static int[] calculateSignalStrength(int x, int cycle, int signalStrength, String command) {
        if (command.equals("addx")) {
            if (cycle == 20) {
                signalStrength += cycle * x;
            }
            else if (cycle > 50) {
                if ((cycle - 20) % 40 == 0) {
                    signalStrength += cycle * x;
                }
                else if ((cycle - 20) % 40 == 39) {
                    signalStrength += (cycle + 1) * x;
                }
            }
            cycle += 2;
        }
        else {
            if (cycle == 20) {
                signalStrength += cycle * x;

            }
            else if ((cycle - 20) % 40 == 0) {
                signalStrength += cycle * x;

            }
            cycle++;
        }
        return new int[]{signalStrength, cycle};
    }
    public static int processCommand(String[] command, int x) {
        if (command[0].equals("addx")) {
            x += Integer.parseInt(command[1]);
        }
        return x;
    }
}