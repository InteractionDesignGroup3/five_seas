package uk.ac.cam.cl.gui;

import java.util.Calendar;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;
import javafx.util.StringConverter;
import uk.ac.cam.cl.data.DataManager;

public class BottomBar extends GridPane {
  private Application parent;
  private DataManager dm = DataManager.getInstance();

  public BottomBar(Application parent) {
    super();

    this.parent = parent;

    this.setId("bottom-bar");

    Slider daySelect = new Slider();
    daySelect.setId("day-select");
    daySelect.setMin(0);
    daySelect.setMax(6);
    daySelect.setPrefWidth(300.0);
    daySelect.setValue(0);
    daySelect.setBlockIncrement(1);
    daySelect.setMajorTickUnit(1);
    daySelect.setMinorTickCount(0);
    daySelect.setShowTickLabels(true);
    daySelect.setShowTickMarks(true);
    daySelect.setSnapToTicks(true);
    daySelect.setLabelFormatter(
        new StringConverter<Double>() {

          @Override
          public String toString(Double f) {
            Calendar calendar = Calendar.getInstance();
            int day = calendar.get(Calendar.DAY_OF_WEEK);
            switch ((int) (Math.round(f) + day - Calendar.MONDAY) % 7) {
              case 0:
                return "Mo";
              case 1:
                return "Tu";
              case 2:
                return "We";
              case 3:
                return "Th";
              case 4:
                return "Fr";
              case 5:
                return "Sa";
              case 6:
                return "Su";
              default:
                return "-";
            }
          }

          @Override
          public Double fromString(String s) {
            return 0.0;
          }
        });

    daySelect
        .valueProperty()
        .addListener(
            new ChangeListener<Number>() {
              @Override
              public void changed(
                  ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                dm.setDay(newValue.intValue());
              }
            });

    GridPane.setHalignment(daySelect, HPos.CENTER);
    this.setAlignment(Pos.CENTER);

    this.add(daySelect, 0, 0);
  }
}
