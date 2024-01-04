package org.jhotdraw.draw.figure.scenarios;
import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import org.jhotdraw.draw.Drawing;
import org.jhotdraw.draw.action.edit.BringToFrontAction;
import org.jhotdraw.draw.figure.Figure;

import java.util.List;

public class WhenBringToFrontIsPerformed extends Stage<WhenBringToFrontIsPerformed> {
    @ExpectedScenarioState
    Drawing drawing;

    @ExpectedScenarioState
    List<Figure> figures;

    public WhenBringToFrontIsPerformed bring_to_front_is_called() {
        BringToFrontAction.bringToFront(drawing, figures);
        return this;
    }
}
