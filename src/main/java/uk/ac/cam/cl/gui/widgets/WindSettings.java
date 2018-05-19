package uk.ac.cam.cl.gui.widgets;

import javafx.scene.control.Label;
import uk.ac.cam.cl.data.AppSettings;

public class WindSettings extends Settings {
    private AppSettings settings = AppSettings.getInstance();

    private static String SETTING_NAME = "wind_unit";

    public WindSettings() {
        Label title = new Label();
        title.setText("Wind Settings");
        this.add(title, 0, 0);
    }

    @Override
    public String getUnit() {
        return settings.get(SETTING_NAME);
    }

    @Override
    public void setUnit(String unit) {
        settings.set(SETTING_NAME, unit);
    }
}
