package uk.ac.cam.cl.gui.widgets;

import javafx.scene.control.*;
import uk.ac.cam.cl.data.AppSettings;
import uk.ac.cam.cl.data.Unit;

import java.util.ArrayList;
import java.util.List;

public class WindSpeedSettings extends Settings {
    private AppSettings settings = AppSettings.getInstance();

    private static String SETTING_NAME = "windSpeedGraphUnit";

    public WindSpeedSettings() {
        Label title = new Label();
        title.setText("Wind Settings");
        title.getStyleClass().add("setting-title");

        ToggleGroup group = new ToggleGroup();
        RadioButton kph = new RadioButton();
        kph.getStyleClass().add("radio-btn");
        kph.setText("KM/H");
        kph.setToggleGroup(group);
        kph.setOnAction((action) -> {
            setUnit(Unit.KILOMETERS_PER_HOUR);
        });

        RadioButton mph = new RadioButton();
        mph.getStyleClass().add("radio-btn");
        mph.setText("MPH");
        mph.setToggleGroup(group);
        mph.setOnAction((action) -> {
            setUnit(Unit.MILES_PER_HOUR);
        });

        this.add(title, 0, 0);
        this.add(kph, 0, 1);
        this.add(mph, 0, 2);
    }

    @Override
    public Unit getUnit() {
        return Unit.fromString(settings.get(SETTING_NAME));
    }

    @Override
    public void setUnit(Unit unit) {
        settings.set(SETTING_NAME, (String) unit.toString());
    }
}
