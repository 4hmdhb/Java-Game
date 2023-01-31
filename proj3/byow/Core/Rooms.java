package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.util.*;

import static java.lang.Math.abs;
import static java.lang.Math.sqrt;

public class Rooms {
    ArrayList<Point> points;
    ArrayList<Edge> edges;
    TETile[][] world;
    private int width;
    public Point door;

    private int height;

    ArrayList<Fridge>  fringe;


    public Rooms(int width, int height){
        this.width = width;
        this.height = height;
        world = new TETile[this.width][this.height];
        for (int x = 0; x < this.width; x += 1) {
            for (int y = 0; y < this.height; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }


        points = new ArrayList<>();
        edges = new ArrayList<>();
        fringe = new ArrayList<>();
//        ter = new TERenderer();
//        ter.initialize(width, height);
    }

    class Edge {
        Point a;
        Point b;
        public Edge(Point a, Point b){
            this.a = a;
            this.b = b;
        }
    }


    public void drawSolidCube(Point start, Point end){
        if (end.x < start.x) {
            Point tmp = start;
            start = end;
            end = tmp;
        }
        int step = 1;

        int width = end.x - start.x;
        int height = end.y - start.y;
        if (height < 0){
            step = -1;
        }
        for (int i = 0; i < abs(height); i++){
            drawHorizontalLine(new Point(start.x, start.y + (step* i)), width, 1, Tileset.FLOOR);
        }

    }


    public Point drawWalls(Point start, Point end) {
        if (end.x < start.x) {
            Point tmp = start;
            start = end;
            end = tmp;
        }
        int width = end.x - start.x;
        int height = end.y - start.y;

        int step = 1;
        if (height < 0){
            step = -1;
            height = -height;
        }
        Point cur;
        cur = drawHorizontalLine(new Point(start.x, start.y), width, 1, Tileset.WALL);
        cur = drawVerticalLine(cur, height, step, Tileset.WALL);

        cur = drawHorizontalLine(cur, width, -1,Tileset.WALL);
        return drawVerticalLine(cur, height, -step, Tileset.WALL);
    }

    public void room(Point start, int width, int height) {
        start = new Point(start.x - width/2, start.y - height/2);
        Point end = new Point(start.x + width, start.y + height);
        drawSolidCube(start, end);
        drawWalls(start, end);

    }

    public void putRoomsInPartitions(ArrayList<BSPTree.Node> lst, long seed) {
        ArrayList<BSPTree.Node> list = new ArrayList<>();
        Random r = new Random(seed);
        for (int i = 0; i < lst.size(); i += 2) {
            BSPTree.Node n = lst.get(i);
            if (n.isLeaf(n)) {
                if (n.width != 0 && n.height != 0) {
                    list.add(n);
                }
            }
        }
        for (int i = 0; i < points.size(); i+=1 ) {
            Point point = points.get(i);
            BSPTree.Node node = list.get(i);
            room(point, r.nextInt(node.width / 2, node.width - 1), r.nextInt(node.height/2, node.height - 1));
        }
    }

    public void fillFringe(){
        for (Point p: points){
            fringe.add(new Fridge(p, Integer.MAX_VALUE));
        }
    }

    public void Prims() {
        Point pnt = points.get(0);
        putIntoFringe(pnt, 0);
        fringe.sort(new DistComparator());
        pnt = fringe.remove(0).a;
        for (int i = 0; i < points.size() - 1; i++) {
            pnt = getAllDistances(pnt);
        }
    }


    public boolean hasNeibors(int x, int y){
        int left_x = x - 1;
        int right_x = x + 1;
        int up_y = y + 1;
        int down_y = y - 1;

        if (left_x >= 0 && right_x < width){
            if (world[left_x][y] == Tileset.FLOOR && world[right_x][y] == Tileset.FLOOR){
                return true;
            }
        }
        if (down_y >= 0 && up_y < height){
            if (world[x][up_y] == Tileset.FLOOR && world[x][down_y] == Tileset.FLOOR){
                return true;
            }
        }
        return false;
    }

    public void openUp(){
        for (int y = 0; y < height; y++){
            for (int x = 0; x < width; x++){
                if (hasNeibors(x, y)){
                    world[x][y] = Tileset.FLOOR;
                }
            }
        }
    }

    class Fridge{
        public Point a;
        public int distance;

        public Fridge(Point a, int distance){
            this.a = a;
            this.distance = distance;
        }
    }

    public boolean hasEdge(Point p){
        for (Edge e: edges){
            if (e.b == p){
                return true;
            }
        }
        return false;
    }

    public void putDoor(long seed){
        Random r = new Random(seed);
        int x = r.nextInt(1, width - 1);
        int y = r.nextInt(0, height);
        for (int i = 0; i < width * height; i++) {
            x = r.nextInt(1, width - 1);
            y = r.nextInt(0, height);
            if (world[x][y] == Tileset.WALL && (world[x + 1][y] == Tileset.WALL && world[x - 1][y] == Tileset.WALL)){
                break;
            }
        }
        world[x][y] = Tileset.LOCKED_DOOR;
        door = new Point(x, y);
    }

    public void mergeWalls(){
        for (int y = 0; y < height; y++){
            for (int x = 0; x < width; x++){
                if (right(x, y) || left(x,y) || up(x,y) || down(x,y)){
                    world[x][y] = Tileset.FLOOR;
                }
            }
        }
    }

    public boolean left(int x, int y){
        if (world[x][y] == Tileset.WALL){
            if (x + 1 < width && y + 1 < height && y - 1 > 0 && x + 1 < width){
                if (world[x + 1][y - 1] == Tileset.WALL && world[x + 1][y] == Tileset.WALL && world[x + 1][y + 1] == Tileset.WALL  && world[x - 1][y] == Tileset.FLOOR){

                    return true;
                }
            }
        }
        return false;
    }

    public boolean down(int x, int y){
        if (world[x][y] == Tileset.WALL){
            if (x - 1 >= 0 && y + 1 < height && y - 1 >= 0 && x + 1 < width){
                if (world[x][y + 1] == Tileset.WALL && world[x - 1][y + 1] == Tileset.WALL && world[x + 1][y + 1] == Tileset.WALL  && world[x][y - 1] == Tileset.FLOOR){
                    return true;
                }
            }
        }
        return false;
    }

    public boolean up(int x, int y){
        if (world[x][y] == Tileset.WALL){
            if (x - 1 >= 0 && y + 1 < height && y - 1 >= 0 && x + 1 < width){
                if (world[x + 1][y - 1] == Tileset.WALL && world[x - 1][y - 1] == Tileset.WALL && world[x][y - 1] == Tileset.WALL && (world[x][y+1] == Tileset.FLOOR)){
                    return true;
                }
            }
        }
        return false;
    }

    public boolean right(int x, int y){
        if (world[x][y] == Tileset.WALL){
            if (x - 1 >= 0 && y + 1 < height && y - 1 >= 0 && x + 1 < width){
                if (world[x - 1][y - 1] == Tileset.WALL && world[x - 1][y] == Tileset.WALL && world[x - 1][y + 1] == Tileset.WALL && (world[x + 1][y] == Tileset.FLOOR)){
                    return true;
                }
            }
        }
        return false;
    }

    public void putIntoFringe(Point a, int distance){
        boolean contains = false;
        for (Fridge f: fringe){
            if (f.a == a) {
                if (f.distance > distance){
                    f.distance = distance;
                    break;
                }
            }
        }
    }


    private static class DistComparator implements Comparator<Fridge> {
        public int compare(Fridge a, Fridge b) {
            return a.distance - b.distance;
        }
    }

    public Point getAllDistances(Point a) {
        int distance = 0;
        for (Point p: points) {
            if (p == a) {
                continue;
            }
            if (hasEdge(p)){
                continue;
            }
            distance = distance(a, p);
            putIntoFringe(p, distance);
        }
        fringe.sort(new DistComparator());
        Point tmp = fringe.remove(0).a;
        edges.add(new Edge(a, tmp));
        return tmp;
    }

    public void drawRealHallway(Point start, Point end) {

        int height = abs(start.y - end.y);
        int width = abs(start.x - end.x);
        if (height == 0 && width == 0) {
            return;
        }
        if (start.x > end.x){
            Point tmp = start;
            start = end;
            end = tmp;
        }
        drawLine(start, end, Tileset.FLOOR);
        if (height == 0){
            drawLine(new Point(start.x, start.y - 1), new Point(end.x, end.y - 1), Tileset.WALL);
            drawLine(new Point(start.x, start.y + 1), new Point(end.x, end.y + 1), Tileset.WALL);
        }
        else if (width == 0) {
            drawLine(new Point(start.x + 1, start.y), new Point(end.x + 1, end.y), Tileset.WALL);
            drawLine(new Point(start.x - 1, start.y), new Point(end.x - 1, end.y), Tileset.WALL);
        }
        else if (start.y > end.y){
            drawLine(new Point(start.x, start.y - 1), new Point(end.x - 1, end.y), Tileset.WALL);
            drawLine(new Point(start.x, start.y + 1), new Point(end.x + 1, end.y), Tileset.WALL);
        }
        else{
            drawLine(new Point(start.x, start.y - 1), new Point(end.x + 1, end.y), Tileset.WALL);
            drawLine(new Point(start.x, start.y + 1), new Point(end.x - 1, end.y), Tileset.WALL);
        }

    }

    public void drawLine(Point start, Point end, TETile tileType ) {
        int width = abs(start.x - end.x);
        int height = abs(start.y - end.y) + 1;
        int width_dir = 1;
        int height_dir = 1;
        Point cur;
        if (start.y > end.y) {
            height_dir = -1;
        }
        if (start.x > end.x) {
            width_dir = -1;
        }
        cur = drawHorizontalLine(start, width, width_dir, tileType);
        drawVerticalLine(cur, height, height_dir, tileType);

    }

    public Point drawHorizontalLine(Point startPoint, int length, int step, TETile tyleType) {
        for (int x = 0; x < length; x += 1) {
            if (outOfBounds(startPoint.x + (step * x), startPoint.y)) {
                continue;
            }
            world[startPoint.x + (step * x)][startPoint.y] = tyleType;
        }
        return new Point(startPoint.x + (step * length), startPoint.y);
    }

    public Point drawVerticalLine(Point startPoint, int length, int step, TETile tyleType) {
        for (int y = 0; y < length; y += 1) {
            if (outOfBounds(startPoint.x, startPoint.y + (step * y))) {
                continue;
            }
            world[startPoint.x][startPoint.y + (step * y)] = tyleType;
        }
        return new Point(startPoint.x, startPoint.y + (step *length));

    }

    public boolean outOfBounds(int x, int y) {
        if (x < 0 || y < 0) {
            return true;
        }
        if (x >= width || y >= height) {
            return true;
        }
        return false;
    }

    public void addPointsinPartition(ArrayList<BSPTree.Node> lst, long seed) {
        Random r = new Random(seed);
        int x;
        int y;
        for (int i = 0; i < lst.size(); i += 2) {
            BSPTree.Node n = lst.get(i);
            if (n.isLeaf(n)) {
                if (n.width != 0 && n.height != 0) {
                    x = n.x + n.width/2;
                    y = n.y + n.height/2;
                    points.add(new Point(x, y));
                }
            }
        }
    }


    public int distance(Point a, Point b){
        int dist_x = a.x - b.x;
        int dist_y = a.y - b.y;
        return (int) sqrt((dist_x * dist_x) + (dist_y * dist_y));
    }

    public int getWidth(){
        return width;
    }
    public int getHeight(){
        return height;
    }

    public void putPoints() {
        for (Point p: points) {
            world[p.x][p.y] = Tileset.MOUNTAIN;
        }
    }
//    public void draw() {
//        ter.renderFrame(world);
//    }

    public TETile[][] returnWorld(){
        return this.world;
    }

}
