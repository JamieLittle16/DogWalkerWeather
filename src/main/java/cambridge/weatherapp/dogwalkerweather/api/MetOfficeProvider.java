package cambridge.weatherapp.dogwalkerweather.api;
import cambridge.weatherapp.dogwalkerweather.model.WeatherData;
import cambridge.weatherapp.dogwalkerweather.model.Location;

import java.io.File;
import java.io.IOException;
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

        return null;
    }   

    // Testing only.
    public static void main(String[] args) {
        MetOfficeProvider mop = new MetOfficeProvider();
        mop.getCurrentWeather(Location.CAMBRIDGE);
    }

}

