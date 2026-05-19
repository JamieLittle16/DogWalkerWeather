package cambridge.weatherapp.dogwalkerweather.api;

import cambridge.weatherapp.dogwalkerweather.model.Location;
import cambridge.weatherapp.dogwalkerweather.model.WeatherData;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CachedWeatherProvider implements WeatherProvider {

        // Helper record (holds one weatherData of cache)
        private record CachedWeather(WeatherData data, Instant fetchTime) {
    }

    // Holds reference to real weather provider
    private final WeatherProvider realApi;
    private final Map<Location, CachedWeather> cache = new ConcurrentHashMap<>();
    private static final long CACHE_DURATION_MINUTES = 15;

    public CachedWeatherProvider(WeatherProvider realApi) {
        this.realApi = realApi;
    }

    @Override
    public WeatherData getCurrentWeather(Location location) {
        // Check the cache first
        if (cache.containsKey(location) && !isExpired(location)) {
            return cache.get(location).data;
        }

        // If it's not cached ask API
        WeatherData freshData = realApi.getCurrentWeather(location);

        // Save it and return it
        cache.put(location, new CachedWeather(freshData, Instant.now()));
        return freshData;
    }

    private boolean isExpired(Location location) {
        CachedWeather savedItem = cache.get(location);
        if (savedItem == null) return true;

        // Calculate how many minutes have passed since we saved it
        long minutesOld = Duration.between(savedItem.fetchTime, Instant.now()).toMinutes();

        // If it's 15 minutes or older, it has expired!
        return minutesOld >= CACHE_DURATION_MINUTES;
    }
}

