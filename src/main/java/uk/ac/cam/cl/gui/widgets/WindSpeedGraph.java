package uk.ac.cam.cl.gui.widgets;

import uk.ac.cam.cl.data.DataPoint;

/**
 * Displays wind speed for one day in a graph.
 * @author Ben Cole
 */
public class WindSpeedGraph extends GraphWidget {

    public WindSpeedGraph() {
        super();
        getStyleClass().add("wind-speed-graph");
    }

    @Override
    public String getName() {
        return "Wind Speed";
    }

    @Override
    protected double getRelevantData(DataPoint dataPoint) {
        return dataPoint.getWindSpeedMPH();
    }
}
