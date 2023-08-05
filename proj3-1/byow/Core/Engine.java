package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;

import byow.byowTools.RandomUtils;
import edu.princeton.cs.algs4.StdDraw;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.Serializable;
import java.util.Locale;
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
    private TETile[] GUI;
    private int prevMouseX;
    private int prevMouseY;
    private Character preKeyPress = 'w';
    private boolean lightOn = false;
    private static String[] boardToWorldMap = {"outside","wall","floor"};
    private static int GUINUM = 4;
    private File savedWorlds = new File("./savedWorld");
    private File savedWorlds2 = new File("./savedWorld2");

    
    public Engine() {
        this.GUI = new TETile[GUINUM];
        this.GUI[0] = new TETile('#',"Mouse initialized ",
                new Color(216, 128, 128), Color.BLACK,"gui0");
        this.GUI[1] = new TETile('#',"Press o to start new world ",
                new Color(216, 128, 128), Color.BLACK,"gui1");
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
                    if(preKeyPress != null && preKeyPress != ':') {
                        return;
                    }
                    //save the world
                    setupFiles();
                    saveTheWorld();
                    System.out.println("KEYB MOVED "+currKey);
                    gameOver = true;
                }
                case 'e' -> {
                    if(preKeyPress != ':') {
                        return;
                    }
                    //save the world2
                    setupFiles2();
                    saveTheWorld2();
                    System.out.println("KEYB MOVED "+currKey);
                    gameOver = true;
                }
                case 'o' -> {
                    createNewWorld("0");
                    preKeyPress = 'o';
                    System.out.println("KEYB INPUT "+currKey + "  NewWorldCreated!");

                }
                case 'l' -> {
                    if (lightOn) {
                        //light off.
                        currGenerator.lightOff();
                        teRender.renderFrame(backWorld, GUI);
                        preKeyPress = null;
                        System.out.println("KEYB INPUT "+currKey + "  LightsOff!");
                        lightOn = false;
                    } else {
                        currGenerator.lightOn();
                        teRender.renderFrame(backWorld, GUI);
                        preKeyPress = 'l';
                        System.out.println("KEYB INPUT "+currKey + "  LightsOn!");
                        lightOn = true;
                    }
                }
            }
        }

    }

    private void saveTheWorld() {
        File prevWorld = new File(savedWorlds,"prevWorld.txt");
        persistenceUtils.writeObject(prevWorld,currGenerator);
    }
    private void saveTheWorld2() {
        File prevWorld2 = new File(savedWorlds2,"prevWorld2.txt");
        persistenceUtils.writeObject(prevWorld2,currGenerator);
    }

    private void setupFiles() {
        if(!savedWorlds.exists()) {
            savedWorlds.mkdir();
        }
    }
    private void setupFiles2() {
        if(!savedWorlds2.exists()) {
            savedWorlds2.mkdir();
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
            GUI[0] = new TETile('#',"Your mouse is at  " + boardToWorldMap[mousePointer],
                    new Color(216, 128, 128), Color.BLACK,"gui");
            teRender.renderFrame(currGenerator.world,GUI);
//            StdDraw.pause(500);
        }
    }

    private String keyboardInit() {
        // display init UI first
        //process user's init input
        String inputHistory = "";
        while (true) {
            drawFrame();
            String input = solicitNCharsInput(1);
            input = input.toLowerCase(Locale.ROOT);
            if(input.equals("q") || input.equals("Q")) {
                return inputHistory;
            }
            if(input.equals("n") || input.equals("N")) {
                createNewWorld(inputHistory);
                return inputHistory;
            }
            if(input.equals("l") || input.equals("L")) {
                //load files
                if(!savedWorlds.exists()) {
                    System.out.println("no saved world, try create a new one pressing n");
                }
                else {
                    resumePrevWorld();
                    return inputHistory;
                }
            }
            if(input.equals("r")) {
                // display a new sub-menu
                drawSavedSubFrame();
                while(true) {
                    // wait for further input
                    String subInput = solicitNCharsInput(1);
                    subInput = subInput.toLowerCase();
                    if(subInput.equals("q")) {
                        break;
                    }
                    switch (subInput) {
                        case "0" -> {
                            if (!savedWorlds.exists()) {
                                System.out.println("no saved world, try other saved slots or create a new one pressing n");
                                break;
                            }
                            resumePrevWorld();
                            return inputHistory;
                        }
                        case "1" -> {
                            if (!savedWorlds2.exists()) {
                                System.out.println("no extra saved world, try other saved slots or create a new one pressing n");
                                break;
                            }
                            resumePrevWorld2();
                            return inputHistory;
                        }
                    }
                }

            }
            if(input.equals("0") || input.equals("1") || input.equals("2")) {
                inputHistory += input;

            }
        }


    }


    private void resumePrevWorld() {
        teRender.initialize(WIDTH, HEIGHT);
        File prevGenerator = new File(savedWorlds,"prevWorld.txt");
        if(!prevGenerator.exists()) {
            createNewWorld("0");
            return;
        }
        this.currGenerator = persistenceUtils.readObject(prevGenerator,RoomGenerator.class);
        this.backWorld = currGenerator.world;
        this.currGenerator.drawRooms(); // call generateRooms() and connect() first;
        teRender.renderFrame(backWorld);
    }

    private void resumePrevWorld2() {
        teRender.initialize(WIDTH, HEIGHT);
        File prevGenerator = new File(savedWorlds2,"prevWorld2.txt");
        if(!prevGenerator.exists()) {
            createNewWorld("0");
            return;
        }
        this.currGenerator = persistenceUtils.readObject(prevGenerator,RoomGenerator.class);
        this.backWorld = currGenerator.world;
        this.currGenerator.drawRooms(); // call generateRooms() and connect() first;
        teRender.renderFrame(backWorld);
    }

    private void createNewWorld(String inputHistory) {
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
        if(!inputHistory.isEmpty()) {
            Character appearanceIndex = inputHistory.charAt(inputHistory.length() - 1);
            currGenerator.changeUserAppearance(Integer.parseInt(String.valueOf(appearanceIndex)));
        }
        currGenerator.drawRooms();
        // draws the world to the screen.
        teRender.renderFrame(backWorld);
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

    private void drawSavedSubFrame() {
        // Check files' existence
        String promt1 = null;
        String promt2 = null;
        if (savedWorlds.exists()) {
            promt1 = "Filled";
        }
        else {
            promt1 = "empty";
        }
        if (savedWorlds2.exists()) {
            promt2 = "Filled";
        }
        else {
            promt2 = "Empty";
        }

        // Board settings
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
        StdDraw.text(WIDTH / 2, HEIGHT * 0.9, "Memory Slots");

        Font fontSmall = new Font("Monaco", Font.BOLD, 20);
        StdDraw.setFont(fontSmall);
        StdDraw.text(WIDTH / 2, HEIGHT / 2 + 2, "Default Slot: Press 0 to load;  Status: " + promt1);
        StdDraw.text(WIDTH / 2, HEIGHT / 2 - 2, "Additional Slot: Press 1 to load;  Status: " + promt2);
        StdDraw.text(WIDTH / 2, HEIGHT * 0.2, "Press Q to return to previous frame");
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
        StdDraw.text(WIDTH / 2, HEIGHT /2 + 4, "new game (N)");
        StdDraw.text(WIDTH / 2, HEIGHT /2 + 2, "load game (L)");
        StdDraw.text(WIDTH / 2, HEIGHT /2, "quit game (Q)");
        StdDraw.text(WIDTH / 2, HEIGHT /2 - 2, "check save slots (R)");
        StdDraw.text(WIDTH / 2, HEIGHT /3, "First, choose your appearance using digits 0,1,2");
        StdDraw.text(WIDTH / 2, HEIGHT /3 - 2, "Then press Q or L or N");
        StdDraw.text(WIDTH / 2, HEIGHT /3 - 4, "0:default avatar;  1:mountain avatar;  2:water avatar");
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
