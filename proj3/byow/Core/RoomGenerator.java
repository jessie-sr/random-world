package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.awt.*;
import java.io.Serializable;
import java.util.*;

public class RoomGenerator implements Serializable {
    private Random random;
    private int numRooms;
    private int minWidth;
    private int maxWidth;
    private int minHeight;
    private int maxHeight;
    public int playerX;
    public int playerY;
    public Room closestRoom;

    public int[][] board = new int[Engine.WIDTH][Engine.HEIGHT];
    public TETile[][] world;
    public TETile userAppearance = Tileset.AVATAR;
    public boolean isLightOn = false;
    public static TETile[] appearanceBindings = {Tileset.AVATAR, Tileset.MOUNTAIN, Tileset.WATER};
    private Map<Integer, Room> roomMap;

    public void initUserPosition() {
        for(int row = 0;row < Engine.WIDTH;row++) {
            for(int col = 0;col< Engine.HEIGHT;col++) {
                if(board[row][col] == 2) {
                    playerX = row;
                    playerY = col;
                    world[playerX][playerY] = Tileset.AVATAR;
                    return;
                }
            }
        }
    }

    public void updateUserPosition() {
        world[playerX][playerY] = Tileset.AVATAR;
    }

    public void changeUserAppearance(int option) {
        userAppearance = appearanceBindings[option];
    }

    public RoomGenerator(TETile[][] world, long seed) {
        this.world = world;
        this.random = new Random(seed);
        this.numRooms = 30;
        this.minWidth = 4;
        this.maxWidth = Engine.WIDTH / 10;
        this.minHeight = 4;
        this.maxHeight = Engine.HEIGHT / 3;
        this.roomMap = new HashMap<>();
    }

    public RoomGenerator(long seed) {
        this.world = new TETile[Engine.WIDTH][Engine.HEIGHT];
        for (int x = 0; x < Engine.WIDTH; x += 1) {
            for (int y = 0; y < Engine.HEIGHT; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }
        this.random = new Random(seed);
        this.numRooms = 30;
        this.minWidth = 4;
        this.maxWidth = Engine.WIDTH / 10;
        this.minHeight = 4;
        this.maxHeight = Engine.HEIGHT / 3;
        this.roomMap = new HashMap<>();
    }




    public void drawRooms() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j] == 1) {
                    if (i > 0 && i < board.length - 1 && j > 0 && j < board[0].length - 1 && changeWall(i, j)) {
                        board[i][j] = 2;
                        world[i][j] = Tileset.FLOOR;
                    } else {
                        world[i][j] = Tileset.WALL;
                    }
                } else if (board[i][j] == 2) {
                    world[i][j] = Tileset.FLOOR;
                }
            }
        }
        if(isLightOn) {
            lightOn();
        }
        world[playerX][playerY] = userAppearance;
    }

    public boolean changeWall(int i, int j) {
        if (board[i - 1][j] == 2 && board[i + 1][j] == 2) {
            return true;
        } else if (board[i][j - 1] == 2 && board[i][j + 1] == 2) {
            return true;
        }
        return false;
    }

    public void generateRooms() {
        int count = 0;
        for (int i = 0; i < numRooms; i++) {
            int width = random.nextInt(maxWidth - minWidth + 1) + minWidth;
            int height = random.nextInt(maxHeight - minHeight + 1) + minHeight;
            int x = random.nextInt(board.length - width);
            int y = random.nextInt(board[0].length - height);

            if (isAreaAvailable(x, y, width, height)) {
                Room newRoom = new Room(world, count, width, height, x, y);
                roomMap.put(count, newRoom);
                occupyArea(x, y, width, height);
                count++;
            }
        }
        // Update numRooms to the actual count.
        this.numRooms = count;
    }

    private boolean isAreaAvailable(int x, int y, int width, int height) {
        for (int i = x; i < x + width; i++) {
            for (int j = y; j < y + height; j++) {
                if (board[i][j] != 0) {
                    return false; // The area is already occupied
                }
            }
        }
        return true;
    }

    private void occupyArea(int x, int y, int width, int height) {
        for (int i = x; i < x + width; i++) {
            for (int j = y; j < y + height; j++) {
                if (i == x || i == x + width - 1 || j == y || j == y + height - 1) {
                    // Mark wall-area as 1.
                    board[i][j] = 1;
                } else {
                    // Mark floor-area as 2.
                    board[i][j] = 2;
                }
            }
        }
    }

    public void connectRooms() {
        for (int i = 0; i < numRooms - 1; i++) {
            Room room1 = roomMap.get(i);
            Room room2 = roomMap.get(i + 1);

            // Get the center coordinates of each room
            int x1 = room1.getX() + room1.getWidth() / 2;
            int y1 = room1.getY() + room1.getHeight() / 2;
            int x2 = room2.getX() + room2.getWidth() / 2;
            int y2 = room2.getY() + room2.getHeight() / 2;

            // Generate a hallway between the two rooms
            generateHallway(x1, y1, x2, y2);
        }

    }

    private void generateHallway(int x1, int y1, int x2, int y2) {
        int dx = Integer.compare(x2, x1); // Direction of movement in the x-axis
        int dy = Integer.compare(y2, y1); // Direction of movement in the y-axis

        // Generate a horizontal hallway
        for (int x = x1; x != x2 + dx; x += dx) {
            int y = y1;
            if (board[x][y] == 1) {
                board[x][y] = 2;
                if (board[x][y + 1] == 0) {
                    board[x][y + 1] = 1;
                }
                if (board[x][y - 1] == 0) {
                    board[x][y - 1] = 1;
                }
            } else if (board[x][y] == 0) {
                board[x][y] = 2;
                if (board[x][y + 1] == 0) {
                    board[x][y + 1] = 1;
                }
                if (board[x][y - 1] == 0) {
                    board[x][y - 1] = 1;
                }
            }
        }

        if (dy > 0) {
            board[x2 + dx][y1 - 1] = 1;
            board[x2 + dx][y1] = 1;
        } else if (dy < 0){
            board[x2 + dx][y1 + 1] = 1;
            board[x2 + dx][y1] = 1;
        }

        // Generate a vertical hallway
        for (int y = y1; y != y2 + dy; y += dy) {
            int x = x2;
            if (board[x][y] == 1) {
                board[x][y] = 2;
                if (board[x - 1][y] == 0) {
                    board[x - 1][y] = 1;
                }
                if (board[x + 1][y] == 0) {
                    board[x + 1][y] = 1;
                }
            } else if (board[x][y] == 0) {
                board[x][y] = 2;
                if (board[x - 1][y] == 0) {
                    board[x - 1][y] = 1;
                }
                if (board[x + 1][y] == 0) {
                    board[x + 1][y] = 1;
                }
            }
        }
    }

    public void lightOn() {
        for (Room r: roomMap.values()) {
            int x = r.getX() + r.getWidth() / 2;
            int y = r.getY() + r.getHeight() / 2;
            if (board[x][y] == 2) {
                world[x][y] = Tileset.LIGHT;
                for (int a = Math.max(x - 3, 0); a < Math.min(x + 4, Engine.WIDTH); a++) {
                    for (int b = Math.max(y - 3, 0); b < Math.min(y + 4, Engine.HEIGHT); b++) {
                        if (!(a == x && b == y) && board[a][b] == 2) {
                            int darkness = Math.max(Math.abs(x - a), Math.abs(y - b));
                            world[a][b] = new TETile('·', new Color(128, 192, 128), new Color(0, 0, 255 - 60 * darkness), "light");
                        }
                    }
                }
            }
            world[playerX][playerY] = userAppearance;
        }
    }

    public void lightOff() {
        drawRooms();
    }
}
