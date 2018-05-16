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
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.*;

import org.json.simple.JSONArray;
import uk.ac.cam.cl.data.AppSettings;
import uk.ac.cam.cl.data.AppSettingsFailure;
import uk.ac.cam.cl.data.DataManager;
import uk.ac.cam.cl.gui.*;

public class Main extends Application {
    private Stage stage;
    private Scene mainScene = null;
    private Scene menuScene = null;
    private Map<String,Widget> widgets = new HashMap<>();
    private AppSettings settings = AppSettings.getInstance();
    // widgets to be added to screen mapping name => widget

    private static int NUM_OF_WIDGETS = 10;

    private List<String> getWidgetList() {
        List<String> wid = new ArrayList<>();

        wid.add("wind");
        wid.add("temp");
        wid.add("visi");
        wid.add("tide");

        return wid;
    }

    private String nameToDesc(String w) {
        String ret = null;
        switch (w) {
            case "wind":
                ret = "Wind speed/direction";
                break;

            case "temp":
                ret = "Temperature";
                break;

            case "visi":
                ret = "Visibility";
                break;

            case "tide":
                ret = "Tides";
                break;
        }
        return ret;
    }

    private Widget stringToWidget(String w) {
        Widget ret = null;
        switch (w) {
            case "wind":
                ret = new WindWidget();
                break;

            case "temp":
                ret = new TemperatureGraph();
                break;

            case "visi":
                ret = new VisibilityGraph();
                break;

            case "tide":
                ret = new TideGraph();
                break;
        }

        return ret;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        //Thread.sleep(2000); // dirty hack as temp fix
        this.stage = primaryStage;
        try {
            JSONArray widgs = settings.get("widgets");
            if (widgs == null) {
                settings.set("widgets", new JSONArray());
            }
            widgs = settings.get("widgets");
            for (int i=0; i<widgs.size(); i++) {
                String w = (String) widgs.get(i);
                widgets.put(w, stringToWidget(w));
            }
        } catch (AppSettingsFailure e) {
            e.printStackTrace();
            settings.set("widgets", new JSONArray());
        }
        showMain();
    }

    /**
     * Show the main panel.
     * */
    public void showMain() {
        ScrollPane scrollPane = new ScrollPane();
        BorderPane root = new BorderPane();
        GridPane topBar = new TopBar(this);
        GridPane mainSec = new GridPane();
        GridPane bottomBar = new BottomBar(this);
        this.stage.setTitle("Five Seas");

        // add widgets to the panel
        int i = 0;
        for (Widget widget : widgets.values()) {
            mainSec.add(widget, 0, i);
            i++;
        }

        root.setTop(topBar);
        root.setBottom(bottomBar);
        scrollPane.setContent(mainSec);
        root.setCenter(scrollPane);
        //root.setStyle("-fx-background-color: #ffffff;");
        root.getStylesheets().add("style.css");


        this.mainScene = new Scene(root, 380, 675);
        this.mainScene.getStylesheets().add("style.css");
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
        mainSec.setId("menu-main");

        List<CheckBox> checkBoxes = new ArrayList<>();
        List<String> wids = getWidgetList();
        for (int i=0; i<wids.size(); i++) {
            final int x = i;
            checkBoxes.add(new CheckBox());
            checkBoxes.get(i).setText(nameToDesc(wids.get(i)));
            checkBoxes.get(i).selectedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable,
                                    Boolean oldValue, Boolean newValue) {
                    if (newValue) {
                        System.out.println("added widget: " + wids.get(x));
                        JSONArray widgs = settings.get("widgets");
                        if (!widgs.contains(wids.get(x))) {
                            widgs.add(wids.get(x));
                        }
                        settings.set("widgets", widgs);
                        widgets.put(wids.get(x), stringToWidget(wids.get(x)));
                    } else {
                        System.out.println("removed widget: " + wids.get(x));
                        JSONArray widgs = settings.get("widgets");
                        while (widgs.contains(wids.get(x))) {
                            widgs.remove(wids.get(x));
                        }
                        settings.set("widgets", widgs);
                        widgets.remove(wids.get(x));
                    }
                }
            });
            JSONArray widgs = settings.get("widgets");
            checkBoxes.get(i).setPadding(new Insets(5, 10, 5, 10));
            checkBoxes.get(i).getStyleClass().add("check-box");
            if (widgs.contains(wids.get(i))) {
                checkBoxes.get(i).setSelected(true);
            } else {
                checkBoxes.get(i).setSelected(false);
            }
            mainSec.add(checkBoxes.get(i), 0, i);
        }

        root.setPadding(new Insets(0));

        this.stage.setTitle("Widget Menu");

        root.setTop(topBar);
        root.setCenter(mainSec);

        this.menuScene = new Scene(root, 380, 675);
        this.menuScene.getStylesheets().add("style.css");
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
