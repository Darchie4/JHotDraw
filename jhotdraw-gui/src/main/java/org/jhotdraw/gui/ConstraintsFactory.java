package org.jhotdraw.gui;

import java.awt.*;

/**
 * Factory for creating constraint objects in the GUI.
 *
 * @author Mikkel Albrechtsen
 * @version $Id$
 */
public class ConstraintsFactory {
    /**
     * Creates a GridBagConstraints object with the given parameters.
     *
     * @param x      The x coordinate of the component.
     * @param y      The y coordinate of the component.
     * @param anchor The anchor of the component. Please see
     *               GridBagConstraints.anchor for more information.
     * @param insets The insets of the component. Please see
     *               GridBagConstraints.insets for more information.
     * @return The GridBagConstraints object.
     */
    public static GridBagConstraints createGridConstraints(int x, int y, int anchor, Insets insets) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = x;
        constraints.gridy = y;
        constraints.insets = insets;
        constraints.anchor = anchor;

        return constraints;
    }

    /**
     * Creates a GridBagConstraints object with the given parameters.
     *
     * @param x         The x coordinate of the component.
     * @param y         The y coordinate of the component.
     * @param anchor    The anchor of the component. Please see
     *                  GridBagConstraints.anchor for more information.
     * @param insets    The insets of the component. Please see
     *                  GridBagConstraints.insets for more information.
     * @param gridWidth The width of the component. Please see
     *                  GridBagConstraints.gridwidth for more information.
     * @param fill      The fill of the component. Please see
     *                  GridBagConstraints.fill for more information.
     * @return The GridBagConstraints object.
     */
    public static GridBagConstraints createGridConstraints(int x, int y, int anchor, Insets insets, int gridWidth, int fill) {
        GridBagConstraints constraints = createGridConstraints(x, y, anchor, insets);
        constraints.gridwidth = gridWidth;
        constraints.fill = fill;

        return constraints;
    }
}
