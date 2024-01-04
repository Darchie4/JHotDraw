package org.jhotdraw.samples.svg.gui;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;

import javax.swing.*;

public class WhenTheToolBarGoesToClosedState extends Stage<WhenTheToolBarGoesToClosedState> {
    @ExpectedScenarioState
    int state;

    @ExpectedScenarioState
    FontToolBar fontToolBar;

    @ExpectedScenarioState
    JComponent jComponent;

    public WhenTheToolBarGoesToClosedState the_state_goes_to_closed() {
        state = 0;
        jComponent = fontToolBar.createDisclosedComponent(state);

        return self();
    }
}
