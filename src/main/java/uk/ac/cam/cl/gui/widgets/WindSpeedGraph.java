package uk.ac.cam.cl.gui.widgets;

import uk.ac.cam.cl.data.AppSettings;
import uk.ac.cam.cl.data.DataPoint;
import uk.ac.cam.cl.data.Unit;

/**
 * Displays wind speed for one day in a graph.
 *
 * @author Ben Cole
 */
public class WindSpeedGraph extends GraphWidget {

  public static final String WIND_SPEED_GRAPH_UNIT_SETTINGS = "windSpeedGraphUnit";

  public WindSpeedGraph() {
    super();
    getStyleClass().add("wind-speed-graph");
  }

  @Override
  public String getName() {
    return "Wind Speed";
  }

  @Override
  public Unit getUnit() {
    String unit = AppSettings
            .getInstance()
            .getOrDefault(WIND_SPEED_GRAPH_UNIT_SETTINGS,
                    Unit.KILOMETERS_PER_HOUR.toString());
    return Unit.fromString(unit);
  }

  @Override
  protected double getRelevantData(DataPoint dataPoint) {
    switch (getUnit()) {
      case MILES_PER_HOUR:
        return dataPoint.getWindSpeedMPH();
      case KILOMETERS_PER_HOUR:
      default:
        return dataPoint.getWindSpeedKmPH();
    }
  }
}
