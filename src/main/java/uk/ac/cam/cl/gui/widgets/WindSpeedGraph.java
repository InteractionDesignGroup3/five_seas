package uk.ac.cam.cl.gui.widgets;

import uk.ac.cam.cl.data.DataPoint;

public class WindSpeedGraph extends GraphWidget {
    @Override
    protected String getChartTitle() {
        return "Wind Speed";
    }

    @Override
    protected double getRelevantData(DataPoint dataPoint) {
        return dataPoint.getWindSpeedMPH();
    }
}
