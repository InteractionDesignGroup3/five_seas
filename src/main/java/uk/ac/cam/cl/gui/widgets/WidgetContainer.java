package uk.ac.cam.cl.gui.widgets;

import java.util.Set;

import javafx.animation.FadeTransition;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import uk.ac.cam.cl.Main;

/**
 * Wraps a Widget with a border.
 *
 * @author Ben Cole
 */

public class WidgetContainer extends BorderPane {
  private Integer position;
  private boolean onMain = true;
  private Widget main;
  private Settings sett;
  private BorderPane top;
  private Button swipe;
  private StackPane view;

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

  public WidgetContainer(Widget widget, Settings sett, Integer pos) {

    position = pos;
    HBox bottom = new HBox();
    Label nameLabel = new Label();
    nameLabel.setText(getWidgetName(widget));
    bottom.getChildren().addAll(nameLabel);
    setBottom(bottom);
    this.getStyleClass().add("widget_container");

    //this(widget, pos);
    this.top = new BorderPane();
    this.swipe = new Button();
    this.swipe.setGraphic(new ImageView(Main.SETTINGS_ICON));

    this.main = widget;
    this.sett = sett;
    this.view = new StackPane();
    this.view.getChildren().addAll(this.sett, this.main);

    this.top.setRight(this.swipe);

    this.swipe.setOnAction((actionEvent) -> {
      swap();
    });

    setCenter(view);
    setTop(top);
  }

  private void swap() {

    if (this.onMain) {
      this.swipe.setGraphic(new ImageView(Main.BACK_ICON));
      top.setRight(null);
      top.setCenter(new Label("Settings"));
      top.setLeft(swipe);

      FadeTransition ft = new FadeTransition(Duration.millis(500), this.main);
      ft.setFromValue(1.0);
      ft.setToValue(0.0);
      ft.setCycleCount(1);
      ft.play();

      FadeTransition ft2 = new FadeTransition((Duration.millis(500)), this.sett);
      ft2.setFromValue(0.0);
      ft2.setToValue(1.0);
      ft2.setCycleCount(1);
      ft2.play();
    } else {
      FadeTransition ft = new FadeTransition(Duration.millis(500), this.sett);
      ft.setFromValue(1.0);
      ft.setToValue(0.0);
      ft.setCycleCount(1);
      ft.play();

      FadeTransition ft2 = new FadeTransition((Duration.millis(500)), this.main);
      ft2.setFromValue(0.0);
      ft2.setToValue(1.0);
      ft2.setCycleCount(1);
      ft2.play();

      this.swipe.setGraphic(new ImageView(Main.SETTINGS_ICON));
      top.setLeft(null);
      top.setCenter(null);
      top.setRight(swipe);
    }

    Node first = view.getChildren().remove(0);
    view.getChildren().add(first);

    this.onMain = !this.onMain;
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
