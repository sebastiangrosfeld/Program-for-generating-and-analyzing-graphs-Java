package org.grapher.logic;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EdgeTest {
    private Edge edge;

    @Before
    public void setUp() {
        edge = new Edge(0, 1.0, Edge.Direction.LEFT);
    }

    @Test
    public void testToString() {
        String edgeAsString = "0 :1.0";

        assertEquals(edgeAsString, edge.toString());
    }
}
