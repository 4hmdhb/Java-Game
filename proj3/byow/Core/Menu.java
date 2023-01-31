package byow.Core;

import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.util.Random;

public class Menu {
    private int width;
    /** The height of the window of this game. */
    private int height;


    public Menu(int width, int height) {
        /* Sets up StdDraw so that it has a width by height grid of 16 by 16 squares as its canvas
         * Also sets up the scale so the top left is (0,0) and the bottom right is (width, height)
         */
        this.width = width;
        this.height = height;
        StdDraw.setCanvasSize(this.width * 16, this.height * 16);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, this.width);
        StdDraw.setYscale(0, this.height);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();

    }

    public char solicitCharInput() {
        //TODO: Read n letters of player input
        char c;


        while (true){

            if (StdDraw.hasNextKeyTyped()){
                c = StdDraw.nextKeyTyped();
                return c;
            }
        }
    }

    public String drawInput() {
        /* Take the input string S and display it at the center of the screen,
         * with the pen settings given below. */
        String str = "";
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        Font fontBig = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(fontBig);
        StdDraw.text(this.width / 2, this.height / 2, "Input seed:");
        StdDraw.show();
        //TODO: If the game is not over, display encouragement, and let the user know if they
        // should be typing their answer or watching for the next round.

        System.out.println("Iput");
        char c = 'A';
        while (c != 's' && c != 'S'){
            System.out.println(c);
            if (StdDraw.hasNextKeyTyped()){
                StdDraw.clear(Color.BLACK);
                StdDraw.text(this.width / 2, this.height / 2, "Input seed:");
                c = StdDraw.nextKeyTyped();
                str += c;
                StdDraw.text(this.width / 2, this.height / 3, str);
                StdDraw.show();
                StdDraw.pause(100);
            }
        }
        return str;
    }

    public void drawFrame() {
        /* Take the input string S and display it at the center of the screen,
         * with the pen settings given below. */
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        Font fontBig = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(fontBig);
        StdDraw.text(this.width / 2, this.height / 2, "CS61B: THE GAME");

        StdDraw.text(this.width / 2, this.height / 4, "New Game (N)");
        StdDraw.text(this.width / 2, this.height / 6, "Load Game (L)");
        StdDraw.text(this.width / 2, this.height / 8, "Quit (Q)");
        StdDraw.show();
    }

}
