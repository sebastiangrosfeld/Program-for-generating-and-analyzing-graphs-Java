package org.grapher.logic;

import java.util.Arrays;
import java.util.Objects;

public class Node {
    private final int index;
    private final Edge[] edges;
    private Position position;

    public Node(int index, Position position, Edge[] edges) {
        this.index = index;
        this.position = position;
        this.edges = edges;
    }

    public int getIndex() {
        return index;
    }

    public Position getNodePosition() {
        return position;
    }

    public void setNodePosition(Position position) {
        this.position = position;
    }

    public Edge[] getEdges() {
        return edges;
    }

    public boolean isOnLeftEdge() {
        return position == Position.LEFT_EDGE || position == Position.TOP_LEFT_CORNER || position == Position.BOTTOM_LEFT_CORNER;
    }

    public boolean isOnTopEdge() {
        return position == Position.TOP_EDGE || position == Position.TOP_LEFT_CORNER || position == Position.TOP_RIGHT_CORNER;
    }

    public boolean isOnRightEdge() {
        return position == Position.RIGHT_EDGE || position == Position.TOP_RIGHT_CORNER || position == Position.BOTTOM_RIGHT_CORNER;
    }

    public boolean isOnBottomEdge() {
        return position == Position.BOTTOM_EDGE || position == Position.BOTTOM_LEFT_CORNER || position == Position.BOTTOM_RIGHT_CORNER;
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(index, position);
        result = 31 * result + Arrays.hashCode(edges);
        return result;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (object == null || getClass() != object.getClass()) {
            return false;
        }

        Node node = (Node) object;
        return index == node.index;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder(index + " " + position + "\n");
        for (Edge edge : edges) {
            result.append("\t");

            if (edge == null) {
                result.append("No edge");
            } else {
                result.append(edge);
            }

            result.append("\n");
        }
        return result.toString();
    }

    public enum Position {
        TOP_LEFT_CORNER,
        TOP_RIGHT_CORNER,
        BOTTOM_LEFT_CORNER,
        BOTTOM_RIGHT_CORNER,
        TOP_EDGE,
        BOTTOM_EDGE,
        LEFT_EDGE,
        RIGHT_EDGE,
        MIDDLE
    }
}
