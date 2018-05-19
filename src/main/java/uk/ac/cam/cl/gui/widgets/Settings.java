package uk.ac.cam.cl.gui.widgets;

import javafx.scene.layout.GridPane;
import uk.ac.cam.cl.data.AppSettings;
import uk.ac.cam.cl.data.Unit;

public class Settings extends GridPane {
    protected String SETTING_NAME = "";

    private String title;

    private AppSettings settings = AppSettings.getInstance();

    public Settings() {
        this.getStyleClass().add("widget");
        this.getStyleClass().add("widget-setting");
    }

    public Unit getUnit() {
        return Unit.fromString(settings.get(SETTING_NAME));
    }

    public void setUnit(Unit unit) {
        settings.set(SETTING_NAME, (String) unit.toString());
    }

    public void setTitle(String tit) {
        title = tit;
    }

    public String getTitle() {
        return title;
    }
}
