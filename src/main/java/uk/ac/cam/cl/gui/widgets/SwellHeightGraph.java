package uk.ac.cam.cl.gui.widgets;

import uk.ac.cam.cl.data.AppSettings;
import uk.ac.cam.cl.data.DataPoint;
import uk.ac.cam.cl.data.Unit;

/**
 * Shows a plot of swell height for one day.
 *
 * @author Ben Cole
 */
public class SwellHeightGraph extends GraphWidget {

  public SwellHeightGraph() {
    super();
    getStyleClass().add("swell-height-graph");
  }

  @Override
  public String getName() {
    return "Swell Height";
  }

  @Override
  public Unit getUnit() {
    String data = AppSettings
            .getInstance()
            .getOrDefault("swellHeightGraphUnit", Unit.METERS.toString());
    return Unit.fromString(data);
  }

  @Override
  protected double getRelevantData(DataPoint dataPoint) {
    double data; // = isMeters() ? dataPoint.getSwellHeightM() : dataPoint.getSwellHeightFeet();
    switch (getUnit()) {
      case FEET:
        data = dataPoint.getSwellHeightFeet();
        break;
      case METERS:
      default:
        data = dataPoint.getSwellHeightM();
    }
    return Math.max(data, 0.0);
  }
}
