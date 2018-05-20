package uk.ac.cam.cl.gui.widgets;

import javafx.animation.FadeTransition;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import uk.ac.cam.cl.Main;
import uk.ac.cam.cl.data.Unit;

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
  private Region spacer;
  private StackPane view;
  private Label nameLabel;
  private HBox bottom;

  public WidgetContainer(Widget widget, Integer pos) {
    super();
    spacer = new Region();
    spacer.getStyleClass().add("button-spacer");
    setCenter(widget);
    position = pos;
    nameLabel = new Label(getWidgetName(widget));
    bottom = new HBox();
    bottom.getChildren().addAll(nameLabel);
    setBottom(bottom);
    this.getStyleClass().add("widget_container");
  }

  public WidgetContainer(Widget widget, Settings sett, Integer pos) {
    this(widget, pos);
    top = new BorderPane();
    swipe = new Button();
    swipe.setGraphic(new ImageView(Main.SETTINGS_ICON));

    this.main = widget;
    this.sett = sett;
    view = new StackPane();
    view.getChildren().addAll(this.sett, this.main);

    top.setRight(swipe);

    swipe.setOnAction(
        (actionEvent) -> {
          swap();
        });

    setCenter(view);
    setTop(top);
  }

  private void swap() {
    if (this.onMain) {
      FadeTransition ft = new FadeTransition(Duration.millis(500), this.main);
      ft.setFromValue(1.0);
      ft.setToValue(0.0);
      ft.setCycleCount(1);
      ft.play();

      this.swipe.setGraphic(new ImageView(Main.BACK_ICON));
      top.setRight(spacer);
      top.setCenter(new Label("Settings"));
      top.setLeft(swipe);
      setBottom(null);

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

      this.swipe.setGraphic(new ImageView(Main.SETTINGS_ICON));
      top.setLeft(null);
      top.setCenter(null);
      top.setRight(swipe);
      setBottom(nameLabel);

      FadeTransition ft2 = new FadeTransition((Duration.millis(500)), this.main);
      ft2.setFromValue(0.0);
      ft2.setToValue(1.0);
      ft2.setCycleCount(1);
      ft2.play();
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
    Unit unit = widget.getUnit();
    if (unit != Unit.NONE) 
      return widget.getName() + " (" + widget.getUnit() + ")";
    else return widget.getName();
  }

  public Integer getPosition() {
    return position;
  }

  public void setPosition(Integer pos) {
    position = pos;
  }
}
