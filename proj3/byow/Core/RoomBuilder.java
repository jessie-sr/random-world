package byow.Core;

import byow.TileEngine.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class RoomBuilder {

    private TETile[][] board = new TETile[WIDTH][HEIGHT];
    private ArrayList<Room> rooms = new ArrayList<>();
    private HashMap<Room, Set<Room>> roomRelation = new HashMap<>(); // to keep track of whether rooms are connected

    private static final int WIDTH = 50; //TBD
    private static final int HEIGHT = 50;


    public int subareaCount; // the number of sub area of the board

    public RoomBuilder() {

        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                board[x][y] = Tileset.NOTHING;
            }
        }


    }

    public void addRoom(Room newRoom) {
        rooms.add(newRoom);
    }

    public void updateBoard() {
        // update the board according to the rooms List
        for(Room room: rooms) {
            // do sth to board
        }
    }

    public void renderBoard() { // Draw out the current board using render machine
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        // omitted



        ter.renderFrame(board);
    }

    public void pathGenerator(Position start) {
        // connect all the rooms using greedy algorithm
        // should modify the TETILE board
        // connect the nearest unconnected room
        // modify the roomRelation Map accordingly
        Position startPosition = start;

    }

    private boolean isConnected(Room room1, Room room2) {
        return roomRelation.get(room1).contains(room2);
    }
}
