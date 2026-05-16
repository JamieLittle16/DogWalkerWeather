package cambridge.weatherapp.dogwalkerweather.util;

import org.jetbrains.annotations.NotNull;
import org.kordamp.ikonli.javafx.FontIcon;
import javafx.scene.paint.Color;

public class IconUtil {

    // Reusable location pin icon
    public static @NotNull FontIcon getLocationIcon() {
        FontIcon icon = new FontIcon("fas-map-marker-alt");
        icon.setIconSize(24);
        icon.setIconColor(Color.valueOf("#2D3E50")); // Dark navy blue
        return icon;
    }

    // Converts MET weather code into icon
    public static FontIcon getWeatherIconFromCode(int weatherCode) {
        FontIcon icon = new FontIcon();
        icon.setIconSize(24); // Keep it sized for the Settings list by default

        switch (weatherCode) {
            case 0: // Clear night
                icon.setIconLiteral("fas-moon");
                icon.setIconColor(Color.valueOf("#f1c40f"));
                break;
            case 1: // Sunny day
                icon.setIconLiteral("fas-sun");
                icon.setIconColor(Color.valueOf("#f1c40f"));
                break;
            case 2: // Partly cloudy (night)
                icon.setIconLiteral("fas-cloud-moon");
                icon.setIconColor(Color.valueOf("#95a5a6"));
                break;
            case 3: // Partly cloudy (day)
                icon.setIconLiteral("fas-cloud-sun");
                icon.setIconColor(Color.valueOf("#95a5a6"));
                break;
            case 5: // Mist
            case 6: // Fog
                icon.setIconLiteral("fas-smog");
                icon.setIconColor(Color.valueOf("#bdc3c7"));
                break;
            case 7: // Cloudy
            case 8: // Overcast
                icon.setIconLiteral("fas-cloud");
                icon.setIconColor(Color.valueOf("#7f8c8d"));
                break;
            case 9:  // Light rain shower (night)
            case 10: // Light rain shower (day)
            case 11: // Drizzle
            case 12: // Light rain
                icon.setIconLiteral("fas-cloud-rain");
                icon.setIconColor(Color.valueOf("#3498db"));
                break;
            case 13: // Heavy rain shower (night)
            case 14: // Heavy rain shower (day)
            case 15: // Heavy rain
                icon.setIconLiteral("fas-cloud-showers-heavy");
                icon.setIconColor(Color.valueOf("#2980b9"));
                break;
            case 16: // Sleet shower (night)
            case 17: // Sleet shower (day)
            case 18: // Sleet
            case 19: // Hail shower (night)
            case 20: // Hail shower (day)
            case 21: // Hail
                icon.setIconLiteral("fas-cloud-meatball"); // Closest FontAwesome to hail/sleet I could find
                icon.setIconColor(Color.valueOf("#bdc3c7"));
                break;
            case 22: // Light snow shower (night)
            case 23: // Light snow shower (day)
            case 24: // Light snow
            case 25: // Heavy snow shower (night)
            case 26: // Heavy snow shower (day)
            case 27: // Heavy snow
                icon.setIconLiteral("fas-snowflake");
                icon.setIconColor(Color.valueOf("#ecf0f1"));
                break;
            case 28: // Thunder shower (night)
            case 29: // Thunder shower (day)
            case 30: // Thunder
                icon.setIconLiteral("fas-bolt");
                icon.setIconColor(Color.valueOf("#f39c12"));
                break;
            default: // 4 (Not used) or missing data
                icon.setIconLiteral("fas-question-circle");
                icon.setIconColor(Color.valueOf("#333333"));
                break;
        }
        return icon;
    }
}
