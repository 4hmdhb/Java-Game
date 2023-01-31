//package byow.Core;
//import byow.InputDemo.InputSource;
//import byow.InputDemo.KeyboardInputSource;
//import byow.TileEngine.TERenderer;
//import org.junit.Test;
//
//import java.util.ArrayList;
//import java.util.Random;
//
//
//public class TestAvatar {
//    @Test
//    public void putAvatar(){
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
//        Avatar a = new Avatar("avatar", b);
//        a.putPoint(10);
//        b.draw();
//        InputSource inputSource = new KeyboardInputSource();
//
//        while (inputSource.possibleNextInput()) {
//            char c = inputSource.getNextKey();
//            if (c == 'D') {
//                a.moveRight();
//            }
//            if (c == 'S') {
//                a.moveDown();
//            }
//            if (c == 'A') {
//                a.moveLeft();
//            }
//            if (c == 'W') {
//                a.moveUp();
//            }
//            b.draw();
//        }
//    }
//}
