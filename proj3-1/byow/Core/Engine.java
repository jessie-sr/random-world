package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import byow.byowTools.RandomUtils;
import edu.princeton.cs.algs4.StdDraw;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.Serializable;
import java.util.Random;

public class Engine implements Serializable {
    TERenderer teRender = new TERenderer();
    /* Feel free to change the WIDTH and HEIGHT. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;
    private boolean gameOver = false;

    private long seed;
    private RoomGenerator currGenerator;
    private TETile[][] backWorld;
    private TETile GUI;
    private int prevMouseX;
    private int prevMouseY;
    private Character preKeyPress;
    private String[] boardToWorldMap = {"outside","wall","grass"};
    private File savedWorlds = new File("./savedWorld");
    
    public Engine() {
        this.GUI = new TETile('#',"Mouse initialized ",
                new Color(216, 128, 128), Color.BLACK,"gui");
    }
    

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
        // display init UI first
        //process user's init input
        String initialInput = keyboardInit();
        if(initialInput.equals("q") || initialInput.equals("Q")) {
            return;
        }
        this.prevMouseX = (int) StdDraw.mouseX();
        this.prevMouseY = (int) StdDraw.mouseY();

        while(!gameOver) {
            checkMouse(prevMouseX,prevMouseY);
            checkKeyBoard();

        }



    }

    private void checkKeyBoard() {
        //TODO: Read n letters of player input
        if(StdDraw.hasNextKeyTyped()) {
            char currKey = StdDraw.nextKeyTyped();
            switch (currKey) {
                case 'w' -> {
                    if (currGenerator.board[currGenerator.playerX][currGenerator.playerY + 1] != 2) {
                        return;
                    } else {
                        currGenerator.playerY += 1;
                        currGenerator.updateUserPosition();
                        currGenerator.drawRooms();
                        teRender.renderFrame(backWorld, GUI);
                        System.out.println("KEYB MOVED " + currKey);
                        preKeyPress = 'w';
                    }
                }
                case 'a' -> {
                    if (currGenerator.board[currGenerator.playerX - 1][currGenerator.playerY] != 2) {
                        return;
                    } else {
                        currGenerator.playerX -= 1;
                        currGenerator.updateUserPosition();
                        currGenerator.drawRooms();
                        teRender.renderFrame(backWorld, GUI);
                        System.out.println("KEYB MOVED " + currKey);
                        preKeyPress = 'a';
                    }
                }
                case 's' -> {
                    if (currGenerator.board[currGenerator.playerX][currGenerator.playerY - 1] != 2) {
                        return;
                    } else {
                        currGenerator.playerY -= 1;
                        currGenerator.updateUserPosition();
                        currGenerator.drawRooms();
                        teRender.renderFrame(backWorld, GUI);
                        System.out.println("KEYB MOVED " +currKey);
                        preKeyPress = 's';
                    }
                }
                case 'd' -> {
                    if (currGenerator.board[currGenerator.playerX + 1][currGenerator.playerY] != 2) {
                        return;
                    } else {
                        currGenerator.playerX += 1;
                        currGenerator.updateUserPosition();
                        currGenerator.drawRooms();
                        teRender.renderFrame(backWorld, GUI);
                        System.out.println("KEYB MOVED "+currKey);
                        preKeyPress = 'd';
                    }
                }
                case ':' -> {
                    preKeyPress = ':';
                }
                case 'q' -> {
                    if(preKeyPress != ':') {
                        return;
                    }
                    //save the world
                    setupFiles();
                    saveTheWorld();
                    gameOver = true;

                }
            }
        }

    }

    private void saveTheWorld() {
        File prevWorld = new File(savedWorlds,"prevWorld.txt");
        persistenceUtils.writeObject(prevWorld,currGenerator);
    }

    private void setupFiles() {
        if(!savedWorlds.exists()) {
            savedWorlds.mkdir();
        }
    }

    public void checkMouse(int prevMouseX,int prevMouseY) {
        int currMouseX = (int) StdDraw.mouseX();
        int currMouseY = (int) StdDraw.mouseY();
        if(currMouseY == HEIGHT) {
            return;
        }
        if(prevMouseX != currMouseX || prevMouseY != currMouseY) {
            int mousePointer = currGenerator.board[currMouseX][currMouseY];
            this.prevMouseX = currMouseX;
            this.prevMouseY = currMouseY;
//            System.out.println("MOUSE MOVED");
            GUI = new TETile('#',"Your mouse is at  " + boardToWorldMap[mousePointer],
                    new Color(216, 128, 128), Color.BLACK,"gui");
            teRender.renderFrame(currGenerator.world,GUI);
//            StdDraw.pause(500);
        }
    }

    private String keyboardInit() {
        // display init UI first
        //process user's init input
        while (true) {
            drawFrame();
            String input = solicitNCharsInput(1);
            if(input.equals("q") || input.equals("Q")) {
                return input;
            }
            if(input.equals("n") || input.equals("N")) {
                this.seed =  RandomUtils.uniform(new Random(),Integer.MAX_VALUE);
                // initialize the tile rendering engine with a window of size WIDTH x HEIGHT
                teRender.initialize(WIDTH, HEIGHT);
                currGenerator = new RoomGenerator(seed);
                backWorld = currGenerator.world;
                // call generateRooms() and connect() first;
                currGenerator.generateRooms();
                currGenerator.connectRooms();
                //update user-position info
                currGenerator.initUserPosition();
                currGenerator.drawRooms();
                // draws the world to the screen.
                teRender.renderFrame(backWorld);
                return input;
            }
            if(input.equals("l") || input.equals("L")) {
                //load files
                if(!savedWorlds.exists()) {
                    System.out.println("no saved world, try create a new one pressing n");
                }
                else {
                    teRender.initialize(WIDTH, HEIGHT);
                    File prevGenerator = new File(savedWorlds,"prevWorld.txt");
                    this.currGenerator = persistenceUtils.readObject(prevGenerator,RoomGenerator.class);
                    this.backWorld = currGenerator.world;
                    this.currGenerator.drawRooms(); // call generateRooms() and connect() first;
                    teRender.renderFrame(backWorld);
                    return input;
                }
            }
        }


    }


    public String solicitNCharsInput(int n) {
        //TODO: Read n letters of player input
        int i=0;
        String ret="";
        while(i<n) {
            if(StdDraw.hasNextKeyTyped()) {
                char currKey = StdDraw.nextKeyTyped();
                i++;
                ret += currKey;
//                drawFrame(ret);
            }
        }
//        StdDraw.pause(500);
        return ret;
    }

    public static void main(String[] args) {
//        drawFrame();
    }

    public void drawMouse(int x, int y) {
        /* Take the input string S and display it at the center of the screen,
         * with the pen settings given below. */
        StdDraw.setCanvasSize(WIDTH * 8, HEIGHT * 16);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, WIDTH);
        StdDraw.setYscale(0, HEIGHT);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();

        StdDraw.setPenColor(Color.WHITE);
        Font fontBig = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(fontBig);
        StdDraw.text(WIDTH / 2, HEIGHT * 0.9, x + "and" +y);
        StdDraw.show();
    }


    public void drawFrame() {
        /* Take the input string S and display it at the center of the screen,
         * with the pen settings given below. */
        StdDraw.setCanvasSize(WIDTH * 8, HEIGHT * 16);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, WIDTH);
        StdDraw.setYscale(0, HEIGHT);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();

        StdDraw.setPenColor(Color.WHITE);
        Font fontBig = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(fontBig);
        StdDraw.text(WIDTH / 2, HEIGHT * 0.9, "CS61BL: THE GAME");

        Font fontSmall = new Font("Monaco", Font.BOLD, 20);
        StdDraw.setFont(fontSmall);
        StdDraw.text(WIDTH / 2, HEIGHT /2, "new game (N)");
        StdDraw.text(WIDTH / 2, HEIGHT /2 + 2, "load game (L)");
        StdDraw.text(WIDTH / 2, HEIGHT /2 + 4, "quit game (Q)");
        StdDraw.show();
    }



    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     *
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     *
     * In other words, running both of these:
     *   - interactWithInputString("n123sss:q")
     *   - interactWithInputString("lww")
     *
     * should yield the exact same world state as:
     *   - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        // TODO: Fill out this method so that it run the engine using the input
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.
        
        Engine engine = new Engine();

        // Start a new game by processing the first character 'n' (new game)
        if (input.charAt(0) == 'n') {
            int seedEndIndex = input.indexOf('s');
            long seed = Long.parseLong(input.substring(1, seedEndIndex));

            // initialize the tile rendering engine with a window of size WIDTH x HEIGHT
            teRender.initialize(WIDTH, HEIGHT);

            // initialize tiles

            RoomGenerator newGenerator = new RoomGenerator(seed);
            backWorld = newGenerator.world;
            newGenerator.drawRooms();

            // draws the world to the screen.
            teRender.renderFrame(backWorld);

            input = input.substring(seedEndIndex);
        } else {
            throw new IllegalArgumentException("Invalid input string. It should start with 'n'.");
        }

        // Process the remaining characters in the input string
        for (int i = 1; i < input.length(); i++) {
            char command = input.charAt(i);
            if (command == ':') {
                // Save and quit the game if the next two characters are 'q'
                if (input.substring(i, i + 2).equals(":q")) {
                    // engine.saveAndQuit();
                    break; // Exit the loop as the game is saved and quit
                } else {
                    throw new IllegalArgumentException("Invalid input string. ':' should be followed by 'q'.");
                }
            } else {
                // Process the regular commands: 'w', 's', 'a', 'd'
                engine.interactWithKeyboard();
            }
        }

        // Get the final world frame after all commands are processed
        return backWorld;
    }
}
