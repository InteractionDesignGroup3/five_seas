package uk.ac.cam.cl.gui.widgets;

import javafx.scene.chart.*;
import javafx.util.StringConverter;
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

    private AreaChart<Number, Number> chart;
    private NumberAxis xAxis;
    private NumberAxis yAxis;

    public GraphWidget() {
        super();
        xAxis = new NumberAxis();
        yAxis = new NumberAxis();
        chart = new AreaChart<>(xAxis, yAxis);

        xAxis.setTickLabelFormatter(new StringConverter<Number>() {
            @Override
            public String toString(Number time) {
//                Instant instant = dataPoint.getTimeAsDate().toInstant();
                Instant instant = Instant.ofEpochMilli(time.longValue());
                System.out.println(time.longValue());
                String timeFormatted = DateTimeFormatter.ofPattern("HH:mm")
                        .withZone(ZoneId.systemDefault())
                        .format(instant);
                return timeFormatted;
            }

            @Override
            public Number fromString(String string) {
                return null;  // Never called
            }
        });
        xAxis.setForceZeroInRange(false);
//        xAxis.setTickUnit(1000*60*60*3);
//        xAxis.setTickCo
//        xAxis.setMinorTickCount(24);

        chart.setLegendVisible(false);
        DataManager.getInstance().addListener(this::plot);
        this.add(chart, 0, 0);
        // TODO Stop graphs from compacting vertically
    }

    /**
     * Plots the passed data. It uses getRelevantData to extract the useful 
     * data from this DataPoint; this code therefore covers all types of graph.
     * @param dataSequenceList data sequences to potentially plot from
     */
    private void plot(List<DataSequence> dataSequenceList) {
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        DataSequence toPlot = 
            dataSequenceList.get(DataManager.getInstance().getDay());
        for (int i = 0; i < toPlot.size(); i++) {
            //TODO Find a better way to plot only hourly values
            if (i % 4 != 0) continue;  
            DataPoint dataPoint = toPlot.get(i);
//            Instant instant = dataPoint.getTimeAsDate().toInstant();
//            String timeFormatted = DateTimeFormatter.ofPattern("HH:mm")
//                    .withZone(ZoneId.systemDefault())
//                    .format(instant);
//            long dayStart = dataPoint.getTimeAsDate().toInstant()
            series.getData().add(new XYChart.Data<>(dataPoint.getTime(),
                    getRelevantData(dataPoint)));
            xAxis.setLowerBound(series.getData().get(0).getXValue().longValue());
            xAxis.setUpperBound(series.getData().get(series.getData().size() - 1).getXValue().longValue());
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
