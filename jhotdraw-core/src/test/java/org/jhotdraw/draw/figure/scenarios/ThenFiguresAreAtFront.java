package org.jhotdraw.draw.figure.scenarios;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import org.jhotdraw.draw.Drawing;
import org.jhotdraw.draw.figure.Figure;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ThenFiguresAreAtFront extends Stage<ThenFiguresAreAtFront> {
    @ExpectedScenarioState
    Drawing drawing;

    @ExpectedScenarioState
    List<Figure> figures;

    public ThenFiguresAreAtFront figures_are_at_the_front() {
        assertEquals(drawing.getChild(0), figures.get(0));
        return this;
    }
}
