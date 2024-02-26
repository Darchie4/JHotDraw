package org.jhotdraw.samples.svg.gui;

import org.jhotdraw.draw.DefaultDrawingEditor;
import org.jhotdraw.draw.DefaultDrawingView;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;

import java.awt.*;
import java.util.Arrays;

import static org.junit.Assert.*;

public class ViewToolBarTest {
    private ViewToolBar viewPalette;
    @Before
    public void setup() {
        viewPalette = new ViewToolBar();
        viewPalette.editor = new DefaultDrawingEditor();
        viewPalette.setView(new DefaultDrawingView());
    }
    @Test
    public void testCreateDisclosedComponent() {
        // Test that component is only created within the two eligible states of 1 & 2

        JComponent component0 = viewPalette.createDisclosedComponent(0);
        JComponent component1 = viewPalette.createDisclosedComponent(1);
        JComponent component2 = viewPalette.createDisclosedComponent(2);
        JComponent component3 = viewPalette.createDisclosedComponent(3);

        assertNull(component0);
        assertNotNull(component1);
        assertNotNull(component2);
        assertNull(component3);
    }

    @Test
    public void testClosedPanel() {
        // Panel setup
        JComponent panel = viewPalette.createDisclosedComponent(1);

        // Test that there are no text fields and 2 buttons in the closed panel

        Component[] components = panel.getComponents();

        assertTrue(
                Arrays.stream(components).noneMatch(component -> component instanceof JTextField)
        );
        assertEquals(2,
                Arrays.stream(components).filter(component -> component instanceof AbstractButton).count()
        );
    }

    @Test
    public void testOpenPanel() {
        // Panel setup
        JComponent panel = viewPalette.createDisclosedComponent(2);

        // Tests that there are 2 text fields and 2 buttons in the open panel

        Component[] components = panel.getComponents();

        assertEquals(2,
                Arrays.stream(components).filter(component -> component instanceof JTextField).count()
        );
        assertEquals(2,
                Arrays.stream(components).filter(component -> component instanceof AbstractButton).count()
        );
    }
}
