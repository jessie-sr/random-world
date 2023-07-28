package byow.Core;

public class Room {
    public int width;
    public int height;
    public Position position;

    public Room(int width, int height, int x, int y) {
        this.width = width;
        this.height = height;
        this.position = new Position(x,y);
    }
}
