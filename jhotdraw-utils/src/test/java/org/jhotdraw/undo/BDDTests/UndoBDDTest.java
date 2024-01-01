package org.jhotdraw.undo.BDDTests;

import com.tngtech.jgiven.junit.SimpleScenarioTest;
import org.junit.Test;

public class UndoBDDTest extends SimpleScenarioTest<UndoBDDTestStage> {

    @Test
    public void undoable_edit_can_be_undone() {
        given().a_user_has_made_an_undoable_edit();
        when().the_user_activates_the_undo_function();
        then().the_edit_should_be_undone();
    }
}
