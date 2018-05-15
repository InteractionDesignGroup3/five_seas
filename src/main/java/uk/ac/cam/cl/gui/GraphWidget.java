package uk.ac.cam.cl.gui;


import javafx.scene.chart.*;
import uk.ac.cam.cl.data.DataManager;
import uk.ac.cam.cl.data.DataPoint;
import uk.ac.cam.cl.data.DataSequence;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;


/**
 * A base Graph widget for displaying plots of data against time.
 */
public abstract class GraphWidget extends Widget {

    private AreaChart<String, Number> mChart;

    public GraphWidget() {
        super();

        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        mChart = new AreaChart<>(xAxis, yAxis);
        mChart.setLegendVisible(false);
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
        XYChart.Series series = new XYChart.Series();
        DataSequence toPlot = dataSequenceList.get(DataManager.getInstance().getDay());
        for (int i = 0; i < toPlot.size(); i++) {
            if (i % 4 != 0) continue;  // TODO: Find a better way to plot only hourly values
            DataPoint dataPoint = toPlot.get(i);
            Instant instant = dataPoint.getTimeAsDate().toInstant();
            String timeFormatted = DateTimeFormatter.ofPattern("HH:mm").withZone(ZoneId.systemDefault()).format(instant);
            series.getData().add(new XYChart.Data(timeFormatted, getRelevantData(dataPoint)));
        }
        mChart.getData().clear();
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
