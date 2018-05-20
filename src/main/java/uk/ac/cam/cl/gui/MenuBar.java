package uk.ac.cam.cl.gui;

import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;

import uk.ac.cam.cl.Main;

public class MenuBar extends BorderPane {
  private Main parent;

  public MenuBar(Main parent) {
    super();
    this.parent = parent;
    this.setId("menu-bar");
    Label title = new Label("Add Widgets");
    title.getStyleClass().add("title");
    Region spacer = new Region();
    spacer.getStyleClass().add("button-spacer");
    this.setLeft(initBackButton());
    this.setCenter(title);
    this.setRight(spacer);
  }

  private Button initBackButton() {
    Button menuBtn = new Button();
    menuBtn.setGraphic(new ImageView(Main.BACK_ICON));
    menuBtn.getStyleClass().add("button");
    GridPane.setHalignment(menuBtn, HPos.CENTER);
    menuBtn.setOnAction(
        (x) -> {
          System.out.println("< menu button pressed");
          parent.showMain();
        });

    return menuBtn;
  }
}
