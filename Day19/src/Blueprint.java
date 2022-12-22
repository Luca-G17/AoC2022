public class Blueprint {
    private final int[] oreRobot; // Costs: [ ORE, CLAY, OBSIDIAN ]
    private final int[] clayRobot;
    private final int[] obsidianRobot;
    private final int[] geodeRobot;
    private final int[] maxResources;
    public Blueprint(int[] oreRobot, int[] clayRobot, int[] obsidianRobot, int[] geodeRobot) {
        this.oreRobot = oreRobot;
        this.clayRobot = clayRobot;
        this.obsidianRobot = obsidianRobot;
        this.geodeRobot = geodeRobot;
        maxResources = new int[4];
        for (int i = 0; i < 3; i++) {
            int max = 0;
            for (int r = 0; r < 4; r++) {
                max = Math.max(max, getRobot(r)[i]);
            }
            maxResources[i] = max;
        }
        maxResources[3] = Integer.MAX_VALUE;
    }
    private boolean canBuild(int[] robot, int[] resources) {
        for (int i = 0; i < 3; i++) {
            if (robot[i] > resources[i])
                return false;
        }
        return true;
    }
    public boolean canBuild(int robot, int[] resources) {
        return switch (robot) {
            case 0 -> canBuild(getOreRobot(), resources);
            case 1 -> canBuild(getClayRobot(), resources);
            case 2 -> canBuild(getObsidianRobot(), resources);
            case 3 -> canBuild(getGeodeRobot(), resources);
            default -> false;
        };
    }
    public int[] getRobot(int robot) {
        return switch (robot) {
            case 0 -> getOreRobot();
            case 1 -> getClayRobot();
            case 2 -> getObsidianRobot();
            case 3 -> getGeodeRobot();
            default -> null;
        };
    }
    public int getMaxResource(int r) { return maxResources[r]; }
    public int[] getOreRobot() { return oreRobot; }
    public int[] getClayRobot() { return clayRobot; }
    public int[] getObsidianRobot() { return obsidianRobot; }
    public int[] getGeodeRobot() { return geodeRobot; }
}
