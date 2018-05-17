package uk.ac.cam.cl.gui.widgets;

import uk.ac.cam.cl.data.DataPoint;

/**
 * Shows a plot of swell height for one day.
 * @author Ben Cole
 */
public class SwellHeightGraph extends GraphWidget {

    @Override
    public String getName() {
        return "Swell Height";
    }

    @Override
    protected double getRelevantData(DataPoint dataPoint) {
        return Math.max(dataPoint.getSwellHeightM(), 0.0);
    }
}
