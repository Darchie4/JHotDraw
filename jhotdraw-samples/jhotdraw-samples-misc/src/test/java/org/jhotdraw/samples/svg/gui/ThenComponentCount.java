package org.jhotdraw.samples.svg.gui;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ScenarioState;

import static org.junit.Assert.assertEquals;

public class ThenComponentCount extends Stage<ThenComponentCount> {
    @ScenarioState
    StrokeToolBarState sharedState;

    public ThenComponentCount theNumberOfComponentsIs(int expectedCount) {
        assertEquals(expectedCount, sharedState.component.getComponentCount());
        return this;
    }
}
