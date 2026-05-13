package cambridge.weatherapp.dogwalkerweather.api;

import cambridge.weatherapp.dogwalkerweather.model.Location;
import cambridge.weatherapp.dogwalkerweather.model.WeatherData;

// Enforces provider to return a WeatherData object for a specified location
public interface WeatherProvider {
    WeatherData getCurrentWeather(Location location);
}
;