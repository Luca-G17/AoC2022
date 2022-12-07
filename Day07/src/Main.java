import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    static List<Directory> dirs = new ArrayList<>();
    public static Directory changeDirectory(Directory current, Directory root, String arg) {
        switch (arg) {
            case "/":
                return root;
            case "..":
                if (current.getParent() != null)
                    return current.getParent();
                else
                    return current;
            default:
                return current.getChildren().get(arg);
        }
    }
    public static void processDirectoryItem(String[] item, Directory current) {
        if (item[0].equals("dir")) { // nested directory
            if (!current.getChildren().containsKey(item[1])) {
                Directory newDir = new Directory(item[1], current);
                current.addChild(newDir);
            }
        }
        else { // file
            if (!current.getChildren().containsKey(item[1])) {
                current.addFile(item[1], Integer.parseInt(item[0]));
            }
        }
    }
    public static int sumTree(Directory tree) {
        int total = 0;
        int childTotal = 0;
        if (tree.getChildren().size() == 0) {
            total = tree.totalFileSize();
        }
        else {
            for (Directory child : tree.getChildren().values()) {
                childTotal += sumTree(child);
            }
            total += tree.totalFileSize();
        }
        tree.addSize(total + childTotal);
        dirs.add(tree);
        return childTotal + total;
    }
    public static int deleteDir(int sum) {
        int unusedSpace = 70000000 - sum;
        int requiredSpace = 30000000 - unusedSpace;
        // find the smallest dir that's greater than the required space
        Directory smallest = null;
        for (int i = 1; i < dirs.size(); i++) {
            Directory dir = dirs.get(i);
            if (dir.getSize() > requiredSpace) {
                if (smallest != null) {
                    if (dir.getSize() < smallest.getSize())
                        smallest = dir;
                }
                else {
                    smallest = dir;
                }
            }

        }
        return smallest.getSize();
    }
    public static int sumDirs() {
        int total = 0;
        for (Directory dir : dirs) {
            if (dir.getSize() <= 100000)
                total += dir.getSize();
        }
        return total;
    }
    public static void main(String[] args) {
        BufferedReader reader;
        Directory root = new Directory("/", null);
        Directory current = root;
        try {
            reader = new BufferedReader(new FileReader("input.txt"));
            String line = reader.readLine();
            while (line != null) {
                String[] command = line.split(" ");
                if (command[1].equals("cd")) {
                    current = changeDirectory(current, root, command[2]);
                    line = reader.readLine();
                }
                else if (command[1].equals("ls")) {
                    boolean lsEnd = false;
                    while (!lsEnd) {
                        line = reader.readLine();
                        if (line != null) {
                            String[] item = line.split(" ");
                            if (item[0].equals("$")) {
                                lsEnd = true;
                            }
                            else {
                                processDirectoryItem(item, current);
                            }
                        } else {
                            lsEnd = true;
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        int sum = sumTree(root);
        int total = sumDirs();

        int sizeOfDeletedDir = deleteDir(sum);
        System.out.printf("Sum of the total sizes of directories with size <= 100000: %d\n", total);
        System.out.printf("Size of deletable directory: %d\n", sizeOfDeletedDir);
    }
}