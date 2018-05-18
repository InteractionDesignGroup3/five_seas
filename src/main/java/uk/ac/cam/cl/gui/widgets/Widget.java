package uk.ac.cam.cl.gui.widgets;

import javafx.scene.layout.GridPane;

/**
 * Encapsulates all widgets.
 *
 * @author Ben Cole
 */
public abstract class Widget extends GridPane {
  public Widget() {
    super();
    this.getStyleClass().add("widget");
  }

  /**
   * Returns the name of the widget to be displayed in its border.
   *
   * @return the name of the widget
   */
  public abstract String getName();

  /**
   * Returns the name of the widget with its unit in brackets.
   *
   * @return the name of the widget and its unit
   */
  public abstract String getUnit();
}
