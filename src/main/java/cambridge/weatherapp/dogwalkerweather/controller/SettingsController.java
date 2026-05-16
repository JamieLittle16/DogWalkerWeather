package cambridge.weatherapp.dogwalkerweather.controller;

import cambridge.weatherapp.dogwalkerweather.model.Location;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import java.util.Optional;

public class SettingsController {
    @FXML private Label currentLocationLabel;
    @FXML private ListView<Location> locationsList;

    @FXML
    private void initialize() {
        // Set current Locarion card text
        Location current = RootController.getInstance().getCurrentLocation();
        currentLocationLabel.setText(current.getDisplayName());

        // Bind the location UI list to the state list
        // Since its an observable list it will auto update the UI upon changing
        locationsList.setItems(RootController.getInstance().getSavedLocations());

        // Listen for clicks on saved locations
        locationsList.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                // Update the location state and navigate to home
                RootController.getInstance().setCurrentLocation(newVal);
                RootController.getInstance().onHomeClicked();
            }
        });
    }

    // Triggered by Back Arrow
    @FXML
    public void onBackClicked() {
        RootController.getInstance().onHomeClicked();
    }

    // Triggered by pressing + button
    @FXML
    public void onAddClicked() {
        // Native popup (quite simple) to select from Enum of locations
        ChoiceDialog<Location> dialog = new ChoiceDialog<>(Location.values()[0], Location.values()); // Just grabs first one
        dialog.setTitle("Add Location");
        dialog.setHeaderText("Search for a new location:");

        Optional<Location> result = dialog.showAndWait();
        result.ifPresent(location -> {
            // Prevent duplicates before adding to the global list
            if (!RootController.getInstance().getSavedLocations().contains(location)) {
                RootController.getInstance().getSavedLocations().add(location);
            }
        });
    }
}
