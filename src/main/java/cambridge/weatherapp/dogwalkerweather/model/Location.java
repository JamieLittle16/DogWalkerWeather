package cambridge.weatherapp.dogwalkerweather.model;

// Other locations need to be added,
// Pairs location with site ID, think they're correct but check
public enum Location {
    CAMBRIDGE("Cambridge", 52.1988349,0.0787878),
    LONDON("London", 51.5285262,-0.2664037),
    OSLO("Oslo", 64.1820777,7.2190473);

    private final String displayName;
    private final Double latitude;
    private final Double longitude;

    // Location Enum Constructor
    Location(String displayName, Double latitude, Double longitude) {
        this.displayName = displayName;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

}
