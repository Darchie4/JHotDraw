/*
 * @(#)BringToFrontAction.java
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
import dk.sdu.mmmi.featuretracer.lib.FeatureEntryPoint;

/**
 * ToFrontAction.
 *
 * @author Werner Randelshofer
 * @version $Id$
 */

public class BringToFrontAction extends AbstractSelectedAction{

    private static final long serialVersionUID = 1L;
    public static final String ID = "edit.bringToFront";
    private static final String BASENAME = "org.jhotdraw.draw.Labels";

    public BringToFrontAction(DrawingEditor editor) {
        super(editor);
        ResourceBundleUtil
                .getBundle(BASENAME)
                .configureAction(this, ID);
        super.updateEnabledState();
    }

    public void actionPerformed(java.awt.event.ActionEvent e) {
        final DrawingView view = super.getView();
        final LinkedList<Figure> figures = new LinkedList<>(view.getSelectedFigures());
        bringToFront(super.getDrawing(), figures);
        super.fireUndoableEditHappened(this.getUndoableEdit(super.getDrawing(), figures));
    }

    @FeatureEntryPoint("bringToFront feature")
    public static void bringToFront(Drawing drawing, Collection<Figure> figures) {
        drawing.sort(figures).forEach(drawing::bringToFront);
    }

    private AbstractUndoableEdit getUndoableEdit(Drawing drawing, Collection<Figure> figures) {
        ResourceBundleUtil labels
                = ResourceBundleUtil.getBundle(BASENAME);
        return new AbstractUndoableEdit() {

            @Override
            public String getPresentationName() {
                return labels.getTextProperty(ID);
            }

            @Override
            public void redo() throws CannotRedoException {
                super.redo();
                BringToFrontAction.bringToFront(drawing, figures);
            }

            @Override
            public void undo() throws CannotUndoException {
                super.undo();
                SendToBackAction.sendToBack(drawing, figures);
            }
        };
    }
}