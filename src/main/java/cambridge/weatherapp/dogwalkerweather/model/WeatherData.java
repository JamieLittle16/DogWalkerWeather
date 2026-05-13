package cambridge.weatherapp.dogwalkerweather.model;

import java.util.List;

// This class holds the current weather data
// The object will be returned by api for a specific query
public class WeatherData {
    private double temperature;
    private double humidity;
    private String groundCondition;
    private List<HourlyForecast> dailyForecast;

    public WeatherData(double temperature, double humidity, String groundCondition, List<HourlyForecast> dailyForecast) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.groundCondition = groundCondition;
        this.dailyForecast = dailyForecast;
    }

    public double getTemperature() { return temperature; }
    public double getHumidity() { return humidity; }
    public String getGroundCondition() { return groundCondition; }
    public List<HourlyForecast> getDailyForecast() { return dailyForecast; }
}
