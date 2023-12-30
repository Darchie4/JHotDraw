/*
 * @(#)UndoRedoManager.java
 *
 * Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
 * You may not use, copy or modify this file, except in compliance with the
 * accompanying license terms.
 */
package org.jhotdraw.undo;

import java.beans.*;
import java.util.*;
import javax.swing.*;
import javax.swing.undo.*;

import org.jhotdraw.undo.actions.RedoAction;
import org.jhotdraw.undo.actions.UndoAction;
import org.jhotdraw.util.*;

/**
 * Same as javax.swing.UndoManager but provides actions for undo and
 * redo operations.
 *
 * @author Werner Randelshofer
 * @version $Id$
 */
public class UndoRedoManager extends UndoManager {

    private static final long serialVersionUID = 1L;
    protected PropertyChangeSupport propertySupport = new PropertyChangeSupport(this);
    private static final boolean DEBUG = true;
    private static final String UNDO_LABEL_TEXT_PATH = "edit.undo.text";
    private static final String REDO_LABEL_TEXT_PATH = "edit.redo.text";
    /**
     * The resource bundle used for internationalisation.
     */
    private static ResourceBundleUtil labels;
    /**
     * This flag is set to true when at
     * least one significant UndoableEdit
     * has been added to the manager since the
     * last call to discardAllEdits.
     */
    private boolean hasSignificantEdits = false;
    /**
     * This flag is set to true when an undo or redo
     * operation is in progress. The UndoRedoManager
     * ignores all incoming UndoableEdit events while
     * this flag is true.
     */
    private boolean undoOrRedoInProgress;
    /**
     * Sending this UndoableEdit event to the UndoRedoManager
     * disables the Undo and Redo functions of the manager.
     */
    public static final UndoableEdit DISCARD_ALL_EDITS = new AbstractUndoableEdit() {
        private static final long serialVersionUID = 1L;

        @Override
        public boolean canUndo() {
            return false;
        }

        @Override
        public boolean canRedo() {
            return false;
        }
    };

    /**
     * The undo action instance.
     */
    private UndoAction undoAction;
    /**
     * The redo action instance.
     */
    private RedoAction redoAction;

    public static ResourceBundleUtil getLabels() {
        if (labels == null) {
            labels = ResourceBundleUtil.getBundle("org.jhotdraw.undo.Labels");
        }
        return labels;
    }

    /**
     * Creates new UndoRedoManager
     */
    public UndoRedoManager() {
        getLabels();
        undoAction = new UndoAction(this);
        redoAction = new RedoAction(this);
    }

    public static void setLocale(Locale l) {
        labels = ResourceBundleUtil.getBundle("org.jhotdraw.undo.Labels", l);
    }

    /**
     * Discards all edits.
     */
    @Override
    public synchronized void discardAllEdits() {
        super.discardAllEdits();
        updateActions();
        setHasSignificantEdits(false);
    }

    public void setHasSignificantEdits(boolean newValue) {
        boolean oldValue = hasSignificantEdits;
        hasSignificantEdits = newValue;
        firePropertyChange("hasSignificantEdits", oldValue, newValue);
    }

    /**
     * Returns true if at least one significant UndoableEdit
     * has been added since the last call to discardAllEdits.
     */
    public boolean hasSignificantEdits() {
        return hasSignificantEdits;
    }

    /**
     * If inProgress, inserts anEdit at indexOfNextAdd, and removes
     * any old edits that were at indexOfNextAdd or later. The die
     * method is called on each edit that is removed is sent, in the
     * reverse of the order the edits were added. Updates
     * indexOfNextAdd.
     *
     * <p>
     * If not inProgress, acts as a CompoundEdit</p>
     *
     * <p>
     * Regardless of inProgress, if undoOrRedoInProgress,
     * calls die on each edit that is sent.</p>
     *
     * @see CompoundEdit#end
     * @see CompoundEdit#addEdit
     */
    @Override
    public synchronized boolean addEdit(UndoableEdit anEdit) {
        printIfInDebugMode("UndoRedoManager@" + hashCode() + ".add " + anEdit);
        if (undoOrRedoInProgress) {
            anEdit.die();
            return true;
        }
        boolean success = super.addEdit(anEdit);
        updateActions();
        if (success && anEdit.isSignificant() && editToBeUndone() == anEdit) {
            setHasSignificantEdits(true);
        }
        return success;
    }

    /**
     * Gets the undo action for use as an Undo menu item.
     */
    public Action getUndoAction() {
        return undoAction;
    }

    /**
     * Gets the redo action for use as a Redo menu item.
     */
    public Action getRedoAction() {
        return redoAction;
    }

    /**
     * Updates the properties of the UndoAction
     * and of the RedoAction.
     */
    private void updateActions() {
        printIfInDebugMode("UndoRedoManager@" + hashCode() + ".updateActions " + editToBeUndone() + " canUndo=" + canUndo() + " canRedo=" + canRedo());
        updateUndoAction();
        updateRedoAction();
    }
    private void updateUndoAction() {
        String label;
        if (canUndo()) {
            undoAction.setEnabled(true);
            label = getUndoPresentationName();
        } else {
            undoAction.setEnabled(false);
            label = labels.getString(UNDO_LABEL_TEXT_PATH);
        }
        setActionNameAndShortDescription(undoAction, label);
    }

    private void updateRedoAction() {
        String label;
        if (canRedo()) {
            redoAction.setEnabled(true);
            label = getRedoPresentationName();
        } else {
            redoAction.setEnabled(false);
            label = labels.getString(REDO_LABEL_TEXT_PATH);
        }
        setActionNameAndShortDescription(redoAction, label);
    }

    private void setActionNameAndShortDescription(AbstractAction action, String label) {
        action.putValue(Action.NAME, label);
        action.putValue(Action.SHORT_DESCRIPTION, label);
    }
    /**
     * Undoes the last edit event.
     * The UndoRedoManager ignores all incoming UndoableEdit events,
     * while undo is in progress.
     */
    @Override
    public void undo() throws CannotUndoException {
        undoOrRedoInProgress = true;
        try {
            super.undo();
        } finally {
            undoOrRedoInProgress = false;
            updateActions();
        }
    }

    /**
     * Redoes the last undone edit event.
     * The UndoRedoManager ignores all incoming UndoableEdit events,
     * while redo is in progress.
     */
    @Override
    public void redo() throws CannotUndoException {
        undoOrRedoInProgress = true;
        try {
            super.redo();
        } finally {
            undoOrRedoInProgress = false;
            updateActions();
        }
    }

    /**
     * Undoes or redoes the last edit event.
     * The UndoRedoManager ignores all incoming UndoableEdit events,
     * while undo or redo is in progress.
     */
    @Override
    public void undoOrRedo() throws CannotUndoException, CannotRedoException {
        undoOrRedoInProgress = true;
        try {
            super.undoOrRedo();
        } finally {
            undoOrRedoInProgress = false;
            updateActions();
        }
    }

    private void printIfInDebugMode(String debugStatement) {
        if (DEBUG) {
            System.out.println(debugStatement);
        }
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.addPropertyChangeListener(listener);
    }

    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        propertySupport.addPropertyChangeListener(propertyName, listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.removePropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        propertySupport.removePropertyChangeListener(propertyName, listener);
    }

    protected void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) {
        propertySupport.firePropertyChange(propertyName, oldValue, newValue);
    }

    protected void firePropertyChange(String propertyName, int oldValue, int newValue) {
        propertySupport.firePropertyChange(propertyName, oldValue, newValue);
    }

    protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        propertySupport.firePropertyChange(propertyName, oldValue, newValue);
    }
}
