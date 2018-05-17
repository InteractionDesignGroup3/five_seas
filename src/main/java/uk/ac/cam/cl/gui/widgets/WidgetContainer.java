package uk.ac.cam.cl.gui.widgets;

import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

/**
 * Wraps a Widget with a border.
 * @author Ben Cole
 */
public class WidgetContainer extends BorderPane {

    public WidgetContainer(Widget widget) {
        super();
        setCenter(widget);
        HBox bottom = new HBox();
        Label nameLabel = new Label(widget.getName());
        bottom.getChildren().addAll(nameLabel);
        setBottom(bottom);
    }
}
