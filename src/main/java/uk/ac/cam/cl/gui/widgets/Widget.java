package uk.ac.cam.cl.gui.widgets;

import javafx.scene.layout.GridPane;

public abstract class Widget extends GridPane {

    // TODO: Change this from GridPane so it stops stretching to fill its container

    public Widget() {
        super();
        this.getStyleClass().add("widget");
    }

    /**
     * Returns the name of the widget to be displayed in its border.
     * @return the name of the widget
     */
    public abstract String getName();
}
