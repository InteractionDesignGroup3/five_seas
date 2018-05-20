package uk.ac.cam.cl.gui;

import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import uk.ac.cam.cl.Main;

/**
 * Represents the border pane displayed at the top of the app including the time selection slider
 *
 * @author Max Campman
 */
public class MenuBar extends BorderPane {
  private Main parent;

  /**
   * Creates a new menu bar to be used with the given main class
   *
   * @param parent the main class
   */
  public MenuBar(Main parent) {
    super();
    this.parent = parent;
    setId("menu-bar");
    Label title = new Label("Add Widgets");
    title.getStyleClass().add("title");
    Region spacer = new Region();
    spacer.getStyleClass().add("button-spacer");
    setLeft(initBackButton());
    setCenter(title);
    setRight(spacer);
  }

  /** Initialises the back button */
  private Button initBackButton() {
    Button menuBtn = new Button();
    menuBtn.setGraphic(new ImageView(Main.BACK_ICON));
    menuBtn.getStyleClass().add("button");
    GridPane.setHalignment(menuBtn, HPos.CENTER);
    menuBtn.setOnAction(
        (x) -> {
          parent.showMain();
        });

    return menuBtn;
  }
}
