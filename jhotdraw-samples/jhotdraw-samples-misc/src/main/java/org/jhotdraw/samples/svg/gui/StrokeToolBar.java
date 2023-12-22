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
    private ResourceBundleUtil labels;

    /**
     * Creates new instance.
     */
    @FeatureEntryPoint(value = "strokeCreation")
    public StrokeToolBar() {
        labels = ResourceBundleUtil.getBundle("org.jhotdraw.samples.svg.Labels");
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


    private void configureJAttributeTextField(JAttributeTextField<?> textField, int columns, String toolTipKey) {
        textField.setColumns(columns);
        textField.setToolTipText(labels.getString(toolTipKey));
        textField.setHorizontalAlignment(JTextField.LEFT);
        textField.putClientProperty("Palette.Component.segmentPosition", "first");
        textField.setUI((PaletteFormattedTextFieldUI) PaletteFormattedTextFieldUI.createUI(textField));
    }

    private JPopupButton StrokeColor() {
        Map<AttributeKey<?>, Object> defaultAttributes = new HashMap<AttributeKey<?>, Object>();
        STROKE_GRADIENT.put(defaultAttributes, null);
        JPopupButton btn = ButtonFactory.createSelectionColorChooserButton(editor,
                STROKE_COLOR, "attribute.strokeColor", labels,
                defaultAttributes, new Rectangle(3, 3, 10, 10), PaletteColorChooserUI.class, disposables);
        btn.setUI((PaletteButtonUI) PaletteButtonUI.createUI(btn));
        btn.setAction(null, null);
        return btn;
    }

    private JPopupButton OpacitySlider() {

        return createButtonWithSlider(JPopupButton.VERTICAL, 0, 100, 100, "attribute.strokeOpacity", STROKE_OPACITY, 100d);
    }

    private JPopupButton CreateStrokeWithPopupSlider() {

        return createButtonWithSlider(JSlider.VERTICAL, 0, 100, 10, "attribute.strokeWidth", STROKE_WIDTH, 1d);
    }

    private JPopupButton createStrokeJoinButton() {
        JPopupButton btn = ButtonFactory.createStrokeJoinButton(editor, labels, disposables);
        btn.setUI((PaletteButtonUI) PaletteButtonUI.createUI(btn));
        return btn;
    }

    private JPopupButton createStrokeDashesButton() {
        JPopupButton btn = ButtonFactory.createStrokeDashesButton(editor, labels);
        btn.setUI((PaletteButtonUI) PaletteButtonUI.createUI(btn));
        return btn;
    }

    private JPopupButton createStrokeCapButton() {
        JPopupButton btn = ButtonFactory.createStrokeCapButton(editor, labels, disposables);
        btn.setUI((PaletteButtonUI) PaletteButtonUI.createUI(btn));
        return btn;
    }

    private JPopupButton createButtonWithSlider(int direction, int min, int max, int currentValue, String toolName, AttributeKey<Double> attribute, double scaleFactor) {

        JPopupButton btn = new JPopupButton();
        JAttributeSlider slider = new JAttributeSlider(
                direction, min, max, currentValue);
        slider.setUI((SliderUI) PaletteSliderUI.createUI(slider));
        btn.add(slider);
        labels.configureToolBarButton(btn, toolName);
        btn.setUI((PaletteButtonUI) PaletteButtonUI.createUI(btn));
        slider.setScaleFactor(scaleFactor);
        disposables.add(new FigureAttributeEditorHandler<Double>(attribute, slider, editor));
        return btn;
    }


    private void StrokeColorFieldAndButton(JPanel p) {
        HashMap<AttributeKey<?>, Object> defaultAttributes = new HashMap<AttributeKey<?>, Object>();
        STROKE_GRADIENT.put(defaultAttributes, null);
        JAttributeTextField<Color> colorField = new JAttributeTextField<Color>();
        configureJAttributeTextField(colorField, 7, "attribute.strokeColor.toolTopText");
        colorField.setFormatterFactory(ColorFormatter.createFormatterFactory(ColorFormatter.Format.RGB_INTEGER_SHORT, false, false));
        disposables.add(new FigureAttributeEditorHandler<Color>(STROKE_COLOR, defaultAttributes, colorField, editor, true));
        GridBagConstraints gbc = createGridBagConstraints(0, 0, 3, new Insets(3, 3, 0, 0), GridBagConstraints.HORIZONTAL, 0.0, 0.0);
        p.add(colorField, gbc);
        gbc = createGridBagConstraints(3, -1, 1, null);
        p.add(StrokeColor(), gbc);
    }

    private void OpacityFieldAndSlider(JPanel p) {
        JAttributeTextField<Double> opacityField = new JAttributeTextField<Double>();
        configureJAttributeTextField(opacityField, 4, "attribute.strokeOpacity.toolTopText");
        JavaNumberFormatter formatter = new JavaNumberFormatter(0d, 100d, 100d, false, "%");
        formatter.setUsesScientificNotation(false);
        formatter.setMaximumFractionDigits(1);
        opacityField.setFormatterFactory(new DefaultFormatterFactory(formatter));
        disposables.add(new FigureAttributeEditorHandler<Double>(STROKE_OPACITY, opacityField, editor));
        GridBagConstraints gbc = createGridBagConstraints(0, -1, 1, new Insets(3, 0, 0, 0), GridBagConstraints.HORIZONTAL, 0.0, 0.0);
        p.add(opacityField, gbc);
         JPopupButton opacityPopupButton = OpacitySlider();
        opacityPopupButton.setIcon(
                new SelectionOpacityIcon(editor, STROKE_OPACITY, null, STROKE_COLOR, Images.createImage(getClass(), labels.getString("attribute.strokeOpacity.largeIcon")),
                        new Rectangle(5, 5, 6, 6), new Rectangle(4, 4, 7, 7)));
        opacityPopupButton.setPopupAnchor(SOUTH_EAST);
        gbc = createGridBagConstraints(1, -1, 1, new Insets(3, 0, 0, 0), GridBagConstraints.HORIZONTAL, 0.0, 1f);
        p.add(opacityPopupButton, gbc);
    }

    private void StrokeWidthFieldAndPopupSlider(JPanel p) {
        JAttributeTextField<Double> strokeWidthField = new JAttributeTextField<Double>();
        configureJAttributeTextField(strokeWidthField, 2, "attribute.strokeWidth.toolTopText");
        JavaNumberFormatter formatter = new JavaNumberFormatter(0d, 100d, 1d);
        formatter.setUsesScientificNotation(false);
        formatter.setMaximumFractionDigits(1);
        strokeWidthField.setFormatterFactory(new DefaultFormatterFactory(formatter));
        disposables.add(new FigureAttributeEditorHandler<Double>(STROKE_WIDTH, strokeWidthField, editor));
        GridBagConstraints gbc = createGridBagConstraints(0, 2, 1, new Insets(3, 0, 0, 0), GridBagConstraints.BOTH, 0.0, 0.0);
        p.add(strokeWidthField, gbc);
        gbc = createGridBagConstraints(1, 2, 1, new Insets(3, 0, 0, 0));
        p.add(CreateStrokeWithPopupSlider(), gbc);
    }

    private void CreateDashField(JPanel p) {
        JAttributeTextField<Double> dashOffsetField = new JAttributeTextField<Double>();
        configureJAttributeTextField(dashOffsetField, 1, "attribute.strokeDashOffset.toolTopText");
        dashOffsetField.setFormatterFactory(JavaNumberFormatter.createFormatterFactory(-1000d, 1000d, 1d));
        disposables.add(new FigureAttributeEditorHandler<Double>(STROKE_DASH_PHASE, dashOffsetField, editor));
        GridBagConstraints gbc = createGridBagConstraints(2, 2, 2, new Insets(3, 3, 0, 0), GridBagConstraints.BOTH, 0.0, 0.0);
        p.add(dashOffsetField, gbc);
        gbc = createGridBagConstraints(4, 2, GridBagConstraints.REMAINDER, new Insets(3, 3, 0, 0));
        p.add(createStrokeDashesButton(), gbc);
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
        GridBagLayout layout = new GridBagLayout();
        p.setLayout(layout);

        switch (state) {
            case 1:
                p.add(StrokeColor(), createGridBagConstraints(0, -1, 1, null));

                JPopupButton OpacityPopupButtonWithSlider = OpacitySlider();
                p.add(OpacityPopupButtonWithSlider, createGridBagConstraints(0, -1, 1, new Insets(3, 0, 0, 0)));
                disposables.add(new SelectionComponentRepainter(editor, OpacityPopupButtonWithSlider));

                p.add(CreateStrokeWithPopupSlider(), createGridBagConstraints(0, -1, 1, new Insets(3, 0, 0, 0)));
                p.add(createStrokeJoinButton(), createGridBagConstraints(-1, 0, 1, new Insets(0, 3, 0, 0)));
                p.add(createStrokeCapButton(), createGridBagConstraints(-1, 1, 1, new Insets(3, 3, 0, 0)));
                p.add(createStrokeDashesButton(), createGridBagConstraints(-1, 2, GridBagConstraints.REMAINDER, new Insets(3, 3, 0, 0)));
                break;
            case 2:
                StrokeColorFieldAndButton(p);
                OpacityFieldAndSlider(p);
                StrokeWidthFieldAndPopupSlider(p);
                CreateDashField(p);
                p.add(createStrokeJoinButton(), createGridBagConstraints(4, 0, 2, new Insets(0, 3, 0, 0)));
                p.add(createStrokeCapButton(), createGridBagConstraints(4, 1, 1, new Insets(3, 3, 0, 0)));
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
