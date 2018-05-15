package uk.ac.cam.cl.gui;


import uk.ac.cam.cl.data.DataPoint;

/**
 * Shows a plot of temperature data for one day.
 * @author Ben Cole
 */
public class TemperatureGraph extends GraphWidget {

    @Override
    protected double getRelevantData(DataPoint dataPoint) {
        return dataPoint.getTemperatureCelcius();
    }

    @Override
    protected String getChartTitle() {
        return "Temperature";
    }
}
