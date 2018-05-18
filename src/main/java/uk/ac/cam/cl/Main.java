package uk.ac.cam.cl;

import static javafx.scene.control.ScrollPane.ScrollBarPolicy;

import java.util.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import uk.ac.cam.cl.data.AppSettings;
import uk.ac.cam.cl.data.DataManager;
import uk.ac.cam.cl.gui.*;
import uk.ac.cam.cl.gui.widgets.*;

public class Main extends Application {
  private Stage stage;
  private Scene mainScene = null;
  private Scene menuScene = null;
  private Map<String, WidgetContainer> widgets = new HashMap<>();
  // widgets to be added to screen mapping name => widget

  private static int NUM_OF_WIDGETS = 10;

  private ArrayList<Widget> widgetList;

  private AppSettings settings = AppSettings.getInstance();

  @Override
  public void start(Stage primaryStage) {
    this.stage = primaryStage;
    showMain();
  }

  /** Show the main panel. */
  public void showMain() {
    BorderPane root = new BorderPane();
    root.setId("root");
    GridPane topBar = new TopBar(this);
    ScrollPane mainScrollable = new ScrollPane();
    GridPane mainSec = new GridPane();
    GridPane bottomBar = new BottomBar(this);
    mainScrollable.setContent(mainSec);
    mainScrollable.setHbarPolicy(ScrollBarPolicy.NEVER);
    mainScrollable.setVbarPolicy(ScrollBarPolicy.NEVER);

    root.setPadding(new Insets(5));
    this.stage.setTitle("Five Seas");

    widgetList =
        new ArrayList<Widget>(
            Arrays.asList(
                new SwellHeightGraph(),
                new TemperatureGraph(),
                new TideGraph(),
                new VisibilityGraph(),
                new WeatherWidget(),
                new WindWidget()));

    for (Integer i = 0; i < widgetList.size(); i++) {
      Widget y = widgetList.get(i);

      if (settings.getOrDefault(getCanonicalName(y), false)) {
        widgets.put(getCanonicalName(y), new WidgetContainer(y));
      }
    }

    //Add widgets to the panel
    int i = 0;
    for (WidgetContainer widgetContainer : widgets.values()) {
      mainSec.add(widgetContainer, 0, i);
      i++;
    }

    root.setCenter(mainScrollable);
    root.setBottom(bottomBar);
    root.setTop(topBar);

    this.mainScene = new Scene(root, 380, 675);
    this.mainScene.getStylesheets().add("style/style.css");
    this.stage.setScene(this.mainScene);
    this.stage.show();
  }

  private String getCanonicalName(Widget w) {
    return w.getName().replaceAll(" ", "_").toLowerCase();
  }

  /** Show the menu panel. */
  public void showMenu() {
    if (menuScene != null) {
      this.stage.setScene(menuScene);
      this.stage.show();
      return;
    }

    BorderPane root = new BorderPane();
    root.setId("root");
    GridPane topBar = new MenuBar(this);
    topBar.setId("menu-bar");
    GridPane mainSec = new GridPane();
    mainSec.setId("menu-main");

    for (Integer i = 0; i < widgetList.size(); i++) {
      Widget y = widgetList.get(i);
      String canonicalName = getCanonicalName(y);
      CheckBox x = new CheckBox();
      x.setText(y.getName());
      x.getStyleClass().add("check-box");
      x.selectedProperty()
          .addListener(
              (observable, oldValue, newValue) -> {
                if (newValue) {
                  widgets.put(canonicalName, new WidgetContainer(y));
                  settings.set(canonicalName, true);
                } else {
                  widgets.remove(canonicalName);
                  settings.set(canonicalName, false);
                }
              });
      mainSec.add(x, 0, i);

      if (settings.getOrDefault(canonicalName, false)) {
        x.setSelected(true);
      }
    }

    this.stage.setTitle("Widget Menu");

    root.setCenter(mainSec);
    root.setTop(topBar);

    this.menuScene = new Scene(root, 380, 675);
    this.menuScene.getStylesheets().add("style/style.css");
    this.stage.setScene(menuScene);
    this.stage.show();
  }

  public static void main(String[] args) {
    DataManager.getInstance()
        .addListener(
            sequence -> {
              System.out.println(sequence);
            });

    launch(args);
  }
}
