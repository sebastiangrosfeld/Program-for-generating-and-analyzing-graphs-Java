package org.grapher.logic;

import org.grapher.logic.Node.Position;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class GraphTest {
    private final double minWage = 0;
    private final double maxWage = 100;
    private final String writeToFileTestFilename = "./src/test/test_files/test_write_to_file.txt";
    private final String consistentGraphFilename = "./src/test/test_files/cos_graph.txt";
    private final String inconsistentGraphFilename = "./src/test/test_files/incos_graph.txt";
    private final String testGraphFilename = "./src/test/test_files/t_graph.txt";
    private final String wrongFileGraphFilename = "./src/test/test_files/wf_graph.txt";
    private Graph graph;

    @Before
    public void setUp() {
        graph = new Graph();
        graph.setRows(4);
        graph.setColumns(6);
    }

    @Test
    public void testGetNodesCount() {
        int expectedNodesCount = 4 * 6;
        assertEquals(expectedNodesCount, graph.getNodesCount());
    }

    @Test
    public void testGenerateWages() {
        graph.generateWages(minWage, maxWage);

        itShouldGenerateWagesInRange(graph);

        boolean edgeIsMissing = false;
        for (Node node : graph.getAdjacencyList()) {
            for (Edge edge : node.getEdges()) {
                if (node.getNodePosition() == Position.MIDDLE && edge == null) {
                    edgeIsMissing = true;
                    break;
                }
            }
        }

        assertFalse(edgeIsMissing);
    }

    @Test
    public void testGenerateConsistent() {
        graph.generateConsistent(minWage, maxWage);

        itShouldGenerateWagesInRange(graph);

        boolean isConsistent = graph.checkConsistency();
        assertTrue(isConsistent);
    }

    @Test
    public void testGenerateRandom() {
        graph.generateRandom(minWage, maxWage);

        itShouldGenerateWagesInRange(graph);
    }

    @Test
    public void testCheckConsistency() {
        graph.readFromFile(consistentGraphFilename);
        boolean isConsistent = graph.checkConsistency();
        assertTrue(isConsistent);

        graph.readFromFile(inconsistentGraphFilename);
        isConsistent = graph.checkConsistency();
        assertFalse(isConsistent);
    }

    @Test
    public void testFindPaths() {
        Path path = new Path(0, 6, 1);
        graph.readFromFile(testGraphFilename);
        graph.getPaths().add(path);
        graph.findPaths();

        int[] nodesSequenceIndexes = new int[4];
        for (int i = 0; i < 4; i++) {
            nodesSequenceIndexes[i] = path.getNodesSequence().get(i).getIndex();
        }

        int[] expectedNodesSequenceIndexes = {0, 1, 5, 6};
        assertArrayEquals(expectedNodesSequenceIndexes, nodesSequenceIndexes);
    }

    // Should throw exception due to call to ErrorAlert without JavaFX launched
    @Test(expected = ExceptionInInitializerError.class)
    public void testReadFromFile() {
        graph.readFromFile(wrongFileGraphFilename);

        assert false;
    }

    @Test
    public void testWriteToFile() {
        graph.readFromFile(inconsistentGraphFilename);
        String expectedGraphAsString = graph.toString();

        graph.writeToFile(writeToFileTestFilename);
        graph.readFromFile(writeToFileTestFilename);
        String writtenGraphAsString = graph.toString();

        assertEquals(expectedGraphAsString, writtenGraphAsString);
    }

    @Test
    public void testGetNodePosition() {
        graph.generateWages(minWage, maxWage);
        Node[] nodes = graph.getAdjacencyList();

        assertEquals(Position.TOP_LEFT_CORNER, nodes[0].getNodePosition());
        assertEquals(Position.TOP_RIGHT_CORNER, nodes[5].getNodePosition());
        assertEquals(Position.BOTTOM_LEFT_CORNER, nodes[18].getNodePosition());
        assertEquals(Position.BOTTOM_RIGHT_CORNER, nodes[23].getNodePosition());
        assertEquals(Position.TOP_EDGE, nodes[3].getNodePosition());
        assertEquals(Position.LEFT_EDGE, nodes[6].getNodePosition());
        assertEquals(Position.RIGHT_EDGE, nodes[11].getNodePosition());
        assertEquals(Position.BOTTOM_EDGE, nodes[22].getNodePosition());
        assertEquals(Position.MIDDLE, nodes[8].getNodePosition());
    }

    @Test
    public void testToString() {
        graph.readFromFile(inconsistentGraphFilename);
        String graphAsString = "4 4\n" +
                "\t 1 :0.3  4 :0.2  \n" +
                "\t 5 :0.2  2 :0.6  0 :0.4  \n" +
                "\t 6 :0.8  3 :0.4  1 :0.6  \n" +
                "\t 7 :0.5  2 :0.8  \n" +
                "\t 8 :0.9  0 :0.8  5 :0.9  \n" +
                "\t 1 :0.5  9 :0.3  6 :0.4  4 :0.4  \n" +
                "\t 10 :0.7  7 :0.7  2 :0.2  5 :0.3  \n" +
                "\t 6 :0.9  3 :0.7  \n" +
                "\t 4 :0.7  12 :0.5  9 :0.2  \n" +
                "\t 13 :0.8  5 :0.8  8 :0.4  10 :0.4  \n" +
                "\t 14 :0.5  6 :0.5  9 :0.7  \n" +
                "\t \n" +
                "\t 13 :0.5  8 :0.5  \n" +
                "\t 9 :0.7  12 :0.7  14 :0.4  \n" +
                "\t 10 :0.8  15 :0.2  13 :0.2  \n" +
                "\t 14 :0.4  \n";

        assertEquals(graphAsString, graph.toString());
    }

    private void itShouldGenerateWagesInRange(Graph graph) {
        boolean wageIsOutOfRange = false;

        for (Node node : graph.getAdjacencyList()) {
            for (Edge edge : node.getEdges()) {
                if (edge != null) {
                    if (edge.getWage() < minWage || edge.getWage() > maxWage) {
                        wageIsOutOfRange = true;
                    }
                }
            }
        }

        assertFalse(wageIsOutOfRange);
    }
}
