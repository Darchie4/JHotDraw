package org.jhotdraw.undo.BDDTests;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import org.jhotdraw.undo.UndoRedoManager;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoableEdit;

public class RedoBDDTestStage extends Stage<RedoBDDTestStage> {

    @ExpectedScenarioState
    private UndoRedoManager undoRedoManager = new UndoRedoManager();

    public RedoBDDTestStage a_user_has_used_the_undo_feature() {
        // Simulate a user using the undo feature
        undoRedoManager.addEdit(generateUndoableEdit());
        undoRedoManager.setHasSignificantEdits(true);
        undoRedoManager.undo();
        assert undoRedoManager.canRedo() : true;
        assert undoRedoManager.hasSignificantEdits() : false;
        return self();
    }

    public RedoBDDTestStage the_user_activates_the_redo_function() {
        // User activates the redo function
        undoRedoManager.redo();
        return self();
    }

    public RedoBDDTestStage the_undone_edit_should_come_back() {
        // Assert that the redo operation is successful
        assert undoRedoManager.canUndo() : true;
        assert undoRedoManager.hasSignificantEdits() : true;
        return self();
    }

    private UndoableEdit generateUndoableEdit() {
        return new UndoableEdit() {
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
        };
    }

}
