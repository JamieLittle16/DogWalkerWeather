package cambridge.weatherapp.dogwalkerweather.model;

import java.time.LocalTime;

/*
* Holds a single hour's worth of forecast data
* Designed to feed the bar charts - may need expanding in the future
*/
public class HourlyForecast {
    private final LocalTime time;
    private final double temperature; // Celsius
    private final double feelsLikeTemperature; // Celsius
    private final double windSpeed; // Miles per hour (m/s in API)
    private final int windDirection; // Coming from direction, degrees relative to North (Clockwise).
    private final double windGustSpeed; // Miles per hour (m/s in API)
    private final int visibility; // Metres
    private final double relativeHumidity; // Percentage
    private final int pressure; // Pascals
    private final int UV;
    private final double dewPoint; // Celsius
    private final int weatherCode;
    private final int precipitationProbability; // Percentage

    public HourlyForecast(LocalTime time,
              double temperature,
              double feelsLikeTemperature,
              double windSpeed,
              int windDirection,
              double windGustSpeed,
              int visibility,
              double relativeHumidity,
              int pressure,
              int UV,
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
    public int getWindDirection() { return windDirection; }
    public double getWindGustSpeed() { return windGustSpeed; }
    public int getVisibility() { return visibility; }
    public double getRelativeHumidity() { return relativeHumidity; }
    public int getPressure() { return pressure; }
    public int getUV() { return UV; }
    public double getDewPoint() { return dewPoint; }
    public int getWeatherCode() { return weatherCode; }
    public int getPrecipitationProbability() { return precipitationProbability; }
}
