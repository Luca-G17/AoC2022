import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    static int MAX_TIME = 24;
    public static void main(String[] args) {
        BufferedReader reader;
        List<Blueprint> blueprints = new ArrayList<>();
        try {
            reader = new BufferedReader(new FileReader("input.txt"));
            String line = reader.readLine();
            while(line != null) {
                String[] costs = line.split(" ");
                blueprints.add(new Blueprint(
                        new int[] { Integer.parseInt(costs[6]), 0, 0 },
                        new int[] { Integer.parseInt(costs[12]), 0, 0 },
                        new int[] { Integer.parseInt(costs[18]), Integer.parseInt(costs[21]), 0 },
                        new int[] { Integer.parseInt(costs[27]), 0, Integer.parseInt(costs[30]) }
                ));
                line = reader.readLine();
            }
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
        int part1 = sumOfBlueprintQuality(blueprints);
        System.out.printf("Total permutations %d\n", x);
        System.out.printf("Sum of quality levels of all blueprints (24 minutes): %d\n", part1);
        System.out.println();
        MAX_TIME = 32;
        x = 0;
        int part2 = productFirst3Blueprints(blueprints);
        System.out.printf("Total permutations %d\n", x);
        System.out.printf("Sum of quality levels of all blueprints (32 minutes): %d\n", part2);
    }
    // Resources: [ ORE, CLAY, OBSIDIAN, GEODE ]
    // Robots:    [ ORE, CLAY, OBSIDIAN, GEODE ]
    public static int[] updateResources(int[] resources, int[] robots) {
        for (int i = 0; i < 4; i++) {
            resources[i] = resources[i] + robots[i];
        }
        return resources;
    }
    public static int[] buildNewRobot(int[] robots, int r) {
        int[] newRbs = new int[4];
        for (int i = 0; i < 4; i++) {
            newRbs[i] = robots[i];
        }
        newRbs[r]++;
        return newRbs;
    }
    public static int[] newRobotCosts(int[] resources, int[] robot) {
        int[] newRes = new int[4];
        for (int i = 0; i < 3; i++) {
            newRes[i] = resources[i] - robot[i];
        }
        newRes[3] = resources[3];
        return newRes;
    }
    // If we produced a geode miner every turn could it do better
    public static boolean pathCanDoBetter(int time, int currMax, int geodes, int gMiners) {
        int score = geodes;
        for (int t = time; t < MAX_TIME; t++) {
            score += gMiners;
            gMiners++;
        }
        return score > currMax;
    }
    public static int productFirst3Blueprints(List<Blueprint> blueprints) {
        int product = 1;
        for (int i = 0; i < 3; i++) {
            product *= maxGeodesFromBlueprint(blueprints.get(i), 0, new int[] { 0, 0, 0, 0 }, new int[] { 1, 0, 0, 0 }, 0);
            System.out.printf("Blueprints processed %d\r", i + 1);
        }
        return product;
    }
    public static int sumOfBlueprintQuality(List<Blueprint> blueprints) {
        int qualitySum = 0;
        for (int i = 0; i < blueprints.size(); i++) {
            int geodes = maxGeodesFromBlueprint(blueprints.get(i), 0, new int[] { 0, 0, 0, 0 }, new int[] { 1, 0, 0, 0 }, 0);
            qualitySum += geodes * (i + 1);
            System.out.printf("Blueprints processed %d\r", i + 1);
        }
        return qualitySum;
    }
    public static boolean shouldBuildRobot(int r, int x, int y, int t, int max) {
        if (r == 3) return true;
        return max >= x;
        /*
        if (r == 3) return true;
        int timeLeft = MAX_TIME - t;
        return !(x * timeLeft + y >= timeLeft * max);

         */
    }
    public static int turnsRequiredToBuildRobot(Blueprint b, int robot, int[] resources, int[] robots, int time) {
        while (!b.canBuild(robot, resources)) {
            resources = updateResources(resources, robots);
            time++;
        }
        return time;
    }
    public static boolean canEverProduceRobot(Blueprint b, int robot, int[] robots) {
        for (int i = 0; i < 3; i++) {
            if (b.getRobot(robot)[i] > 0 && robots[i] == 0)
                return false;
        }
        return true;
    }
    static int x = 0;
    public static int maxGeodesFromBlueprint(Blueprint blueprint, int time, int[] resources, int[] robots, int currMax) {
        // Start of minute processing:
        if (time >= MAX_TIME) { // return geodes
            x++;
            return resources[3];
        }
        if (!pathCanDoBetter(time, currMax, resources[3], robots[3]))
            return 0;
        int maxG = 0;
        for (int i = 3; i >= 0; i--) {
            if (canEverProduceRobot(blueprint, i, robots)) {
                if (shouldBuildRobot(i, robots[i], resources[i], time, blueprint.getMaxResource(i))) {
                    int[] res = new int[] { resources[0], resources[1], resources[2], resources[3] };
                    int t = turnsRequiredToBuildRobot(blueprint, i, res, robots, time);
                    if (t >= MAX_TIME) {
                        x++;
                        maxG = Math.max(maxG, resources[3] + (MAX_TIME - t) * robots[3]);
                    } else {
                        res = newRobotCosts(res, blueprint.getRobot(i));
                        updateResources(res, robots);
                        int[] rbs = buildNewRobot(robots, i);
                        maxG = Math.max(maxG, maxGeodesFromBlueprint(blueprint, t + 1, res, rbs, maxG));
                    }
                }
            }
        }
        return maxG;
    }
}