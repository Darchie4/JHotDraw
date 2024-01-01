package org.jhotdraw.undo.BDDTests;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import org.jhotdraw.undo.UndoRedoManager;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoableEdit;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UndoBDDTestStage extends Stage<UndoBDDTestStage> {

    @ExpectedScenarioState
    private UndoRedoManager undoRedoManager = new UndoRedoManager();

    @ProvidedScenarioState
    private boolean undoResult;

    public UndoBDDTestStage a_user_has_made_an_undoable_edit() {
        // Simulate a user making an undoable edit
        undoRedoManager.addEdit(new UndoableEdit() {
            private boolean canUndo = true;
            private boolean canRedo = false;

            @Override
            public void undo() throws CannotUndoException {
                canUndo = false;
                canRedo = true;
            }

            @Override
            public boolean canUndo() {
                return canUndo;
            }

            @Override
            public void redo() throws CannotRedoException {
                canRedo = false;
                canUndo = true;
            }

            @Override
            public boolean canRedo() {
                return canRedo;
            }

            @Override
            public void die() {

            }

            @Override
            public boolean addEdit(UndoableEdit anEdit) {
                return false;
            }

            @Override
            public boolean replaceEdit(UndoableEdit anEdit) {
                return false;
            }

            @Override
            public boolean isSignificant() {
                return true;
            }

            @Override
            public String getPresentationName() {
                return null;
            }

            @Override
            public String getUndoPresentationName() {
                return null;
            }

            @Override
            public String getRedoPresentationName() {
                return null;
            }
        });
        undoRedoManager.setHasSignificantEdits(true);

        assert undoRedoManager.hasSignificantEdits() : true;
        assert undoRedoManager.canUndo() : true;
        return self();
    }

    public UndoBDDTestStage the_user_activates_the_undo_function() {
        // User activates the undo function
        undoRedoManager.undo();
        return self();
    }

    public UndoBDDTestStage the_edit_should_be_undone() {
        // Assert that the undo operation is successful
        assertFalse(undoRedoManager.canUndo());
        assertTrue(undoRedoManager.canRedo());

        return self();
    }
}
