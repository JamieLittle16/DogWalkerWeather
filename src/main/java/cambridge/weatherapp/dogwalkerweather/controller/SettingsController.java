package cambridge.weatherapp.dogwalkerweather.controller;

import cambridge.weatherapp.dogwalkerweather.model.Location;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import java.util.Optional;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import org.kordamp.ikonli.javafx.FontIcon;

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

        // Create UI card for each saved element
        locationsList.setCellFactory(listView -> new ListCell<Location>() {
            @Override
            protected void updateItem(Location location, boolean empty) {
                super.updateItem(location, empty);

                // If row is empty make it invisible
                if (empty || location == null) {
                    setText(null);
                    setGraphic(null);
                    setStyle("-fx-background-color: transparent;");
                } else {
                    // Create text label
                    Label label = new Label(location.getDisplayName());
                    label.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #333333;");

                    // Puts a mapPin next to each of them. Can change to actual weather later, but good placeholder
                    FontIcon mapPin = cambridge.weatherapp.dogwalkerweather.util.IconUtil.getLocationIcon();

                    // Put it in HBox (acting as the "Card")
                    HBox card = new HBox(15, mapPin, label);
                    card.setAlignment(Pos.CENTER_LEFT);
                    card.setPadding(new Insets(15, 20, 15, 20));

                    card.setStyle("-fx-background-color: #e0e0e0; -fx-background-radius: 10;");

                    // Tell JavaFX to display card, not text
                    setGraphic(card);
                    setText(null);

                    // Make list row transparent and put a 10 px gap between elements
                    setStyle("-fx-background-color: transparent; -fx-padding: 0 0 10 0;");
                }
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
