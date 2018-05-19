package uk.ac.cam.cl.gui.widgets;

import javafx.scene.layout.GridPane;
import uk.ac.cam.cl.data.Unit;

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
   * Returns the Unit in use by this widget.
   *
   * @return the Unit in use
   */
  public abstract Unit getUnit();
}
