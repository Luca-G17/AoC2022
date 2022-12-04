import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        BufferedReader reader;
        int fullyContainedCount = 0;
        int overlapCount = 0;
        try {
            reader = new BufferedReader(new FileReader("input.txt"));
            String line = reader.readLine();
            while (line != null) {
                if (doSectionAssignmentsFullyOverlap(line))
                    fullyContainedCount++;
                if (doSectionAssignmentsOverlap(line))
                    overlapCount++;
                line = reader.readLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.printf("Total number of assignment pairs that fully contain each other: %d\n", fullyContainedCount);
        System.out.printf("Total number of assignments that overlap: %d\n", overlapCount);
    }
    public static boolean doSectionAssignmentsOverlap(String input) {
        String[] assignmentStrings = input.split(",");
        int[][] assignments = new int[2][];
        assignments[0] = convertAssignmentToInt(assignmentStrings[0]);
        assignments[1] = convertAssignmentToInt(assignmentStrings[1]);
        if (assignments[0][0] > assignments[1][0]) {
            // swap assignments if the first starts after the second
            int[] tmp = assignments[0];
            assignments[0] = assignments[1];
            assignments[1] = tmp;
        }
        return assignments[0][1] >= assignments[1][0];
    }
    public static boolean doSectionAssignmentsFullyOverlap(String input) {
        String[] assignmentStrings = input.split(",");
        int[][] assignments = new int[2][];
        assignments[0] = convertAssignmentToInt(assignmentStrings[0]);
        assignments[1] = convertAssignmentToInt(assignmentStrings[1]);
        if (assignmentLength(assignments[0]) < assignmentLength(assignments[1])) {
            // swap assignments
            int[] tmp = assignments[0];
            assignments[0] = assignments[1];
            assignments[1] = tmp;
        }
        // Longer assignment is now first
        // check if both a1 start <= a2 start and a1 end > a2 end
        return assignments[0][0] <= assignments[1][0] && assignments[0][1] >= assignments[1][1];
    }
    public static int[] convertAssignmentToInt(String assignment) {
        String[] startEnd = assignment.split("-");
        return new int[] { Integer.parseInt(startEnd[0]), Integer.parseInt(startEnd[1]) };
    }
    public static int assignmentLength(int[] assigment) {
        return assigment[1] - assigment[0];
    }
}