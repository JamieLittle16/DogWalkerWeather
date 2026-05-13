package cambridge.weatherapp.dogwalkerweather.model;

// Other locations need to be added,
// Pairs location with site ID, think they're correct but check
public enum Location {
    CAMBRIDGE("Cambridge", "310042"),
    LONDON("London", "350661"),
    OSLO("Oslo", "123456"); // Don't think MET Support none UK

    private final String displayName;
    private final String apiId;

    // Location Enum Constructor
    Location(String displayName, String apiId) {
        this.displayName = displayName;
        this.apiId = apiId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getApiId() {
        return apiId;
    }
}
