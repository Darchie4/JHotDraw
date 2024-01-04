package org.jhotdraw.draw.figure;

import com.tngtech.jgiven.junit5.ScenarioTest;
import org.jhotdraw.draw.figure.scenarios.GivenFigures;
import org.jhotdraw.draw.figure.scenarios.ThenFiguresAreAtFront;
import org.jhotdraw.draw.figure.scenarios.WhenBringToFrontIsPerformed;
import org.junit.jupiter.api.Test;

class BringToFrontActionTest extends ScenarioTest<GivenFigures,
        WhenBringToFrontIsPerformed, ThenFiguresAreAtFront> {

    @Test
    void testBringToFrontAction() {
        given().some_figures_in_a_drawing();
        when().bring_to_front_is_called();
        then().figures_are_at_the_front();
    }
}

