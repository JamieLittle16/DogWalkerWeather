module cambridge.weatherapp.dogwalkerweather {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.ikonli.javafx;

    requires atlantafx.base;

    opens cambridge.weatherapp.dogwalkerweather to javafx.fxml;
    opens cambridge.weatherapp.dogwalkerweather.controller to javafx.fxml;
    exports cambridge.weatherapp.dogwalkerweather;
}