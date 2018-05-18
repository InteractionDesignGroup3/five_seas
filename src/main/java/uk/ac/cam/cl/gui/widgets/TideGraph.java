package uk.ac.cam.cl.gui.widgets;

import uk.ac.cam.cl.data.DataPoint;

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
  protected double getRelevantData(DataPoint dataPoint) {
    return dataPoint.getTideHeightM();
  }
}
