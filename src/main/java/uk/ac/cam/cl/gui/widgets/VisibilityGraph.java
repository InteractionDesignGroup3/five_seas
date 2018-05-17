package uk.ac.cam.cl.gui.widgets;

import uk.ac.cam.cl.data.DataPoint;

public class VisibilityGraph extends GraphWidget {

    @Override
    public String getName() {
        return "Visibility";
    }

    @Override
    protected double getRelevantData(DataPoint dataPoint) {
        return dataPoint.getVisibilityMiles();
    }
}
