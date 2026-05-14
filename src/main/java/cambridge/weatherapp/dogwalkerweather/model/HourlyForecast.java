package cambridge.weatherapp.dogwalkerweather.model;

import java.time.LocalTime;

/*
* Holds a single hour's worth of forecast data
* Designed to feed the bar charts - may need expanding in the future
*/
public class HourlyForecast {
    private final LocalTime time;
    private final double temperature;
    private final double feelsLikeTemperature;
    private final double windSpeed;
    private final double windDirection;
    private final double windGustSpeed;
    private final double visibility;
    private final double relativeHumidity;
    private final double pressure;
    private final double UV;
    private final double dewPoint;
    private final int weatherCode;
    private final int precipitationProbability;

    public HourlyForecast(LocalTime time,
              double temperature,
              double feelsLikeTemperature,
              double windSpeed,
              double windDirection,
              double windGustSpeed,
              double visibility,
              double relativeHumidity,
              double pressure,
              double UV,
              double dewPoint,
              int weatherCode,
              int precipitationProbability) {
        this.time = time;
        this.temperature = temperature;
        this.feelsLikeTemperature = feelsLikeTemperature;
        this.windSpeed = windSpeed;
        this.windDirection = windDirection;
        this.windGustSpeed = windGustSpeed;
        this.visibility = visibility;
        this.relativeHumidity = relativeHumidity;
        this.pressure = pressure;
        this.UV = UV;
        this.dewPoint = dewPoint;
        this.weatherCode = weatherCode;
        this.precipitationProbability = precipitationProbability;
    }
    // Getters for the UI Controller to read
    public LocalTime getTime() { return time; }
    public double getTemperature() { return temperature; }
    public double getFeelsLikeTemperature() { return feelsLikeTemperature; }
    public double getWindSpeed() { return windSpeed; }
    public double getWindDirection() { return windDirection; }
    public double getWindGustSpeed() { return windGustSpeed; }
    public double getVisibility() { return visibility; }
    public double getRelativeHumidity() { return relativeHumidity; }
    public double getPressure() { return pressure; }
    public double getUV() { return UV; }
    public double getDewPoint() { return dewPoint; }
    public int getWeatherCode() { return weatherCode; }
    public int getPrecipitationProbability() { return precipitationProbability; }
}
