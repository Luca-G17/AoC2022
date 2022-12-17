import java.util.*;

public class CaveRoom {
    private final List<String> adjStrs;
    private final List<CaveRoom> adj = new ArrayList<>();
    private final Map<String, LinkedList<CaveRoom>> shortestPaths = new HashMap<>();
    private final Map<String, Integer> distances = new HashMap<>();
    private final int flowRate;
    private final String name;
    public CaveRoom(int flowRate, String name, List<String> adjStrs) {
        this.flowRate = flowRate;
        this.name = name;
        this.adjStrs = adjStrs;
    }

    public List<String> getAdjstrs() { return adjStrs; }
    public void addAdj(CaveRoom c) { adj.add(c); }
    public List<CaveRoom> getAdj() { return adj; }
    public int getFlowRate() { return flowRate; }
    public String getName() { return name; }
    public Integer getDistanceTo(String caveRoom) { return distances.get(caveRoom); }
    public LinkedList<CaveRoom> getShortestPathTo(String startRoom) { return shortestPaths.get(startRoom); }
    public void setDistanceTo(String caveRoom, int distance) { distances.put(caveRoom, distance); }
    public void setShortestPathTo(String caveRoom, LinkedList<CaveRoom> shortestPath) { shortestPaths.}
}
