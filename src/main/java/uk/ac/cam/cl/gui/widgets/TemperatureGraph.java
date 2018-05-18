package uk.ac.cam.cl.gui.widgets;

import uk.ac.cam.cl.data.AppSettings;
import uk.ac.cam.cl.data.DataPoint;

/**
 * Shows a plot of temperature data for one day.
 *
 * @author Ben Cole
 */
public class TemperatureGraph extends GraphWidget {
  public TemperatureGraph() {
    super();
    getStyleClass().add("temperature-graph");
  }

  @Override
  protected double getRelevantData(DataPoint dataPoint) {
    return isCelsius() ? dataPoint.getTemperatureCelsius() : dataPoint.getTemperatureFahrenheit();
  }

  @Override
  public String getName() {
    return "Temperature";
  }

  @Override
  public String getUnit() {
    return isCelsius() ? "°C" : "°F";
  }

  private boolean isCelsius() {
    return AppSettings.getInstance()
            .getOrDefault("temperatureUnit", "celsius")
            .equals("celsius");
  }
}
