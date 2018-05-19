package uk.ac.cam.cl.gui.widgets;

import uk.ac.cam.cl.data.AppSettings;
import uk.ac.cam.cl.data.DataPoint;
import uk.ac.cam.cl.data.Unit;

/**
 * Shows a plot of tide data for one day.
 *
 * @author Ben Cole
 */
public class TideGraph extends GraphWidget {
  public TideGraph() {
    super();
    getStyleClass().add("tide-graph");
  }

  @Override
  public String getName() {
    return "Tides";
  }

  @Override
  public Unit getUnit() {
    String unit = AppSettings.getInstance().getOrDefault("tideGraphUnit", Unit.METERS.toString());
    return Unit.fromString(unit);
  }

  @Override
  protected double getRelevantData(DataPoint dataPoint) {
    switch (getUnit()) {
      case FEET:
        return dataPoint.getTideHeightFeet();
      case METERS:
      default:
        return dataPoint.getTideHeightM();
    }
  }

  private boolean isMeters() {
    return AppSettings.getInstance()
            .getOrDefault("heightUnit", "meter")
            .equals("meter");
  }
}
