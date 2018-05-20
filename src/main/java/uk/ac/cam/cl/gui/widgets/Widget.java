package uk.ac.cam.cl.gui.widgets;

import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import uk.ac.cam.cl.data.DataManager;
import uk.ac.cam.cl.data.DataSequence;
import uk.ac.cam.cl.data.Unit;

import java.util.List;

/**
 * Encapsulates all widgets.
 *
 * @author Ben Cole
 */
public abstract class Widget extends GridPane {

  private boolean initialised;
  private Label noDataLabel;

  public Widget() {
    super();
    this.getStyleClass().add("widget");
    initialised = false;
    noDataLabel = new Label("No Data");
    add(noDataLabel, 0, 0);
    DataManager.getInstance().addListener(this::temporaryHandleNewData);
  }

  /**
   * Returns the name of the widget to be displayed in its border.
   *
   * @return the name of the widget
   */
  public abstract String getName();

  /**
   * Returns the Unit in use by this widget.
   *
   * @return the Unit in use
   */
  public abstract Unit getUnit();

  /**
   * Sets up the interface, ready to start displaying data.
   */
  public abstract void initialise();

  /**
   * Temporarily handles the old DataManager listener implementation that
   * sends the whole week's data rather than just the correct day's data.
   *
   * TODO: GET RID OF THIS METHOD!!
   *
   * @param dataSequenceList the list of all data for this week in this location
   */
  public void temporaryHandleNewData(List<DataSequence> dataSequenceList) {
    handleNewData(dataSequenceList.get(DataManager.getInstance().getDay()));
  }

  /**
   * Handles processing and displaying any new data that is given to this
   * widget. If this is the first time that any data has been supplied to the
   * widget, this method also handles removing the 'no data' placeholder Label
   * and sets up the widget's actual interface.
   *
   * @param dataSequence the data sequence for the selected day in the selected
   *                     location
   */
  public void handleNewData(DataSequence dataSequence) {
    if (!initialised) {
      getChildren().remove(noDataLabel);
      initialise();
      initialised = true;
    }
    displayData(dataSequence);
  }

  /**
   * Removes all currently displayed data and instead displays data from the
   * passed DataSequence object.
   *
   * @param dataSequence the sequence of data to be used or displayed by the
   *                     widget
   */
  protected abstract void displayData(DataSequence dataSequence);
}
