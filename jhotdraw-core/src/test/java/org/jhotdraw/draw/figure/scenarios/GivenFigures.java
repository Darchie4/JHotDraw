package org.jhotdraw.draw.figure.scenarios;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import org.jhotdraw.draw.Drawing;
import org.jhotdraw.draw.QuadTreeDrawing;
import org.jhotdraw.draw.figure.Figure;
import org.jhotdraw.draw.figure.RectangleFigure;

import java.util.List;

public class GivenFigures extends Stage<GivenFigures> {
    @ProvidedScenarioState
    Drawing drawing;

    @ProvidedScenarioState
    List<Figure> figures;

    public GivenFigures some_figures_in_a_drawing() {
        drawing = new QuadTreeDrawing();
        figures = List.of(new RectangleFigure(), new RectangleFigure(), new RectangleFigure());
        figures.forEach(drawing::add);
        return this;
    }
}
