package org.jhotdraw.samples.svg.gui;

import com.tngtech.jgiven.annotation.ScenarioState;
import org.jhotdraw.draw.DrawingEditor;
import org.junit.Test;
import com.tngtech.jgiven.junit.ScenarioTest;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class StrokeToolBarScenarioTest extends ScenarioTest<GivenStateStrokeTool, WhenComponentCreated, ThenComponentCount> {
    @ScenarioState
    StrokeToolBarState sharedState = new StrokeToolBarState();

    @Test
    public void ensureCorrectNumberOfComponentsBasedOnState() {
        int[] states = {1, 2, 0, -1};
        int[] expectedComponentCounts = {6, 10, 0, 0};

        MockitoAnnotations.initMocks(this);
        AbstractToolBar bar = new StrokeToolBar();
        DrawingEditor mockEditor = Mockito.mock(DrawingEditor.class);
        bar.editor = mockEditor;
        sharedState.strokeToolBar = (StrokeToolBar) bar;
        for (int i = 0; i < states.length * 2; i++) { // Run through twice to check for no side effects
            int index = i % states.length;
            int currentState = states[index];
            int expectedComponentCount = expectedComponentCounts[index];

            given().aState(currentState);
            when().createDisclosedComponentIsCalled();
            then().theNumberOfComponentsIs(expectedComponentCount);
        }
    }
}
