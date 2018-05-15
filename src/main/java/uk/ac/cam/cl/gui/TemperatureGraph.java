package uk.ac.cam.cl.gui;


import uk.ac.cam.cl.data.DataPoint;

public class TemperatureGraph extends GraphWidget {

    @Override
    protected double getRelevantData(DataPoint dataPoint) {
        return dataPoint.getTemperatureCelcius();
    }
}
