package org.grapher.logic;

import org.grapher.gui.ErrorAlert;
import org.grapher.gui.InformationAlert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Dijkstra {
    private static final String PATH_NOT_EXISTS_INFO_TITLE = "Ścieżka nie istnieje";
    private static final String PATH_NOT_EXISTS_INFO_MESSAGE = "Zadana ścieżka nie istnieje.\n";
    private static final String PATH_SEEK_ERROR_TITLE = "Błąd podczas szukania ścieżki";
    private static final String PATH_SEEK_ERROR_MESSAGE = "Podczas szukania ścieżki wystąpił błąd.";

    public void findPath(Graph graph, Path path) {
        ArrayList<Node> adjacencyList = getAdjacencyList(graph);

        double[] distances = new double[adjacencyList.size()];
        Node[] predecessors = new Node[adjacencyList.size()];
        boolean[] visited = new boolean[adjacencyList.size()];

        initializeDistances(distances, path);

        try {
            for (int i = 0; i < adjacencyList.size(); i++) {
                Node minDistanceNode = findMinDistanceNode(adjacencyList, distances, visited);

                updateDistancesAndPredecessors(minDistanceNode, distances, predecessors);
            }

            predecessorsToNodesSequence(predecessors, graph, path);
        } catch (NullPointerException nullPointerException) {
            handlePathNotFound(path);
        }
    }

    private ArrayList<Node> getAdjacencyList(Graph graph) {
        ArrayList<Node> adjacencyList = new ArrayList<>();
        Collections.addAll(adjacencyList, graph.getAdjacencyList());
        return adjacencyList;
    }

    private void initializeDistances(double[] distances, Path path) {
        Arrays.fill(distances, Double.MAX_VALUE);
        distances[path.getSource()] = 0;
    }

    private Node findMinDistanceNode(ArrayList<Node> adjacencyList, double[] distances, boolean[] visited) {
        Node minDistanceNode = null;
        double minDistance = Double.MAX_VALUE;

        for (Node node : adjacencyList) {
            if (distances[node.getIndex()] < minDistance && !visited[node.getIndex()]) {
                minDistance = distances[node.getIndex()];
                minDistanceNode = node;
            }
        }

        try {
            visited[minDistanceNode.getIndex()] = true;
        } catch (NullPointerException nullPointerException) {
            ErrorAlert.showError(PATH_SEEK_ERROR_TITLE, PATH_SEEK_ERROR_MESSAGE);
        }

        return minDistanceNode;
    }

    private void updateDistancesAndPredecessors(Node minDistanceNode, double[] distances, Node[] predecessors) {
        try {
            for (Edge edge : minDistanceNode.getEdges()) {
                if (edge != null && distances[edge.getNodeIndex()] > distances[minDistanceNode.getIndex()] + edge.getWage()) {
                    distances[edge.getNodeIndex()] = distances[minDistanceNode.getIndex()] + edge.getWage();
                    predecessors[edge.getNodeIndex()] = minDistanceNode;
                }
            }
        } catch (NullPointerException nullPointerException) {
            System.err.println(nullPointerException.getMessage());
        }
    }

    private void predecessorsToNodesSequence(Node[] predecessors, Graph graph, Path path) {
        path.getNodesSequence().add(graph.getAdjacencyList()[path.getDestination()]);
        buildNodesSequence(predecessors, path, path.getDestination());
        reverseNodesSequence(path);
    }

    private void buildNodesSequence(Node[] predecessors, Path path, int destination) {
        if (path.getSource() == destination) {
            return;
        }

        Node predecessor = predecessors[destination];
        path.getNodesSequence().add(predecessor);

        buildNodesSequence(predecessors, path, predecessor.getIndex());
    }

    private void reverseNodesSequence(Path path) {
        Collections.reverse(path.getNodesSequence());
    }

    private void handlePathNotFound(Path path) {
        String id = "Id: " + path.getId() + "\n";
        String start = "Start: " + path.getSource() + "\n";
        String end = "Koniec: " + path.getDestination();
        String message = PATH_NOT_EXISTS_INFO_MESSAGE + id + start + end;

        InformationAlert.showNotification(PATH_NOT_EXISTS_INFO_TITLE, message);

        path.getNodesSequence().clear();
    }
}
