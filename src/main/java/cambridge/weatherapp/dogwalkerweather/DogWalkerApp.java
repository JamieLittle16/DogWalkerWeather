package cambridge.weatherapp.dogwalkerweather;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import atlantafx.base.theme.PrimerLight;

import java.io.IOException;

public class DogWalkerApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // Enables Theme
        Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());

        FXMLLoader fxmlLoader = new FXMLLoader(DogWalkerApp.class.getResource("/cambridge/weatherapp/dogwalkerweather/RootLayout.fxml"));

        // Set dimensions to look like a mobile phone (width 390, height 844) // TODO: Ensure this is correct - Scalable?
        Scene scene = new Scene(fxmlLoader.load(), 400, 844);

        stage.setTitle("Dog Walker Weather");
        stage.setScene(scene);
        stage.show();
    }
}
