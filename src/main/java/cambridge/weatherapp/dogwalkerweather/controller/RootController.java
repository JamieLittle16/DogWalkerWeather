package cambridge.weatherapp.dogwalkerweather.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import java.io.IOException;

/*
* This Class is responsible for switching screens.
* The different screens will be loaded into this view
* It could contain some essential buttons that appear on all pages i.e. a navbar
* This is a singleton class so can be referenced from anywhere
**/
public class RootController {
    @FXML private BorderPane mainContainer;

    private static RootController instance;

    @FXML
    public void initialize() {
        // When JavaFX loads this class save it to a static variable
        instance = this;
        // Load the Home screen immediately when the app starts
        loadScreen("Home.fxml");
    }

    // Returns the Singleton instance of the class
    public static RootController getInstance() {
        return instance;
    }

    // Triggered by the "Home" button in the nav bar
    @FXML
    public void onHomeClicked() {
        loadScreen("Home.fxml");
    }

    // Triggered by the "Settings/Location" button in the nav bar
    @FXML
    public void onSettingsClicked() {
        // We will create Settings.fxml later, but the button is ready!
        loadScreen("Settings.fxml");
    }

    // Swaps the current loaded screen
    private void loadScreen(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cambridge/weatherapp/dogwalkerweather/" + fxmlFile));
            Parent screen = loader.load();

            // Swap core component of the screen with new component
            mainContainer.setCenter(screen);
        } catch (IOException e) {
            System.err.println("Could not load screen " + fxmlFile);
            e.printStackTrace();
        }

    }
}
