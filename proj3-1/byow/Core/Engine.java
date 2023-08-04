package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import byow.byowTools.RandomUtils;
import edu.princeton.cs.algs4.StdDraw;

import java.awt.Color;
import java.awt.Font;
import java.util.Random;

public class Engine {
    TERenderer teRender = new TERenderer();
    /* Feel free to change the WIDTH and HEIGHT. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;
    private boolean gameOver = false;

    private long seed;
    private RoomGenerator currGenerator;
    private TETile[][] finalWorldFrame = null;

    private static final String[] ENCOURAGEMENT = {"You can do this!", "I believe in you!",
            "You got this!", "You're a star!", "Go Bears!",
            "Too easy for you!", "Wow, so impressive!"};

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
        // display init UI first
        //process user's init input
        keyboardInit();
        int prevMouseX = (int) StdDraw.mouseX();
        int prevMouseY = (int) StdDraw.mouseY();
        while(!gameOver) {
            int currMouseX = (int) StdDraw.mouseX();
            int currMouseY = (int) StdDraw.mouseY();
            if(currMouseY == 0) {
                continue;
            }
            if(prevMouseX != currMouseX || prevMouseY != currMouseY) {
                int mousePointer = currGenerator.board[currMouseX][currMouseY-1];
                prevMouseX = currMouseX;
                prevMouseY = currMouseY;
                System.out.println("IF TRUE");
                TETile GUI = new TETile('#',"You are at  " + mousePointer,
                        new Color(216, 128, 128), Color.darkGray,"gui");
                teRender.renderFrame(currGenerator.world,GUI);

                
            }
            StdDraw.pause(500);
        }



    }

    private void keyboardInit() {
        // display init UI first
        drawFrame();
        //process user's init input
        String input = solicitNCharsInput(1);
        if(input.equals("q") || input.equals("Q")) {
            return;
        }
        if(input.equals("n") || input.equals("N")) {
            this.seed =  RandomUtils.uniform(new Random(),Integer.MAX_VALUE);

            // initialize the tile rendering engine with a window of size WIDTH x HEIGHT
            teRender.initialize(WIDTH, HEIGHT,0,1);
            // initialize tiles
            finalWorldFrame = new TETile[WIDTH][HEIGHT];
            for (int x = 0; x < WIDTH; x += 1) {
                for (int y = 0; y < HEIGHT; y += 1) {
                    finalWorldFrame[x][y] = Tileset.NOTHING;
                }
            }
            currGenerator = new RoomGenerator(finalWorldFrame, seed);
            currGenerator.drawRooms();
            // draws the world to the screen.
            teRender.renderFrame(finalWorldFrame);
        }
        if(input.equals("l") || input.equals("L")) {
            // load files
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

        TETile[][] finalWorldFrame = null;
        Engine engine = new Engine();

        // Start a new game by processing the first character 'n' (new game)
        if (input.charAt(0) == 'n') {
            int seedEndIndex = input.indexOf('s');
            long seed = Long.parseLong(input.substring(1, seedEndIndex));

            // initialize the tile rendering engine with a window of size WIDTH x HEIGHT
            teRender.initialize(WIDTH, HEIGHT);

            // initialize tiles
            finalWorldFrame = new TETile[WIDTH][HEIGHT];
            for (int x = 0; x < WIDTH; x += 1) {
                for (int y = 0; y < HEIGHT; y += 1) {
                    finalWorldFrame[x][y] = Tileset.NOTHING;
                }
            }

            RoomGenerator newGenerator = new RoomGenerator(finalWorldFrame, seed);
            newGenerator.drawRooms();

            // draws the world to the screen.
            teRender.renderFrame(finalWorldFrame);

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
        return finalWorldFrame;
    }
}
