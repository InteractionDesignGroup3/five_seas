package uk.ac.cam.cl.gui.widgets;

import java.util.ArrayList;
import java.util.List;

import uk.ac.cam.cl.data.AppSettings;
import uk.ac.cam.cl.data.DataPoint;
import uk.ac.cam.cl.data.Unit;

/**
 * Displays wind speed for one day in a graph
 *
 * @author Ben Cole
 */
public class PrecipitationGraph extends GraphWidget {
  public PrecipitationGraph() {
    super();
    getStyleClass().add("precipitation-graph");
  }

  @Override
  public String getName() {
    return "Precipitation";
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
  
  @Override
  public String getSettingName() {
    return "precipitationGraphUnit";
  }

  @Override
  public List<Unit> getAvailableUnits() {
    return new ArrayList<Unit>(){
      private static final long serialVersionUID = 1L; 
      {
        add(Unit.MILIMETERS); 
        add(Unit.INCHES); 
      }
    };
  }
}
