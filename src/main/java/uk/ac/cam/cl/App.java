package uk.ac.cam.cl;

import java.nio.file.Paths;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import org.json.simple.JSONObject;

import uk.ac.cam.cl.data.APIConnector;

public class App extends Application {
    public static void main(String args[]) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Five Seas");
        StackPane root = new StackPane();
        primaryStage.setScene(new Scene(root, 760, 1350));
        primaryStage.show();
    }
}

