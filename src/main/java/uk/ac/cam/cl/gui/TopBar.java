package uk.ac.cam.cl.gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import uk.ac.cam.cl.Main;

public class TopBar extends GridPane {
    private Main parent;

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
