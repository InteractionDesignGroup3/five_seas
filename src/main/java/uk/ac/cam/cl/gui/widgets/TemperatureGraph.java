package uk.ac.cam.cl.gui.widgets;

import uk.ac.cam.cl.data.AppSettings;
import uk.ac.cam.cl.data.DataPoint;
import uk.ac.cam.cl.data.Unit;

/**
 * Shows a plot of temperature data for one day.
 *
 * @author Ben Cole
 */
public class TemperatureGraph extends GraphWidget {

  public static final String TEMPERATURE_GRAPH_UNIT_SETTINGS = "temperatureGraphUnit";

  public TemperatureGraph() {
    super();
    getStyleClass().add("temperature-graph");
  }

  @Override
  protected double getRelevantData(DataPoint dataPoint) {
    switch (getUnit()) {
      case FAHRENHEIT:
        return dataPoint.getTemperatureFahrenheit();
      case CELSIUS:
      default:
        return dataPoint.getTemperatureCelsius();
    }
  }

  @Override
  public String getName() {
    return "Temperature";
  }

  @Override
  public Unit getUnit() {
    String unitName = AppSettings.getInstance()
            .getOrDefault(TEMPERATURE_GRAPH_UNIT_SETTINGS,
                    Unit.CELSIUS.toString());
    return Unit.fromString(unitName);
  }

}
