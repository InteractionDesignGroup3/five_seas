package uk.ac.cam.cl;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.*;
import uk.ac.cam.cl.data.DataManager;
import uk.ac.cam.cl.gui.*;
import uk.ac.cam.cl.gui.widgets.*;

public class Main extends Application {
    private Stage stage;
    private Scene mainScene = null;
    private Scene menuScene = null;
    private Map<String,WidgetContainer> widgets = new HashMap<>();
    // widgets to be added to screen mapping name => widget

    private static int NUM_OF_WIDGETS = 10;

    private String WIND_COMPASS_CODE = "wind_compass";
    private String TEMPERATURE_GRAPH_CODE = "temperature_graph";
    private String WIND_SPEED_GRAPH_CODE = "wind_speed_graph";
    private String VISIBILITY_GRAPH_CODE = "visibility_graph";
    private String SWELL_HEIGHT_GRAPH_CODE = "swell_height";

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
        GridPane topBar = new TopBar(this);
        GridPane mainSec = new GridPane();
        GridPane bottomBar = new BottomBar(this);

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
        root.setCenter(mainSec);

        this.mainScene = new Scene(root, 380, 675);
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
        GridPane topBar = new MenuBar(this);
        GridPane mainSec = new GridPane();

        
        //Example check box for widget settings with an example of how to 
        //add widgets to the main screen

        CheckBox windCompassCheckBox = new CheckBox();
        windCompassCheckBox.setText("Wind compass");
        windCompassCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) widgets.put(WIND_COMPASS_CODE, new WidgetContainer(new WindWidget()));
            else widgets.remove(WIND_COMPASS_CODE);
        });

        CheckBox temperatureGraphCheckBox = new CheckBox();
        temperatureGraphCheckBox.setText("Temperature graph");
        temperatureGraphCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) widgets.put(TEMPERATURE_GRAPH_CODE, new WidgetContainer(new TemperatureGraph()));
            else widgets.remove(TEMPERATURE_GRAPH_CODE);
        });

        CheckBox windSpeedGraphCheckBox = new CheckBox();
        windSpeedGraphCheckBox.setText("Wind speed graph");
        windSpeedGraphCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) widgets.put(WIND_SPEED_GRAPH_CODE, new WidgetContainer(new WindSpeedGraph()));
            else widgets.remove(WIND_SPEED_GRAPH_CODE);
        });

        CheckBox visibilityGraphCheckBox = new CheckBox();
        visibilityGraphCheckBox.setText("Visibility graph");
        visibilityGraphCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) widgets.put(VISIBILITY_GRAPH_CODE, new WidgetContainer(new VisibilityGraph()));
            else widgets.remove(VISIBILITY_GRAPH_CODE);
        });

        CheckBox swellHeightGraphCheckBox = new CheckBox();
        swellHeightGraphCheckBox.setText("Swell height graph");
        swellHeightGraphCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) widgets.put(SWELL_HEIGHT_GRAPH_CODE, new WidgetContainer(new SwellHeightGraph()));
            else widgets.remove(SWELL_HEIGHT_GRAPH_CODE);
        });


        mainSec.add(windCompassCheckBox, 0, 0);
        mainSec.add(temperatureGraphCheckBox, 0, 1);
        mainSec.add(windSpeedGraphCheckBox, 0, 2);
        mainSec.add(visibilityGraphCheckBox, 0, 3);
        mainSec.add(swellHeightGraphCheckBox, 0, 4);

        root.setPadding(new Insets(5));

        this.stage.setTitle("Widget Menu");

        root.setTop(topBar);
        root.setCenter(mainSec);

        this.menuScene = new Scene(root, 380, 675);
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
