/*
 * @(#)StrokeToolBar.java
 *
 * Copyright (c) 2007-2008 The authors and contributors of JHotDraw.
 * You may not use, copy or modify this file, except in compliance with the
 * accompanying license terms.
 */
package org.jhotdraw.samples.svg.gui;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.SliderUI;

import static org.jhotdraw.draw.AttributeKeys.FONT_FACE;
import static org.jhotdraw.draw.AttributeKeys.FONT_SIZE;

import org.jhotdraw.draw.figure.CompositeFigure;
import org.jhotdraw.draw.DrawingEditor;
import org.jhotdraw.draw.figure.Figure;
import org.jhotdraw.draw.figure.TextHolderFigure;
import org.jhotdraw.draw.event.FigureAttributeEditorHandler;
import org.jhotdraw.draw.event.SelectionComponentDisplayer;
import org.jhotdraw.draw.event.SelectionComponentRepainter;
import org.jhotdraw.draw.gui.JAttributeSlider;
import org.jhotdraw.draw.gui.JAttributeTextField;
import org.jhotdraw.draw.tool.TextAreaCreationTool;
import org.jhotdraw.draw.tool.TextCreationTool;
import org.jhotdraw.gui.JFontChooser;
import org.jhotdraw.gui.JPopupButton;
import org.jhotdraw.gui.action.ButtonFactory;
import org.jhotdraw.gui.ConstraintsFactory;
import org.jhotdraw.gui.plaf.palette.PaletteButtonUI;
import org.jhotdraw.gui.plaf.palette.PaletteFormattedTextFieldUI;
import org.jhotdraw.gui.plaf.palette.PaletteSliderUI;
import org.jhotdraw.formatter.FontFormatter;
import org.jhotdraw.formatter.JavaNumberFormatter;
import org.jhotdraw.util.*;

/**
 * StrokeToolBar.
 *
 * @author Werner Randelshofer
 * @version $Id$
 */
public class FontToolBar extends AbstractToolBar {

    private static final long serialVersionUID = 1L;
    private SelectionComponentDisplayer displayer;

    /**
     * Creates new instance.
     */
    public FontToolBar() {
        ResourceBundleUtil labels = ResourceBundleUtil.getBundle("org.jhotdraw.samples.svg.Labels");
        setName(labels.getString("font.toolbar"));
        JFontChooser.loadAllFonts();
        setDisclosureStateCount(3);
    }

    @Override
    public void setEditor(DrawingEditor newValue) {
        if (displayer != null) {
            displayer.dispose();
            displayer = null;
        }
        super.setEditor(newValue);
        if (newValue != null) {
            displayer = new SelectionComponentDisplayer(editor, this) {
                @Override
                public void updateVisibility() {
                    boolean newValue = editor != null && editor.getActiveView() != null && (isVisibleIfCreationTool && ((editor.getTool() instanceof TextCreationTool) || editor.getTool() instanceof TextAreaCreationTool) || containsTextHolderFigure(editor.getActiveView().getSelectedFigures()));
                    JComponent component = getComponent();
                    if (component == null) {
                        dispose();
                        return;
                    }
                    component.setVisible(newValue);
                    // The following is needed to trick BoxLayout
                    if (newValue) {
                        component.setPreferredSize(null);
                    } else {
                        component.setPreferredSize(new Dimension(0, 0));
                    }
                    component.revalidate();
                }

                private boolean containsTextHolderFigure(Collection<Figure> figures) {
                    for (Figure f : figures) {
                        if (f instanceof TextHolderFigure) {
                            return true;
                        } else if (f instanceof CompositeFigure) {
                            if (containsTextHolderFigure(((CompositeFigure) f).getChildren())) {
                                return true;
                            }
                        }
                    }
                    return false;
                }
            };
        }
    }

    @Override
    protected JComponent createDisclosedComponent(int state) {
        if (state < 1 || state > 2) {
            return null;
        }

        JPanel panel = new JPanel();

        if (editor == null) {
            return panel;
        }

        boolean large = state == 2;

        // Base layout
        GridBagLayout layout = new GridBagLayout();
        panel.setLayout(layout);
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(5, 5, 5, 8));

        ResourceBundleUtil labels = ResourceBundleUtil.getBundle("org.jhotdraw.samples.svg.Labels");

        // Font face
        int fieldColumnSize = 2;
        int constraintsGrid = 2;

        if (large) {
            fieldColumnSize += 10;
            constraintsGrid += 1;
        }

        JAttributeTextField<Font> faceField = new JAttributeTextField<Font>();
        faceField.setColumns(fieldColumnSize);
        faceField.setToolTipText(labels.getString("attribute.font.toolTipText"));
        faceField.setHorizontalAlignment(JAttributeTextField.RIGHT);
        faceField.putClientProperty("Palette.Component.segmentPosition", "first");
        faceField.setUI((PaletteFormattedTextFieldUI) PaletteFormattedTextFieldUI.createUI(faceField));
        faceField.setHorizontalAlignment(JTextField.LEADING);
        faceField.setFormatterFactory(FontFormatter.createFormatterFactory());
        disposables.add(new FigureAttributeEditorHandler<Font>(FONT_FACE, faceField, editor));
        panel.add(faceField, ConstraintsFactory.createGridConstraints(0, 0, GridBagConstraints.FIRST_LINE_START, new Insets(0, 0, 0, 0), constraintsGrid, GridBagConstraints.HORIZONTAL));

        AbstractButton btn = ButtonFactory.createFontButton(editor, FONT_FACE, labels, disposables);
        btn.setUI((PaletteButtonUI) PaletteButtonUI.createUI(btn));

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.WEST;
        panel.add(btn, constraints);

        // Font size field with slider
        JPanel subPanel = new JPanel(new GridBagLayout());
        subPanel.setOpaque(false);

        JAttributeTextField<Double> sizeField = new JAttributeTextField<Double>();
        sizeField.setColumns(1);
        sizeField.setToolTipText(labels.getString("attribute.fontSize.toolTipText"));
        sizeField.setHorizontalAlignment(JAttributeTextField.RIGHT);
        sizeField.setUI((PaletteFormattedTextFieldUI) PaletteFormattedTextFieldUI.createUI(sizeField));
        sizeField.setFormatterFactory(JavaNumberFormatter.createFormatterFactory(0d, 1000d, 1d));
        sizeField.setHorizontalAlignment(JTextField.LEADING);
        disposables.add(new FigureAttributeEditorHandler<Double>(FONT_SIZE, sizeField, editor));
        constraints = ConstraintsFactory.createGridConstraints(0, 1, GridBagConstraints.FIRST_LINE_START, new Insets(3, 0, 0, 0), 2, GridBagConstraints.HORIZONTAL);
        constraints.weightx = 1f;
        subPanel.add(sizeField, constraints);

        JAttributeSlider sizeSlider = new JAttributeSlider(JSlider.VERTICAL, 0, 100, 12);
        sizeSlider.setUI((SliderUI) PaletteSliderUI.createUI(sizeSlider));
        sizeSlider.setScaleFactor(1d);
        disposables.add(new FigureAttributeEditorHandler<Double>(FONT_SIZE, sizeSlider, editor));

        JPopupButton sizePopupButton = new JPopupButton();
        sizePopupButton.add(sizeSlider);
        labels.configureToolBarButton(sizePopupButton, "attribute.fontSize");
        sizePopupButton.setUI((PaletteButtonUI) PaletteButtonUI.createUI(sizePopupButton));
        sizePopupButton.setPopupAnchor(SOUTH_EAST);
        disposables.add(new SelectionComponentRepainter(editor, sizePopupButton));
        subPanel.add(sizePopupButton, ConstraintsFactory.createGridConstraints(2, 1, GridBagConstraints.FIRST_LINE_START, new Insets(3, 0, 0, 0)));

        panel.add(subPanel, ConstraintsFactory.createGridConstraints(0, 1, GridBagConstraints.FIRST_LINE_START, new Insets(2, 0, 0, 0), 2, GridBagConstraints.BOTH));

        // Font style buttons
        addStyleButton(panel, ButtonFactory.createFontStyleBoldButton(editor, labels, disposables));
        addStyleButton(panel, ButtonFactory.createFontStyleItalicButton(editor, labels, disposables));
        addStyleButton(panel, ButtonFactory.createFontStyleUnderlineButton(editor, labels, disposables));

        return panel;
    }

    /**
     * Adds a style button to the panel, on the row two.
     *
     * @param panel  The panel to add the button to.
     * @param button The button to add.
     */
    private void addStyleButton(JPanel panel, AbstractButton button) {
        button.setUI((PaletteButtonUI) PaletteButtonUI.createUI(button));
        panel.add(button, ConstraintsFactory.createGridConstraints(GridBagConstraints.RELATIVE, 2, GridBagConstraints.WEST, new Insets(3, 0, 0, 0)));
    }

    @Override
    protected String getID() {
        return "font";
    }

    @Override
    protected int getDefaultDisclosureState() {
        return 1;
    }

    /**
     * This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
