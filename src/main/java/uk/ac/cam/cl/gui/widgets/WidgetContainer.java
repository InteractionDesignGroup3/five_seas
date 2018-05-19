package uk.ac.cam.cl.gui.widgets;

import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

/**
 * Wraps a Widget with a border.
 *
 * @author Ben Cole
 */

public class WidgetContainer extends BorderPane {
  private Integer position;

  public WidgetContainer(Widget widget, Integer pos) {
    super();
    setCenter(widget);
    position = pos;
    HBox bottom = new HBox();
    Label nameLabel = new Label();
    nameLabel.setText(getWidgetName(widget));
    bottom.getChildren().addAll(nameLabel);
    setBottom(bottom);
    this.getStyleClass().add("widget_container");
  }

  /**
   * Returns the formatted widget name followed by its unit in brackets.
   *
   * @param widget the widget to format
   * @return the formatted name of the widget with a unit
   */
  private String getWidgetName(Widget widget) {
    return widget.getName() + " (" + widget.getUnit() + ")";
  }

  public Integer getPosition()
  {
    return position;
  }
  public void setPosition(Integer pos)
  {
    position = pos;
  }
}
