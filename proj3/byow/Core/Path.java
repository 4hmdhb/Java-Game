package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.*;

public class Path {
    Point start;
    Point end;
    int width;
    int height;
    Rooms r;
    ArrayList<Edge> edges;

    Boolean[][] visited;


    public Path(Rooms r){
        this.width = r.getWidth();
        this.height = r.getHeight();
        this.r = r;
        visited = new Boolean[width][height];
        edges = new ArrayList<>();
    }

    public void falsify(){
        for (int y = 0; y < height; y++){
            for (int x = 0; x < width; x++){
                visited[x][y] = false;
            }
        }
    }

    public boolean canGo(Point a){
        int x = a.x;
        int y = a.y;


        if (x < 0 || x >= width){
            return false;
        }
        if (y < 0 || y >= height){
            return false;
        }

        if (visited[x][y]) {
            return false;
        }

        visited[x][y] = true;
        if ( r.world[x][y] == Tileset.WALL){
            return false;
        }
        return true;
    }

    public boolean equal(Point a, Point b){
        if (a.x == b.x && a.y == b.y){
            return true;
        }
        return false;
    }

    public boolean breadthFirst(Point a, Point b){

        solve(a,b);
        return true;
    }

    class Edge {
        Point a;
        Point b;
        public Edge(Point a, Point b){
            this.a = a;
            this.b = b;
        }
    }

    public void solve(Point a, Point b){
        LinkedList<Point> queee =new LinkedList<>();
        queee.add(a);
        edges = new ArrayList<>();
        this.start = a;
        this.end = b;
        falsify();

        Point left;
        Point right;
        Point up;
        Point down;
        Point tmp;
        while(!queee.isEmpty()){
            tmp = queee.remove();
            visited[tmp.x][tmp.y] = true;
            left = new Point(tmp.x - 1, tmp.y);
            right = new Point(tmp.x + 1, tmp.y);
            up = new Point(tmp.x, tmp.y + 1);
            down = new Point(tmp.x, tmp.y - 1);

            if (canGo(left)){
                queee.add(left);
                edges.add(new Edge(tmp, left));
            }

            if (canGo(right)){
                queee.add(right);
                edges.add(new Edge(tmp, right));
            }

            if (canGo(up)){
                queee.add(up);
                edges.add(new Edge(tmp, up));
            }

            if (canGo(down)){
                queee.add(down);
                edges.add(new Edge(tmp, down));
            }

            if (equal(left, b) || equal(right, b) || equal(up, b) || equal(down, b)){
                break;
            }
        }
    }


    public void printEdges(){

        for (Edge e: edges){
            r.world[e.a.x][e.a.y] = Tileset.MOUNTAIN;
            r.world[e.b.x][e.b.y] = Tileset.MOUNTAIN;
        }
    }

    public Edge findPoint(Point dest){
        for (Edge e: edges){
            if (equal(e.b, dest)){
                return e;
            }
        }
        return null;
    }

    public Point findPoint2(Point dest){
        for (Edge e: edges){
            if (equal(e.b, dest)){
                return e.a;
            }
        }
        return null;
    }

    /** i = 0 to hide, else show */
    public void printPath(){
        Edge path = findPoint(end);
        while (path!= null && !equal(path.a, start)){
            r.world[path.a.x][path.a.y] = Tileset.MOUNTAIN;
            path = findPoint(path.a);
        }
    }

    public void unPrintPath(){
        Edge path = findPoint(end);
        while (path!= null && !equal(path.a, start)){
            r.world[path.a.x][path.a.y] = Tileset.FLOOR;
            path = findPoint(path.a);
        }
    }

}
