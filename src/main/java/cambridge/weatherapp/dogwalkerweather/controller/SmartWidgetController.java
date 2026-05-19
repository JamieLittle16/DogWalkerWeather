package cambridge.weatherapp.dogwalkerweather.controller;

import cambridge.weatherapp.dogwalkerweather.model.SmartWidgetType;
import cambridge.weatherapp.dogwalkerweather.model.WeatherData;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import static cambridge.weatherapp.dogwalkerweather.model.SmartWidgetType.UV;

public class SmartWidgetController {
    @FXML private Label title;
    @FXML private Label value;

    private SmartWidgetType type;

    public void setType(SmartWidgetType type, WeatherData data) {
        this.type = type;
        switch (type) {
            case DEW_POINT:
                title.setText("Dew Point");
                value.setText(String.valueOf(data.getCurrentDewPoint()));
                break;

            case WIND_SPEED:
                title.setText("Wind Speed");
                value.setText(String.valueOf(data.getCurrentWindSpeed()));
                break;

            case VISIBILITY:
                title.setText("Visibility");
                value.setText(String.valueOf(data.getCurrentVisibility()));
                break;

            case HUMIDITY:
                title.setText("Humidity");
                value.setText(String.valueOf(data.getCurrentRelativeHumidity()));
                break;

            case PRESSURE:
                title.setText("Pressure");
                value.setText(String.valueOf(data.getCurrentPressure()));
                break;

            case UV:
                title.setText("UV");
                value.setText(String.valueOf(data.getCurrentUV()));
                break;
        }
    }
}
