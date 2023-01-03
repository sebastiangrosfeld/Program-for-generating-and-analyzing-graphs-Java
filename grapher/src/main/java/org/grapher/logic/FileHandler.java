package org.grapher.logic;

import org.grapher.gui.ErrorAlert;
import org.grapher.logic.Edge.Direction;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class FileHandler {
    private static final String FILE_ERROR = "Błąd odczytu / zapisu pliku";

    public void readFile(Graph graph, String filename) {
        Scanner scanner = prepareFileForReading(filename);
        int rows;
        int columns;

        if (scanner == null) {
            return;
        }

        if (scanner.hasNextInt()) {
            rows = scanner.nextInt();
        } else {
            showWrongFileFormatError();
            return;
        }

        if (scanner.hasNextInt()) {
            columns = scanner.nextInt();
        } else {
            showWrongFileFormatError();
            return;
        }

        if (!scanner.hasNextLine()) {
            showWrongFileFormatError();
            return;
        }

        scanner.nextLine();

        if (rows < 1 || columns < 1) {
            showWrongFileFormatError();
            return;
        }

        graph.setRows(rows);
        graph.setColumns(columns);
        graph.setAdjacencyList(setNodes(scanner, graph));
    }

    public void writeFile(Graph graph, String filename) {
        File file = new File(filename);

        if (file.exists() && !file.delete()) {
            showCannotOpenFileError(filename);
        }

        createFile(file);
        printGraphToFile(file, graph);
    }

    private void createFile(File file) {
        try {
            if (!file.createNewFile()) {
                throw new IOException();
            }
        } catch (IOException ioException) {
            showCannotOpenFileError(file.getName());
        }
    }

    private void printGraphToFile(File file, Graph graph) {
        try {
            PrintWriter printWriter = new PrintWriter(file);
            printWriter.print(graph.toString());
            printWriter.close();
        } catch (IOException ioException) {
            showCannotOpenFileError(file.getName());
        }
    }

    private Node[] setNodes(Scanner scanner, Graph graph) {
        Node[] adjacencyList = new Node[graph.getNodesCount()];

        for (int nodeIndex = 0; nodeIndex < graph.getNodesCount(); nodeIndex++) {
            adjacencyList[nodeIndex] = setNode(scanner, graph, nodeIndex);
        }

        return adjacencyList;
    }

    private Node setNode(Scanner scanner, Graph graph, int nodeIndex) {
        if (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            Scanner lineScanner = new Scanner(line);
            return new Node(nodeIndex, graph.getNodePosition(nodeIndex), setEdges(lineScanner, nodeIndex));
        } else {
            return new Node(nodeIndex, graph.getNodePosition(nodeIndex), new Edge[Direction.values().length]);
        }
    }

    private Edge[] setEdges(Scanner lineScanner, int nodeIndex) {
        Edge[] edges = new Edge[Direction.values().length];

        setDelimiter(lineScanner, "  ");

        for (Direction direction : Direction.values()) {
            Edge edge = null;

            if (lineScanner.hasNext()) {
                edge = setEdge(lineScanner.next(), nodeIndex);
            }

            edges[direction.ordinal()] = edge;
        }

        return edges;
    }

    private Edge setEdge(String line, int nodeIndex) {
        int destinationNodeIndex;
        double wage;

        line = prepareLineForReading(line);

        if (line.equals("")) {
            return null;
        }

        Scanner scanner = new Scanner(line);
        setDelimiter(scanner, ":");

        if (scanner.hasNextInt()) {
            destinationNodeIndex = scanner.nextInt();
        } else {
            showWrongFileFormatError();
            return null;
        }

        if (scanner.hasNext()) {
            wage = Double.parseDouble(scanner.next());
        } else {
            showWrongFileFormatError();
            return null;
        }

        return new Edge(destinationNodeIndex, wage, setDirection(destinationNodeIndex, nodeIndex));
    }

    private Scanner prepareFileForReading(String filename) {
        try {
            File file = new File(filename);
            Scanner scanner = new Scanner(file);
            setDelimiter(scanner, " ");

            return scanner;
        } catch (FileNotFoundException fileNotFoundException) {
            showCannotOpenFileError(filename);
            return null;
        }
    }

    private Direction setDirection(int destinationNodeIndex, int sourceNodeIndex) {
        if (destinationNodeIndex == sourceNodeIndex + 1) {
            return Direction.RIGHT;
        } else if (destinationNodeIndex == sourceNodeIndex - 1) {
            return Direction.LEFT;
        } else if (destinationNodeIndex < sourceNodeIndex - 1) {
            return Direction.UP;
        } else if (destinationNodeIndex > sourceNodeIndex + 1) {
            return Direction.DOWN;
        }

        return null;
    }

    private void setDelimiter(Scanner scanner, String string) {
        String delimiter = "\\s*" + string + "\\s*";
        scanner.useDelimiter(delimiter);
    }

    private String prepareLineForReading(String line) {
        return line.trim();
    }

    private void showCannotOpenFileError(String filename) {
        ErrorAlert.showError(FILE_ERROR, "Nie można otworzyć pliku " + filename + ".");
    }

    private void showWrongFileFormatError() {
        ErrorAlert.showError(FILE_ERROR, "Błędny format pliku.");
    }
}
