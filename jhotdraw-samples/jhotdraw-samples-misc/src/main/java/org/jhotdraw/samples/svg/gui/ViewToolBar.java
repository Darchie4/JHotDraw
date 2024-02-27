/*
 * @(#)CanvasToolBar.java
 *
 * Copyright (c) 2007-2008 The authors and contributors of JHotDraw.
 * You may not use, copy or modify this file, except in compliance with the
 * accompanying license terms.
 */
package org.jhotdraw.samples.svg.gui;

import org.jhotdraw.gui.action.ButtonFactory;
import org.jhotdraw.gui.plaf.palette.PaletteFormattedTextFieldUI;
import org.jhotdraw.gui.plaf.palette.PaletteButtonUI;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Objects;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.text.DefaultFormatterFactory;
import org.jhotdraw.draw.DrawingView;
import org.jhotdraw.draw.GridConstrainer;
import org.jhotdraw.gui.JLifeFormattedTextField;
import org.jhotdraw.formatter.JavaNumberFormatter;
import org.jhotdraw.util.*;
import org.jhotdraw.util.prefs.PreferencesUtil;

/**
 * ViewToolBar.
 * <p>
 * Note: you must explicitly set the view before createDisclosedComponents is
 * called for the first time.
 *
 * @author Werner Randelshofer
 * @version $Id$
 */
public class ViewToolBar extends AbstractToolBar {

    private static final long serialVersionUID = 1L;
    private DrawingView view;
    private static final ResourceBundleUtil LABELS = ResourceBundleUtil.getBundle("org.jhotdraw.samples.svg.Labels");
    private String gridSizeKey = "view.gridSize";

    /**
     * Creates new instance.
     */
    public ViewToolBar() {
        setName(LABELS.getString(getID() + ".toolbar"));
        setDisclosureStateCount(3);
    }

    public void setView(DrawingView view) {
        this.view = view;
        prefs = PreferencesUtil.userNodeForPackage(getClass());
        GridConstrainer constrainer = (GridConstrainer) view.getVisibleConstrainer();
        constrainer.setHeight(prefs.getDouble(gridSizeKey, 8d));
        constrainer.setWidth(prefs.getDouble(gridSizeKey, 8d));
    }

    @Override
    protected JComponent createDisclosedComponent(int state) {
        // Abort if no editor is set
        if (editor == null) {
            return null;
        }

//        return switch (state) {
//            case 1 -> halfOpenPanel();
//            case 2 -> fullOpenPanel();
//            default -> null;
//        };

        JPanel p = null;
        switch (state) {
            case 1:
                p = halfOpenPanel();
                break;
            case 2:
                p = fullOpenPanel();
                break;
            default:
        }
        return p;
    }

    private JPanel halfOpenPanel() {

        // Toggle Grid Button
        AbstractButton toggleGridButton = createGridButton();

        GridBagConstraints toggleGridButtonConstraints = createDefaultGridBagConstraints();

        // Zoom Button
        AbstractButton zoomButton = createZoomButton();
        zoomButton.setPreferredSize(new Dimension(zoomButton.getPreferredSize().width,
                toggleGridButton.getPreferredSize().height));

        GridBagConstraints zoomButtonConstraints = createZoomGridbagConstraints();
        zoomButtonConstraints.weightx = 1;

        // Create JPanel and add all buttons, grid bag layouts and constraints
        JPanel p = setPanel();
        p.setLayout(new GridBagLayout());
        p.add(toggleGridButton, toggleGridButtonConstraints);
        p.add(zoomButton, zoomButtonConstraints);

        return p;
    }

    private JPanel fullOpenPanel() {

        // Grid Size Field
        JLifeFormattedTextField gridSizeField = getGridSizeField();

        GridBagConstraints gridFieldConstraints = createDefaultGridBagConstraints();
        gridFieldConstraints.gridx = 0;

        // Toggle Grid Button
        AbstractButton toggleGridButton = createGridButton();
        GridBagConstraints toggleGridConstraints = createDefaultGridBagConstraints();

        // Zoom Factor Field
        JLifeFormattedTextField zoomFactorField = getZoomFactorField();

        GridBagConstraints zoomFieldConstraints = createDefaultGridBagConstraints();
        zoomFieldConstraints.gridx = 0;
        zoomFieldConstraints.gridy = 1;
        zoomFieldConstraints.insets = new Insets(3, 0, 0, 0);

        // Zoom Button
        AbstractButton zoomButton = createZoomButton();
        zoomButton.setPreferredSize(new Dimension(zoomButton.getPreferredSize().width, zoomFactorField.getPreferredSize().height));

        GridBagConstraints zoomButtonConstraints = createZoomGridbagConstraints();

        // Create JPanel and add all buttons, grid bag layouts and constraints
        JPanel p = setPanel();

        p.setLayout(new GridBagLayout());
        p.add(gridSizeField, gridFieldConstraints);
        p.add(toggleGridButton, toggleGridConstraints);
        p.add(zoomFactorField, zoomFieldConstraints);
        p.add(zoomButton, zoomButtonConstraints);

        return p;
    }

    private JLifeFormattedTextField getZoomFactorField() {

        JLifeFormattedTextField scaleFactorField = new JLifeFormattedTextField();
        scaleFactorField.setColumns(4);
        scaleFactorField.setToolTipText(LABELS.getString("view.zoomFactor.toolTipText"));
        scaleFactorField.setHorizontalAlignment(JLifeFormattedTextField.RIGHT);
        scaleFactorField.putClientProperty("Palette.Component.segmentPosition", "first");
        scaleFactorField.setUI((PaletteFormattedTextFieldUI) PaletteFormattedTextFieldUI.createUI(scaleFactorField));
        JavaNumberFormatter formatter = new JavaNumberFormatter(0.01d, 50d, 100d, false, "%");
        formatter.setUsesScientificNotation(false);
        formatter.setMaximumFractionDigits(1);
        scaleFactorField.setFormatterFactory(new DefaultFormatterFactory(formatter));
        scaleFactorField.setHorizontalAlignment(JTextField.LEADING);
        scaleFactorField.setValue(view.getScaleFactor());

        scaleFactorField.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ("value".equals(evt.getPropertyName()) && (evt.getNewValue() != null)) {
                        view.setScaleFactor((Double) evt.getNewValue());

                }
            }
        });

        view.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (Objects.equals(evt.getPropertyName(), DrawingView.SCALE_FACTOR_PROPERTY) && (evt.getNewValue() != null)) {
                        scaleFactorField.setValue((Double) evt.getNewValue());

                }
            }
        });

        return scaleFactorField;
    }

    private JLifeFormattedTextField getGridSizeField() {
        JLifeFormattedTextField gridSizeField = new JLifeFormattedTextField();
        gridSizeField.setColumns(4);
        gridSizeField.setToolTipText(LABELS.getString("view.gridSize.toolTipText"));
        gridSizeField.setHorizontalAlignment(JLifeFormattedTextField.RIGHT);
        gridSizeField.putClientProperty("Palette.Component.segmentPosition", "first");
        gridSizeField.setUI((PaletteFormattedTextFieldUI) PaletteFormattedTextFieldUI.createUI(gridSizeField));
        gridSizeField.setFormatterFactory(JavaNumberFormatter.createFormatterFactory(0d, 1000d, 1d, true));
        gridSizeField.setHorizontalAlignment(JTextField.LEADING);

        final GridConstrainer constrainer = (GridConstrainer) view.getVisibleConstrainer();

        gridSizeField.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ("value".equals(evt.getPropertyName()) && (evt.getNewValue() != null)) {
                        constrainer.setWidth((Double) evt.getNewValue());
                        constrainer.setHeight((Double) evt.getNewValue());
                        prefs = PreferencesUtil.userNodeForPackage(getClass());
                        try {
                            prefs.putDouble(gridSizeKey, (Double) evt.getNewValue());
                        } catch (IllegalStateException e) { //ignore
                        }
                        view.getComponent().repaint();

                }
            }
        });

        gridSizeField.setValue(constrainer.getHeight());
        return gridSizeField;
    }

    private JPanel setPanel() {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setBorder(new EmptyBorder(5, 5, 5, 8));
        return p;
    }

    private GridBagConstraints createDefaultGridBagConstraints() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(0, 0, 0, 0);
        return gbc;
    }

    private GridBagConstraints createZoomGridbagConstraints() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.insets = new Insets(3, 0, 0, 0);
        gbc.weighty = 1;
        return gbc;
    }

    private AbstractButton createGridButton() {
        AbstractButton btn = ButtonFactory.createToggleGridButton(view);
        btn.setUI((PaletteButtonUI) PaletteButtonUI.createUI(btn));
        LABELS.configureToolBarButton(btn, "alignGrid");
        return btn;
    }

    private AbstractButton createZoomButton() {
        AbstractButton btn = ButtonFactory.createZoomButton(view);
        btn.setUI((PaletteButtonUI) PaletteButtonUI.createUI(btn));
        LABELS.configureToolBarButton(btn, "view.zoomFactor");
        btn.setText("100 %");
        return btn;
    }

    @Override
    protected String getID() {
        return "view";
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