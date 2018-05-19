package uk.ac.cam.cl.gui.widgets;

import uk.ac.cam.cl.data.AppSettings;
import uk.ac.cam.cl.data.DataPoint;
import uk.ac.cam.cl.data.Unit;

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
  public Unit getUnit() {
    String unit = AppSettings
            .getInstance()
            .getOrDefault("visibilityGraphUnit", Unit.KILOMETERS.toString());
    return Unit.fromString(unit);
  }

  @Override
  protected double getRelevantData(DataPoint dataPoint) {
    switch (getUnit()) {
      case MILES:
        return dataPoint.getVisibilityMiles();
      case KILOMETERS:
      default:
        return dataPoint.getVisibilityKM();
    }
  }
}
