package org.jhotdraw.samples.svg.gui;

import com.tngtech.jgiven.junit.ScenarioTest;
import org.junit.Test;

public class FontToolBarBDDTest extends ScenarioTest<GivenUserOpensFontToolBar, WhenTheToolBarGoesToLargeState, ThenTheFontToolBarSholdBeLargeOpen> {
    @Test
    public void open_toolbar() {
        given().the_user_opens_the_font_tool_bar();
        when().the_state_goes_to_large();
        then().the_font_tool_bar_should_have_been_opened_large();
    }
}