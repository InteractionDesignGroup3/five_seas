package uk.ac.cam.cl.gui;

import com.github.kevinsawicki.http.HttpRequest;
import impl.org.controlsfx.autocompletion.AutoCompletionTextFieldBinding;
import impl.org.controlsfx.autocompletion.SuggestionProvider;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import uk.ac.cam.cl.Main;
import uk.ac.cam.cl.data.Config;
import uk.ac.cam.cl.data.ConfigurationException;
import uk.ac.cam.cl.data.DataManager;
import uk.ac.cam.cl.data.apis.HereMaps;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class TopBar extends GridPane {
    private Main parent;

    private DataManager dm = DataManager.getInstance();

    List<String> places = new ArrayList<>();

    public TopBar(Main parent) {
        super();

        this.parent = parent;

        ColumnConstraints col0cons = new ColumnConstraints();
        col0cons.setPercentWidth(10);
        ColumnConstraints col1cons = new ColumnConstraints();
        col1cons.setPercentWidth(80);

        this.getColumnConstraints().addAll(col0cons, col1cons, col0cons);

        this.add(initLocButton(), 0, 0);
        this.add(initSearchBox(), 1, 0);
        this.add(initMenuButton(), 2, 0);
    }

    private Button initLocButton() {
        Button locBtn = new Button();
        locBtn.setText("#");
        GridPane.setHalignment(locBtn, HPos.CENTER);
        return locBtn;
    }

    private TextField initSearchBox() {
        TextField searchBox = new TextField();
        searchBox.setText("Location");
        GridPane.setHalignment(searchBox, HPos.CENTER);

        AutoCompletionBinding<String> stringAutoCompletionBinding = TextFields.bindAutoCompletion(searchBox, t-> {
            HereMaps maps = HereMaps.getInstance();
            places = maps.getPlaces(searchBox.getText());
            return places;
        });

        return searchBox;
    }

    private Button initMenuButton() {
        Button menuBtn = new Button();
        menuBtn.setText("+");
        GridPane.setHalignment(menuBtn, HPos.CENTER);
        menuBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("+ menu button pressed");
                parent.showMenu();
            }
        });

        return menuBtn;
    }
}
