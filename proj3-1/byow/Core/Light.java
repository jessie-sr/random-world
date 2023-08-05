package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.awt.*;
import java.io.Serializable;

public class Light implements Serializable {
    private int x;
    private int y;
    private TETile[][] world;
    private int[][] board;

    public Light(TETile[][] world, int[][] board, int x, int y) {
        this.world = world;
        this.board = board;
        if (board[x][y] == 2) {
            this.x = x;
            this.y = y;
            world[x][y] = Tileset.LIGHT;
        }
    }

    public void On() {
        for (int a = Math.max(x - 3, 0); a < Math.min(x + 4, Engine.WIDTH); a++) {
            for (int b = Math.max(y - 3, 0); b < Math.min(y + 4, Engine.HEIGHT); b++) {
                if (!(a == x && b == y) && board[a][b] == 2) {
                    int darkness = Math.max(Math.abs(x - a), Math.abs(y - b));
                    world[a][b] = new TETile('·', new Color(128, 192, 128), new Color(0, 0, 255 - 60 * darkness), "light");
                }
            }
        }
    }

    public void Off() {
        for (int a = Math.max(x - 3, 0); a < Math.min(x + 4, Engine.WIDTH); a++) {
            for (int b = Math.max(y - 3, 0); b < Math.min(y + 4, Engine.HEIGHT); b++) {
                if (board[a][b] == 1) {
                    world[a][b] = Tileset.WALL;
                } else if (board[a][b] == 2) {
                    world[a][b] = Tileset.FLOOR;
                }
            }
        }
    }
}
