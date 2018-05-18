package uk.ac.cam.cl.gui.widgets;

import uk.ac.cam.cl.data.AppSettings;
import uk.ac.cam.cl.data.DataPoint;

/**
 * Displays wind speed for one day in a graph.
 *
 * @author Ben Cole
 */
public class WindSpeedGraph extends GraphWidget {
  public WindSpeedGraph() {
    super();
    getStyleClass().add("wind-speed-graph");
  }

  @Override
  public String getName() {
    return "Wind Speed";
  }

  @Override
  public String getUnit() {
    return isMPH() ? "mph" : "kph";
  }

  @Override
  protected double getRelevantData(DataPoint dataPoint) {
    return isMPH() ? dataPoint.getWindSpeedMPH() : dataPoint.getWindSpeedKmPH();
  }

  private boolean isMPH() {
    return AppSettings.getInstance()
            .getOrDefault("speedUnit", "mph")
            .equals("mph");
  }
}
