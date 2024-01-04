package org.jhotdraw.samples.svg.gui;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import org.jhotdraw.draw.DrawingEditor;
import org.mockito.Mockito;

import javax.swing.*;
import java.util.LinkedList;

public class WhenTheToolBarGoesToLargeState extends Stage<WhenTheToolBarGoesToLargeState> {
    @ExpectedScenarioState
    int state;

    @ExpectedScenarioState
    FontToolBar fontToolBar;

    @ExpectedScenarioState
    JComponent jComponent;

    public WhenTheToolBarGoesToLargeState the_state_goes_to_large() {
        state = 2;
        fontToolBar.editor = Mockito.mock(DrawingEditor.class);
        fontToolBar.disposables = Mockito.mock(LinkedList.class);
        jComponent = fontToolBar.createDisclosedComponent(state);

        return self();
    }
}
