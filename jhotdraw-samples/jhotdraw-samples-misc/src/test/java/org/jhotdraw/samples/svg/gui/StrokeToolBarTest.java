package org.jhotdraw.samples.svg.gui;

import org.jhotdraw.draw.DrawingEditor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import javax.swing.*;

import static org.junit.Assert.*;


public class StrokeToolBarTest {

    private AbstractToolBar toolbar;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        AbstractToolBar bar = new StrokeToolBar();
        DrawingEditor mockEditor = Mockito.mock(DrawingEditor.class);
        bar.editor = mockEditor;
        toolbar = (StrokeToolBar) bar;

    }

    @Test
    public void testCreateDisclosedComponentState1() {
        int state = 1;
        JComponent result = toolbar.createDisclosedComponent(state);
        assertNotNull("Component should not be null for state 1", result);

        assertTrue("Result should be a JPanel for state 1", result instanceof JPanel);
    }

    @Test
    public void testCreateDisclosedComponentState2() {
        int state = 2;
        JComponent result = toolbar.createDisclosedComponent(state);
        assertNotNull("Component should not be null for state 2", result);

        assertTrue("Result should be a JPanel for state 2", result instanceof JPanel);
    }

    @Test
    public void testCreateDisclosedComponentDefaultState() {
        int state = 999; // An unknown state
        JComponent result = toolbar.createDisclosedComponent(state);
        assertNotNull("Component should not be null for default state", result);

    }

    @Test
    public void testCreateDisclosedComponentWithNullEditor() {
        toolbar.setEditor(null); // Assuming there's a method to set editor
        int state = 1;
        JComponent result = toolbar.createDisclosedComponent(state);
        assertNotNull("Component should not be null when editor is null", result);
        }

    @Test
    public void testCreateDisclosedComponentWithNumberOfElementsCase1() {
        int panel =  toolbar.createDisclosedComponent(1).getComponents().length;
        int expectedElements = 6;
        assertEquals("Number of elements should be 6", expectedElements, panel);

    }
    @Test
    public void testCreateDisclosedComponentWithNumberOfElementsCase2() {
        int panel =  toolbar.createDisclosedComponent(2).getComponents().length;
        int expectedElements = 10;
        assertEquals("Number of elements should be 10", expectedElements, panel);

    }
    @Test
    public void testCreateDisclosedComponentWithNumberOfElementsCase0() {
        toolbar.editor = null;
        int panel =  toolbar.createDisclosedComponent(2).getComponents().length;
        int expectedElements = 0;
        assertEquals("Number of elements should be 0", expectedElements, panel);

    }
}

