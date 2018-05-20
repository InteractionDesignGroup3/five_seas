package uk.ac.cam.cl.gui.widgets;

import java.util.ArrayList;
import java.util.List;

import uk.ac.cam.cl.data.AppSettings;
import uk.ac.cam.cl.data.DataPoint;
import uk.ac.cam.cl.data.Unit;

/**
 * Shows a plot of temperature data for one day.
 *
 * @author Ben Cole
 */
public class TemperatureGraph extends GraphWidget {
  public TemperatureGraph() {
    super();
    getStyleClass().add("temperature-graph");
  }

  @Override
  protected double getRelevantData(DataPoint dataPoint) {
    switch (getUnit()) {
      case FAHRENHEIT:
        return dataPoint.getTemperatureFahrenheit();
      case CELSIUS:
      default:
        return dataPoint.getTemperatureCelsius();
    }
  }

  @Override
  public String getName() {
    return "Temperature";
  }

  @Override
  public String getSettingName() {
    return "temperatureGraphUnit";
  }

  @Override
  public List<Unit> getAvailableUnits() {
    return new ArrayList<Unit>(){
      private static final long serialVersionUID = 1L; 
      {
        add(Unit.CELSIUS); 
        add(Unit.FAHRENHEIT); 
      }
    };
  }
}
