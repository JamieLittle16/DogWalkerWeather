package cambridge.weatherapp.dogwalkerweather.controller;

import cambridge.weatherapp.dogwalkerweather.api.FakeWeatherProvider;
import cambridge.weatherapp.dogwalkerweather.api.WeatherProvider;
import cambridge.weatherapp.dogwalkerweather.model.Location;
import cambridge.weatherapp.dogwalkerweather.model.WeatherData;
import javafx.fxml.FXML;
import javafx.scene.control.Label;


/*
* Acts as the central brain for Home.fxl screen
* It listens to the UI, asks the API for data, and updates the screen.
* Follows MVC (Model View Controller) architecture
* */
public class HomeController {
    // @FML tells Java to look in Home.fxl for this label, and links it to the var
    @FXML private Label locationLabel;
    @FXML private Label tempLabel;
    // @FXML private Label groundLabel;
    @FXML private HumidityCardController humidityWidgetController;

    @FXML
    public void initialize() {

        // Loads current active location by asking the root controller
        Location activeLocation = RootController.getInstance().getCurrentLocation();
        updateUI(activeLocation);
    }

    @FXML
    public void onLocationClicked() {
        RootController.getInstance().onSettingsClicked();
    }

    private void updateUI(Location location) {
        // Ask the contract for the data
        WeatherData data = RootController.getInstance().getWeatherProvider().getCurrentWeather(location);

        locationLabel.setText(location.getDisplayName());

        // Update the screen
        tempLabel.setText(data.getCurrentTemperature() + " °C");
        // groundLabel.setText(data.getGroundCondition());
        humidityWidgetController.updateHumidity(data.getCurrentRelativeHumidity());
    }
}
