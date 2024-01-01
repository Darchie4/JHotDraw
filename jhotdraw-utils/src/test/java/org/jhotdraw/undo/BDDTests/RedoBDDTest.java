package org.jhotdraw.undo.BDDTests;

import com.tngtech.jgiven.junit.SimpleScenarioTest;
import org.junit.Test;

public class RedoBDDTest extends SimpleScenarioTest<RedoBDDTestStage> {

    @Test
    public void undid_edits_can_be_redone() {
        given().a_user_has_used_the_undo_feature();
        when().the_user_activates_the_redo_function();
        then().the_undone_edit_should_come_back();
    }
}
