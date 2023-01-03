package org.grapher.logic;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PathTest {
    private Path path;

    @Before
    public void setUp() {
        path = new Path(0, 5, 1, true);
    }

    @Test
    public void testIsValid() {
        assertTrue(path.isValid());

        path.setSource(null);
        path.setDestination(null);
        assertFalse(path.isValid());

        path.setSource(0);
        assertFalse(path.isValid());

        path.setSource(1);
        path.setDestination(1);
        assertFalse(path.isValid());
    }

    @Test
    public void testToString() {
        Node firstNode = new Node(0, Node.Position.TOP_LEFT_CORNER, new Edge[4]);
        Node secondNode = new Node(1, Node.Position.TOP_EDGE, new Edge[4]);
        path.getNodesSequence().add(firstNode);
        path.getNodesSequence().add(secondNode);

        String pathAsString = "1\n";
        pathAsString += "\t" + firstNode + "\n";
        pathAsString += "\t" + secondNode + "\n";

        assertEquals(pathAsString, path.toString());
    }
}
