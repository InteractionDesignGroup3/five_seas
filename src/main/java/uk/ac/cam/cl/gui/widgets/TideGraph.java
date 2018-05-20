package uk.ac.cam.cl.gui.widgets;

import java.util.ArrayList;
import java.util.List;
import uk.ac.cam.cl.data.DataPoint;
import uk.ac.cam.cl.data.Unit;

/**
 * Shows a plot of tide data for one day
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
  protected double getRelevantData(DataPoint dataPoint) {
    switch (getUnit()) {
      case FEET:
        return dataPoint.getTideHeightFeet();
      case METERS:
      default:
        return dataPoint.getTideHeightM();
    }
  }

  @Override
  public String getSettingName() {
    return "tideGraphUnit";
  }

  @Override
  public List<Unit> getAvailableUnits() {
    return new ArrayList<Unit>() {
      private static final long serialVersionUID = 1L;

      {
        add(Unit.METERS);
        add(Unit.FEET);
      }
    };
  }
}
