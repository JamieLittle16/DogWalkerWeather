package cambridge.weatherapp.dogwalkerweather.model;

import java.util.List;

// This class holds the current weather data
// The object will be returned by api for a specific query
public class WeatherData {
    private String groundCondition;
    private List<HourlyForecast> dailyForecast;

    public WeatherData(String groundCondition, List<HourlyForecast> dailyForecast) {
        this.groundCondition = groundCondition;
        this.dailyForecast = dailyForecast;
    }

    public String getGroundCondition() { return groundCondition; }
    public List<HourlyForecast> getDailyForecast() { return dailyForecast; }

    public double getCurrentTemperature() { return dailyForecast.get(0).getTemperature(); }
    public double getCurrentFeelsLikeTemperature() { return dailyForecast.get(0).getFeelsLikeTemperature(); }
    public double getCurrentWindSpeed() { return dailyForecast.get(0).getWindSpeed(); }
    public double getCurrentWindDirection() { return dailyForecast.get(0).getWindDirection(); }
    public double getCurrentWindGustSpeed() { return dailyForecast.get(0).getWindGustSpeed(); }
    public double getCurrentVisibility() { return dailyForecast.get(0).getVisibility(); }
    public double getCurrentRelativeHumidity() { return dailyForecast.get(0).getRelativeHumidity(); }
    public double getCurrentPressure() { return dailyForecast.get(0).getPressure(); }
    public double getCurrentUV() { return dailyForecast.get(0).getUV(); }
    public double getCurrentDewPoint() { return dailyForecast.get(0).getDewPoint(); }
    public int getCurrentWeatherCode() { return dailyForecast.get(0).getWeatherCode(); }
    public int getCurrentPrecipitationProbability() { return dailyForecast.get(0).getPrecipitationProbability(); }
}
