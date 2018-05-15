package uk.ac.cam.cl.gui;


import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import uk.ac.cam.cl.data.DataManager;
import uk.ac.cam.cl.data.DataPoint;
import uk.ac.cam.cl.data.DataSequence;

import java.util.List;

public abstract class GraphWidget extends Widget {

    private AreaChart<Number, Number> mChart;

    public GraphWidget() {
        super();

        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        mChart = new AreaChart<>(xAxis, yAxis);
        DataManager.getInstance().addListener(this::plot);

        this.add(mChart, 0, 0);
    }

    /**
     * Plots the passed data. It uses getRelevantData to extract the useful data from this
     * DataPoint; this code therefore covers all types of graph.
     * @param dataSequenceList A List of DataSequence objects, where the DataSequence at index
     *                         n contains DataPoint objects for the day n days from today.
     */
    private void plot(List<DataSequence> dataSequenceList) {
        int index = getSelectedDay();
        XYChart.Series series = new XYChart.Series();
        DataSequence toPlot = dataSequenceList.get(index);
        for (int i = 0; i < toPlot.size(); i++) {
            DataPoint dataPoint = toPlot.get(i);
            series.getData().add(new XYChart.Data(dataPoint.getTime(), getRelevantData(dataPoint)));
        }
        mChart.getData().addAll(series);
    }

    protected abstract double getRelevantData(DataPoint dataPoint);

    /**
     * Returns the currently selected day, determined by the user's selection on the slider
     * @return currentDay, an integer in [0, 6]
     */
    private int getSelectedDay() {
        // TODO: Handle passing selected day
        return 0;
    }


}
