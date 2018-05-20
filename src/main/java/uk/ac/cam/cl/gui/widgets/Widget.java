package uk.ac.cam.cl.gui.widgets;

import javafx.geometry.HPos;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import uk.ac.cam.cl.data.DataManager;
import uk.ac.cam.cl.data.DataSequence;
import uk.ac.cam.cl.data.Unit;


/**
 * Encapsulates all widgets.
 *
 * @author Ben Cole
 */
public abstract class Widget extends GridPane {

  private boolean initialised;
  private Text noData;
  private DataSequence dataSequence;

  public Widget() {
    super();
    this.getStyleClass().add("widget");
    initialised = false;

    ColumnConstraints col = new ColumnConstraints();
    col.setPercentWidth(100);
    getColumnConstraints().addAll(col);

    noData = new Text("No Data");
    noData.setTextAlignment(TextAlignment.CENTER);
    GridPane.setHalignment(noData, HPos.CENTER);
    GridPane.setFillWidth(noData, true);
    add(noData, 0, 0);
    DataManager.getInstance().addListener(this::handleNewData);
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
      getChildren().remove(noData);
      initialise();
      initialised = true;
    }
    this.dataSequence = dataSequence;
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

  /**
   * Forces the widget to reprocess the data and redraw. Called when a widget's
   * settings screen is closed, as settings such as selected unit may have
   * changed.
   */
  public void refresh() {
    handleNewData(dataSequence);
  }
}
