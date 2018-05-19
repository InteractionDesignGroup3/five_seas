package uk.ac.cam.cl.gui.widgets;

import javafx.scene.layout.GridPane;

public abstract class Settings extends GridPane {
    public Settings() {
        this.getStyleClass().add("widget");
    }

    public abstract String getUnit();
    public abstract void setUnit(String unit);
}
