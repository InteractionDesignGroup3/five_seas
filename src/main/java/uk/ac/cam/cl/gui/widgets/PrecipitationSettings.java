package uk.ac.cam.cl.gui.widgets;

import javafx.scene.control.*;
import uk.ac.cam.cl.data.AppSettings;
import uk.ac.cam.cl.data.Unit;

import java.util.ArrayList;
import java.util.List;

public class PrecipitationSettings extends Settings {
    private AppSettings settings = AppSettings.getInstance();

    public PrecipitationSettings() {
        SETTING_NAME = PrecipitationGraph.PRECIPITATION_GRAPH_UNIT_SETTINGS;

        ToggleGroup group = new ToggleGroup();
        RadioButton mm = new RadioButton();
        mm.getStyleClass().add("radio-btn");
        mm.setText("mm");
        mm.setToggleGroup(group);
        mm.setOnAction((action) -> {
            setUnit(Unit.MILIMETERS);
        });

        RadioButton in = new RadioButton();
        in.getStyleClass().add("radio-btn");
        in.setText("in");
        in.setToggleGroup(group);
        in.setOnAction((action) -> {
            setUnit(Unit.INCHES);
        });

        Unit curr = getUnit();
        if (curr == Unit.MILIMETERS) mm.setSelected(true);
        else in.setSelected(true);

        this.add(mm, 0, 1);
        this.add(in, 0, 2);
    }

    @Override
    public Unit getUnit() {
        return Unit.fromString(settings.getOrDefault(SETTING_NAME, Unit.KILOMETERS_PER_HOUR.toString()));
    }
}
