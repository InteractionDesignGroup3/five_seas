package uk.ac.cam.cl.gui.widgets;

import javafx.geometry.HPos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Label;
import javafx.scene.text.TextAlignment;
import uk.ac.cam.cl.data.DataManager;
import uk.ac.cam.cl.data.DataPoint;
import uk.ac.cam.cl.data.DataSequence;
import javafx.scene.layout.BorderImage;
import javafx.scene.image.Image;
import uk.ac.cam.cl.gui.widgets.Widget;

import java.nio.file.Path;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class WeatherWidget extends Widget
{
    /**
     * A weather widget for displaying the hourly weather
     * @author Andrew Coalter
     */

    // static final map which maps weather codes to their associated symbols
    // 00. no clouds 01. partial cloud 02. unknown 03. light cloud 04. brown cloud 05. cloud with bar 06. lightning 07. dark cloud 08. rain and snow 09. light rain 10. snow 11. heavy rain 12. hail 13. shower 14. snow shower
    private static final Map<Long, String> weatherCodeToImagePath = new HashMap<Long, String>(){
        { put(1L, "01"); put(2L, "02"); put(3L, "03"); put(4L, "04"); put(5L, "04"); put(6L, "04"); put(7L, "04"); put(8L, "04"); put(9L, "04"); put(10L, "05");
          put(11L, "05"); put(12L, "05"); put(13L, "06"); put(14L, "07"); put(15L, "07"); put(16L, "07"); put(17L, "06"); put(18L, "05"); put(19L, "05"); put(20L, "08");
          put(21L, "09"); put(22L, "10"); put(23L, "08"); put(24L, "08"); put(25L, "11"); put(26L, "10"); put(27L, "12"); put(28L, "05"); put(29L, "06"); put(30L, "04");
          put(31L, "04"); put(32L, "04"); put(33L, "04"); put(34L, "04"); put(35L, "04"); put(36L, "10"); put(37L, "10"); put(38L, "10"); put(39L, "10"); put(40L, "05");
          put(41L, "05"); put(42L, "05"); put(43L, "05"); put(44L, "05"); put(45L, "05"); put(46L, "05"); put(47L, "05"); put(48L, "05"); put(49L, "05"); put(50L, "09");
          put(51L, "09"); put(52L, "09"); put(53L, "09"); put(54L, "11"); put(55L, "11"); put(56L, "09"); put(57L, "11"); put(58L, "09"); put(59L, "11"); put(60L, "09");
          put(61L, "09"); put(62L, "09"); put(63L, "09"); put(64L, "11"); put(65L, "11"); put(66L, "09"); put(67L, "11"); put(68L, "09"); put(69L, "11"); put(70L, "10");
          put(71L, "10"); put(72L, "10"); put(73L, "10"); put(74L, "10"); put(75L, "10"); put(76L, "10"); put(77L, "10"); put(78L, "10"); put(79L, "10"); put(80L, "13");
          put(81L, "13"); put(82L, "13"); put(83L, "13"); put(84L, "13"); put(85L, "14"); put(86L, "14"); put(87L, "14"); put(88L, "14"); put(89L, "12"); put(90L, "12");
          put(91L, "09"); put(92L, "11"); put(93L, "06"); put(94L, "06"); put(95L, "06"); put(96L, "06"); put(97L, "06"); put(98L, "06"); put(99L, "06"); put(0L, "00");
        }
    };

    public WeatherWidget()
    {
        super();
        DataManager.getInstance().addListener(this::assign);
    }

    /**
     * Generates the widget and displays the relevant symbol and time
     * @param dataSequenceList data sequences to get weather codes from
     */
    private void assign(List<DataSequence> dataSequenceList)
    {
        ScrollPane scrollPane = new ScrollPane();
        GridPane gridPane = new GridPane();
        DataSequence dataSequence = dataSequenceList.get(DataManager.getInstance().getDay());
        for(int i = 0; i < dataSequence.size(); i++)
        {
            if (i % 4 != 0) continue;
            DataPoint dataPoint = dataSequence.get(i);
            Label label = new Label();
            Instant instant = new Date(dataPoint.getTime()).toInstant();
            Image image = getSymbol(dataPoint.getWeatherCode(), instant);
            ImageView imageView = new ImageView();
            imageView.setImage(image);
            String timeFormatted = DateTimeFormatter.ofPattern("HH:mm").withZone(ZoneId.systemDefault()).format(instant);
            label.setText(timeFormatted);
            gridPane.add(imageView, i, 0);
            gridPane.add(label, i, 1);
            GridPane.setHalignment(label, HPos.CENTER);
        }
        scrollPane.setContent(gridPane);
        scrollPane.setPrefViewportWidth(1000);
        scrollPane.setPrefViewportHeight(100);
        this.add(scrollPane, 0, 0);
        Label label = new Label();
    }

    /**
     * This method takes in a weather code and returns the relevant weather symbol image
     * @param weatherCode is the weather code to fetch a symbol for
     * @param instant holds the time that the weather code is for
     * @return dataSequenceList data sequences to potentially plot from
     */
    private Image getSymbol(long weatherCode, Instant instant)
    {
        int timeHour = Integer.parseInt(DateTimeFormatter.ofPattern("HH").withZone(ZoneId.systemDefault()).format(instant));
        int day = 0;
        //arbitary selection of when night time is
        if(timeHour < 6 || timeHour > 20)
        { day = 1;}
        Image image = new Image("file:src/main/resources/symbols/" + day + weatherCodeToImagePath.get(weatherCode) + ".png");
        return image;
    }

    @Override
    public String getName() {
        return "Weather";
    }
}
