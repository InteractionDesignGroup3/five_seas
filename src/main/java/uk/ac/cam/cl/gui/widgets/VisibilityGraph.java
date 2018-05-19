package uk.ac.cam.cl.gui.widgets;

import uk.ac.cam.cl.data.AppSettings;
import uk.ac.cam.cl.data.DataPoint;

public class VisibilityGraph extends GraphWidget {
  public VisibilityGraph() {
    super();
    getStyleClass().add("visibility-graph");
  }

  @Override
  public String getName() {
    return "Visibility";
  }

  @Override
  public String getUnit() {
    return isMiles() ? "mi" : "km";
  }

  @Override
  protected double getRelevantData(DataPoint dataPoint) {
    return isMiles() ? dataPoint.getVisibilityMiles() : dataPoint.getVisibilityKM();
  }

  private boolean isMiles() {
    return AppSettings.getInstance()
            .getOrDefault("distanceUnit", "kilometers")
            .equals("miles");
  }
}
