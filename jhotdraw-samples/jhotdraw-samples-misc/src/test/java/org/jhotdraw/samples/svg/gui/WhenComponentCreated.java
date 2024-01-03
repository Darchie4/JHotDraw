package org.jhotdraw.samples.svg.gui;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ScenarioState;

public class WhenComponentCreated extends Stage<WhenComponentCreated> {
    @ScenarioState
    StrokeToolBarState sharedState;

    public WhenComponentCreated createDisclosedComponentIsCalled() {
        sharedState.component = sharedState.strokeToolBar.createDisclosedComponent(sharedState.state);
        return this;
    }
}
