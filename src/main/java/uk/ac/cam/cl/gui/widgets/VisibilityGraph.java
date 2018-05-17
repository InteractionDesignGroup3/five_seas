package uk.ac.cam.cl.gui.widgets;

import uk.ac.cam.cl.data.DataPoint;

public class VisibilityGraph extends GraphWidget {

    @Override
    protected String getChartTitle() {
        return "Visibility";
    }

    @Override
    protected double getRelevantData(DataPoint dataPoint) {
        return dataPoint.getVisibilityMiles();
    }
}
