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
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.collections.transformation.FilteredList;

public class SettingsController {
    @FXML private Label currentLocationLabel;
    @FXML private ListView<Location> locationsList;

    @FXML
    private void initialize() {
        // Set current Locarion card text
        Location current = RootController.getInstance().getCurrentLocation();
        currentLocationLabel.setText(current.getDisplayName());

        // Create a filter that hides the active location from the list
        FilteredList<Location> filteredLocations = new FilteredList<>(
                RootController.getInstance().getSavedLocations(),
                loc -> loc != current
        );

        // Bind the location UI list to the state list
        // Since its an observable list it will auto update the UI upon changing
        locationsList.setItems(filteredLocations);

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
                    setStyle("-fx-background-color: transparent; -fx-padding: 0;");
                } else {
                    // Create text label
                    Label label = new Label(location.getDisplayName());
                    label.getStyleClass().add("title-4");

                    // Puts a mapPin next to each of them.
                    FontIcon mapPin = cambridge.weatherapp.dogwalkerweather.util.IconUtil.getLocationIcon();

                    Region spacer = new Region();
                    HBox.setHgrow(spacer, Priority.ALWAYS);

                    FontIcon weatherIcon = new FontIcon("fas-cloud-sun"); // TODO: Make this the actual weather
                    weatherIcon.setIconSize(20);
                    weatherIcon.setIconColor(Color.valueOf("#bdc3c7")); // Soft silver

                    // Put it in HBox (acting as the "Card")
                    HBox card = new HBox(15, mapPin, label, spacer, weatherIcon);
                    card.setAlignment(Pos.CENTER_LEFT);
                    card.setPadding(new Insets(15, 20, 15, 20));

                    String defaultStyle =
                            "-fx-background-color: rgba(255, 255, 255, 0.85); " +
                                    "-fx-background-radius: 12; " +
                                    "-fx-cursor: hand; " + // Turns the mouse into a pointer
                                    "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.15), 8, 0, 0, 4);";

                    String hoverStyle =
                            "-fx-background-color: rgba(255, 255, 255, 1.0); " +
                                    "-fx-background-radius: 12; " +
                                    "-fx-cursor: hand; " +
                                    "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.25), 12, 0, 0, 6);";

                    // Set card style
                    card.setStyle(defaultStyle);

                    // Applies hover styles
                    card.setOnMouseEntered(event -> card.setStyle(hoverStyle));
                    card.setOnMouseExited(event -> card.setStyle(defaultStyle));

                    // Tell JavaFX to display card, not text
                    setGraphic(card);
                    setText(null);

                    // Make list row transparent and put a 10 px gap between elements
                    setStyle("-fx-background-color: transparent; -fx-padding: 0 0 12 0; -fx-control-inner-background-alt: transparent; -fx-selection-bar: transparent; -fx-selection-bar-non-focused: transparent;");                }
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
