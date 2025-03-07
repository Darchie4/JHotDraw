/*
 * @(#)SendToBackAction.java
 *
 * Copyright (c) 2003-2008 The authors and contributors of JHotDraw.
 * You may not use, copy or modify this file, except in compliance with the
 * accompanying license terms.
 */
package org.jhotdraw.draw.action.edit;

import org.jhotdraw.draw.action.AbstractSelectedAction;
import org.jhotdraw.draw.figure.Figure;
import java.util.*;
import javax.swing.undo.*;
import org.jhotdraw.draw.*;
import org.jhotdraw.util.ResourceBundleUtil;

/**
 * SendToBackAction.
 *
 * @author Werner Randelshofer
 * @version $Id$
 */
public class SendToBackAction extends AbstractSelectedAction {

    private static final long serialVersionUID = 1L;
    public static final String ID = "edit.sendToBack";

    /**
     * Creates a new instance.
     */
    public SendToBackAction(DrawingEditor editor) {
        super(editor);
        ResourceBundleUtil labels
                = ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
        labels.configureAction(this, ID);
        updateEnabledState();
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        final Drawing drawing = super.getDrawing();
        final LinkedList<Figure> figures = new LinkedList<>(super.getSelectedFigures());
        sendToBack(drawing, figures);
        fireUndoableEditHappened(new AbstractUndoableEdit() {
            private static final long serialVersionUID = 1L;

            @Override
            public String getPresentationName() {
                ResourceBundleUtil labels
                        = ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
                return labels.getTextProperty(ID);
            }

            @Override
            public void redo() throws CannotRedoException {
                super.redo();
                SendToBackAction.sendToBack(drawing, figures);
            }

            @Override
            public void undo() throws CannotUndoException {
                super.undo();
                BringToFrontAction.bringToFront(drawing, figures);
            }
        });
    }

    public static void sendToBack(Drawing drawing, Collection<Figure> figures) {
        for (Figure figure : figures) { // XXX Shouldn't the figures be sorted here back to front?
            drawing.sendToBack(figure);
        }
    }
}
