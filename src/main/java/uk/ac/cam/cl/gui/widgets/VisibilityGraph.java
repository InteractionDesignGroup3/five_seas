package uk.ac.cam.cl.gui.widgets;

import java.util.ArrayList;
import java.util.List;
import uk.ac.cam.cl.data.DataPoint;
import uk.ac.cam.cl.data.Unit;

/**
 * Shows a plot of the visibility data for one day
 *
 * @author Ben Cole
 */
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
  protected double getRelevantData(DataPoint dataPoint) {
    switch (getUnit()) {
      case MILES:
        return dataPoint.getVisibilityMiles();
      case KILOMETERS:
      default:
        return dataPoint.getVisibilityKM();
    }
  }

  @Override
  public String getSettingName() {
    return "visibilityGraphUnit";
  }

  @Override
  public List<Unit> getAvailableUnits() {
    return new ArrayList<Unit>() {
      private static final long serialVersionUID = 1L;

      {
        add(Unit.KILOMETERS);
        add(Unit.MILES);
      }
    };
  }
}
