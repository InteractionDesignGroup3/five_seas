package uk.ac.cam.cl.gui.widgets;

import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;

import uk.ac.cam.cl.data.AppSettings;
import uk.ac.cam.cl.data.Unit;

public class Settings extends GridPane {
    private String title;
    private Widget widget;

    private AppSettings settings = AppSettings.getInstance();

    public Settings(Widget widget) {
        getStyleClass().add("widget");
        getStyleClass().add("widget-setting");
        this.widget = widget;

        int i = 0;
        ToggleGroup group = new ToggleGroup();
        Unit current = getUnit();
        for (Unit unit : widget.getAvailableUnits()) {
          RadioButton button = new RadioButton();
          button.setText(unit.toString());
          button.setToggleGroup(group);
          button.setOnAction((action) -> {
            setUnit(unit);
          });
          if (unit == current) button.setSelected(true);
          add(button, 0, i);
          i++;
        }
    }

    public Unit getUnit() {
        return Unit.fromString(settings.getOrDefault(widget.getSettingName(), widget.getAvailableUnits().get(0).toString()));
    }

    public void setUnit(Unit unit) {
        settings.set(widget.getSettingName(), unit.toString());
    }

    public String getTitle() {
        return title;
    }
}
