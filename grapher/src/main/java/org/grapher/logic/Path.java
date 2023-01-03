package org.grapher.logic;

import java.util.ArrayList;
import java.util.Objects;

public class Path {
    private final int id;
    private final ArrayList<Node> nodesSequence = new ArrayList<>();
    private Integer source = null;
    private Integer destination = null;
    private boolean showWages;
    private boolean display = true;

    public Path(int id) {
        this.id = id;
    }

    public Path(int source, int destination, int id) {
        this.source = source;
        this.destination = destination;
        this.id = id;
    }

    public Path(int source, int destination, int id, boolean showWages) {
        this.source = source;
        this.destination = destination;
        this.id = id;
        this.showWages = showWages;
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public Integer getDestination() {
        return destination;
    }

    public void setDestination(Integer destination) {
        this.destination = destination;
    }

    public int getId() {
        return id;
    }

    public boolean isShowWages() {
        return showWages;
    }

    public void setShowWages(boolean showWages) {
        this.showWages = showWages;
    }

    public boolean isDisplay() {
        return display;
    }

    public void setDisplay(boolean display) {
        this.display = display;
    }

    public ArrayList<Node> getNodesSequence() {
        return nodesSequence;
    }

    public boolean isValid() {
        if (!isConfigured()) {
            return false;
        }

        return !Objects.equals(source, destination);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder(id + "\n");
        for (Node node : nodesSequence) {
            result.append("\t").append(node.toString()).append("\n");
        }
        return result.toString();
    }

    private boolean isConfigured() {
        return source != null && destination != null;
    }
}
