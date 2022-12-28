import java.util.ArrayList;
import java.util.List;

public class BlizzardState {
    private int[][] map;
    private List<Blizzard> blizzards = new ArrayList<>();
    public BlizzardState(int[][] map, List<Blizzard> blizzards) {
        this.map = new int[map.length][map[0].length];
        for (int i = 0; i < map.length; i++) {
            for (int f = 0; f < map[i].length; f++) {
                this.map[i][f] = map[i][f];
            }
        }
        this.blizzards.addAll(blizzards);
    }
    public List<Blizzard> getBlizzards() { return blizzards; }
    public int[][] getMap() { return map; }
}
