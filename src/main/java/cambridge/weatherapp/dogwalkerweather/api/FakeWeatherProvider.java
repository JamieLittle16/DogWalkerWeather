package cambridge.weatherapp.dogwalkerweather.api;

import cambridge.weatherapp.dogwalkerweather.model.HourlyForecast;
import cambridge.weatherapp.dogwalkerweather.model.Location;
import cambridge.weatherapp.dogwalkerweather.model.WeatherData;

public class FakeWeatherProvider implements WeatherProvider{
    @Override
    public WeatherData getCurrentWeather(Location location) {
        System.err.println("WARNING: Using FAKE test data for location " + location.getDisplayName());

        java.util.List<HourlyForecast> fakeChartData = java.util.List.of(
                new HourlyForecast(java.time.LocalTime.of(12, 0), 18.0, 16.5, 0),
                new HourlyForecast(java.time.LocalTime.of(13, 0), 19.5, 17.0, 10),
                new HourlyForecast(java.time.LocalTime.of(14, 0), 20.0, 18.5, 80) // Big rain spike!
        );

        // Returning obvious fake data
        return new WeatherData(99.9, 99.9, "[TEST] Dry", fakeChartData);
    }
}
