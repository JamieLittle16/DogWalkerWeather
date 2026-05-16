package cambridge.weatherapp.dogwalkerweather.api;

import java.time.LocalTime;
import java.util.List;
import java.util.ArrayList;

import cambridge.weatherapp.dogwalkerweather.model.HourlyForecast;
import cambridge.weatherapp.dogwalkerweather.model.Location;
import cambridge.weatherapp.dogwalkerweather.model.WeatherData;


public class FakeWeatherProvider implements WeatherProvider{
    @Override
    public WeatherData getCurrentWeather(Location location) {
        System.err.println("WARNING: Using FAKE test data for location " + location.getDisplayName());

        List<HourlyForecast> fakeChartData = new ArrayList<>(List.of(
                new HourlyForecast(LocalTime.of(12, 0), 18.0, 16.5, 5.0, 180.0, 10.0, 10000.0, 55.0, 1012.0, 4.0, 10.0, 1, 0),
                new HourlyForecast(LocalTime.of(13, 0), 19.5, 17.0, 6.0, 190.0, 12.0, 10000.0, 50.0, 1011.0, 5.0, 11.0, 2, 10),
                new HourlyForecast(LocalTime.of(14, 0), 20.0, 18.5, 8.0, 200.0, 15.0, 8000.0, 80.0, 1009.0, 2.0, 14.0, 3, 80) // Big rain spike!
        ));

        // Returning obvious fake data
        return new WeatherData("[TEST] Dry", fakeChartData);
    }
}
