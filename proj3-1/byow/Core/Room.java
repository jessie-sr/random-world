package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.awt.*;
import java.io.Serializable;

public class Room implements Serializable {

    private int id;
    private int width;
    private int height;
    private int x;
    private int y;

    private Light light;

    private TETile[][] world;

    public Room(TETile[][] world, int id, int width, int height, int x, int y) {
        this.id = id;
        this.world = world;
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void addLight(int[][] board) {
        int i = this.getX() + this.getWidth() / 2;
        int j = this.getY() + this.getHeight() / 2;
        this.light = new Light(this.world, board, i, j);
    }

    public Light getLight() {
        return this.light;
    }
}

