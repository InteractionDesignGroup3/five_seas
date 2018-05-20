package uk.ac.cam.cl.gui.widgets;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.StringConverter;
import uk.ac.cam.cl.data.*;

/**
 * This widget indicates wind speed and direction. It follows the metaphor of a weather vane.
 *
 * @author Mike Cachopo
 */
public class WindWidget extends Widget {
  private DataSequence dataSequence;
  private Label timeValue, gustSpeed, numberBox, north;
  private Shape compass, vane;
  private Slider timeSelecter;

  @Override
  public void initialise() {
    north = new Label("N");
    north.getStyleClass().add("cardinal");
    numberBox = new Label();
    timeValue = new Label();
    gustSpeed = new Label();
    timeSelecter = new Slider();
    Circle circle = new Circle(0, 0, 65);
    Rectangle rect = new Rectangle(0, -65, 65, 65);
    vane = Path.union(circle, rect);
    vane.setId("vane");
    compass = new Circle(0, 0, Math.sqrt(2) * 65);
    compass.setId("compass");

    // Assign vane and text box to the middle of the widget
    StackPane mainPane = new StackPane();
    mainPane.getChildren().addAll(compass, vane, north, numberBox);
    mainPane.setPrefHeight(400);
    mainPane.setPrefWidth(675);

    StackPane bottomPane = new StackPane();
    bottomPane.getChildren().addAll(gustSpeed, timeValue);
    StackPane.setAlignment(timeValue, Pos.BOTTOM_RIGHT);
    StackPane.setAlignment(gustSpeed, Pos.BOTTOM_LEFT);
    StackPane.setAlignment(north, Pos.TOP_CENTER);

    // Adjust the time selecter preferences
    timeSelecter.setMax(95);
    timeSelecter.setShowTickLabels(true);
    timeSelecter.setShowTickMarks(true);
    timeSelecter.setMajorTickUnit(24);
    timeSelecter.setMinorTickCount(24);
    timeSelecter.setSnapToTicks(true);
    timeSelecter.setBlockIncrement(1);

    // Properly display the labels
    timeSelecter.setLabelFormatter(
        new StringConverter<Double>() {
          @Override
          public String toString(Double d) {
            return intToDate(d.intValue());
          }

          @Override
          public Double fromString(String s) {
            return 0.0;
          }
        });

    // Make it so changing the slider updates the display
    timeSelecter
        .valueProperty()
        .addListener(
            (a, b, c) -> {
              this.update();
            });

    GridPane.setMargin(timeSelecter, new Insets(10, 10, 5, 10));
    // Add all components to the widget display
    add(mainPane, 0, 1);
    add(bottomPane, 0, 2);
    add(timeSelecter, 0, 3);
  }

  /** Updates the widget display. Should be called when either day of week or time of day change. */
  private void update() {
    // Number of 15 minute blocks past midnight, and associated DataPoint
    int i = (int) timeSelecter.getValue();
    DataPoint dataPoint = dataSequence.get(i);
    timeValue.setText(intToDate(i));
    double value = Math.round(getValueInCorrectUnit(dataPoint) * 10.0) / 10.0;
    String unitAsString = getUnit().toString();
    gustSpeed.setText("Gusts " + Double.toString(value) + unitAsString);
    vane.setRotate(dataPoint.getWindDirection() - 45);
    numberBox.setText(Double.toString(value) + unitAsString);
  }

  /**
   * Returns the wind speed of a DataPoint object in the selected unit
   *
   * @param dataPoint the DataPoint containing the data to be extracted
   *                  in the selected unit
   * @return the wind speed in the selected unit
   */
  private double getValueInCorrectUnit(DataPoint dataPoint) {
    Unit unit = getUnit();
    switch (unit) {
      case MILES_PER_HOUR:
        return dataPoint.getWindSpeedMPH();
      case METRES_PER_SECOND:
        return dataPoint.getWindSpeedMS();
      case KNOTS:
        return dataPoint.getWindSpeedKnots();
      case KILOMETERS_PER_HOUR:
      default:
        return dataPoint.getWindSpeedKmPH();
    }
  }

  /**
   * Updates the dataSequence of the widget and the display
   *
   * @param dataSequence Data from the DataManager class
   */
  @Override
  protected void displayData(DataSequence dataSequence) {
    this.dataSequence = dataSequence;
    this.update();
  }

  /**
   * Turns some number of 15 minute blocks past midnight into a time of day format
   *
   * @param i number of 15 minute blocks to add to midnight
   * @return the date as a string
   */
  private String intToDate(int i) {
    return DateTimeFormatter.ofPattern("HH:mm")
        .withZone(ZoneId.systemDefault())
        .format(
            new Date(dataSequence.get(0).getTime())
                .toInstant() // To account for different midnight times
                .plusSeconds(i * 60 * 15));
  }

  @Override
  public String getName() {
    return "Weather Vane";
  }

  @Override
  public String getSettingName() {
    return "windWidgetUnit";
  }

  @Override
  public List<Unit> getAvailableUnits() {
    return new ArrayList<Unit>(){
      private static final long serialVersionUID = 1L; 
      {
        add(Unit.KILOMETERS_PER_HOUR); 
        add(Unit.MILES_PER_HOUR); 
        add(Unit.METRES_PER_SECOND); 
        add(Unit.KNOTS); 
      }
    };
  }
}
