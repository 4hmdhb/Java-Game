package byow.Core;

import java.util.ArrayList;
import java.util.Random;

public class BSPTree {
    private final Node root;
    private ArrayList<Node> NodeList;

    public BSPTree(int width, int height) {
        this.root = new Node(null, 0, 0, width, height);
    }

    public ArrayList<Node> getRoomNodes() {
        ArrayList<Node> nodes = new ArrayList<>();
        Node curr = root;
        getRoomsHelper(curr, nodes);
        return nodes;
    }

    private void getRoomsHelper(Node node, ArrayList<Node> nodeList) {
        if (node.isLeaf(node)) {
            if (node.width != 0 && node.height != 0) {
                nodeList.add(node);
            }
        } else {
            getNodesHelper(node.childA, nodeList);
            getNodesHelper(node.childB, nodeList);
        }
    }

    public ArrayList<Node> getNodes() {
        ArrayList<Node> nodes = new ArrayList<>();
        Node curr = root;
        getNodesHelper(curr, nodes);
        return nodes;
    }

    private void getNodesHelper(Node node, ArrayList<Node> nodeList) {
        nodeList.add(node);
        if (!node.isLeaf(node)) {
            getNodesHelper(node.childA, nodeList);
            getNodesHelper(node.childB, nodeList);
        }
    }

    public void createTree(Random random, int splitAmount) {
        ArrayList<Node> nodes;

        for (int i = 0; i < splitAmount; i += 1) {
            nodes = getNodes();
            for (Node node : nodes) {
                if (!node.isLeaf(node)) {
                    continue;
                }

                int splitPoint = 0;
                int splitType = getRandomDirection(random);
                int widthIndex = node.getWidth() - 5;
                int heightIndex = node.getHeight() - 5;

                if (splitType == 0) { // Vertical
                    if (widthIndex > 10) {
                        splitPoint = random.nextInt(5, node.getWidth() - 5);
                    }
                } else if (splitType == 1) { // Horizontal
                    if (heightIndex > 10) {
                        splitPoint = random.nextInt(5, node.getHeight() - 5);
                    }
                } else {
                    continue;
                }
                node.partition(splitType, splitPoint);

            }
        }
    }

    public int getRandomDirection(Random random) {
        return random.nextInt(2);
    }

    // Node Class that keeps track of partitions
    public class Node {
        private final Node parent;
        int x;
        int y;
        int width;
        int height;
        int xSplit;
        int ySplit;
        Node childA;
        Node childB;
        private int direction;

        public Node(Node parent, int x, int y, int width, int height) {
            this.parent = parent;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        public Point getPoint(Node node) {
            return new Point(x, y);
        }

        public Point getEndPoint(Node node) {
            return new Point(x + width, y + height);
        }

        public boolean isLeaf(Node node) {
            return node.childA == null && node.childB == null;
        }

        public void partition(int direction, int splitPoint) {
            this.direction = direction;
            if (direction == 0) { //vertical split
                xSplit = splitPoint;
                childA = new Node(this, x, y, splitPoint, height);
                childB = new Node(this, x + splitPoint, y, width - splitPoint, height);
            } else if (direction == 1) { //horizontal split
                ySplit = splitPoint;
                childA = new Node(this, x, y, width, splitPoint);
                childB = new Node(this, x, y + splitPoint, width, height - splitPoint);
            }
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }
    }
}
