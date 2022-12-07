import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Directory {
    private final String name;
    private final Directory parent;
    private final Map<String, Integer> files = new HashMap<>();
    private final Map<String, Directory> children = new HashMap<>();
    private int size = 0;

    public Directory(String name, Directory parent) {
        this.name = name;
        this.parent = parent;
    }

    public int totalFileSize() {
        int total = 0;
        for (Integer value : files.values()) {
            total += value;
        }
        return total;
    }
    public void addFile(String name, int size) { files.put(name, size); }
    public void addSize(int size) { this.size += size; }
    public void addChild(Directory child) { children.put(child.getName(), child); }

    public Directory getParent() { return parent; }
    public int getSize() { return size; }
    public Map<String, Directory> getChildren() { return children; }
    public Map<String, Integer> getFiles() { return files; }
    public String getName() { return name; }
}
