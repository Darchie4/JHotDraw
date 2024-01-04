package org.jhotdraw.samples.svg.gui;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;

import javax.swing.*;

public class WhenTheToolBarGoesToSmallState extends Stage<WhenTheToolBarGoesToSmallState> {
    @ExpectedScenarioState
    int state;

    @ExpectedScenarioState
    FontToolBar fontToolBar;

    @ExpectedScenarioState
    JComponent jComponent;

    public WhenTheToolBarGoesToSmallState the_state_goes_to_small() {
        state = 1;
        jComponent = fontToolBar.createDisclosedComponent(state);

        return self();
    }
}
