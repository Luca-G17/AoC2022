import java.awt.Point;

public class Elf {
    private Point currentPos = new Point();
    private Point proposedPos = new Point();
    private boolean overlap = false;
    public Elf(Point p) {
        currentPos.setLocation(p);
    }

    public boolean isOverlap() { return overlap; }
    public void setOverlap(boolean overlap) { this.overlap = overlap; }

    public Point getCurrentPos() { return currentPos; }
    public Point getProposedPos() { return proposedPos; }
    public void setCurrentPos(Point currentPos) { this.currentPos = currentPos; }
    public void setProposedPos(Point proposedPos) {
        this.proposedPos.setLocation(proposedPos);
    }

    public void move(Elf[][] grid) {
        grid[currentPos.y][currentPos.x] = null;
        currentPos.setLocation(proposedPos);
        grid[currentPos.y][currentPos.x] = this;
        overlap = false;
    }
    public boolean setProposedPos(Elf[][] elves, String[] directions) {
        Point pp = null;
        boolean moved = true;
        for (String s : directions) {
            pp = getNewPosition(elves, s);
            if (pp != null)
                break;
        }
        if (!shouldMove(elves, directions) || pp == null) { // Shouldn't move || can't move
            pp = new Point(currentPos.x, currentPos.y);
            moved = false;
        }
        proposedPos = pp;
        return moved;
    }
    private boolean shouldMove(Elf[][] elves, String[] directions) {
        for (String s : directions) {
            if (getNewPosition(elves, s) == null)
                return true;
        }
        return false;
    }
    private boolean checkRow(Elf[][] elves, int r, int x) {
        for (int i = -1; i < 2; i++) {
            if (elves[r][x + i] != null)
                return false;
        }
        return true;
    }
    private boolean checkCol(Elf[][] elves, int c, int y) {
        for (int i = -1; i < 2; i++) {
            if (elves[y + i][c] != null)
                return false;
        }
        return true;
    }
    private Point getNewPosition(Elf[][] elves, String dir) {
        switch (dir) {
            case "North" -> { // N, NE, NW
                if (checkRow(elves, currentPos.y - 1, currentPos.x))
                    return new Point(currentPos.x, currentPos.y - 1);
            }
            case "South" -> { // S, SE, SW
                if (checkRow(elves, currentPos.y + 1, currentPos.x))
                    return new Point(currentPos.x, currentPos.y + 1);
            }
            case "West" -> { // W, NW, SW
                if (checkCol(elves, currentPos.x - 1, currentPos.y))
                    return new Point(currentPos.x - 1, currentPos.y);
            }
            case "East" -> { // E, NE, SE
                if (checkCol(elves, currentPos.x + 1, currentPos.y))
                    return new Point(currentPos.x + 1, currentPos.y);
            }
        }
        return null;
    }

    @Override
    public int hashCode() {
        return proposedPos.hashCode();
    }
}
