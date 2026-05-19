package cambridge.weatherapp.dogwalkerweather.api;
import cambridge.weatherapp.dogwalkerweather.model.WeatherData;
import cambridge.weatherapp.dogwalkerweather.model.Location;
import cambridge.weatherapp.dogwalkerweather.model.HourlyForecast;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Collections;
import java.util.ArrayList;
import java.time.*;
import static java.util.Map.entry;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.ObjectMapper;


public class MetOfficeProvider implements WeatherProvider {
    private static String forecastApi = "https://data.hub.api.metoffice.gov.uk/sitespecific/v0";
    private static String landObservationsApi = "https://data.hub.api.metoffice.gov.uk/observation-land/1";

    /** Returns the path of the API configuration file. */
    private static String getApiFilePath() {
        Path userDir = Paths.get(System.getProperty("user.dir")).toAbsolutePath();
        Path finalPath = userDir;

        if (userDir.getFileName().toString().equals("target") || 
            userDir.getFileName().toString().equals("build")) {
            finalPath = userDir.getParent();
        }

        return finalPath.resolve("api.json").toString();
    }

    /** Retrieves the required API key. */
    private static String getApiKey(String key) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(new File(getApiFilePath()));
            return rootNode.get(key).asText();
        } catch (IOException e) {
            System.err.println("Error reading API key file: " + e.getMessage());
            return "";
        }
    }

    /**
     * Performs a Met Office API request, accepting query parameters.
     * The API key must also be provided.
     * Returns the response contents (JSON).
     */
    private static JsonNode getApiOutput(String api, Map<String, String> params, String apiKey) {
        java.net.URI finalUri = null;
        if (!params.isEmpty()) {
            String queryString = params.entrySet().stream()
                .map(entry -> URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8) + "=" + 
                            URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8))
                .collect(Collectors.joining("&"));
            finalUri = URI.create(api + "?" + queryString);
        } else {
            finalUri = URI.create(api);
        }

        System.out.println(finalUri);

        HttpRequest request = HttpRequest.newBuilder().uri(finalUri).header("apikey", apiKey).GET().build();
        HttpClient client = HttpClient.newHttpClient();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 404) {
                return null;
            }

            ObjectMapper mapper = new ObjectMapper();
            try {
                return mapper.readTree(response.body());
            } catch (IOException e) {
                System.err.println("Error with parsing API output: " + e.getMessage());
                return null;
            }

        } catch (Exception e) {
            System.err.println("Error with API request: " + e.getMessage());
            return null;
        }
    }

    /**
     * Retrieves the weather conditions for a particular location.
     */
    public WeatherData getCurrentWeather(Location location) {
        String forecastApiKey = getApiKey("forecast");
        Map<String, String> forecastParams = Map.of(
            "dataSource", "BD1",
            "latitude", location.getLatitude().toString(),
            "longitude", location.getLongitude().toString()
        );
        JsonNode forecastOutput = getApiOutput(
            forecastApi + "/point/hourly", forecastParams, forecastApiKey
        );

        String landApiKey = getApiKey("land");
        Map<String, String> nearestObsParams = Map.of(
            "lat", String.format("%.2f", location.getLatitude()),
            "lon", String.format("%.2f", location.getLongitude())
        );
        ArrayNode nearestObsOutput = (ArrayNode)getApiOutput(
            landObservationsApi + "/nearest", nearestObsParams, landApiKey
        );
        String geoHash = nearestObsOutput.get(0).get("geohash").asText();
        JsonNode landObsOutput = getApiOutput(
            landObservationsApi + "/" + geoHash, Collections.emptyMap(), landApiKey
        );

        // Extract the relevant hourly forecast data.
        ArrayList<HourlyForecast> hourlyConditions = new ArrayList<>();
        ArrayNode hourlyJsonArray = (ArrayNode)forecastOutput
            .get("features")
            .get(0).get("properties")
            .get("timeSeries");
        for (JsonNode hourly : hourlyJsonArray) {
            LocalTime dateTime = OffsetDateTime.parse(hourly.get("time").asText()).toLocalTime();
            double temperature = hourly.get("screenTemperature").asDouble();
            double feelsLike = hourly.get("feelsLikeTemperature").asDouble();
            double windSpeed = hourly.get("windSpeed10m").asDouble() / 1609.34 * 3600; // m/s -> mph
            windSpeed = BigDecimal.valueOf(windSpeed).setScale(2, RoundingMode.HALF_UP).doubleValue();
            int windDirection = hourly.get("windDirectionFrom10m").asInt();
            double windGustSpeed = hourly.get("windGustSpeed10m").asDouble() / 1609.34 * 3600;
            windGustSpeed = BigDecimal.valueOf(windGustSpeed).setScale(2, RoundingMode.HALF_UP).doubleValue();
            int visibility = hourly.get("visibility").asInt();
            double relativeHumidity = hourly.get("screenRelativeHumidity").asDouble();
            int pressure = hourly.get("mslp").asInt();
            int uv = hourly.get("uvIndex").asInt();
            double dewPoint = hourly.get("screenDewPointTemperature").asDouble();
            int weatherCode = hourly.get("significantWeatherCode").asInt();
            int precipitationProb = hourly.get("probOfPrecipitation").asInt();

            HourlyForecast hourlyForecast = new HourlyForecast(
                dateTime,
                temperature,
                feelsLike,
                windSpeed,
                windDirection,
                windGustSpeed,
                visibility,
                relativeHumidity,
                pressure,
                uv,
                dewPoint,
                weatherCode,
                precipitationProb
            );
            hourlyConditions.add(hourlyForecast);
        }

        // Attempt to deduce ground conditions from recent land observations.
        String groundCondition = "N/A";
        if (landObsOutput != null) {
            double wetnessScore = 0;
            Map<Integer, Integer> codeToWeight = Map.ofEntries(
                // Light rain - minor
                entry(9, 30), entry(10, 30), entry(12, 30),
                // Drizzle - very minor
                entry(11, 15),
                // Sleet / Hail / Light Snow - moderate
                entry(16, 45), entry(17, 45), entry(18, 45), entry(19, 45), entry(20, 45), entry(21, 45),
                // Heavy rain - major
                entry(13, 70), entry(14, 70), entry(15, 70),
                // Thunderstorms - major
                entry(28, 80), entry(29, 80), entry(30, 80),
                // Heavy Snow - very messy
                entry(25, 100), entry(26, 100), entry(27, 100)
            );
            // Weight the most recent hour the most strongly.
            // Older hours have lower weighting.
            double multiplierPerHour = 0.88;
            // Minimum wetness score to display "Wet".
            double wetThreshold = 15;
            // Number of hours to consider (up to 48 based on API).
            int hoursCount = 24;
            for (int hoursAgo = 0; hoursAgo < hoursCount; ++hoursAgo) {
                JsonNode hourlyObs = landObsOutput.get(hoursAgo);
                if (hourlyObs == null) {
                    break;
                }
                JsonNode weatherCodeNode = hourlyObs.get("weather_code");
                if (weatherCodeNode == null) {
                    wetnessScore = -1;
                    break;
                }
                int weatherCode = weatherCodeNode.asInt();
                wetnessScore += codeToWeight.getOrDefault(weatherCode, 0) * Math.pow(multiplierPerHour, hoursAgo);
            }
            if (wetnessScore == -1) {
                groundCondition = "N/A";
            } else {
                groundCondition = wetnessScore >= wetThreshold ? "Wet" : "Dry";
            }
        }

        return new WeatherData(groundCondition, hourlyConditions);
    }   

    // Testing only.
    public static void main(String[] args) {
        MetOfficeProvider mop = new MetOfficeProvider();
        WeatherData wd = mop.getCurrentWeather(Location.CAMBRIDGE);
        System.out.println("Ground: " + wd.getGroundCondition().toString());
        System.out.println("Temperature: " + wd.getCurrentTemperature() + "C");
        System.out.println("Feels Like: " + wd.getCurrentFeelsLikeTemperature() + "C");
        System.out.println("Wind Speed: " + wd.getCurrentWindSpeed() + "mph");
        System.out.println("Wind Direction: " + wd.getCurrentWindDirection() + " deg");
        System.out.println("Wind Gust Speed: " + wd.getCurrentWindGustSpeed() + "mph");
        System.out.println("Visibility: " + wd.getCurrentVisibility() + "m");
        System.out.println("Relative Humidity: " + wd.getCurrentRelativeHumidity() + "%");
        System.out.println("Pressure: " + wd.getCurrentPressure() + " Pa");
        System.out.println("UV: " + wd.getCurrentUV());
        System.out.println("Dew Point: " + wd.getCurrentDewPoint() + "C");
        System.out.println("Weather Code: " + wd.getCurrentWeatherCode());
        System.out.println("Precipitation Odds: " + wd.getCurrentPrecipitationProbability() + "%");
    }

}

