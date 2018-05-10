package uk.ac.cam.cl;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.layout.StackPane;
import javafx.scene.Scene;

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

