package uk.ac.cam.cl.gui.widgets;

import uk.ac.cam.cl.data.DataPoint;

public class SwellHeightGraph extends GraphWidget {

    @Override
    protected String getChartTitle() {
        return "Swell Height";
    }

    @Override
    protected double getRelevantData(DataPoint dataPoint) {
        return Math.max(dataPoint.getSwellHeightM(), 0.0);
    }
}
