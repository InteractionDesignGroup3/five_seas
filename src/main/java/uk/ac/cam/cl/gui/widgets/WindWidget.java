package uk.ac.cam.cl.gui.widgets;

import javafx.scene.control.Label;

/**
 * This is an example class, additional functionality should be added.
 * @author Max Campman
 * */
public class WindWidget extends Widget {
    public WindWidget() {
        super();

        Label label = new Label();
        label.setText("Wind");

        this.add(label, 0, 0);

    }
}
