package uk.ac.cam.cl.gui.widgets;

import uk.ac.cam.cl.data.AppSettings;
import uk.ac.cam.cl.data.DataPoint;
import uk.ac.cam.cl.data.Unit;

/**
 * Displays wind speed for one day in a graph.
 *
 * @author Ben Cole
 */
public class PrecipitationGraph extends GraphWidget {

  public static final String PRECIPITATION_GRAPH_UNIT_SETTINGS = "precipitationGraphUnit";

  public PrecipitationGraph() {
    super();
    getStyleClass().add("precipitation-graph");
  }

  @Override
  public String getName() {
    return "Precipitation";
  }

  @Override
  public Unit getUnit() {
    String unit = AppSettings
            .getInstance()
            .getOrDefault(PRECIPITATION_GRAPH_UNIT_SETTINGS,
                    Unit.MILIMETERS.toString());
    return Unit.fromString(unit);
  }

  @Override
  protected double getRelevantData(DataPoint dataPoint) {
    switch (getUnit()) {
      case INCHES:
        return dataPoint.getPrecipitationInches();
      case MILIMETERS:
      default:
        return dataPoint.getPrecipitationMM();
    }
  }
}
