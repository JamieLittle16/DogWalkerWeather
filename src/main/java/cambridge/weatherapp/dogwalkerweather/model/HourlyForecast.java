package cambridge.weatherapp.dogwalkerweather.model;

import java.time.LocalTime;

/*
* Holds a single hour's worth of forecast data
* Designed to feed the bar charts - may need expanding in the future
*/
public class HourlyForecast {
    private LocalTime time;
    private double temperature;
    private double feelsLikeTemperature;
    private int precipitationProbability;

    public HourlyForecast(LocalTime time, double temperature, double feelsLikeTemperature, int precipitationProbability) {
        this.time = time;
        this.temperature = temperature;
        this.feelsLikeTemperature = feelsLikeTemperature;
        this.precipitationProbability = precipitationProbability;
    }
    // Getters for the UI Controller to read
    public LocalTime getTime() { return time; }
    public double getTemperature() { return temperature; }
    public double getFeelsLikeTemperature() { return feelsLikeTemperature; }
    public int getPrecipitationProbability() { return precipitationProbability; }
}
