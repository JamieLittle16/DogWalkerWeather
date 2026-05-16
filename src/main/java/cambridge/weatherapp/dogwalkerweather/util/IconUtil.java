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
}
