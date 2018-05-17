package uk.ac.cam.cl.gui.widgets;

import javafx.scene.layout.GridPane;

public abstract class Widget extends GridPane {

    public Widget() {
        super();
        this.getStyleClass().add("widget");
    }

    public abstract String getName();
}
