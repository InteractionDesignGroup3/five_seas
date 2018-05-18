package uk.ac.cam.cl.gui.widgets;

import uk.ac.cam.cl.data.AppSettings;
import uk.ac.cam.cl.data.DataPoint;

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
  public String getUnit() {
    return isMeters() ? "m" : "ft";
  }

  @Override
  protected double getRelevantData(DataPoint dataPoint) {
    double data = isMeters() ? dataPoint.getSwellHeightM() : dataPoint.getSwellHeightFeet();
    return Math.max(data, 0.0);
  }

  private boolean isMeters() {
    return AppSettings.getInstance()
            .getOrDefault("heightUnit", "meter")
            .equals("meter");
  }
}
