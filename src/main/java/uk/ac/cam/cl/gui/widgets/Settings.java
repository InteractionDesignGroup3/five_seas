package uk.ac.cam.cl.gui.widgets;

import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import uk.ac.cam.cl.data.AppSettings;
import uk.ac.cam.cl.data.Unit;

/**
 * Generates a settings grid pane for any widget with a number of possible units
 *
 * @author Max Campman, Nathan Corbyn
 */
public class Settings extends GridPane {
  private String title;
  private Widget widget;
  private AppSettings settings = AppSettings.getInstance();

  /**
   * Create a new settings page for the given widget
   *
   * @param widget the widget to create the settings page for
   */
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
      button.setOnAction(
          (action) -> {
            settings.set(widget.getSettingName(), unit.toString());
          });
      if (unit == current) button.setSelected(true);
      add(button, 0, i);
      i++;
    }
  }

  /**
   * Get the unit value from the application settings
   *
   * @return the unit
   */
  public Unit getUnit() {
    return Unit.fromString(
        settings.getOrDefault(
            widget.getSettingName(), widget.getAvailableUnits().get(0).toString()));
  }
}
