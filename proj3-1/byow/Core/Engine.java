package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

public class Engine {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
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
            ter.initialize(WIDTH, HEIGHT);

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
            ter.renderFrame(finalWorldFrame);

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
