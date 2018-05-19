package uk.ac.cam.cl.gui.widgets;

import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import uk.ac.cam.cl.data.AppSettings;
import uk.ac.cam.cl.data.Unit;

public class TemperatureSettings extends Settings {
    private AppSettings settings = AppSettings.getInstance();

    public TemperatureSettings() {
        SETTING_NAME = "temperatureGraphUnit";
        Label title = new Label();
        title.setText("SwellHeight Settings");
        title.getStyleClass().add("setting-title");

        ToggleGroup group = new ToggleGroup();
        RadioButton opt1 = new RadioButton();
        opt1.getStyleClass().add("radio-btn");
        opt1.setText("\u00b0C");
        opt1.setToggleGroup(group);
        opt1.setOnAction((action) -> {
            setUnit(Unit.CELSIUS);
        });

        RadioButton opt2 = new RadioButton();
        opt2.getStyleClass().add("radio-btn");
        opt2.setText("\u00bF");
        opt2.setToggleGroup(group);
        opt2.setOnAction((action) -> {
            setUnit(Unit.FAHRENHEIT);
        });

        this.add(title, 0, 0);
        this.add(opt1, 0, 1);
        this.add(opt2, 0, 2);
    }
}
