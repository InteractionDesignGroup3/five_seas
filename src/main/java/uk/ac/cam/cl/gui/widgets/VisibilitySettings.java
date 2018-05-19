package uk.ac.cam.cl.gui.widgets;

import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import uk.ac.cam.cl.data.AppSettings;
import uk.ac.cam.cl.data.Unit;

public class VisibilitySettings extends Settings {
    private AppSettings settings = AppSettings.getInstance();

    public VisibilitySettings() {
        SETTING_NAME = "visibilityGraphUnit";
        Label title = new Label();
        title.setText("Visibility Settings");
        title.getStyleClass().add("setting-title");

        ToggleGroup group = new ToggleGroup();
        RadioButton opt1 = new RadioButton();
        opt1.getStyleClass().add("radio-btn");
        opt1.setText("kilometres");
        opt1.setToggleGroup(group);
        opt1.setOnAction((action) -> {
            setUnit(Unit.KILOMETERS);
        });

        RadioButton opt2 = new RadioButton();
        opt2.getStyleClass().add("radio-btn");
        opt2.setText("miles");
        opt2.setToggleGroup(group);
        opt2.setOnAction((action) -> {
            setUnit(Unit.MILES);
        });

        this.add(title, 0, 0);
        this.add(opt1, 0, 1);
        this.add(opt2, 0, 2);
    }
}
