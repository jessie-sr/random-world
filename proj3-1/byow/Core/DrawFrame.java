package byow.Core;

import edu.princeton.cs.algs4.StdDraw;

import java.awt.*;
import java.io.File;


public class DrawFrame {
    public static int WIDTH = Engine.WIDTH;
    public static int HEIGHT = Engine.HEIGHT;

    private static File savedWorlds = new File("./savedWorld");
    private static File savedWorlds2 = new File("./savedWorld2");



    static void drawInputStringToFrame(String inputSeed) {
        /* Take the input string S and display it at the center of the screen,
         * with the pen settings given below. */
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        Font fontBig = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(fontBig);
        StdDraw.text(WIDTH / 2, HEIGHT * 0.7, "Input seed, Suffix S to start game");
        StdDraw.text(WIDTH / 2, HEIGHT * 0.5, "currently inputted: ");
        StdDraw.text(WIDTH / 2, HEIGHT * 0.4, inputSeed);
        StdDraw.show();
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

    static void drawSavedSubFrame() {
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
    static void drawAppearanceSelection() {
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
        StdDraw.text(WIDTH / 2, HEIGHT * 0.9, "Choose Your Appearance!");

        Font fontSmall = new Font("Monaco", Font.BOLD, 20);
        StdDraw.setFont(fontSmall);
        StdDraw.text(WIDTH / 2, HEIGHT * 0.6 + 2, "Default Avatar: Press 0 to choose");
        StdDraw.text(WIDTH / 2, HEIGHT * 0.6 - 2, "Mountain Avatar: Press 1 to choose");
        StdDraw.text(WIDTH / 2, HEIGHT * 0.6 - 6, "Water Avatar: Press 2 to choose");
        StdDraw.text(WIDTH / 2, HEIGHT * 0.1, "Press Q to return to previous frame");
        StdDraw.show();

    }
    public static void drawFrame() {
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
//        StdDraw.text(WIDTH / 2, HEIGHT /3, "First, choose your appearance using digits 0,1,2");
        StdDraw.text(WIDTH / 2, HEIGHT /2 - 4, "choose appearance (S)");
//        StdDraw.text(WIDTH / 2, HEIGHT /3 - 4, "0:default avatar;  1:mountain avatar;  2:water avatar");
        StdDraw.show();
    }

}
