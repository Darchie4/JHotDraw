package org.jhotdraw.samples.svg.gui;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;

import org.jhotdraw.draw.DrawingEditor;
import org.mockito.Mockito;

import javax.swing.*;
import java.util.LinkedList;

public class GivenUserOpensFontToolBar extends Stage<GivenUserOpensFontToolBar> {

    @ProvidedScenarioState
    FontToolBar fontToolBar;

    @ProvidedScenarioState
    JComponent jComponent;

    public GivenUserOpensFontToolBar the_user_opens_the_font_tool_bar() {
        fontToolBar = Mockito.mock(FontToolBar.class);
        fontToolBar.editor = Mockito.mock(DrawingEditor.class);
        fontToolBar.disposables = Mockito.mock(LinkedList.class);
        Mockito.when(fontToolBar.createDisclosedComponent(Mockito.anyInt())).thenCallRealMethod();

        return self();
    }
}
