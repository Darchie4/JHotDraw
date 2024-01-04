package org.jhotdraw.gui;

import junit.framework.TestCase;

import java.awt.*;

public class ConstraintsFactoryTest extends TestCase {

    public void testCreateGridConstraints() {
        GridBagConstraints constraints = ConstraintsFactory.createGridConstraints(1, 2, 3, null);
        assertEquals(1, constraints.gridx);
        assertEquals(2, constraints.gridy);
        assertEquals(3, constraints.anchor);
        assertNull(constraints.insets);
    }

    public void testTestCreateGridConstraints() {
        Insets insets = new Insets(1, 2, 3, 4);
        GridBagConstraints constraints = ConstraintsFactory.createGridConstraints(1, 2, 3, insets, 4, 5);
        assertEquals(1, constraints.gridx);
        assertEquals(2, constraints.gridy);
        assertEquals(3, constraints.anchor);
        assertEquals(insets, constraints.insets);
        assertEquals(4, constraints.gridwidth);
        assertEquals(5, constraints.fill);
    }
}