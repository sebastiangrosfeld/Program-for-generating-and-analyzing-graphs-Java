package org.grapher.logic;

import org.grapher.logic.Edge.Direction;
import org.grapher.logic.Node.Position;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class NodeTest {
    private Node node;

    @Before
    public void setUp() {
        node = new Node(0, Position.TOP_LEFT_CORNER, new Edge[4]);
        node.getEdges()[2] = new Edge(1, 1.0, Direction.RIGHT);
        node.getEdges()[3] = new Edge(2, 2.0, Direction.DOWN);
    }

    @Test
    public void testIsOnLeftEdge() {
        node.setNodePosition(Position.TOP_LEFT_CORNER);
        assertTrue(node.isOnLeftEdge());

        node.setNodePosition(Position.LEFT_EDGE);
        assertTrue(node.isOnLeftEdge());

        node.setNodePosition(Position.BOTTOM_LEFT_CORNER);
        assertTrue(node.isOnLeftEdge());

        node.setNodePosition(Position.MIDDLE);
        assertFalse(node.isOnLeftEdge());
    }

    @Test
    public void testIsOnTopEdge() {
        node.setNodePosition(Position.TOP_LEFT_CORNER);
        assertTrue(node.isOnTopEdge());

        node.setNodePosition(Position.TOP_EDGE);
        assertTrue(node.isOnTopEdge());

        node.setNodePosition(Position.TOP_RIGHT_CORNER);
        assertTrue(node.isOnTopEdge());

        node.setNodePosition(Position.MIDDLE);
        assertFalse(node.isOnTopEdge());
    }

    @Test
    public void testIsOnRightEdge() {
        node.setNodePosition(Position.TOP_RIGHT_CORNER);
        assertTrue(node.isOnRightEdge());

        node.setNodePosition(Position.RIGHT_EDGE);
        assertTrue(node.isOnRightEdge());

        node.setNodePosition(Position.BOTTOM_RIGHT_CORNER);
        assertTrue(node.isOnRightEdge());

        node.setNodePosition(Position.MIDDLE);
        assertFalse(node.isOnRightEdge());
    }

    @Test
    public void testIsOnBottomEdge() {
        node.setNodePosition(Position.BOTTOM_LEFT_CORNER);
        assertTrue(node.isOnBottomEdge());

        node.setNodePosition(Position.BOTTOM_EDGE);
        assertTrue(node.isOnBottomEdge());

        node.setNodePosition(Position.BOTTOM_RIGHT_CORNER);
        assertTrue(node.isOnBottomEdge());

        node.setNodePosition(Position.MIDDLE);
        assertFalse(node.isOnBottomEdge());
    }

    @Test
    public void testEquals() {
        Node equalNode = new Node(0, Position.TOP_LEFT_CORNER, new Edge[4]);
        Node notEqualNode = new Node(1, Position.TOP_EDGE, new Edge[4]);

        assertEquals(node, equalNode);
        assertNotEquals(node, notEqualNode);
    }

    @Test
    public void testToString() {
        String nodeAsString = "0 TOP_LEFT_CORNER\n";
        nodeAsString += "\tNo edge\n";
        nodeAsString += "\tNo edge\n";
        nodeAsString += "\t1 :1.0\n";
        nodeAsString += "\t2 :2.0\n";

        assertEquals(nodeAsString, node.toString());
    }
}
