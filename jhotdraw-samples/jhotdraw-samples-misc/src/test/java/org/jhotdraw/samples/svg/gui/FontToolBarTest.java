package org.jhotdraw.samples.svg.gui;

import junit.framework.TestCase;
import org.jhotdraw.draw.DrawingEditor;
import org.jhotdraw.draw.gui.JAttributeTextField;
import org.mockito.Mockito;

import javax.swing.*;
import java.util.LinkedList;

public class FontToolBarTest extends TestCase {

    public void testCreateDisclosedComponent() {
        FontToolBar fontToolBar = Mockito.mock(FontToolBar.class);

        Mockito.when(fontToolBar.createDisclosedComponent(Mockito.anyInt())).thenCallRealMethod();

        fontToolBar.editor = null;

        JComponent jComponent = fontToolBar.createDisclosedComponent(0);
        assertNull(jComponent);

        jComponent = fontToolBar.createDisclosedComponent(1);
        assertNotNull(jComponent);

        jComponent = fontToolBar.createDisclosedComponent(2);
        assertNotNull(jComponent);

        jComponent = fontToolBar.createDisclosedComponent(3);
        assertNull(jComponent);

        jComponent = fontToolBar.createDisclosedComponent(1);
        assertTrue(jComponent instanceof JPanel);
        assertEquals(0, ((JPanel) jComponent).getComponentCount());

        fontToolBar.editor = Mockito.mock(DrawingEditor.class);
        fontToolBar.disposables = Mockito.mock(LinkedList.class);

        jComponent = fontToolBar.createDisclosedComponent(1);
        assertTrue(jComponent instanceof JPanel);
        assertEquals(6, ((JPanel) jComponent).getComponentCount());
        assertTrue(((JPanel) jComponent).getComponent(0) instanceof JAttributeTextField);
        assertTrue(((JPanel) jComponent).getComponent(1) instanceof AbstractButton);
        assertTrue(((JPanel) jComponent).getComponent(2) instanceof JPanel);
        assertTrue(((JPanel) jComponent).getComponent(3) instanceof AbstractButton);
        assertTrue(((JPanel) jComponent).getComponent(4) instanceof AbstractButton);
        assertTrue(((JPanel) jComponent).getComponent(5) instanceof AbstractButton);
    }

    public void testGetID() {
        FontToolBar fontToolBar = new FontToolBar();

        assertNotNull(fontToolBar.getID());
        assertEquals("font", fontToolBar.getID());
    }

    public void testGetDefaultDisclosureState() {
        FontToolBar fontToolBar = new FontToolBar();

        assertEquals(1, fontToolBar.getDefaultDisclosureState());
    }
}