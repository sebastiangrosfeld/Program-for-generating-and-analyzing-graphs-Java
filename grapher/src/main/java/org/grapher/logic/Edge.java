package org.grapher.logic;

public class Edge {
    private final int nodeIndex;
    private final double wage;
    private final Direction direction;

    public Edge(int node, double wage, Direction direction) {
        this.nodeIndex = node;
        this.wage = wage;
        this.direction = direction;
    }

    public int getNodeIndex() {
        return nodeIndex;
    }

    public double getWage() {
        return wage;
    }

    public Direction getDirection() {
        return direction;
    }

    @Override
    public String toString() {
        return nodeIndex + " :" + wage;
    }

    public enum Direction {
        LEFT,
        UP,
        RIGHT,
        DOWN
    }
}
