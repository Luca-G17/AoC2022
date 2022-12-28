import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static int DECRYPTION_KEY = 811589153;
    public static void main(String[] args) {
        // Use an array as a linked list for instant access
        // PAIN ^^
        CircularLinkedList file = new CircularLinkedList();
        List<Node> nodes = getNodeList(file, 1);

        CircularLinkedList file2 = new CircularLinkedList();
        List<Node> nodes2 = getNodeList(file2, DECRYPTION_KEY);

        mixFile(file, nodes);
        BigInteger coords = getCoords(file, nodes, nodes.size());
        System.out.printf("Sum of three numbers that form grove coordinates: %s\n", coords);

        mixFile10(file2, nodes2);
        coords = getCoords(file2, nodes2, nodes2.size());
        System.out.printf("Sum of three numbers that form grove coordinates after 10 mixes: %s\n", coords);
    }

    public static List<Node> getNodeList(CircularLinkedList file, long decryptionKey) {
        BufferedReader reader;
        List<Node> nodes = new ArrayList<>();
        try {
            reader = new BufferedReader(new FileReader("input.txt"));
            String line = reader.readLine();
            while (line != null) {
                nodes.add(file.add((new BigInteger(line)).multiply(BigInteger.valueOf(decryptionKey))));
                line = reader.readLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return nodes;
    }
    public static BigInteger getCoords(CircularLinkedList file, List<Node> nodes, int size) {
        // Find node with value 0
        Node n = nodes.get(0);
        int x = 0;
        while (n.getValue().equals(BigInteger.ZERO)) n = nodes.get(x++);

        BigInteger i0 = file.indexOf(n);
        BigInteger i1000 = i0.add(BigInteger.valueOf(1000)).mod(BigInteger.valueOf(size));
        BigInteger i2000 = i0.add(BigInteger.valueOf(2000)).mod(BigInteger.valueOf(size));
        BigInteger i3000 = i0.add(BigInteger.valueOf(3000)).mod(BigInteger.valueOf(size));
        return file.get(i1000.longValue()).getValue().add(file.get(i2000.longValue()).getValue()).add(file.get(i3000.longValue()).getValue());
    }
    public static void mixFile10(CircularLinkedList file, List<Node> nodes) {
        for (int i = 0; i < 10; i++) {
            mixFile(file, nodes);
        }
    }
    public static void mixFile(CircularLinkedList file, List<Node> nodes) {
        for (int i = 0; i < nodes.size(); i++) {
            mixNode(file, nodes.get(i), nodes.size());
        }
    }
    public static void mixNode(CircularLinkedList file, Node node, long size) {
        BigInteger newPos = (file.indexOf(node).add(node.getValue())).mod(BigInteger.valueOf(size - 1));
        file.move(node, newPos);
    }
}