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
    /* Feel free to change the WIDTH and HEIGHT. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;
    private static String[] boardToWorldMap = {"outside", "wall", "floor"};
    private static int GUINUM = 4;
    TERenderer teRender = new TERenderer();
    private boolean gameOver = false;
    private long seed;
    private RoomGenerator currGenerator;
    private TETile[][] backWorld;
    private TETile[] GUI;
    private int prevMouseX;
    private int prevMouseY;
    private Character preKeyPress = 'w';
    private File savedWorlds = new File("./savedWorld.txt");
    private File savedWorlds2 = new File("./savedWorld2");


    public Engine() {
        this.GUI = new TETile[GUINUM];
        this.GUI[0] = new TETile('#', "Mouse initialized ",
                new Color(216, 128, 128), Color.BLACK, "gui0");
        this.GUI[1] = new TETile('#', "Press o to start new world ",
                new Color(216, 128, 128), Color.BLACK, "gui1");
    }

    public static void main(String[] args) {
//        drawFrame();
    }

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
        // display init UI first
        //process user's init input
        keyboardInit();
        this.prevMouseX = (int) StdDraw.mouseX();
        this.prevMouseY = (int) StdDraw.mouseY();

        while (!gameOver) {
            checkMouse(prevMouseX, prevMouseY);
            checkKeyBoard("");

        }

    }

    private String keyboardInit() {
        // display init UI first
        //process user's init input
        String inputHistory = "";
        while (true) {
            DrawFrame.drawFrame();
            String input = solicitNCharsInput(1);
            input = input.toLowerCase(Locale.ROOT);
            if (input.equals("q") || input.equals("Q")) {
                System.exit(0);
                return inputHistory;
            }
            if (input.equals("n") || input.equals("N")) {
                DrawFrame.drawInputStringToFrame("");
                String inputSeed = solicitSeed();
                createNewWorld(inputHistory, inputSeed, true);
                return inputHistory;
            }
            if (input.equals("l") || input.equals("L")) {
                //load files
//                if(!savedWorlds.exists()) {
//                    System.out.println("no saved world, try create a new one pressing n");
//                }
//                else {
                resumePrevWorld(true);
                return inputHistory;
//                }
            }
            if (input.equals("r")) {
                // display a new sub-menu
                DrawFrame.drawSavedSubFrame();
                while (true) {
                    // wait for further input
                    String subInput = solicitNCharsInput(1);
                    subInput = subInput.toLowerCase();
                    if (subInput.equals("q")) {
                        break;
                    }
                    switch (subInput) {
                        case "0" -> {
                            if (!savedWorlds.exists()) {
                                System.out.println("no saved world, try other saved slots or create a new one pressing n");
                                break;
                            }
                            resumePrevWorld(true);
                            return inputHistory;
                        }
                        case "1" -> {
                            if (!savedWorlds2.exists()) {
                                System.out.println("no extra saved world, try other saved slots or create a new one pressing n");
                                break;
                            }
                            resumePrevWorld2(true);
                            return inputHistory;
                        }
                    }
                }

            }
            if (input.equals("s")) {
                // display a new sub-menu
                DrawFrame.drawAppearanceSelection();
                while (true) {
                    // wait for further input
                    String subInput = solicitNCharsInput(1);
                    subInput = subInput.toLowerCase();
                    if (subInput.equals("q")) {
                        break;
                    }
                    if (subInput.equals("0") || subInput.equals("1") || subInput.equals("2")) {
                        inputHistory += subInput;
                        break;
                    }

                }


            }
        }


    }

    private void checkKeyBoard(String ch) {
        //DONE: Read n letters of player input
        if (StdDraw.hasNextKeyTyped() || !ch.isEmpty()) {
            char currKey;
            if (!ch.isEmpty()) {
                currKey = ch.charAt(0);
            } else {
                currKey = StdDraw.nextKeyTyped();
            }
            switch (currKey) {
                case 'w' -> {
                    if (currGenerator.board[currGenerator.playerX][currGenerator.playerY + 1] != 2) {
                        return;
                    } else {
                        currGenerator.playerY += 1;
                        System.out.println("KEYB MOVED " + currKey);
                        preKeyPress = 'w';
                    }
                }
                case 'a' -> {
                    if (currGenerator.board[currGenerator.playerX - 1][currGenerator.playerY] != 2) {
                        return;
                    } else {
                        currGenerator.playerX -= 1;
                        System.out.println("KEYB MOVED " + currKey);
                        preKeyPress = 'a';
                    }
                }
                case 's' -> {
                    if (currGenerator.board[currGenerator.playerX][currGenerator.playerY - 1] != 2) {
                        return;
                    } else {
                        currGenerator.playerY -= 1;
                        System.out.println("KEYB MOVED " + currKey);
                        preKeyPress = 's';
                    }
                }
                case 'd' -> {
                    if (currGenerator.board[currGenerator.playerX + 1][currGenerator.playerY] != 2) {
                        return;
                    } else {
                        currGenerator.playerX += 1;
                        System.out.println("KEYB MOVED " + currKey);
                        preKeyPress = 'd';
                    }
                }
                case ':' -> {
                    preKeyPress = ':';
                }
                case 'q' -> {
                    if (preKeyPress != ':') {
                        return;
                    }
                    //save the world
                    setupFiles();
                    saveTheWorld();
                    System.out.println("KEYB MOVED " + currKey);
//                    gameOver = true;
                }
                case 'e' -> {
                    if (preKeyPress != ':') {
                        return;
                    }
                    //save the world2
                    setupFiles2();
                    saveTheWorld2();
                    System.out.println("KEYB MOVED " + currKey);
                    gameOver = true;
                }
                case 'o' -> {
                    createNewWorld("0", "", true);
                    preKeyPress = 'o';
                    System.out.println("KEYB INPUT " + currKey + "  NewWorldCreated!");

                }
                case 'l' -> {
//                    if (currGenerator.isLightOn) {
//                        //light off.
//                        currGenerator.isLightOn = false;
//                        currGenerator.lightOff();
//                        teRender.renderFrame(backWorld, GUI);
//                        System.out.println("KEYB INPUT " + currKey + "  LightsOff!");
//                    } else {
//                        currGenerator.lightOn();
//                        teRender.renderFrame(backWorld, GUI);
//                        preKeyPress = 'l';
//                        System.out.println("KEYB INPUT " + currKey + "  LightsOn!");
//                        currGenerator.isLightOn = true;
//                    }
                    resumePrevWorld(false);
                }
            }
            if (currKey != 'l' && currKey != 'o') { // if user inputs wasd
                currGenerator.updateUserPosition();
                currGenerator.drawRooms();
                teRender.renderFrame(backWorld, GUI);
            }

        }

    }

    private void saveTheWorld() {
//        File prevWorld = new File(savedWorlds, "prevWorld.txt");
        persistenceUtils.writeObject(savedWorlds, currGenerator);
    }

    private void saveTheWorld2() {
        File prevWorld2 = new File(savedWorlds2, "prevWorld2.txt");
        persistenceUtils.writeObject(prevWorld2, currGenerator);
    }

    // tobedone!!!!!
    private void setupFiles() {
//        if(!savedWorlds.exists()) {
//            savedWorlds.mkdir();
//        }
//        savedWorlds.mkdir();
    }

    private void setupFiles2() {
        if (!savedWorlds2.exists()) {
            savedWorlds2.mkdir();
        }
    }

    public void checkMouse(int prevMouseX, int prevMouseY) {
        int currMouseX = (int) StdDraw.mouseX();
        int currMouseY = (int) StdDraw.mouseY();
        if (currMouseY == HEIGHT) {
            return;
        }
        if (prevMouseX != currMouseX || prevMouseY != currMouseY) {
            int mousePointer = currGenerator.board[currMouseX][currMouseY];
            this.prevMouseX = currMouseX;
            this.prevMouseY = currMouseY;
//            System.out.println("MOUSE MOVED");
            GUI[0] = new TETile('#', "Your mouse is at  " + boardToWorldMap[mousePointer],
                    new Color(216, 128, 128), Color.BLACK, "gui");
            teRender.renderFrame(currGenerator.world, GUI);
//            StdDraw.pause(500);
        }
    }

    private void resumePrevWorld(boolean keyBoardStart) {
        teRender.initialize(WIDTH, HEIGHT);
//        File prevGenerator = new File(savedWorlds, "prevWorld.txt");
//        if (!prevGenerator.exists()) {
//            createNewWorld("0", "", keyBoardStart);
//            return;
//        }
        this.currGenerator = persistenceUtils.readObject(savedWorlds, RoomGenerator.class);
        this.backWorld = currGenerator.world;
        this.currGenerator.drawRooms(); // call generateRooms() and connect() first;
        if (keyBoardStart) {
            teRender.renderFrame(backWorld);
        }
    }

    private void resumePrevWorld2(boolean keyBoardStart) {
        teRender.initialize(WIDTH, HEIGHT);
        File prevGenerator = new File(savedWorlds2, "prevWorld2.txt");
        if (!prevGenerator.exists()) {
            createNewWorld("0", "", keyBoardStart);
            return;
        }
        this.currGenerator = persistenceUtils.readObject(prevGenerator, RoomGenerator.class);
        this.backWorld = currGenerator.world;
        this.currGenerator.drawRooms(); // call generateRooms() and connect() first;
        if (keyBoardStart) {
            teRender.renderFrame(backWorld);
        }
    }

    private void createNewWorld(String inputHistory, String inputSeed, Boolean keyBoardStart) {
        if (!inputSeed.isEmpty()) {
            this.seed = Long.parseLong(inputSeed);
        } else {
            this.seed = RandomUtils.uniform(new Random(), Integer.MAX_VALUE);
        }
        // initialize the tile rendering engine with a window of size WIDTH x HEIGHT
        teRender.initialize(WIDTH, HEIGHT);
        currGenerator = new RoomGenerator(seed);
        backWorld = currGenerator.world;
        // call generateRooms() and connect() first;
        currGenerator.generateRooms();
        currGenerator.connectRooms();
        //update user-position info
        currGenerator.initUserPosition();
        if (!inputHistory.isEmpty()) {
            Character appearanceIndex = inputHistory.charAt(inputHistory.length() - 1);
            currGenerator.changeUserAppearance(Integer.parseInt(String.valueOf(appearanceIndex)));
        }
        currGenerator.drawRooms();
        // draws the world to the screen.
        if (keyBoardStart) {
            teRender.renderFrame(backWorld);
        }
    }

    public String solicitSeed() {
        //DONE: Read n letters of player input
        String ret = "";
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char currKey = StdDraw.nextKeyTyped();
                if (currKey == 's' || currKey == 'S') {
                    return ret;
                }
                ret += currKey;
                DrawFrame.drawInputStringToFrame(ret);
            }
        }
    }

    public String solicitNCharsInput(int n) {
        //DONE: Read n letters of player input
        int i = 0;
        String ret = "";
        while (i < n) {
            if (StdDraw.hasNextKeyTyped()) {
                char currKey = StdDraw.nextKeyTyped();
                i++;
                ret += currKey;
//                drawFrame(ret);
            }
        }
//        StdDraw.pause(500);
        return ret;
    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     * <p>
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     * <p>
     * In other words, running both of these:
     * - interactWithInputString("n123sss:q")
     * - interactWithInputString("lww")
     * <p>
     * should yield the exact same world state as:
     * - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        // DONE: Fill out this method so that it run the engine using the input
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.

        input = input.toLowerCase(Locale.ROOT);
        // Start a new game by processing the first character 'n' (new game)
        if (input.charAt(0) == 'n') {
            int seedEndIndex = input.indexOf('s');
            String inputSeed = input.substring(1, seedEndIndex);
            createNewWorld("", inputSeed, false);
            input = input.substring(seedEndIndex);
        } else if (input.charAt(0) == 'l') {
            resumePrevWorld(false);
            input = input.substring(1);
        }
//            else {
//                throw new IllegalArgumentException("Invalid input string. It should start with 'n or l '.");
//        }

        // Process the remaining characters in the input string
        for (int i = 1; i < input.length(); i++) {
            char command = input.charAt(i);
            checkKeyBoard(String.valueOf(command));
//            if (command == ':') {
//                // Save and quit the game if the next two characters are 'q'
//                if (input.substring(i, i + 2).equals(":q")) {
//                    // engine.saveAndQuit();
//                    break; // Exit the loop as the game is saved and quit
//                } else {
//                    throw new IllegalArgumentException("Invalid input string. ':' should be followed by 'q'.");
//                }
//            } else {
//                // Process the regular commands: 'w', 's', 'a', 'd'
//                engine.interactWithKeyboard();
//            }
        }
//
//        teRender.renderFrame(backWorld,GUI);
//        this.prevMouseX = (int) StdDraw.mouseX();
//        this.prevMouseY = (int) StdDraw.mouseY();
//
//        while(!gameOver) {
//            checkMouse(prevMouseX,prevMouseY);
//            checkKeyBoard("");
//
//        }

        // Get the final world frame after all commands are processed TO the Autograder!!!
        return backWorld;
    }
}
