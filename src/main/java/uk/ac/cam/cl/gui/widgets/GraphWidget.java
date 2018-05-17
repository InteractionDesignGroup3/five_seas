package uk.ac.cam.cl.gui.widgets;

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
 * @author Ben Cole
 */
public abstract class GraphWidget extends Widget {

    private AreaChart<String, Number> chart;
    private CategoryAxis xAxis;
    private NumberAxis yAxis;

    public GraphWidget() {
        super();
        xAxis = new CategoryAxis();
        yAxis = new NumberAxis();
        chart = new AreaChart<>(xAxis, yAxis);
        chart.setTitle(getChartTitle());
        chart.setLegendVisible(false);
        DataManager.getInstance().addListener(this::plot);
        this.add(chart, 0, 0);
        // TODO Stop graphs from compacting vertically
    }

    /**
     * Returns the name of the chart, determined by the specific graph.
     * @return the name of the chart
     */
    protected abstract String getChartTitle();

    /**
     * Plots the passed data. It uses getRelevantData to extract the useful 
     * data from this DataPoint; this code therefore covers all types of graph.
     * @param dataSequenceList data sequences to potentially plot from
     */
    private void plot(List<DataSequence> dataSequenceList) {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        DataSequence toPlot = 
            dataSequenceList.get(DataManager.getInstance().getDay());
        for (int i = 0; i < toPlot.size(); i++) {
            //TODO Find a better way to plot only hourly values
            if (i % 4 != 0) continue;  
            DataPoint dataPoint = toPlot.get(i);
            Instant instant = dataPoint.getTimeAsDate().toInstant();
            String timeFormatted = DateTimeFormatter.ofPattern("HH:mm")
                    .withZone(ZoneId.systemDefault())
                    .format(instant);
            series.getData().add(new XYChart.Data<String, Number>(timeFormatted, 
                    getRelevantData(dataPoint)));
        }

        chart.getData().clear();
        chart.getData().add(series);
    }

    /**
     * Returns the needed data from a DataPoint object - this is specific to 
     * the graph being displayed. Subclasses implement this method and extract 
     * the relevant information from the passed DataPoint object.
     * @param dataPoint a DataPoint object from which to extract data
     * @return the data that was extracted from it
     */
    protected abstract double getRelevantData(DataPoint dataPoint);

}
