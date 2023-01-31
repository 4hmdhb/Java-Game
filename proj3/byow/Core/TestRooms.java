//package byow.Core;
//
//import byow.TileEngine.TERenderer;
//import edu.princeton.cs.introcs.StdDraw;
//import org.junit.Test;
//
//import java.util.ArrayList;
//import java.util.Random;
//
//public class TestRooms {
////    @Test
////    public void TestRandomPoints() {
////        Rooms r = new Rooms();
////
////        r.pickRandomPoints(10, 5);
////        r.putPoints();
////        r.draw();
////        while(true){
////
////        }
////    }
////
////
////
////    @Test
////    public void TestPrims() {
////        Rooms b = new Rooms();
////        b.pickRandomPoints(5, 5);
////        b.Prims();
////        Point a = b.edges.get(0).a;
////        Point bb = b.edges.get(0).b;
////        System.out.print(a.x);
////        System.out.print(a.y);
////        System.out.print(bb.x);
////        System.out.print(bb.y);
////    }
////
////    @Test
////    public void Maze() {
////        Rooms b = new Rooms();
////        b.pickRandomPoints(5, 5);
////        b.putPoints();
////        b.draw();
////        b.Prims();
////        b.connectAllEdges();
////        while(true){
////
////        }
////    }
//
////    @Test
////    public void TestRooms() {
////        Rooms b = new Rooms();
////        b.pickRandomPoints(5, 5);
////        b.fillFringe();
////
////
////        b.Prims();
////        for (Rooms.Edge e: b.edges){
////            Point a = e.a;
////            Point bb = e.b;
////            b.drawRealHallway(a, bb);
////
////            a.print();
////            bb.print();
////            System.out.println();
////        }
////
//////        b.drawRealHallway(a, bb);
//////        b.world[41][14] = Tileset.WALL;
//////        b.world[41][14] = Tileset.WALL;
////
////        b.putRooms();
////        b.putPoints();
////        b.openUp();
////        b.draw();
////
////        while (true){
////
////        }
////    }
//
//    @Test
//    public void TestRoomsWithPartitions() {
//        Random r = new Random(12);
//        BSPTree tree = new BSPTree(60, 30);
//        tree.createTree(r, 10);
//        ArrayList<BSPTree.Node> partitions = tree.getRoomNodes();
//
//        Rooms b = new Rooms(60, 30);
//        b.addPointsinPartition(partitions, 60);
//
//        b.fillFringe();
//        b.putPoints();
//
//        b.Prims();
//        for (Rooms.Edge e : b.edges) {
//            Point a = e.a;
//            Point bb = e.b;
//            b.drawRealHallway(a, bb);
//
//            a.print();
//            bb.print();
//            System.out.println();
//        }
////        b.drawRealHallway(a, bb);
////        b.world[41][14] = Tileset.WALL;
////        b.world[41][14] = Tileset.WALL;
//
//        b.putRoomsInPartitions(partitions, 10);
////        b.putPoints();
//        b.mergeWalls();
//
//        b.openUp();
//        b.putDoor(300);
//        b.draw();
//
//
//        b.putDoor(10);
//        b.draw();
//        while (true){
//
//        }
//    }
//}
