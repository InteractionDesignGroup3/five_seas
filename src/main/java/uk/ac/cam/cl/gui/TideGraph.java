package uk.ac.cam.cl.gui;

import uk.ac.cam.cl.data.DataPoint;

public class TideGraph extends GraphWidget {

    @Override
    protected String getChartTitle() {
        return "Tides";
    }

    @Override
    protected double getRelevantData(DataPoint dataPoint) {
        return dataPoint.getTideHeightM();
    }
}