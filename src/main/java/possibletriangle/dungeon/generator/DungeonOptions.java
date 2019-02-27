package possibletriangle.dungeon.generator;

public class DungeonOptions {

    public static final int MAX_FLOORS = 10;
    public static final int FLOOR_HEIGHT = 8;

    private int floorCount = 4;
    private boolean hasCeiling = true;
    private boolean rotateRooms = true;

    private int floorCount1 = floorCount;
    private boolean hasCeiling1 = hasCeiling;
    private boolean rotateRooms1 = rotateRooms;

    public int floorCount() {
        return floorCount1;
    }

    public boolean hasCeiling() {
        return hasCeiling1;
    }

    public boolean rotateRooms() {
        return rotateRooms1;
    }

    public void setFloorCount(int i) {
        this.floorCount1 = i;
    }

    public void setCeiling(boolean i) {
        this.hasCeiling1 = i;
    }

    public void setRotate(boolean i) {
        this.rotateRooms1 = i;
    }

    public void apply() {
        rotateRooms = rotateRooms1;
        hasCeiling = hasCeiling1;
        floorCount = floorCount1;
    }

    public void cancel() {
        rotateRooms1 = rotateRooms;
        hasCeiling1 = hasCeiling;
        floorCount1 = floorCount;
    }

}
