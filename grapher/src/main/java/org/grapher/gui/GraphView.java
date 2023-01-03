package org.grapher.gui;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import org.grapher.logic.Edge;
import org.grapher.logic.Graph;
import org.grapher.logic.Node;
import org.grapher.logic.Path;

import java.util.ArrayList;
import java.util.Random;

import static org.grapher.gui.PathsScene.ERROR_TITLE;
import static org.grapher.gui.PathsScene.PATH_INVALID_ERROR;

public class GraphView extends Pane {
    public static final double GAP_BETWEEN_NODES = 100;
    private static final double WIDTH = 700;
    private static final double HEIGHT = 680;
    private static final double PADDING = 10;
    private final Graph graph;
    private final Pane container = new Pane();
    private NodeComponent[] nodeComponents;
    private NodeComponent pathSourceNodeComponent = null;
    private NodeComponent pathDestinationNodeComponent = null;

    public GraphView(Graph graph) {
        this.graph = graph;
    }

    public void initialize() {
        createContainer();
        createScrollPane();
    }

    public void drawGraph() {
        resetGraphView();
        initializeNodeComponents();
        drawNodes();
    }

    public void drawPaths() {
        drawGraph();

        for (Path path : graph.getPaths()) {
            if (path.isDisplay() && path.isValid()) {
                drawPath(path);
            }
        }
    }

    private void resetGraphView() {
        container.getChildren().clear();
    }

    private void createContainer() {
        container.setMinSize(WIDTH, HEIGHT);
        container.setPadding(new Insets(PADDING));
        container.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
    }

    private void createScrollPane() {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setLayoutX(20);
        scrollPane.setLayoutY(20);
        scrollPane.setPrefSize(WIDTH, HEIGHT);
        scrollPane.setContent(container);
        getChildren().add(scrollPane);
    }

    private void initializeNodeComponents() {
        nodeComponents = new NodeComponent[graph.getNodesCount()];

        for (Node node : graph.getAdjacencyList()) {
            createNodeComponent(node);
        }
    }

    private void createNodeComponent(Node node) {
        NodeComponent nodeComponent = new NodeComponent();
        nodeComponent.create(node);
        nodeComponents[node.getIndex()] = nodeComponent;
    }

    private void drawNodes() {
        for (int i = 0; i < nodeComponents.length; i++) {
            drawNode(i);
        }
    }

    private void drawNode(int index) {
        NodeComponent nodeComponent = nodeComponents[index];
        nodeComponent.relocate(calculateXNodeOffset(index), calculateYNodeOffset(index));
        container.getChildren().add(nodeComponent);
        registerMouseEvent(nodeComponent);
    }

    private double calculateXNodeOffset(int index) {
        return ((index % graph.getColumns()) * GAP_BETWEEN_NODES) + calculateBaseNodeOffset();
    }

    private double calculateYNodeOffset(int index) {
        return ((double) (index / graph.getColumns()) * GAP_BETWEEN_NODES) + calculateBaseNodeOffset();
    }

    private double calculateBaseNodeOffset() {
        return (PADDING + (NodeComponent.NODE_RADIUS / 2));
    }

    private void registerMouseEvent(final NodeComponent nodeComponent) {
        nodeComponent.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                setSelectedNodeComponent(mouseEvent);
                createPath(graph.getPaths());
            }

            private void setSelectedNodeComponent(MouseEvent mouseEvent) {
                if (mouseEvent.isShiftDown()) {
                    pathDestinationNodeComponent = nodeComponent;
                } else {
                    pathSourceNodeComponent = nodeComponent;
                }
            }
        });
    }

    private void createPath(ArrayList<Path> paths) {
        if (bothNodeComponentsSelected()) {
            int source = pathSourceNodeComponent.getNodeIndex();
            int destination = pathDestinationNodeComponent.getNodeIndex();

            if (sourceAndDestinationValid(source, destination)) {
                Path path = new Path(source, destination, calculateNewId(paths), true);
                paths.add(path);

                resetSelectedNodeComponents();

                graph.findPaths();
                drawPath(path);
            }
        }
    }

    private boolean bothNodeComponentsSelected() {
        return pathSourceNodeComponent != null && pathDestinationNodeComponent != null;
    }

    private boolean sourceAndDestinationValid(int source, int destination) {
        if (source == destination) {
            ErrorAlert.showError(ERROR_TITLE, PATH_INVALID_ERROR);
            resetSelectedNodeComponents();
            return false;
        }

        return true;
    }

    private int calculateNewId(ArrayList<Path> paths) {
        int pathsSize = paths.size();

        if (pathsSize == 0) {
            return 1;
        }

        return paths.get(pathsSize - 1).getId() + 1;
    }

    private void resetSelectedNodeComponents() {
        pathSourceNodeComponent = null;
        pathDestinationNodeComponent = null;
    }

    private void drawPath(Path path) {
        markNodesAndEdges(getNodeComponentsForPath(path), path);
    }

    private NodeComponent[] getNodeComponentsForPath(Path path) {
        ArrayList<Node> nodesSequence = path.getNodesSequence();
        NodeComponent[] pathNodeComponents = new NodeComponent[nodesSequence.size()];

        for (int i = 0; i < nodesSequence.size(); i++) {
            Node node = nodesSequence.get(i);
            pathNodeComponents[i] = nodeComponents[node.getIndex()];
        }

        return pathNodeComponents;
    }

    private void markNodesAndEdges(NodeComponent[] nodeComponents, Path path) {
        Color color = getRandomColor();

        for (int i = 1; i < nodeComponents.length; i++) {
            markNode(nodeComponents, path, i, color);
        }
    }

    private Color getRandomColor() {
        Random random = new Random();
        Color color = Color.color(random.nextDouble(), random.nextDouble(), random.nextDouble());

        while (color.getBrightness() < 0.3) {
            color = color.brighter();
        }

        return color;
    }

    private void markNode(NodeComponent[] nodeComponents, Path path, int index, Color color) {
        NodeComponent sourceNodeComponent = nodeComponents[index - 1];
        NodeComponent destinationNodeComponent = nodeComponents[index];
        sourceNodeComponent.setColor(color);
        destinationNodeComponent.setColor(color);

        EdgeComponent edgeComponent = getEdgeComponent(sourceNodeComponent.getEdgeComponents(), getNodeIndex(path, index - 1), getNodeIndex(path, index));
        markEdge(edgeComponent, color, path.isShowWages());
    }

    private int getNodeIndex(Path path, int pathNodeIndex) {
        Node node = path.getNodesSequence().get(pathNodeIndex);
        return node.getIndex();
    }

    private void markEdge(EdgeComponent edgeComponent, Color color, boolean showWage) {
        edgeComponent.setColor(color);

        if (showWage) {
            edgeComponent.showWage();
        }
    }

    private EdgeComponent getEdgeComponent(EdgeComponent[] edgeComponents, int sourceNodeIndex, int destinationNodeIndex) {
        if (sourceNodeIndex + 1 == destinationNodeIndex) {
            return edgeComponents[Edge.Direction.RIGHT.ordinal()];
        } else if (sourceNodeIndex - 1 == destinationNodeIndex) {
            return edgeComponents[Edge.Direction.LEFT.ordinal()];
        } else if (sourceNodeIndex + graph.getColumns() == destinationNodeIndex) {
            return edgeComponents[Edge.Direction.DOWN.ordinal()];
        } else {
            return edgeComponents[Edge.Direction.UP.ordinal()];
        }
    }
}
