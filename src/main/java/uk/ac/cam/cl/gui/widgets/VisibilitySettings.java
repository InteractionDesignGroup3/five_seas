package uk.ac.cam.cl.gui.widgets;

import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import uk.ac.cam.cl.data.AppSettings;
import uk.ac.cam.cl.data.Unit;

public class VisibilitySettings extends Settings {
    private AppSettings settings = AppSettings.getInstance();

    public VisibilitySettings() {
        SETTING_NAME = VisibilityGraph.VISIBILITY_GRAPH_UNIT_SETTINGS;

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

        Unit curr = getUnit();
        if (curr == Unit.KILOMETERS) {
            opt1.setSelected(true);
        } else {
            opt2.setSelected(true);
        }

        this.add(opt1, 0, 1);
        this.add(opt2, 0, 2);
    }

    @Override
    public Unit getUnit() {
        return Unit.fromString(settings.getOrDefault(SETTING_NAME, Unit.KILOMETERS.toString()));
    }
}
