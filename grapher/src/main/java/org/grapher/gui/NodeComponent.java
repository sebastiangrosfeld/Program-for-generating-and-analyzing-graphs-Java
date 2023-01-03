package org.grapher.gui;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.grapher.logic.Edge;
import org.grapher.logic.Node;

public class NodeComponent extends Group {
    public static final double NODE_RADIUS = 20;
    private final EdgeComponent[] edgeComponents = new EdgeComponent[4];
    private final Circle circle = new Circle();
    private Color color = Color.WHITE;
    private int nodeIndex;

    public void setColor(Color color) {
        this.color = color;
        circle.setFill(color);
    }

    public EdgeComponent[] getEdgeComponents() {
        return edgeComponents;
    }

    public int getNodeIndex() {
        return nodeIndex;
    }

    public void create(Node node) {
        nodeIndex = node.getIndex();
        createNode();
        createEdges(node);
    }

    private void createNode() {
        circle.setRadius(NODE_RADIUS);
        circle.setFill(color);
        getChildren().add(circle);
    }

    private void createEdges(Node node) {
        for (Edge edge : node.getEdges()) {
            createEdge(edge);
        }
    }

    private void createEdge(Edge edge) {
        if (edge != null) {
            EdgeComponent edgeComponent = new EdgeComponent(edge.getWage());
            edgeComponent.create();

            Edge.Direction edgeDirection = edge.getDirection();
            setEdgeDirection(edgeDirection, edgeComponent);

            edgeComponents[edgeDirection.ordinal()] = edgeComponent;
            getChildren().add(edgeComponent);
        }
    }

    private void setEdgeDirection(Edge.Direction edgeDirection, EdgeComponent edgeComponent) {
        switch (edgeDirection) {
            case LEFT:
                setEdgeDirectionLeft(edgeComponent);
                break;
            case UP:
                setEdgeDirectionUp(edgeComponent);
                break;
            case RIGHT:
                break;
            case DOWN:
                setEdgeDirectionDown(edgeComponent);
                break;
        }
    }

    private void setEdgeDirectionLeft(EdgeComponent edgeComponent) {
        edgeComponent.setRotate(180);
        edgeComponent.setTranslateX(-GraphView.GAP_BETWEEN_NODES);
        edgeComponent.setTranslateY(10);
        edgeComponent.getLabel().setRotate(180);
        setTranslateX(-80);
    }

    private void setEdgeDirectionUp(EdgeComponent edgeComponent) {
        edgeComponent.setRotate(270);
        edgeComponent.setTranslateX(-(GraphView.GAP_BETWEEN_NODES / 2) - 5);
        edgeComponent.setTranslateY(-(GraphView.GAP_BETWEEN_NODES / 2) + 5);
        setTranslateY(-80);
    }

    private void setEdgeDirectionDown(EdgeComponent edgeComponent) {
        edgeComponent.setRotate(90);
        edgeComponent.setTranslateX(-(GraphView.GAP_BETWEEN_NODES / 2) + 5);
        edgeComponent.setTranslateY((GraphView.GAP_BETWEEN_NODES / 2) + 5);
    }
}
