package cambridge.weatherapp.dogwalkerweather.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class HumidityCardController {
    @FXML private Label humidityValueLabel;

    public void updateHumidity(double humidityPercentage) {
        humidityValueLabel.setText(humidityPercentage + "%");
    }
}
