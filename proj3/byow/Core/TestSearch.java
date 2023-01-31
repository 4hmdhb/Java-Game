//package byow.Core;
//
//import byow.TileEngine.TERenderer;
//import byow.TileEngine.TETile;
//import byow.TileEngine.Tileset;
//import org.junit.Test;
//
//import java.util.ArrayList;
//import java.util.Random;
//
//public class TestSearch {
//
//    @Test
//    public void TestRoomsWithPartitions() {
//        Random r = new Random(12);
//        BSPTree tree = new BSPTree(60, 30);
//        tree.createTree(r, 10);
//        ArrayList<BSPTree.Node> partitions = tree.getRoomNodes();
//
//        Rooms b = new Rooms(60, 30 );
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
////        b.putDoor(300);
//        b.draw();
//
//
//        Path p = new Path(b);
//
//        p.solve(new Point(5,7), new Point(30, 5));
//        p.printPath();
//        p.unPrintPath();
////        p.solve(new Point(5,7), new Point(20, 5));
//        p.solve(new Point(5,7), new Point(13, 25));
//        p.printPath();
//        b.world[5][7] = Tileset.GRASS;
//        b.world[13][25] = Tileset.GRASS;
//        b.draw();
//        while (true){
//
//        }
//        }
//}
