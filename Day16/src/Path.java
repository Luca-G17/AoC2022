import java.util.HashSet;
import java.util.Set;

public class Path {
    private boolean active = true;
    private final Set<CaveRoom> path;
    private final int flow;
    private int key = 0;
    public Path(int flow, Set<CaveRoom> path) {
        this.flow = flow;
        this.path = path;
    }
    public int getFlow() { return flow; }
    public Set<CaveRoom> getPath() { return path; }
    public int getKey() { return key; }
    public void setKey(int key) { this.key = key; }

    public void setActive(boolean active) { this.active = active; }

    public boolean isActive() { return active; }
}
