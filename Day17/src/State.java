import java.awt.*;

public class State {
    private final int rockType;
    private final int jetIndex;
    private final int minX;
    private final Rock rock;
    private final int rockIndex;
    private int cycleLength;
    private final int highestPoint;
    private int cycleHeight;
    State(int rockType, int jetIndex, int rockIndex, int minX, Rock rock, int highestPoint) {
        this.rockType = rockType;
        this.jetIndex = jetIndex;
        this.minX = minX;
        this.rock = rock;
        this.rockIndex = rockIndex;
        this.highestPoint = highestPoint;
    }

    public int getCycleHeight() { return cycleHeight; }
    public void setCycleHeight(int cycleHeight) { this.cycleHeight = cycleHeight; }
    public int getHighestPoint() { return highestPoint; }
    public int getRockIndex() { return rockIndex; }
    public Rock getRock() { return rock; }
    public int getJetIndex() { return jetIndex; }

    public int getMinX() { return minX; }

    public int getRockType() { return rockType; }

    public int getCycleLength() { return cycleLength; }

    public void setCycleLength(int cycleLength) { this.cycleLength = cycleLength; }

    @Override
    public boolean equals(Object obj) {
        State s2 = (State) obj;
        return (s2.getJetIndex() == this.getJetIndex()
                 && s2.getMinX() == this.getMinX()
                 && s2.getRockType() == this.getRockType());
    }
}
