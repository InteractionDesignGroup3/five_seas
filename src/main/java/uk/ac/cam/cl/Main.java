package uk.ac.cam.cl;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.awt.*;
import java.util.*;
import uk.ac.cam.cl.data.DataManager;
import uk.ac.cam.cl.gui.*;
import uk.ac.cam.cl.gui.MenuBar;
import uk.ac.cam.cl.gui.widgets.*;

public class Main extends Application {
    private Stage stage;
    private Scene mainScene = null;
    private Scene menuScene = null;
    private Map<String,WidgetContainer> widgets = new HashMap<>();
    // widgets to be added to screen mapping name => widget

    private static int NUM_OF_WIDGETS = 10;

    private final ArrayList<Widget> widgetList = new ArrayList<Widget>(Arrays.asList(
            new SwellHeightGraph(), new TemperatureGraph(), new TideGraph(), new VisibilityGraph(), new WeatherWidget(), new WindWidget()));

    @Override
    public void start(Stage primaryStage) {
        this.stage = primaryStage;
        showMain();
    }

    /**
     * Show the main panel.
     * */
    public void showMain() {
        BorderPane root = new BorderPane();
        root.setId("root");
        GridPane topBar = new TopBar(this);
        ScrollPane mainScrollable = new ScrollPane();
        GridPane mainSec = new GridPane();
        GridPane bottomBar = new BottomBar(this);
        mainScrollable.setContent(mainSec);

        root.setPadding(new Insets(5));
        this.stage.setTitle("Five Seas");

        // add widgets to the panel
        int i = 0;
        for (WidgetContainer widgetContainer : widgets.values()) {
            mainSec.add(widgetContainer, 0, i);
            i++;
        }

        root.setTop(topBar);
        root.setBottom(bottomBar);
        root.setCenter(mainScrollable);

        this.mainScene = new Scene(root, 380, 675);
        this.mainScene.getStylesheets().add("style/style.css");
        this.stage.setScene(this.mainScene);
        this.stage.show();
    }

    /**
     * Show the menu panel.
     * */
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

        
        //Example check box for widget settings with an example of how to 
        //add widgets to the main screen

        for(Integer i = 0; i < widgetList.size(); i++)
        {
            Widget y = widgetList.get(i);
            CheckBox x = new CheckBox();
            x.setText(y.getName());
            x.getStyleClass().add("check-box");
            x.selectedProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) widgets.put(y.getName().replaceAll(" ", "_").toLowerCase(), new WidgetContainer(y));
                else widgets.remove(y.getName());
            });
            mainSec.add(x, 0, i);
        }

        this.stage.setTitle("Widget Menu");

        root.setTop(topBar);
        root.setCenter(mainSec);

        this.menuScene = new Scene(root, 380, 675);
        this.menuScene.getStylesheets().add("style/style.css");
        this.stage.setScene(menuScene);
        this.stage.show();
    }

    public static void main(String[] args) {
        DataManager.getInstance().addListener(sequence -> {
            System.out.println(sequence); 
        });

        launch(args);
    }
}
