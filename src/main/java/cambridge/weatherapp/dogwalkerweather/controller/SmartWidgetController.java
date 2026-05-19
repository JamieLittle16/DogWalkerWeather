package cambridge.weatherapp.dogwalkerweather.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class SmartWidgetController {
    @FXML private Label title;
    @FXML private Label value;

    public void updateHumidity(double humidityPercentage) {
        value.setText(humidityPercentage + "%");
    }
}
