package org.grapher.logic;

import org.grapher.logic.Node.Position;

import java.util.ArrayList;
import java.util.Random;

import static org.grapher.logic.Edge.Direction;

public class Graph {
    private final ArrayList<Path> paths = new ArrayList<>();
    private final Dijkstra dijkstra = new Dijkstra();
    private final BFS bfs = new BFS();
    private final FileHandler fileHandler = new FileHandler();
    private int rows;
    private int columns;
    private Node[] adjacencyList;

    public Graph() {
    }

    public Graph(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getColumns() {
        return columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public Node[] getAdjacencyList() {
        return adjacencyList;
    }

    public void setAdjacencyList(Node[] adjacencyList) {
        this.adjacencyList = adjacencyList;
    }

    public ArrayList<Path> getPaths() {
        return paths;
    }

    public int getNodesCount() {
        return rows * columns;
    }

    public void generateWages(double minWage, double maxWage) {
        generate(minWage, maxWage, false);
    }

    public void generateConsistent(double minWage, double maxWage) {
        do {
            generate(minWage, maxWage, true);
        } while (!checkConsistency());
    }

    public void generateRandom(double minWage, double maxWage) {
        generate(minWage, maxWage, true);
    }

    public boolean checkConsistency() {
        return bfs.checkConsistency(this);
    }

    public void findPaths() {
        if (checkConsistency()) {
            for (Path path : paths) {
                if (path.getNodesSequence().size() == 0 && path.isValid()) {
                    dijkstra.findPath(this, path);
                }
            }
        }
    }

    public void readFromFile(String filename) {
        paths.clear();
        fileHandler.readFile(this, filename);
    }

    public void writeToFile(String filename) {
        fileHandler.writeFile(this, filename);
    }

    public Position getNodePosition(int nodeIndex) {
        if (nodeIndex == 0) {
            return Position.TOP_LEFT_CORNER;
        }

        if (nodeIndex == (rows * columns) - 1) {
            return Position.BOTTOM_RIGHT_CORNER;
        }

        if (nodeIndex == columns - 1) {
            return Position.TOP_RIGHT_CORNER;
        }

        if (nodeIndex == (rows * columns) - columns) {
            return Position.BOTTOM_LEFT_CORNER;
        }

        if (nodeIndex < columns) {
            return Position.TOP_EDGE;
        }

        if (nodeIndex >= (rows * columns) - columns) {
            return Position.BOTTOM_EDGE;
        }

        if (nodeIndex % columns == 0) {
            return Position.LEFT_EDGE;
        }

        if (nodeIndex % columns == columns - 1) {
            return Position.RIGHT_EDGE;
        }

        return Position.MIDDLE;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        appendGraphSizeInfo(result);
        appendNodes(result);
        return result.toString();
    }

    private void appendGraphSizeInfo(StringBuilder stringBuilder) {
        stringBuilder.append(rows).append(" ").append(columns).append("\n");
    }

    private void appendNodes(StringBuilder stringBuilder) {
        for (Node node : adjacencyList) {
            appendNode(stringBuilder, node);
        }
    }

    private void appendNode(StringBuilder stringBuilder, Node node) {
        stringBuilder.append("\t ");
        appendNodeEdges(stringBuilder, node);
        stringBuilder.append("\n");
    }

    private void appendNodeEdges(StringBuilder stringBuilder, Node node) {
        for (Direction edgeDirection : Direction.values()) {
            Edge edge = node.getEdges()[edgeDirection.ordinal()];
            appendEdge(stringBuilder, edge);
        }
    }

    private void appendEdge(StringBuilder stringBuilder, Edge edge) {
        if (edge != null) {
            stringBuilder.append(edge).append("  ");
        }
    }

    private void generate(double minWage, double maxWage, boolean randomEdge) {
        paths.clear();
        initializeAdjacencyList();

        for (Node node : adjacencyList) {
            for (Direction edgeDirection : Direction.values()) {
                if (edgeDirection == Direction.LEFT && node.isOnLeftEdge()) {
                    continue;
                }

                if (edgeDirection == Direction.UP && node.isOnTopEdge()) {
                    continue;
                }

                if (edgeDirection == Direction.RIGHT && node.isOnRightEdge()) {
                    continue;
                }

                if (edgeDirection == Direction.DOWN && node.isOnBottomEdge()) {
                    continue;
                }

                if (randomEdge && !edgeExists()) {
                    continue;
                }

                createEdge(node, edgeDirection, getRandomWage(minWage, maxWage));
            }
        }
    }

    private void initializeAdjacencyList() {
        adjacencyList = new Node[getNodesCount()];

        for (int index = 0; index < adjacencyList.length; index++) {
            adjacencyList[index] = new Node(index, getNodePosition(index), new Edge[Direction.values().length]);
        }
    }

    private void createEdge(Node node, Direction direction, double wage) {
        node.getEdges()[direction.ordinal()] = new Edge(getNeighbour(node, direction), wage, direction);
    }

    private int getNeighbour(Node node, Direction edgeDirection) {
        int nodeIndex = node.getIndex();

        if (edgeDirection == Direction.LEFT) {
            return nodeIndex - 1;
        } else if (edgeDirection == Direction.UP) {
            return nodeIndex - columns;
        } else if (edgeDirection == Direction.RIGHT) {
            return nodeIndex + 1;
        } else {
            return nodeIndex + columns;
        }
    }

    private boolean edgeExists() {
        Random random = new Random();
        return random.nextBoolean();
    }

    private double getRandomWage(double minWage, double maxWage) {
        Random random = new Random();
        return minWage + (random.nextDouble() * (maxWage - minWage));
    }
}
