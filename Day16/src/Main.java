import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


public class Main {
    public static void main(String[] args) {
        BufferedReader reader;
        Map<String, CaveRoom> caveRooms = new HashMap<>();
        try {
            reader = new BufferedReader(new FileReader("test.txt"));
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
        System.out.println();
    }
    public static void assignAdjacentCaves(Map<String, CaveRoom> caveRooms) {
        for (CaveRoom c : caveRooms.values()) {
            for (String adj : c.getAdjstrs()) {
                c.addAdj(caveRooms.get(adj));
            }
        }
    }
    private static void dijkstra(Map<String, CaveRoom> caves, String startStr) {
        int vertices = caves.size();
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

                }
            }
        }
    }
    public static void calculateMinimumDistance(CaveRoom n, CaveRoom source, String start) {
        int sourceDistance = source.getDistanceTo(start);
        // Make sure to init distance to nodes first to Integer.MAX_VALUE
        if (sourceDistance + 1 < n.getDistanceTo(start)) {
            n.setDistanceTo(start,sourceDistance + 1);
            LinkedList<CaveRoom> = new LinkedList<>(source.)
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