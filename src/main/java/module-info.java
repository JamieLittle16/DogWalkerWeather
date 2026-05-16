module cambridge.weatherapp.dogwalkerweather {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;
    requires java.net.http;

    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.fontawesome5;

    requires atlantafx.base;
    requires org.jetbrains.annotations;

    opens cambridge.weatherapp.dogwalkerweather to javafx.fxml;
    opens cambridge.weatherapp.dogwalkerweather.controller to javafx.fxml;
    exports cambridge.weatherapp.dogwalkerweather;
}