import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


public class Main {
    public static void main(String[] args) {
        BufferedReader reader;
        Map<String, CaveRoom> caveRooms = new HashMap<>();
        try {
            reader = new BufferedReader(new FileReader("input.txt"));
            String line = reader.readLine();
            while (line != null) {
                CaveRoom newRoom = newCaveRoom(line.split(" "));
                caveRooms.put(newRoom.getName(), newRoom);
                line = reader.readLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        assignAdjacentCaves(caveRooms);
        initCaveDistances(caveRooms);
        initShortestPaths(caveRooms);
        Set<CaveRoom> path = new HashSet<>();
        List<CaveRoom[]> allPaths = new ArrayList<>();
        path.add(caveRooms.get("AA"));
        int maxFlow = calculateMaxFlowRateFrom(caveRooms, path, "AA", 0, 1);
        int maxFlowWithElephant = getMaxFlowWithElephant(caveRooms);
        System.out.printf("Max flow rate: %d\n", maxFlow);
        System.out.printf("Max flow rate with elephant: %d\n", maxFlowWithElephant);
        System.out.println();
    }
    public static void initShortestPaths(Map<String, CaveRoom> caves) {
        for (CaveRoom c : caves.values()) {
            dijkstra(caves, c.getName());
        }
    }
    public static Map<CaveRoom, Integer> getValveMap(List<Path> paths) {
        Map<CaveRoom, Integer> valveMap = new HashMap<>();
        Set<CaveRoom> valves = new HashSet<>();
        for (Path path : paths) {
            valves.addAll(path.getPath());
        }
        for (int i = 0; i < valves.size(); i++) {
            valveMap.put(valves.stream().toList().get(i), i);
        }
        return valveMap;
    }
    public static int generatePathKey(Path path, Map<CaveRoom, Integer> valveMap) {
        int key = 0;
        for (CaveRoom room : path.getPath()) {
            key += 2 << valveMap.get(room);
        }
        return key;
    }
    public static int getMaxFlowWithElephant(Map<String, CaveRoom> caves) {
        List<Path> allPaths = new ArrayList<>();
        for (int i = 4; i <= 30; i++) {
            getAllFlows(caves, "AA", new HashSet<>(), 0, 4, allPaths, i);
        }
        Map<CaveRoom, Integer> valveMap = getValveMap(allPaths);
        for (int i = 0; i < allPaths.size(); i++) {
            Path path = allPaths.get(i);
            System.out.printf("Paths processed %07d/%d\r", i + 1, allPaths.size());
            if (path.getPath().size() > (valveMap.size() / 2) - 3 && path.getPath().size() < (valveMap.size() / 2) + 3) {
                path.setKey(generatePathKey(path, valveMap));
            } else {
                path.setActive(false);
            }
        }
        System.out.println();
        int maxFlow = 0;
        for (int i = 0; i < allPaths.size(); i++) {
            Path p1 = allPaths.get(i);
            System.out.printf("Paths processed %07d/%d\r", i + 1, allPaths.size());
            if (p1.isActive()) {
                for (int f = i + 1; f < allPaths.size(); f++) {
                    Path p2 = allPaths.get(f);
                    if (p2.isActive()) {
                        int flow = 0;
                        if ((p2.getKey() & p1.getKey()) == 0) {
                            flow += p1.getFlow();
                            flow += p2.getFlow();
                        }
                        maxFlow = Math.max(maxFlow, flow);
                    }
                }
            }
        }
        return maxFlow;
    }
    public static void getAllFlows(Map<String, CaveRoom> caves, String startStr, Set<CaveRoom> path, int flow, int time, List<Path> allPaths, int maxTime) {
        CaveRoom start = caves.get(startStr);
        Set<CaveRoom> newPath = new HashSet<>(path.stream().toList());
        if (start.getFlowRate() > 0 && time != maxTime) {
            time++;
            flow += (30 - time) * start.getFlowRate();
        }
        if (!startStr.equals("AA"))
            newPath.add(start);
        if (time >= maxTime) {
            Path pathContainer = new Path(flow, newPath);
            allPaths.add(pathContainer);
            return;
        }
        for (CaveRoom n : caves.values()) {
            if (n.getFlowRate() > 0 && !n.equals(start) && !n.isValveOpen()) {
                int newTime = start.getDistanceTo(n.getName()) + time;
                n.setValveOpen(true);
                getAllFlows(caves, n.getName(), newPath, flow, newTime, allPaths, maxTime);
                n.setValveOpen(false);
            }
        }
        Path pathContainer = new Path(flow, newPath);
        allPaths.add(pathContainer);
    }

    public static int calculateMaxFlowRateFrom(Map<String, CaveRoom> caves, Set<CaveRoom> path, String startStr, int flow, int time) {
        CaveRoom start = caves.get(startStr);
        if (start.getFlowRate() > 0 && time != 30) {
            time++;
            flow += (30 - time + 1) * start.getFlowRate();
        }
        int maxFlow = flow;
        CaveRoom pathRoom = start;
        if (time >= 30) return flow;
        for (CaveRoom n : caves.values()) {
            if (n.getFlowRate() > 0 && !n.equals(start) && !n.isValveOpen() && !n.isOpenedByHuman()) {
                int newTime = start.getDistanceTo(n.getName()) + time;
                n.setValveOpen(true);
                int newFlow = calculateMaxFlowRateFrom(caves, path, n.getName(), flow, newTime);
                n.setValveOpen(false);
                if (newFlow > maxFlow) {
                    maxFlow = newFlow;
                    pathRoom = n;
                }
            }
        }
        path.add(pathRoom);
        return maxFlow;
    }
    public static void assignAdjacentCaves(Map<String, CaveRoom> caveRooms) {
        for (CaveRoom c : caveRooms.values()) {
            for (String adj : c.getAdjstrs()) {
                c.addAdj(caveRooms.get(adj));
            }
        }
    }
    public static void initCaveDistances(Map<String, CaveRoom> caveRooms) {
        for (CaveRoom c1 : caveRooms.values()) {
            for (CaveRoom c2 : caveRooms.values()) {
                c1.setDistanceTo(c2.getName(), Integer.MAX_VALUE);
                c1.setShortestPathTo(c2.getName(), new LinkedList<>());
            }
        }
    }
    private static void dijkstra(Map<String, CaveRoom> caves, String startStr) {
        CaveRoom start = caves.get(startStr);
        start.setDistanceTo(startStr, 0);

        Set<CaveRoom> closed = new HashSet<>();
        Set<CaveRoom> open = new HashSet<>();
        open.add(start);
        while(open.size() != 0) {
            CaveRoom curr = getLowestDistanceRoom(open, startStr);
            open.remove(curr);
            for (CaveRoom n : curr.getAdj()) {
                if (!closed.contains(n)) {
                    calculateMinimumDistance(n, curr, startStr);
                    open.add(n);
                }
            }
            closed.add(curr);
        }
    }
    public static void calculateMinimumDistance(CaveRoom n, CaveRoom source, String start) {
        int sourceDistance = source.getDistanceTo(start);
        // Make sure to init distance to nodes first to Integer.MAX_VALUE
        if (sourceDistance + 1 < n.getDistanceTo(start)) {
            n.setDistanceTo(start,sourceDistance + 1);
            LinkedList<CaveRoom> shortestPath = new LinkedList<>(source.getShortestPathTo(start));
            shortestPath.add(source);
            n.setShortestPathTo(start, shortestPath);
        }
    }
    public static CaveRoom getLowestDistanceRoom(Set<CaveRoom> open, String start) {
        CaveRoom lowestDistanceRoom = null;
        int lowestDistance = Integer.MAX_VALUE;
        for (CaveRoom room : open) {
            int roomDistance = room.getDistanceTo(start);
            if (roomDistance < lowestDistance) {
                lowestDistance = roomDistance;
                lowestDistanceRoom = room;
            }
        }
        return lowestDistanceRoom;
    }
    public static CaveRoom newCaveRoom(String[] caveParams) {
        int flow = Integer.parseInt(caveParams[4].split("=")[1].replace(";", ""));
        List<String> adj = new ArrayList<>();
        for (int i = 9; i < caveParams.length; i++) {
            adj.add(caveParams[i].replace(",", ""));
        }
        return new CaveRoom(flow, caveParams[1], adj);
    }
}