/*
 * @(#)StrokeToolBar.java
 *
 * Copyright (c) 2007-2008 The authors and contributors of JHotDraw.
 * You may not use, copy or modify this file, except in compliance with the
 * accompanying license terms.
 */
package org.jhotdraw.samples.svg.gui;

//import dk.sdu.mmmi.featuretracer.lib.FeatureEntryPoint;

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
    private  SelectionComponentDisplayer displayer;
    private final ResourceBundleUtil labels;

    /**
     * Creates new instance.
     */
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

    private void addNumberFormatterToTextField(JAttributeTextField<Double> component, double min, double max, double mulitiplier, boolean allowNull, String unit) {
        JavaNumberFormatter formatter = new JavaNumberFormatter(min, max, mulitiplier, allowNull, unit);
        formatter.setUsesScientificNotation(false);
        formatter.setMaximumFractionDigits(1);
        component.setFormatterFactory(new DefaultFormatterFactory(formatter));
    }

    private void configureJAttributeTextField(JAttributeTextField<?> textField, int columns, String toolTipKey) {
        textField.setColumns(columns);
        textField.setToolTipText(labels.getString(toolTipKey));
        textField.setHorizontalAlignment(JTextField.LEFT);
        textField.putClientProperty("Palette.Component.segmentPosition", "first");
        textField.setUI((PaletteFormattedTextFieldUI) PaletteFormattedTextFieldUI.createUI(textField));
    }

    private JPopupButton createStrokeColor() {
        Map<AttributeKey<?>, Object> defaultAttributes = new HashMap<>();
        STROKE_GRADIENT.put(defaultAttributes, null);
        JPopupButton btn = ButtonFactory.createSelectionColorChooserButton(editor,
                STROKE_COLOR, "attribute.strokeColor", labels,
                defaultAttributes, new Rectangle(3, 3, 10, 10), PaletteColorChooserUI.class, disposables);
        btn.setUI((PaletteButtonUI) PaletteButtonUI.createUI(btn));
        btn.setAction(null, null);
        return btn;
    }

    private JPopupButton createOpacitySlider() {
        JPopupButton opacityPopupButtonWithSlider = new JPopupButton();
        disposables.add(new SelectionComponentRepainter(editor, opacityPopupButtonWithSlider));
        return createButtonWithSlider(JPopupButton.VERTICAL, 0, 100, 100, "attribute.strokeOpacity", STROKE_OPACITY, 100d);
    }

    private JPopupButton createStrokeWidthPopupSlider() {
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


    private JAttributeTextField<Color> strokeColorField() {
        HashMap<AttributeKey<?>, Object> defaultAttributes = new HashMap<>();
        JAttributeTextField<Color> colorField = new JAttributeTextField<>();
        configureJAttributeTextField(colorField, 7, "attribute.strokeColor.toolTopText");
        colorField.setFormatterFactory(ColorFormatter.createFormatterFactory(ColorFormatter.Format.RGB_INTEGER_SHORT, false, false));
        disposables.add(new FigureAttributeEditorHandler<Color>(STROKE_COLOR, defaultAttributes, colorField, editor, true));
        return colorField;
    }

    private JAttributeTextField<Double> createOpacityField() {
        JAttributeTextField<Double> opacityField = new JAttributeTextField<>();
        configureJAttributeTextField(opacityField, 4, "attribute.strokeOpacity.toolTopText");
        addNumberFormatterToTextField(opacityField, 0d, 100d, 100d, false, "%");
        disposables.add(new FigureAttributeEditorHandler<Double>(STROKE_OPACITY, opacityField, editor));
        return opacityField;
    }

    private JAttributeTextField<Double> createStrokeWidthField() {
        JAttributeTextField<Double> strokeWidthField = new JAttributeTextField<>();
        configureJAttributeTextField(strokeWidthField, 2, "attribute.strokeWidth.toolTopText");
        addNumberFormatterToTextField(strokeWidthField, 0d, 100d, 1d, false, "");
        disposables.add(new FigureAttributeEditorHandler<Double>(STROKE_WIDTH, strokeWidthField, editor));
        return strokeWidthField;
    }

    private JAttributeTextField<Double> createDashField() {
        JAttributeTextField<Double> dashOffsetField = new JAttributeTextField<>();
        configureJAttributeTextField(dashOffsetField, 1, "attribute.strokeDashOffset.toolTopText");
        dashOffsetField.setFormatterFactory(JavaNumberFormatter.createFormatterFactory(-1000d, 1000d, 1d));
        disposables.add(new FigureAttributeEditorHandler<Double>(STROKE_DASH_PHASE, dashOffsetField, editor));
        return dashOffsetField;
    }

    //  @FeatureEntryPoint(value = "stroke_tool")
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
                try {
                    createState1(p);
                } catch (Exception e) {
                    System.err.println("Error creating state 1");
                    e.printStackTrace();
                }
                break;
            case 2:
                try {
                    createState2(p);
                } catch (Exception e) {
                    System.err.println("Error creating state 2");
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
        return p;
    }

    private void createState1(JPanel p) {
        p.add(createStrokeColor(), createGridBagConstraints(0, -1, 1, null));
        p.add(createOpacitySlider(), createGridBagConstraints(0, -1, 1, new Insets(3, 0, 0, 0)));
        p.add(createStrokeWidthPopupSlider(), createGridBagConstraints(0, -1, 1, new Insets(3, 0, 0, 0)));
        p.add(createStrokeJoinButton(), createGridBagConstraints(-1, 0, 1, new Insets(0, 3, 0, 0)));
        p.add(createStrokeCapButton(), createGridBagConstraints(-1, 1, 1, new Insets(3, 3, 0, 0)));
        p.add(createStrokeDashesButton(), createGridBagConstraints(-1, 2, GridBagConstraints.REMAINDER, new Insets(3, 3, 0, 0)));
    }

    private void createState2(JPanel p) {

        p.add(strokeColorField(), createGridBagConstraints(0, 0, 3, new Insets(3, 3, 0, 0), GridBagConstraints.HORIZONTAL, 0.0, 0.0));
        p.add(createStrokeColor(), createGridBagConstraints(3, -1, 1, null));

        p.add(createOpacityField(), createGridBagConstraints(0, -1, 1, new Insets(3, 0, 0, 0), GridBagConstraints.HORIZONTAL, 0.0, 0.0));
        p.add(createOpacitySlider(), createGridBagConstraints(1, -1, 1, new Insets(3, 0, 0, 0), GridBagConstraints.HORIZONTAL, 0.0, 1f));

        p.add(createStrokeWidthField(), createGridBagConstraints(0, 2, 1, new Insets(3, 0, 0, 0), GridBagConstraints.BOTH, 0.0, 0.0));
        p.add(createStrokeWidthPopupSlider(), createGridBagConstraints(1, 2, 1, new Insets(3, 0, 0, 0)));

        p.add(createDashField(), createGridBagConstraints(2, 2, 2, new Insets(3, 3, 0, 0), GridBagConstraints.BOTH, 0.0, 0.0));

        p.add(createStrokeDashesButton(), createGridBagConstraints(4, 2, GridBagConstraints.REMAINDER, new Insets(3, 3, 0, 0)));

        p.add(createStrokeJoinButton(), createGridBagConstraints(4, 0, 2, new Insets(0, 3, 0, 0)));

        p.add(createStrokeCapButton(), createGridBagConstraints(4, 1, 1, new Insets(3, 3, 0, 0)));
    }

    @Override
    protected String getID() {
        return "stroke";
    }

    @Override
    protected int getDefaultDisclosureState() {
        return 1;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
