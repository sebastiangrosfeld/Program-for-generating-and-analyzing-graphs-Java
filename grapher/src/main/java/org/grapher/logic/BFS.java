package org.grapher.logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class BFS {
    public boolean checkConsistency(Graph graph) {
        FIFOQueue fifoQueue = new FIFOQueue();

        return isConsistent(fifoQueue, graph);
    }

    private Integer[] initializeAncestors(Graph graph) {
        Integer[] ancestors = new Integer[graph.getNodesCount()];
        Arrays.fill(ancestors, null);

        return ancestors;
    }

    private Colors[] initializeColors(Graph graph, int top) {
        Colors[] colors = new Colors[graph.getNodesCount()];

        for (int i = 0; i < graph.getNodesCount(); i++) {
            colors[i] = Colors.WHITE;
        }
        colors[top] = Colors.GREY;

        return colors;
    }

    private void addTop(FIFOQueue fifoQueue, Integer[] ancestors, Colors[] colors, Integer top, int index) {
        colors[index] = Colors.GREY;
        ancestors[index] = top;
        fifoQueue.add(index);
    }

    private boolean isConsistent(FIFOQueue fifoQueue, Graph graph) {
        for (int nodeIndex = 0; nodeIndex < graph.getNodesCount(); nodeIndex++) {
            if (!runBfsForNode(nodeIndex, graph, fifoQueue)) {
                return false;
            }
        }

        return true;
    }

    private boolean runBfsForNode(int nodeIndex, Graph graph, FIFOQueue fifoQueue) {
        Integer[] ancestors = initializeAncestors(graph);
        Colors[] colors = initializeColors(graph, nodeIndex);
        fifoQueue.add(nodeIndex);

        while (fifoQueue.isNotEmpty()) {
            int top = fifoQueue.pop();
            Node node = graph.getAdjacencyList()[top];

            if (!checkEdges(node, ancestors, colors, fifoQueue, top)) {
                return false;
            }

            colors[top] = Colors.BLACK;
        }

        return checkConsistencyForNode(nodeIndex, graph, ancestors);
    }

    private boolean checkEdges(Node node, Integer[] ancestors, Colors[] colors, FIFOQueue fifoQueue, Integer top) {
        int numberOfNullNeighbours = 0;

        for (Edge edge : node.getEdges()) {
            if (edge != null) {
                int nodeIndex = edge.getNodeIndex();

                if (Objects.equals(colors[nodeIndex], Colors.WHITE)) {
                    addTop(fifoQueue, ancestors, colors, top, nodeIndex);
                }
            } else {
                numberOfNullNeighbours++;
            }
        }

        return numberOfNullNeighbours != 4;
    }

    private boolean checkConsistencyForNode(int nodeIndex, Graph graph, Integer[] ancestors) {
        for (int i = 0; i < graph.getNodesCount(); i++) {
            if (ancestors[i] == null && nodeIndex != i) {
                return false;
            }
        }

        return true;
    }

    private enum Colors {
        WHITE,
        GREY,
        BLACK
    }

    private class FIFOQueue {
        private final ArrayList<Integer> queue = new ArrayList<>();

        public void add(Integer top) {
            queue.add(top);
        }

        public int pop() {
            int firstInQueue = queue.get(0);
            queue.remove(0);

            return firstInQueue;
        }

        public boolean isNotEmpty() {
            return !queue.isEmpty();
        }
    }
}
