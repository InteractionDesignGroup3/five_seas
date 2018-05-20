package uk.ac.cam.cl.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;

import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

import uk.ac.cam.cl.Main;
import uk.ac.cam.cl.data.DataManager;
import uk.ac.cam.cl.data.Location;

public class TopBar extends BorderPane {
  private Main parent;

  private DataManager dm = DataManager.getInstance();

  private List<Location> places = new ArrayList<>();

  public TopBar(Main parent) {
    super();

    this.parent = parent;
    this.setId("top-bar");

    this.setLeft(initLocButton());
    this.setCenter(initSearchBox());
    this.setRight(initMenuButton());
  }

  private Button initLocButton() {
    Button locBtn = new Button();
    locBtn.setGraphic(new ImageView(Main.LOCATION_ICON));
    locBtn.getStyleClass().add("button");
    return locBtn;
  }

  private TextField initSearchBox() {
    TextField searchBox = new TextField();
    searchBox.setPromptText("Location");
    searchBox.setText(dm.getLocation().getName());
    searchBox.setId("search-box");

    AutoCompletionBinding<String> stringAutoCompletionBinding =
        TextFields.bindAutoCompletion(
            searchBox,
            t -> {
              places = dm.getLocations(searchBox.getText());
              return places.stream().map(x -> x.getName()).collect(Collectors.toList());
            });

    stringAutoCompletionBinding.setPrefWidth(280);

    searchBox.setOnAction(
        (event) -> {
          for (Location loc : places) {
            if (loc.getName().equals(searchBox.getText())) {
              dm.setLocation(loc);
              break;
            }
          }
        });

    return searchBox;
  }

  private Button initMenuButton() {
    Button menuBtn = new Button();
    menuBtn.setGraphic(new ImageView(Main.ADD_ICON));
    menuBtn.getStyleClass().add("button");
    menuBtn.setOnAction(
        new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent event) {
            System.out.println("+ menu button pressed");
            parent.showMenu();
          }
        });

    return menuBtn;
  }
}
