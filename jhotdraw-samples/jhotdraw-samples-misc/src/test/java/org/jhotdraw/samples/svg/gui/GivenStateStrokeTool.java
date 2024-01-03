package org.jhotdraw.samples.svg.gui;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ScenarioState;

public class GivenStateStrokeTool extends Stage<GivenStateStrokeTool> {
    @ScenarioState
        StrokeToolBarState sharedState;

    public GivenStateStrokeTool aState(int state) {
        sharedState.state = state;
            return this;
    }
}

