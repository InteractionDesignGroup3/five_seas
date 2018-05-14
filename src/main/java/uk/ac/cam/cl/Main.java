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

public class Main extends Application {
    private Stage stage;
    private Scene mainScene = null;
    private Scene menuScene = null;
    private Map<String,Widget> widgets = new HashMap<>();  
    // widgets to be added to screen mapping name => widget

    private static int NUM_OF_WIDGETS = 10;

    @Override
    public void start(Stage primaryStage) throws Exception {
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

        this.stage.setTitle("Five seas");

        // add widgets to the panel
        int i=0;
        for (Widget widget : widgets.values()) {
            mainSec.add(widget, 0, i);
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
        StackPane mainSec = new StackPane();

        
        //Example check box for widget settings with an example of how to 
        //add widgets to the main screen
        CheckBox windWidgCheck = new CheckBox();
        windWidgCheck.setText("Wind speed/direction");
        windWidgCheck.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, 
                    Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    widgets.put("wind", new WindWidget());
                } else {
                    widgets.remove("wind");
                }
            }
        });
        mainSec.getChildren().add(windWidgCheck);

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
