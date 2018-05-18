package uk.ac.cam.cl.gui.widgets;

import javafx.scene.layout.GridPane;

//TODO Change this from GridPane so it stops stretching to fill its container
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
}
