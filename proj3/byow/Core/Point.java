package byow.Core;

public class Point{
    int x;
    int y;
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public void print(){
        System.out.print("x: ");
        System.out.print(this.x);
        System.out.print(", ");
        System.out.print("y: ");
        System.out.println(this.y);
    }
}