package org.jhotdraw.samples.svg.gui;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import org.jhotdraw.draw.gui.JAttributeTextField;
import org.mockito.Mockito;

import javax.swing.*;

import static org.junit.Assert.*;

public class ThenTheFontToolBarSholdBeLargeOpen extends Stage<ThenTheFontToolBarSholdBeLargeOpen> {
    @ExpectedScenarioState
    FontToolBar fontToolBar;

    @ExpectedScenarioState
    JComponent jComponent;

    public ThenTheFontToolBarSholdBeLargeOpen the_font_tool_bar_should_have_been_opened_large() {
        Mockito.verify(fontToolBar).createDisclosedComponent(2);

        assertNotNull(jComponent);
        assertEquals(6, ((JPanel) jComponent).getComponentCount());
        assertTrue(((JPanel) jComponent).getComponent(0) instanceof JAttributeTextField);
        assertTrue(((JPanel) jComponent).getComponent(1) instanceof AbstractButton);
        assertTrue(((JPanel) jComponent).getComponent(2) instanceof JPanel);
        assertTrue(((JPanel) jComponent).getComponent(3) instanceof AbstractButton);
        assertTrue(((JPanel) jComponent).getComponent(4) instanceof AbstractButton);
        assertTrue(((JPanel) jComponent).getComponent(5) instanceof AbstractButton);

        return self();
    }
}
