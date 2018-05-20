package uk.ac.cam.cl.gui.widgets;

import java.util.ArrayList;
import java.util.List;
import uk.ac.cam.cl.data.DataPoint;
import uk.ac.cam.cl.data.Unit;

/**
 * Shows a plot of swell height for one day
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

  @Override
  public String getSettingName() {
    return "swellHeightGraphUnit";
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
