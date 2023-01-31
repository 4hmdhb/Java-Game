package byow.Core;

import byow.InputDemo.InputSource;
import byow.InputDemo.KeyboardInputSource;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Out;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.introcs.StdAudio;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;

public class Engine {
    TERenderer ter;
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;
    private boolean GAMEOVER = true;
    private long seed;
    private Point door;


    public Engine(){
        ter = new TERenderer();
        ter.initialize(WIDTH, 35);
    }

    private long parseString(String str){
        String string = str.toUpperCase();
        String integers = "";
        for (char c: string.toCharArray()){
            if (c == 'N' || c == 'L'){
                continue;
            }
            if (c == 'S' || c == 'W' || c == 'A' || c == 'D') {
                break;
            }
            integers += Character.toString(c);
        }
        return Long.parseLong(integers);
    }

    private String getMovCommand(String str, Integer start) {
        String movement = "";
        for (int i = start; i < str.length(); i+=1) {
            if (str.charAt(i) == ':') {
                break;
            }
            movement += str.charAt(i);
        }
        return movement.toUpperCase();
    }

    private void moveAvatar(String move, Avatar a, Rooms b) {
        for (int i = 0; i < move.length(); i += 1) {
            char c = move.charAt(i);
            if (c == 'D') {
                a.moveRight();
            }
            if (c == 'S') {
                a.moveDown();
            }
            if (c == 'A') {
                a.moveLeft();
            }
            if (c == 'W') {
                a.moveUp();
            }
            ter.renderFrame(b.world);
        }
    }

    private String getKey(String str) {
        String string = str.toUpperCase();
        String command = "";
        char c = string.charAt(0);
        if (c == 'L') {
            command += c;
        }
        if (c == 'N') {
            command += c;
        }
        return command;
    }


    public void processTheRoom(Rooms theRoom, long seed){

        Random r = new Random(seed);
        BSPTree tree = new BSPTree(WIDTH, HEIGHT);
        tree.createTree(r, r.nextInt(5, 15));
        ArrayList<BSPTree.Node> partitions = tree.getRoomNodes();

        theRoom.addPointsinPartition(partitions, seed);
        theRoom.fillFringe();
        theRoom.putPoints();
        theRoom.Prims();
        for (Rooms.Edge e : theRoom.edges) {
            Point a = e.a;
            Point bb = e.b;
            theRoom.drawRealHallway(a, bb);
        }
        theRoom.putRoomsInPartitions(partitions, seed);
        theRoom.mergeWalls();

        theRoom.openUp();
        theRoom.openUp();
        theRoom.putDoor(seed);
        this.door = theRoom.door;
    }

    public void interactWithInputString(String input) {
        // TODO: Fill out this method so that it run the engine using the input
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.

        long seed = parseString(input);
        this.seed = seed;
        String s = Long.toString(seed);


        Rooms theRoom = new Rooms(WIDTH, HEIGHT);
        processTheRoom(theRoom, seed);
        String command = getKey(s);
        Avatar avatar = new Avatar("avatar", theRoom);
        avatar.putPoint(seed);
        String movement = getMovCommand(input, s.length() + 1);
        moveAvatar(movement, avatar, theRoom);

        Avatar enemy = new Avatar("enemy", theRoom);
        enemy.putPoint(seed - 3);

        processInput(theRoom, avatar, enemy);

    }

    private void saveWorld(TETile[][] world) {
        String filename = "./byow/saves/world_file.txt";
        Out out = new Out(filename);

        String filename2 = "./byow/saves/seed.txt";
        Out out2 = new Out(filename2);

        String filename3 = "./byow/saves/door.txt";
        Out out3 = new Out(filename3);

        for (TETile[] x : world) {
            for (TETile y : x) {
                out.print(y.character() + ",");
            }
        }
        out2.print(String.valueOf(this.seed));
        out3.print(String.valueOf(this.door.x) + "," + String.valueOf(this.door.y));
    }

    private static TETile[][] loadWorld(Avatar avatar, Avatar enemy) {
        TERenderer ter = new TERenderer();
        String filename = "./byow/saves/world_file.txt";
        In in =  new In(filename);
        String[] characters = in.readAllLines();
        int count = 0;
        TETile[][] world = new TETile[80][30];
        int width = world.length;
        int height = world[0].length;

        for (String str : characters) {
            characters = str.split(",");

            for (int x = 0; x < width; x += 1) {
                for (int y = 0; y < height; y += 1) {
                    if (characters[count].equals(" ")) {
                        world[x][y] = Tileset.NOTHING;
                        count += 1;
                    }
                    else if (characters[count].equals("#")) {
                        world[x][y] = Tileset.WALL;
                        count += 1;
                    }
                    else if (characters[count].equals("·")) {
                        world[x][y] = Tileset.FLOOR;
                        count += 1;
                    }
                    else if (characters[count].equals("@")) {
                        world[x][y] = Tileset.AVATAR;
                        avatar.putPoint(x, y);
                        count += 1;
                    }
                    else if (characters[count].equals("♠")) {
                        world[x][y] = Tileset.TREE;
                        enemy.putPoint(x, y);
                        count += 1;
                    }
                    else if (characters[count].equals("█")) {
                        world[x][y] = Tileset.LOCKED_DOOR;
                        count += 1;
                    } else {
                        world[x][y] = Tileset.FLOWER;
                        count +=1;
                    }
                }
            }
        }
        return world;
    }

    private Point getDoor() {
        String filename = "./byow/saves/door.txt";
        In in = new In(filename);
        String[] characters = in.readAllLines();
        for (String str : characters) {
            characters = str.split(",");
        }
        int x = Integer.parseInt(characters[0]);
        int y = Integer.parseInt(characters[1]);
        return new Point(x,y);
    }


    public void drawFrame() {
        /* Take the input string S and display it at the center of the screen,
         * with the pen settings given below. */
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);

        StdDraw.text(WIDTH / 2, this.HEIGHT / 2, "CS61B: THE GAME");
        StdDraw.text(this.WIDTH / 2, this.HEIGHT / 4, "New Game (N)");
        StdDraw.text(this.WIDTH / 2, this.HEIGHT / 6, "Load Game (L)");
        StdDraw.text(this.WIDTH / 2, this.HEIGHT / 8, "Quit (Q)");
        StdDraw.text(this.WIDTH/ 2, this.HEIGHT / 2 - 2, "By: Alexandria Gooray and Bayel Asylbekov");
        StdDraw.show();
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
        
        StdDraw.text(this.WIDTH / 2, this.HEIGHT / 2, "Input seed:");
        StdDraw.show();
        //TODO: If the game is not over, display encouragement, and let the user know if they
        // should be typing their answer or watching for the next round.

        System.out.println("Input");
        char c = 'A';
        while (c != 's' && c != 'S'){

            if (StdDraw.hasNextKeyTyped()){
                StdDraw.clear(Color.BLACK);
                StdDraw.text(this.WIDTH / 2, this.HEIGHT / 2, "Input seed:");
                c = StdDraw.nextKeyTyped();
                str += c;
                StdDraw.text(this.WIDTH / 2, this.HEIGHT / 3, str);
                StdDraw.show();
                StdDraw.pause(100);
            }
        }
        return str;
    }

    public void mainMenu(){

        drawFrame();

        PlayMusic p = new PlayMusic();
        p.PlayMusicMenu("byow/Core/Music/music_zapsplat_easy_cheesyWAV.wav");

        char c = solicitCharInput();
        if (c == 'n' || c == 'N'){
            GAMEOVER = false;
            interactWithInputString(drawInput());
        }
        if (c == 'l' || c == 'L'){
            GAMEOVER = false;
            loadWorld();
        }
        if (c == 'q' || c == 'Q'){
            System.exit(0);
        }
    }

    public void mainMenuNoMusic(){

        drawFrame();


        char c = solicitCharInput();
        if (c == 'n' || c == 'N'){
            GAMEOVER = false;
            interactWithInputString(drawInput());
        }
        if (c == 'l' || c == 'L'){
            GAMEOVER = false;
            loadWorld();
        }
        if (c == 'q' || c == 'Q'){
            System.exit(0);
        }
    }


    public void processInput(Rooms b, Avatar avatar, Avatar enemy){
        InputSource inputSource = new KeyboardInputSource();
        Path path = new Path(b);
        PlayMusic p = new PlayMusic();
        path.solve(avatar.getPoint(), enemy.getPoint());
        path.printPath();

        int counter = 0;
        int printPath = 0;


        while (inputSource.possibleNextInput()) {

            if (StdDraw.hasNextKeyTyped()) {
                path.unPrintPath();
                char c = inputSource.getNextKey();
                if (c == 'D') {
                    avatar.moveRight();
                }
                if (c == 'S') {
                    avatar.moveDown();
                }
                if (c == 'A') {
                    avatar.moveLeft();
                }
                if (c == 'W') {
                    avatar.moveUp();
                }
                if (c == 'N') {
                    printPath += 1;
                }
                if (c == 'Q') {
                    saveWorld(b.world);
                    System.exit(0);
                }
                if (c == 'L') {
                    b.world = loadWorld(avatar, enemy);

                }
            }
            if (counter == 100) {
                enemy.goToPoint(path.findPoint2(new Point(enemy.x, enemy.y)));
                counter = 0;
            }
            counter = counter + 1;
            if (avatar.equals(enemy.getPoint())){
                StdDraw.clear(new Color(0, 0, 0));
                StdDraw.text(WIDTH/2, HEIGHT/2, "Game over: You Lost");
                StdDraw.show();
                p.PlayMusicSE("byow/Core/Music/game-over(lost).wav");
                StdDraw.pause(3000);
                mainMenuNoMusic();
            }
            if (avatar.equals(b.door)){
                StdDraw.clear(new Color(0, 0, 0));
                StdDraw.text(WIDTH/2, HEIGHT/2, "You won!");
                StdDraw.show();
                p.PlayMusicSE("byow/Core/Music/game over (won).wav");
                StdDraw.pause(3000);
                mainMenuNoMusic();
            }
            path.solve(avatar.getPoint(), enemy.getPoint());
            if (printPath % 2 == 0) {
                path.printPath();
            }
            ter.renderFrame(b.world);
            StdDraw.setPenColor(Color.WHITE);
            StdDraw.textLeft(1, 33, "Seed: " + seed);
            StdDraw.show();
            mouse(b);
        }

    }

    public void loadWorld() {
        Rooms b = new Rooms(WIDTH, HEIGHT);
        Avatar avatar = new Avatar("avatar", b);
        Avatar enemy = new Avatar("enemy", b);
        b.world = loadWorld(avatar, enemy);
        b.door = getDoor();
        this.door = b.door;
        String filename = "./byow/saves/seed.txt";
        In in =  new In(filename);
        String[] seed = in.readAllLines();
        this.seed = parseString(seed[0]);
        processInput(b, avatar, enemy);
    }

    private void mouse(Rooms b) {
        if ((int) StdDraw.mouseX() < 80 && (int) StdDraw.mouseY() < 30) {
            int x = (int) StdDraw.mouseX();
            int y = (int) StdDraw.mouseY();
            if (b.world[x][y].equals(Tileset.LOCKED_DOOR)) {
                ter.renderFrame(b.world);
                StdDraw.enableDoubleBuffering();
                StdDraw.setPenColor(Color.WHITE);
                StdDraw.text(WIDTH/20, 33, "Seed: " + seed);
                StdDraw.text(WIDTH / 2, 33, "This is a Locked Door. Get here to win!");
                StdDraw.show();
                StdDraw.pause(300);
            }
            if (b.world[x][y].equals(Tileset.WALL)) {
                ter.renderFrame(b.world);
                StdDraw.enableDoubleBuffering();
                StdDraw.setPenColor(Color.WHITE);
                StdDraw.textLeft(1, 33, "Seed: " + seed);
                StdDraw.text(WIDTH / 2, 33, "This is a Wall ... You can't go through this.");
                StdDraw.show();
                StdDraw.pause(300);
            }
            if (b.world[x][y].equals(Tileset.TREE)) {
                ter.renderFrame(b.world);
                StdDraw.enableDoubleBuffering();
                StdDraw.setPenColor(Color.WHITE);
                StdDraw.textLeft(1, 33, "Seed: " + seed);
                StdDraw.text(WIDTH / 2, 33, "This is an enemy. Don't get caught by it!");
                StdDraw.show();
                StdDraw.pause(300);
            }
            if (b.world[x][y].equals(Tileset.AVATAR)) {
                ter.renderFrame(b.world);
                StdDraw.enableDoubleBuffering();
                StdDraw.setPenColor(Color.WHITE);
                StdDraw.textLeft(1, 33, "Seed: " + seed);
                StdDraw.text(WIDTH / 2, 33, "This is you :)");
                StdDraw.show();
                StdDraw.pause(300);
            }
            if (b.world[x][y].equals(Tileset.MOUNTAIN)) {
                ter.renderFrame(b.world);
                StdDraw.enableDoubleBuffering();
                StdDraw.setPenColor(Color.WHITE);
                StdDraw.textLeft(1, 33, "Seed: " + seed);
                StdDraw.text(WIDTH / 2, 33, "This is the enemy path to you ... Don't let them catch you!");
                StdDraw.show();
                StdDraw.pause(100);
            }
        }
    }
}
