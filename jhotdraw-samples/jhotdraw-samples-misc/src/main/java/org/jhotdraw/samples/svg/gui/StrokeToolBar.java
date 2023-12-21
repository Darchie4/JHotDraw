/*
 * @(#)StrokeToolBar.java
 *
 * Copyright (c) 2007-2008 The authors and contributors of JHotDraw.
 * You may not use, copy or modify this file, except in compliance with the
 * accompanying license terms.
 */
package org.jhotdraw.samples.svg.gui;

import dk.sdu.mmmi.featuretracer.lib.FeatureEntryPoint;
import org.jhotdraw.gui.action.ButtonFactory;
import org.jhotdraw.gui.plaf.palette.PaletteFormattedTextFieldUI;
import org.jhotdraw.gui.plaf.palette.PaletteButtonUI;
import org.jhotdraw.gui.plaf.palette.PaletteSliderUI;
import org.jhotdraw.gui.plaf.palette.PaletteColorChooserUI;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.SliderUI;
import javax.swing.text.DefaultFormatterFactory;

import org.jhotdraw.draw.AttributeKey;
import org.jhotdraw.draw.DrawingEditor;
import org.jhotdraw.draw.event.FigureAttributeEditorHandler;
import org.jhotdraw.draw.event.SelectionComponentDisplayer;
import org.jhotdraw.draw.event.SelectionComponentRepainter;
import org.jhotdraw.draw.gui.JAttributeSlider;
import org.jhotdraw.draw.gui.JAttributeTextField;
import org.jhotdraw.gui.JPopupButton;

import static org.jhotdraw.samples.svg.SVGAttributeKeys.*;

import org.jhotdraw.text.ColorFormatter;
import org.jhotdraw.formatter.JavaNumberFormatter;
import org.jhotdraw.util.*;

/**
 * StrokeToolBar.
 *
 * @author Werner Randelshofer
 * @version $Id$
 */
public class StrokeToolBar extends AbstractToolBar {

    private static final long serialVersionUID = 1L;
    private SelectionComponentDisplayer displayer;

    /**
     * Creates new instance.
     */
    @FeatureEntryPoint(value = "strokeCreation")
    public StrokeToolBar() {
        ResourceBundleUtil labels = ResourceBundleUtil.getBundle("org.jhotdraw.samples.svg.Labels");
        setName(labels.getString("stroke.toolbar"));
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
            displayer = new SelectionComponentDisplayer(editor, this);
        }
    }

    private GridBagConstraints createGridBagConstraints(int gridx, int gridy, int gridwidth, Insets insets) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = gridx;
        gbc.gridy = gridy;
        gbc.gridwidth = gridwidth;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.insets = insets == null ? new Insets(0, 0, 0, 0) : insets;
        return gbc;
    }

    private GridBagConstraints createGridBagConstraints(int gridx, int gridy, int gridwidth, Insets insets, int fill, double weightx, double weighty) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = gridx;
        gbc.gridy = gridy;
        gbc.gridwidth = gridwidth;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.fill = fill;
        gbc.weightx = weightx;
        gbc.weighty = weighty;
        gbc.insets = (insets != null) ? insets : new Insets(0, 0, 0, 0);
        return gbc;
    }

    private void StrokeColor(JPanel p, ResourceBundleUtil labels) {
        Map<AttributeKey<?>, Object> defaultAttributes = new HashMap<AttributeKey<?>, Object>();
        STROKE_GRADIENT.put(defaultAttributes, null);
        AbstractButton btn = ButtonFactory.createSelectionColorChooserButton(editor,
                STROKE_COLOR, "attribute.strokeColor", labels,
                defaultAttributes, new Rectangle(3, 3, 10, 10), PaletteColorChooserUI.class, disposables);
        btn.setUI((PaletteButtonUI) PaletteButtonUI.createUI(btn));
        ((JPopupButton) btn).setAction(null, null);
        GridBagConstraints gbc = createGridBagConstraints(0, -1, 1, null);
        p.add(btn, gbc);
    }

    private void OpacitySlider(JPanel p, ResourceBundleUtil labels) {

        JPopupButton opacityPopupButton = new JPopupButton();
        JAttributeSlider opacitySlider = new JAttributeSlider(JSlider.VERTICAL, 0, 100, 100);
        opacityPopupButton.add(opacitySlider);
        labels.configureToolBarButton(opacityPopupButton, "attribute.strokeOpacity");
        opacityPopupButton.setUI((PaletteButtonUI) PaletteButtonUI.createUI(opacityPopupButton));
        opacityPopupButton.setIcon(
                new SelectionOpacityIcon(editor, STROKE_OPACITY, null, STROKE_COLOR, Images.createImage(getClass(), labels.getString("attribute.strokeOpacity.largeIcon")),
                        new Rectangle(5, 5, 6, 6), new Rectangle(4, 4, 7, 7)));
        opacityPopupButton.setPopupAnchor(SOUTH_EAST);
        disposables.add(new SelectionComponentRepainter(editor, opacityPopupButton));
        GridBagConstraints gbc = createGridBagConstraints(0, -1, 1, new Insets(3, 0, 0, 0));
        p.add(opacityPopupButton, gbc);
        opacitySlider.setUI((SliderUI) PaletteSliderUI.createUI(opacitySlider));
        opacitySlider.setScaleFactor(100d);
        disposables.add(new FigureAttributeEditorHandler<Double>(STROKE_OPACITY, opacitySlider, editor));
    }

    private void CreateStrokeWithPopupSlider(JPanel p, ResourceBundleUtil labels) {
        JPopupButton strokeWidthPopupButton = new JPopupButton();
        JAttributeSlider strokeWidthSlider = new JAttributeSlider(
                JSlider.VERTICAL, 0, 50, 1);
        strokeWidthSlider.setUI((SliderUI) PaletteSliderUI.createUI(strokeWidthSlider));
        strokeWidthPopupButton.add(strokeWidthSlider);
        labels.configureToolBarButton(strokeWidthPopupButton, "attribute.strokeWidth");
        strokeWidthPopupButton.setUI((PaletteButtonUI) PaletteButtonUI.createUI(strokeWidthPopupButton));
        GridBagConstraints gbc = createGridBagConstraints(0, -1, 1, new Insets(3, 0, 0, 0));
        p.add(strokeWidthPopupButton, gbc);
        disposables.add(new FigureAttributeEditorHandler<Double>(STROKE_WIDTH, strokeWidthSlider, editor));
    }

    private void createStrokeDashesButtons(JPanel p, ResourceBundleUtil labels) {
        AbstractButton btn = ButtonFactory.createStrokeJoinButton(editor, labels, disposables);
        btn.setUI((PaletteButtonUI) PaletteButtonUI.createUI(btn));
        GridBagConstraints gbc = createGridBagConstraints(-1, 0, 1, new Insets(0, 3, 0, 0));
        p.add(btn, gbc);
        btn = ButtonFactory.createStrokeCapButton(editor, labels, disposables);
        btn.setUI((PaletteButtonUI) PaletteButtonUI.createUI(btn));
        gbc = createGridBagConstraints(-1, 1, 1, new Insets(3, 3, 0, 0));
        p.add(btn, gbc);
        btn = ButtonFactory.createStrokeDashesButton(editor, new double[][]{
                null,
                {4d, 4d},
                {2d, 2d},
                {4d, 2d},
                {2d, 4d},
                {8d, 2d},
                {6d, 2d, 2d, 2d}}, labels, disposables);
        btn.setUI((PaletteButtonUI) PaletteButtonUI.createUI(btn));
        gbc = createGridBagConstraints(-1, 2, GridBagConstraints.REMAINDER, new Insets(3, 3, 0, 0));
        p.add(btn, gbc);

    }

    private void StrokeColorFieldAndButton(JPanel p, ResourceBundleUtil labels) {
        HashMap<AttributeKey<?>, Object> defaultAttributes = new HashMap<AttributeKey<?>, Object>();
        STROKE_GRADIENT.put(defaultAttributes, null);
        JAttributeTextField<Color> colorField = new JAttributeTextField<Color>();
        colorField.setColumns(7);
        colorField.setToolTipText(labels.getString("attribute.strokeColor.toolTipText"));
        colorField.putClientProperty("Palette.Component.segmentPosition", "first");
        colorField.setUI((PaletteFormattedTextFieldUI) PaletteFormattedTextFieldUI.createUI(colorField));
        colorField.setFormatterFactory(ColorFormatter.createFormatterFactory(ColorFormatter.Format.RGB_INTEGER_SHORT, false, false));
        colorField.setHorizontalAlignment(JTextField.LEFT);
        disposables.add(new FigureAttributeEditorHandler<Color>(STROKE_COLOR, defaultAttributes, colorField, editor, true));
        GridBagConstraints gbc = createGridBagConstraints(0, 0, 3, new Insets(3, 3, 0, 0), GridBagConstraints.HORIZONTAL, 0.0, 0.0);
        p.add(colorField, gbc);
        AbstractButton btn = ButtonFactory.createSelectionColorChooserButton(editor,
                STROKE_COLOR, "attribute.strokeColor", labels,
                defaultAttributes, new Rectangle(3, 3, 10, 10), PaletteColorChooserUI.class, disposables);
        btn.setUI((PaletteButtonUI) PaletteButtonUI.createUI(btn));
        ((JPopupButton) btn).setAction(null, null);
        gbc = createGridBagConstraints(3, -1, 1, null);
        p.add(btn, gbc);
    }

    private void OpacityFieldAndSlider(JPanel p, ResourceBundleUtil labels) {
        JAttributeTextField<Double> opacityField = new JAttributeTextField<Double>();
        opacityField.setColumns(4);
        opacityField.setToolTipText(labels.getString("attribute.strokeOpacity.toolTipText"));
        opacityField.setHorizontalAlignment(JAttributeTextField.RIGHT);
        opacityField.putClientProperty("Palette.Component.segmentPosition", "first");
        opacityField.setUI((PaletteFormattedTextFieldUI) PaletteFormattedTextFieldUI.createUI(opacityField));
        opacityField.setHorizontalAlignment(JTextField.LEFT);
        JavaNumberFormatter formatter = new JavaNumberFormatter(0d, 100d, 100d, false, "%");
        formatter.setUsesScientificNotation(false);
        formatter.setMaximumFractionDigits(1);
        opacityField.setFormatterFactory(new DefaultFormatterFactory(formatter));
        disposables.add(new FigureAttributeEditorHandler<Double>(STROKE_OPACITY, opacityField, editor));
        GridBagConstraints gbc = createGridBagConstraints(0, -1, 1, new Insets(3, 0, 0, 0), GridBagConstraints.HORIZONTAL, 0.0, 0.0);
        p.add(opacityField, gbc);
        JPopupButton opacityPopupButton = new JPopupButton();
        JAttributeSlider opacitySlider = new JAttributeSlider(JSlider.VERTICAL, 0, 100, 100);
        opacityPopupButton.add(opacitySlider);
        labels.configureToolBarButton(opacityPopupButton, "attribute.strokeOpacity");
        opacityPopupButton.setUI((PaletteButtonUI) PaletteButtonUI.createUI(opacityPopupButton));
        opacityPopupButton.setIcon(
                new SelectionOpacityIcon(editor, STROKE_OPACITY, null, STROKE_COLOR, Images.createImage(getClass(), labels.getString("attribute.strokeOpacity.largeIcon")),
                        new Rectangle(5, 5, 6, 6), new Rectangle(4, 4, 7, 7)));
        opacityPopupButton.setPopupAnchor(SOUTH_EAST);
        disposables.add(new SelectionComponentRepainter(editor, opacityPopupButton));
        gbc = createGridBagConstraints(1, -1, 1, new Insets(3, 0, 0, 0), GridBagConstraints.HORIZONTAL, 0.0, 1f);
        p.add(opacityPopupButton, gbc);
        opacitySlider.setUI((SliderUI) PaletteSliderUI.createUI(opacitySlider));
        opacitySlider.setScaleFactor(100d);
        disposables.add(new FigureAttributeEditorHandler<Double>(STROKE_OPACITY, opacitySlider, editor));
    }

    private void StrokeWidthFieldAndPopupSlider(JPanel p, ResourceBundleUtil labels) {
        JAttributeTextField<Double> strokeWidthField = new JAttributeTextField<Double>();
        strokeWidthField.setColumns(2);
        strokeWidthField.setToolTipText(labels.getString("attribute.strokeWidth.toolTipText"));
        strokeWidthField.setHorizontalAlignment(JAttributeTextField.LEFT);
        strokeWidthField.putClientProperty("Palette.Component.segmentPosition", "first");
        strokeWidthField.setUI((PaletteFormattedTextFieldUI) PaletteFormattedTextFieldUI.createUI(strokeWidthField));
        JavaNumberFormatter formatter = new JavaNumberFormatter(0d, 100d, 1d);
        formatter.setUsesScientificNotation(false);
        formatter.setMaximumFractionDigits(1);
        strokeWidthField.setFormatterFactory(new DefaultFormatterFactory(formatter));
        disposables.add(new FigureAttributeEditorHandler<Double>(STROKE_WIDTH, strokeWidthField, editor));
        GridBagConstraints gbc = createGridBagConstraints(0, 2, 1, new Insets(3, 0, 0, 0), GridBagConstraints.BOTH, 0.0, 0.0);
        p.add(strokeWidthField, gbc);
        JPopupButton strokeWidthPopupButton = new JPopupButton();
        JAttributeSlider strokeWidthSlider = new JAttributeSlider(
                JSlider.VERTICAL, 0, 50, 1);
        strokeWidthSlider.setUI((SliderUI) PaletteSliderUI.createUI(strokeWidthSlider));
        strokeWidthPopupButton.add(strokeWidthSlider);
        labels.configureToolBarButton(strokeWidthPopupButton, "attribute.strokeWidth");
        strokeWidthPopupButton.setUI((PaletteButtonUI) PaletteButtonUI.createUI(strokeWidthPopupButton));
        gbc = createGridBagConstraints(1, 2, 1, new Insets(3, 0, 0, 0));
        p.add(strokeWidthPopupButton, gbc);
        disposables.add(new FigureAttributeEditorHandler<Double>(STROKE_WIDTH, strokeWidthSlider, editor));
        AbstractButton btn = ButtonFactory.createStrokeJoinButton(editor, labels, disposables);
        btn.setUI((PaletteButtonUI) PaletteButtonUI.createUI(btn));
        gbc = createGridBagConstraints(4, 0, 2, new Insets(0, 3, 0, 0));
        p.add(btn, gbc);
        btn = ButtonFactory.createStrokeCapButton(editor, labels, disposables);
        btn.setUI((PaletteButtonUI) PaletteButtonUI.createUI(btn));
        gbc = createGridBagConstraints(4, 1, 1, new Insets(3, 3, 0, 0));
        p.add(btn, gbc);
    }

    private void CreateDashField(JPanel p, ResourceBundleUtil labels) {
        JAttributeTextField<Double> dashOffsetField = new JAttributeTextField<Double>();
        dashOffsetField.setColumns(1);
        dashOffsetField.setToolTipText(labels.getString("attribute.strokeDashPhase.toolTipText"));
        dashOffsetField.setHorizontalAlignment(JAttributeTextField.LEFT);
        //dashOffsetField.putClientProperty("Palette.Component.segmentPosition", "first");
        dashOffsetField.setUI((PaletteFormattedTextFieldUI) PaletteFormattedTextFieldUI.createUI(dashOffsetField));
        dashOffsetField.setFormatterFactory(JavaNumberFormatter.createFormatterFactory(-1000d, 1000d, 1d));
        disposables.add(new FigureAttributeEditorHandler<Double>(STROKE_DASH_PHASE, dashOffsetField, editor));
        GridBagConstraints gbc = createGridBagConstraints(2, 2, 2, new Insets(3, 3, 0, 0), GridBagConstraints.BOTH, 0.0, 0.0);
        p.add(dashOffsetField, gbc);
        AbstractButton btn = ButtonFactory.createStrokeDashesButton(editor, new double[][]{
                null,
                {4d, 4d},
                {2d, 2d},
                {4d, 2d},
                {2d, 4d},
                {8d, 2d},
                {6d, 2d, 2d, 2d}}, labels, disposables);

        btn.setUI((PaletteButtonUI) PaletteButtonUI.createUI(btn));
        gbc = createGridBagConstraints(4, 2, GridBagConstraints.REMAINDER, new Insets(3, 3, 0, 0));
        p.add(btn, gbc);
    }

    @FeatureEntryPoint(value = "stroketool")
    @Override
    protected JComponent createDisclosedComponent(int state) {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setBorder(new EmptyBorder(5, 5, 5, 8));
        // Abort if no editor is put
        if (editor == null) {
            return p;
        }

        ResourceBundleUtil labels = ResourceBundleUtil.getBundle("org.jhotdraw.samples.svg.Labels");
        GridBagLayout layout = new GridBagLayout();
        p.setLayout(layout);
        GridBagConstraints gbc = null;
        AbstractButton btn = null;

        switch (state) {
            case 1:
                StrokeColor(p, labels);
                OpacitySlider(p, labels);
                CreateStrokeWithPopupSlider(p, labels);
                createStrokeDashesButtons(p, labels);
                break;
            case 2:
                StrokeColorFieldAndButton(p, labels);
                OpacityFieldAndSlider(p, labels);
                StrokeWidthFieldAndPopupSlider(p, labels);
                CreateDashField(p, labels);
                break;
        }
        return p;
    }

    @Override
    protected String getID() {
        return "stroke";
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
        setOpaque(false);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
