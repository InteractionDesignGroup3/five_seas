package uk.ac.cam.cl;

import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import uk.ac.cam.cl.gui.widgets.*;

import java.util.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import static javafx.scene.control.ScrollPane.ScrollBarPolicy;
import uk.ac.cam.cl.data.AppSettings;
import uk.ac.cam.cl.data.DataManager;
import uk.ac.cam.cl.gui.*;

public class Main extends Application {
  private Stage stage;
  private Scene mainScene = null;
  private Scene menuScene = null;
  private Map<String, WidgetContainer> widgets = new HashMap<>();
  // widgets to be added to screen mapping name => widget

  private static int NUM_OF_WIDGETS = 10;
  private ArrayList<Widget> widgetList;
  private ArrayList<WidgetContainer> widgetOrder;
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
    topBar.setOnDragOver(event -> {
      event.acceptTransferModes(TransferMode.MOVE);
      mainScrollable.setVvalue(mainScrollable.getVvalue()-0.015);
      event.consume();
    });
    GridPane mainSec = new GridPane();
    GridPane bottomBar = new BottomBar(this);
    bottomBar.setOnDragOver(event -> {
      event.acceptTransferModes(TransferMode.MOVE);
      mainScrollable.setVvalue(mainScrollable.getVvalue()+0.015);
      event.consume();
    });
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
                new WindSpeedGraph(),
                new WeatherWidget(),
                new WindWidget()));

    widgetOrder = new ArrayList<>();
    int j = 0;
    for(Integer i = 0; i < widgetList.size(); i++)
    {
      Widget y = widgetList.get(i);

      if (settings.getOrDefault(getCanonicalName(y), false)) {
        WidgetContainer z = new WidgetContainer(y, j);
        z.setOnDragDetected(event -> {
          Dragboard db = z.startDragAndDrop(TransferMode.MOVE);
          ClipboardContent c = new ClipboardContent();
          c.putString(z.getPosition().toString());
          db.setContent(c);
          event.consume();
        });
        z.setOnDragOver(event -> {
          event.acceptTransferModes(TransferMode.MOVE);
          event.consume();
        });
        z.setOnDragDropped(event -> {
          int pos = Integer.parseInt((event.getDragboard().getContent(DataFormat.PLAIN_TEXT)).toString());
          WidgetContainer temp = widgetOrder.get(pos);
          widgetOrder.remove(pos);
          int position = z.getPosition();
          temp.setPosition(z.getPosition());
          if(z.getPosition() < pos) {
            for (int k = z.getPosition(); k < pos; k++) {
              widgetOrder.get(k).setPosition(widgetOrder.get(k).getPosition() + 1);
            }
          }
          else
          {
            for (int k = pos; k < z.getPosition(); k++) {
              widgetOrder.get(k).setPosition(widgetOrder.get(k).getPosition() - 1); 
            }
          }
          widgetOrder.add(position, temp);
          addWidgets(mainSec);
          event.consume();
        });
        widgets.put(getCanonicalName(y), z);
        widgetOrder.add(j, z);
        j++;
      }
    }

    // add widgets to the panel
    addWidgets(mainSec);

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
      
      Label label = new Label(y.getName());
      label.setContentDisplay(ContentDisplay.RIGHT);

      CheckBox x = new CheckBox();
      label.setGraphic(x);
      x.getStyleClass().add("check-box");
      x.selectedProperty()
          .addListener(
              (observable, oldValue, newValue) -> {
                if (newValue) {
                  widgets.put(canonicalName, new WidgetContainer(y, 0));
                  settings.set(canonicalName, true);
                } else {
                  widgets.remove(canonicalName);
                  settings.set(canonicalName, false);
                }
              });
      mainSec.add(label, 0, i);

      if (settings.getOrDefault(canonicalName, false)) x.setSelected(true);
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

  private void addWidgets(GridPane mainSec)
  {
    mainSec.getChildren().clear();
    int i = 0;
    for (WidgetContainer widgetContainer : widgetOrder) {
      mainSec.add(widgetContainer, 0, i);
      i++;
    }
  }
}
