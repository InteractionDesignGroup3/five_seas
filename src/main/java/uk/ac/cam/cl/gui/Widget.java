package uk.ac.cam.cl.gui;

import javafx.scene.layout.GridPane;
import uk.ac.cam.cl.data.DataSequence;

import java.util.List;
import java.util.function.Consumer;

public abstract class Widget extends GridPane {
    public Widget() {
        super();
        this.setMaxWidth(360);
        this.getStyleClass().add("widget");
    }
}
